package dev.chudziudgi.ohmc.drop.paper.database;

import com.google.common.base.Stopwatch;
import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.dao.DaoManager;
import com.j256.ormlite.field.DataPersisterManager;
import com.j256.ormlite.jdbc.DataSourceConnectionSource;
import com.j256.ormlite.support.ConnectionSource;
import com.zaxxer.hikari.HikariDataSource;
import java.io.File;
import java.sql.SQLException;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.TimeUnit;
import java.util.logging.Logger;

public class DatabaseManager {

    private final Logger logger;
    private final File dataFolder;
    private final DatabaseSettings settings;
    private final Map<Class<?>, Dao<?, ?>> cachedDao = new ConcurrentHashMap<>();
    private HikariDataSource dataSource;
    private ConnectionSource connectionSource;

    public DatabaseManager(Logger logger, File dataFolder, DatabaseSettings settings) {
        this.logger = logger;
        this.dataFolder = dataFolder;
        this.settings = settings;
    }

    public void connect() {
        try {
            DataPersisterManager.registerDataPersisters(InstantPersister.getSingleton());

            Stopwatch stopwatch = Stopwatch.createStarted();

            com.j256.ormlite.logger.Logger.setGlobalLogLevel(this.settings.logLevel());

            this.dataSource = new HikariDataSource();
            this.dataSource.addDataSourceProperty("cachePrepStmts", "true");
            this.dataSource.addDataSourceProperty("prepStmtCacheSize", "250");
            this.dataSource.addDataSourceProperty("prepStmtCacheSqlLimit", "2048");
            this.dataSource.addDataSourceProperty("useServerPrepStmts", "true");

            this.dataSource.setMaximumPoolSize(this.settings.poolSize());
            this.dataSource.setConnectionTimeout(this.settings.timeout());
            this.dataSource.setUsername(this.settings.username());
            this.dataSource.setPassword(this.settings.password());

            DatabaseDriverType type = this.settings.databaseType();
            this.dataSource.setDriverClassName(type.getDriver());

            String jdbcUrl = switch (type) {
                case H2, SQLITE -> type.formatUrl(dataFolder);
                case POSTGRESQL -> type.formatUrl(
                    this.settings.hostname(),
                    this.settings.port(),
                    this.settings.database(),
                    DatabaseConnectionDriverConstant.sslParamForPostgreSQL(this.settings.ssl())
                );
                case MYSQL -> type.formatUrl(
                    this.settings.hostname(),
                    this.settings.port(),
                    this.settings.database(),
                    DatabaseConnectionDriverConstant.sslParamForMySQL(this.settings.ssl())
                );
                case MARIADB -> type.formatUrl(
                    this.settings.hostname(),
                    this.settings.port(),
                    this.settings.database(),
                    String.valueOf(this.settings.ssl())
                );
            };

            this.dataSource.setJdbcUrl(jdbcUrl);

            this.connectionSource = new DataSourceConnectionSource(this.dataSource, jdbcUrl);

            this.logger.info("Loaded database " + type + " in " + stopwatch.elapsed(TimeUnit.MILLISECONDS) + "ms");
        }
        catch (Exception exception) {
            throw new DatabaseException("Failed to connect to the database", exception);
        }
    }

    public void close() {
        try {
            if (this.dataSource != null) {
                this.dataSource.close();
            }
            if (this.connectionSource != null) {
                this.connectionSource.close();
            }
        }
        catch (Exception exception) {
            this.logger.severe("Failed to close database connection: " + exception.getMessage());
        }
    }

    @SuppressWarnings("unchecked")
    public <T, ID> Dao<T, ID> getDao(Class<T> type) {
        return (Dao<T, ID>) this.cachedDao.computeIfAbsent(type, clazz -> {
            try {
                return DaoManager.createDao(this.connectionSource, clazz);
            } catch (SQLException e) {
                throw new DatabaseException("Failed to create DAO for " + clazz.getName(), e);
            }
        });
    }

    public ConnectionSource connectionSource() {
        return this.connectionSource;
    }
}

package dev.chudziudgi.ohmc.drop.paper.database;

import com.j256.ormlite.logger.Level;
import eu.okaeri.configs.OkaeriConfig;
import eu.okaeri.configs.annotation.Comment;

public class DatabaseConfig extends OkaeriConfig implements DatabaseSettings {

    @Comment({"Typ sterownika bazy danych (np. SQLITE, H2, MYSQL, MARIADB, POSTGRESQL).",
              "Domyślnie H2 - baza plikowa, nie wymaga zewnętrznego serwera."})
    public DatabaseDriverType databaseType = DatabaseDriverType.H2;

    @Comment({"Adres serwera bazy danych.", "Dla baz lokalnych zwykle 'localhost'."})
    public String hostname = "localhost";

    @Comment({"Port serwera bazy danych. Typowe porty:",
              " - MySQL/MariaDB: 3306",
              " - PostgreSQL: 5432",
              " - H2/SQLite: nie dotyczy (baza plikowa)"})
    public int port = 3306;

    @Comment("Nazwa bazy danych do której się łączymy.")
    public String database = "ohmc_drop";

    @Comment("Nazwa użytkownika bazy danych.")
    public String username = "root";

    @Comment("Hasło do bazy danych.")
    public String password = "";

    @Comment("Włącz SSL dla połączenia z bazą danych.")
    public boolean ssl = false;

    @Comment({"Poziom logowania ORMLite (np. DEBUG, INFO, WARN, ERROR).",
              "https://ormlite.com/javadoc/ormlite-core/com/j256/ormlite/logger/Level.html"})
    public Level logLevel = Level.WARNING;

    @Comment("Rozmiar puli połączeń.")
    public int poolSize = 8;

    @Comment("Timeout połączenia w milisekundach.")
    public int timeout = 30_000;

    @Override
    public DatabaseDriverType databaseType() {
        return this.databaseType;
    }

    @Override
    public String hostname() {
        return this.hostname;
    }

    @Override
    public int port() {
        return this.port;
    }

    @Override
    public String database() {
        return this.database;
    }

    @Override
    public String username() {
        return this.username;
    }

    @Override
    public String password() {
        return this.password;
    }

    @Override
    public boolean ssl() {
        return this.ssl;
    }

    @Override
    public Level logLevel() {
        return this.logLevel;
    }

    @Override
    public int poolSize() {
        return this.poolSize;
    }

    @Override
    public int timeout() {
        return this.timeout;
    }
}

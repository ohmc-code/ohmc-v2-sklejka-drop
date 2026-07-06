package dev.chudziudgi.ohmc.drop.paper.database;

import com.j256.ormlite.logger.Level;

public interface DatabaseSettings {

    DatabaseDriverType databaseType();

    String hostname();

    int port();

    String database();

    String username();

    String password();

    boolean ssl();

    Level logLevel();

    int poolSize();

    int timeout();
}

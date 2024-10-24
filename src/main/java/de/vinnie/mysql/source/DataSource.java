package de.vinnie.mysql.source;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import de.vinnie.network.ConfigManager;
import de.vinnie.network.SystemManager;
import de.vinnie.network.config.ConfigTypes;
import de.vinnie.network.config.ConfigValues;

import java.util.Objects;

public class DataSource {

    private static volatile DataSource instance;
    private static HikariDataSource dataSource;
    private static SystemManager systemManager;

    private DataSource() {
        systemManager = SystemManager.getSystemManager();
    }

    public static DataSource getInstance() {
        if (instance == null) {
            synchronized (DataSource.class) {
                if (instance == null) {
                    instance = new DataSource();
                }
            }
        }
        return instance;
    }


    /*
      ___       _ _   _       _ _     _
     |_ _|_ __ (_) |_(_) __ _| (_)___(_)_ __   __ _
      | || '_ \| | __| |/ _` | | |_  / | '_ \ / _` |
      | || | | | | |_| | (_| | | |/ /| | | | | (_| |
     |___|_| |_|_|\__|_|\__,_|_|_/___|_|_| |_|\__, |
                                              |___/
    */
    public void openDataSource() {
        loadDatabaseDriver();

        try {
            HikariConfig hikariConfig = setupHikariConfig();
            dataSource = new HikariDataSource(hikariConfig);
            systemManager.logInfo("Successfully initialized HikariCP connection pool.");

        } catch (IllegalStateException e) {
            systemManager.logInfo("Database configuration is missing or invalid");
        } catch (RuntimeException e) {
            systemManager.logInfo("Failed to initialize the HikariCP connection pool");
        } catch (Exception e) {
            systemManager.logInfo("An unexpected error occurred while initializing HikariCP");
        }
    }

    public void closeDataSource() {
        if (dataSource != null && !dataSource.isClosed()) {
            dataSource.close();
            systemManager.logInfo("HikariCP connection pool closed successfully.");
        }
    }

    public static HikariDataSource getDataSource() {
        return dataSource;
    }


    /*
      _   _      _                   __  __      _   _               _
     | | | | ___| |_ __   ___ _ __  |  \/  | ___| |_| |__   ___   __| |___
     | |_| |/ _ \ | '_ \ / _ \ '__| | |\/| |/ _ \ __| '_ \ / _ \ / _` / __|
     |  _  |  __/ | |_) |  __/ |    | |  | |  __/ |_| | | | (_) | (_| \__ \
     |_| |_|\___|_| .__/ \___|_|    |_|  |_|\___|\__|_| |_|\___/ \__,_|___/
                  |_|
    */
    private void loadDatabaseDriver() {
        try {
            Class.forName("org.mariadb.jdbc.Driver");
            systemManager.logInfo("MariaDB driver loaded successfully.");
        } catch (ClassNotFoundException e) {
            systemManager.logInfo("MariaDB driver not found");
        }
    }

    private HikariConfig setupHikariConfig() {
        ConfigValues.DatabaseConfig dbConfig = Objects.requireNonNull(ConfigManager.getConfig(ConfigTypes.MYSQL)).databaseConfig;
        ConfigValues.SSLConfig sslConfig = Objects.requireNonNull(ConfigManager.getConfig(ConfigTypes.MYSQL)).sslConfig;
        ConfigValues.HikariConfig hkConfig = Objects.requireNonNull(ConfigManager.getConfig(ConfigTypes.MYSQL)).hikariConfig;

        HikariConfig config = new HikariConfig();
        config.setJdbcUrl(buildJdbcUrl(dbConfig));
        config.setUsername(dbConfig.user);
        config.setPassword(dbConfig.password);

        applyHikariProperties(config, hkConfig, sslConfig);

        return config;
    }

    private String buildJdbcUrl(final ConfigValues.DatabaseConfig dbConfig) {
        return String.format("jdbc:mariadb://%s:%d/%s", dbConfig.host, dbConfig.port, dbConfig.name);
    }

    private void applyHikariProperties(final HikariConfig config, final ConfigValues.HikariConfig hkConfig, final ConfigValues.SSLConfig sslConfig) {
        config.setMaximumPoolSize(hkConfig.maximumPoolSize);
        config.setMinimumIdle(hkConfig.minimumIdle);
        config.setConnectionTimeout(hkConfig.connectionTimeOut);
        config.setIdleTimeout(hkConfig.idleTimeout);
        config.setMaxLifetime(hkConfig.poolLifeTime);
        config.setLeakDetectionThreshold(hkConfig.threshold);

        config.addDataSourceProperty("cachePrepStmts", String.valueOf(hkConfig.cachePrepStmts));
        config.addDataSourceProperty("prepStmtCacheSize", String.valueOf(hkConfig.prepStmtCacheSize));
        config.addDataSourceProperty("prepStmtCacheSqlLimit", String.valueOf(hkConfig.prepStmtCacheSqlLimit));

        config.addDataSourceProperty("verifyServerCertificate", String.valueOf(sslConfig.verifyServerCertificate));
        config.addDataSourceProperty("sslMode", getSSLMode(sslConfig.sslMode));
        config.addDataSourceProperty("requireSSL", String.valueOf(sslConfig.requireSSL));
    }

    private String getSSLMode(final String sslModeValue) {
        if (sslModeValue.equalsIgnoreCase("disabled")) {
            return "disabled";
        } else if (sslModeValue.equalsIgnoreCase("trust")) {
            return "trust";
        } else {
            return "disabled";
        }
    }
}
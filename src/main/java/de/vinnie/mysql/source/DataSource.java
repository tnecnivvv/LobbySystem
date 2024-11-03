package de.vinnie.mysql.source;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import de.vinnie.network.ConfigManager;
import de.vinnie.network.SystemManager;
import de.vinnie.network.config.ConfigTypes;
import de.vinnie.network.config.ConfigValues;

import java.util.Objects;
import java.util.logging.Level;

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
            systemManager.log(Level.INFO,"Successfully initialized HikariCP connection pool.", null);

        } catch (IllegalStateException e) {
            systemManager.log(Level.WARNING,"Database configuration is missing or invalid", e);
        } catch (RuntimeException e) {
            systemManager.log(Level.SEVERE,"Failed to initialize the HikariCP connection pool", e);
        } catch (Exception e) {
            systemManager.log(Level.SEVERE,"An unexpected error occurred while initializing HikariCP", e);
        }
    }

    public void closeDataSource() {
        if (dataSource != null && !dataSource.isClosed()) {
            dataSource.close();
            systemManager.log(Level.INFO,"HikariCP connection pool closed successfully.", null);
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
            systemManager.log(Level.INFO,"MariaDB driver loaded successfully.", null);
        } catch (ClassNotFoundException e) {
            systemManager.log(Level.SEVERE,"MariaDB driver not found", e);
        }
    }

    private HikariConfig setupHikariConfig() {
        ConfigValues.DatabaseConfig dbConfig = Objects.requireNonNull(ConfigManager.getConfig(ConfigTypes.MYSQL)).databaseConfig;
        ConfigValues.SSLConfig sslConfig = Objects.requireNonNull(ConfigManager.getConfig(ConfigTypes.MYSQL)).sslConfig;
        ConfigValues.HikariConfig hkConfig = Objects.requireNonNull(ConfigManager.getConfig(ConfigTypes.MYSQL)).hikariConfig;

        HikariConfig config = new HikariConfig();
        config.setJdbcUrl(String.format("jdbc:mariadb://%s:%d/%s", dbConfig.host, dbConfig.port, dbConfig.name));
        config.setUsername(dbConfig.user);
        config.setPassword(dbConfig.password);

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
        config.addDataSourceProperty("sslMode",
                sslConfig.sslMode.equalsIgnoreCase("disabled") ? "disabled" :
                        (sslConfig.sslMode.equalsIgnoreCase("trust") ? "trust" : "disabled")
        );
        config.addDataSourceProperty("requireSSL", String.valueOf(sslConfig.requireSSL));

        return config;
    }
}
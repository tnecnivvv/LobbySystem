package com.tnecnivvv.api.mysql.source;

import com.tnecnivvv.api.config.ConfigurationTypes;
import com.tnecnivvv.api.config.types.MySqlConfiguration;
import com.tnecnivvv.lobbySystem.dependencies.SimpleLogger;
import com.tnecnivvv.lobbySystem.plugin.ServiceLocator;
import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import com.zaxxer.hikari.pool.HikariPool;
import org.jetbrains.annotations.NotNull;

import java.util.logging.Level;

public class DataSource {

    private static HikariDataSource dataSource;
    private final SimpleLogger simpleLogger;

    private final MySqlConfiguration.MySQL mySqlConfiguration;
    private final MySqlConfiguration.Hikari hikariConfiguration;

    public DataSource() {
        MySqlConfiguration configuration = ServiceLocator.getInstance().
                getConfigurationAPI().getConfiguration(
                        ConfigurationTypes.MYSQL,
                        MySqlConfiguration.class
                );
        mySqlConfiguration = configuration.getMysql();
        hikariConfiguration = configuration.getHikari();

        simpleLogger = ServiceLocator.getInstance().getSimpleLogger();
    }

    /**
     * Initializes the HikariCP connection pool using configuration data.
     */
    public synchronized void initializeHikari() {
        if (dataSource != null) {
            return;
        }

        try {
            loadDatabaseDriver();
            dataSource = new HikariDataSource(getHikariConfig());
            simpleLogger.log(Level.INFO, "Successfully initialized HikariCP connection pool.", null);
        } catch (IllegalStateException exception) {
            simpleLogger.log(Level.WARNING, "Database configuration is missing or invalid.", null);
        } catch (RuntimeException exception) {
            if (exception instanceof HikariPool.PoolInitializationException &&
                    exception.getCause() instanceof java.sql.SQLInvalidAuthorizationSpecException) {
                simpleLogger.log(Level.SEVERE, "Access denied for database user. Please verify your credentials.", null);
            } else {
                simpleLogger.log(Level.SEVERE, "Failed to initialize the HikariCP connection pool", exception);
            }
        } catch (Exception exception) {
            simpleLogger.log(Level.SEVERE, "An unexpected error occurred while initializing HikariCP.", exception);
        }
    }

    /**
     * Closes the HikariCP connection pool if it is initialized and open.
     */
    public void closeHikariConnection() {
        if (dataSource != null && !dataSource.isClosed()) {
            dataSource.close();
            simpleLogger.log(Level.INFO, "HikariCP connection pool closed successfully.", null);
        }
    }

    /**
     * Retrieves the currently initialized HikariCP data source.
     */
    public static HikariDataSource getDataSource() {
        return dataSource;
    }

    /**
     * Loads the MariaDB database driver required to enable database connections.
     */
    private void loadDatabaseDriver() {
        try {
            Class.forName("org.mariadb.jdbc.Driver");
            simpleLogger.log(Level.INFO, "MariaDB driver loaded successfully.", null);
        } catch (ClassNotFoundException exception) {
            simpleLogger.log(Level.SEVERE, "MariaDB driver not found", exception);
        }
    }

    /**
     * Builds and configures a {@link HikariConfig} instance based on the database configuration.
     *
     * @return a configured {@link HikariConfig} object ready to be used with {@link HikariDataSource}.
     */
    private @NotNull HikariConfig getHikariConfig() {
        HikariConfig config = new HikariConfig();
        config.setJdbcUrl(String.format(
                "jdbc:mariadb://%s:%d/%s",
                mySqlConfiguration.getHost(),
                mySqlConfiguration.getPort(),
                mySqlConfiguration.getDatabase()
        ));
        config.setUsername(mySqlConfiguration.getUser());
        config.setPassword(mySqlConfiguration.getPassword());

        config.setMaximumPoolSize(hikariConfiguration.getPooling().getMaxConnections());
        config.setMinimumIdle(hikariConfiguration.getPooling().getMinIdleConnections());
        config.setConnectionTimeout(hikariConfiguration.getPooling().getConnectionTimeout());
        config.setIdleTimeout(hikariConfiguration.getPooling().getIdleTimeout());
        config.setMaxLifetime(hikariConfiguration.getPooling().getMaxLifetime());
        config.setLeakDetectionThreshold(hikariConfiguration.getDetection().getThreshold());

        config.addDataSourceProperty("cachePrepStmts", String.valueOf(hikariConfiguration.getCaching().isEnableCaching()));
        config.addDataSourceProperty("prepStmtCacheSize", String.valueOf(hikariConfiguration.getCaching().getCacheSize()));
        config.addDataSourceProperty("prepStmtCacheSqlLimit", String.valueOf(hikariConfiguration.getCaching().getSqlLimit()));

        return config;
    }
}
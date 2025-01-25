package com.tnecnivvv.lobbySystem.plugin;

import com.tnecnivvv.api.config.ConfigurationAPI;
import com.tnecnivvv.api.mysql.DatabaseAPI;
import com.tnecnivvv.api.mysql.redis.Redis;
import com.tnecnivvv.api.mysql.source.DataSource;
import com.tnecnivvv.lobbySystem.Registry;
import com.tnecnivvv.lobbySystem.dependencies.SimpleLogger;

public class ServiceLocator {

    private volatile static ServiceLocator instance;
    private final Registry registry;

    private ServiceLocator() {
        registry = Registry.getInstance();
    }

    public static ServiceLocator getInstance() {
        if (instance == null) {
            synchronized (ServiceLocator.class) {
                if (instance == null) {
                    instance = new ServiceLocator();
                }
            }
        }
        return instance;
    }

    public ConfigurationAPI getConfigurationAPI() {
        return registry.resolveClass(ConfigurationAPI.class);
    }

    public SimpleLogger getSimpleLogger() {
        return registry.resolveClass(SimpleLogger.class);
    }

    public DatabaseAPI getDatabaseAPI() {
        return registry.resolveClass(DatabaseAPI.class);
    }

    public DataSource getDataSource() {
        return registry.resolveClass(DataSource.class);
    }

    public Redis getRedis() {
        return registry.resolveClass(Redis.class);
    }
}
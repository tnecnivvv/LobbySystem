package com.tnecnivvv.lobbySystem.plugin;

import com.tnecnivvv.api.config.ConfigurationAPI;
import com.tnecnivvv.api.mysql.DatabaseAPI;
import com.tnecnivvv.api.mysql.redis.Redis;
import com.tnecnivvv.api.mysql.source.DataSource;
import com.tnecnivvv.lobbySystem.Registry;
import com.tnecnivvv.lobbySystem.dependencies.SimpleLogger;
import com.tnecnivvv.lobbySystem.dependencies.TextSerializer;
import com.tnecnivvv.lobbySystem.dependencies.Updater;

import java.util.logging.Level;

public class PluginHelper {

    private volatile static PluginHelper instance;
    private final Registry registry;

    private PluginHelper() {
        registry = Registry.getInstance();
    }

    /**
     * Singleton Instance
     *
     * @return {@link PluginHelper} instance
     */
    public static PluginHelper getInstance() {
        if (instance == null) {
            synchronized (PluginHelper.class) {
                if (instance == null) {
                    instance = new PluginHelper();
                }
            }
        }
        return instance;
    }

    public void initializeHelper() {
        registry.registerClass(ConfigurationAPI.class, new ConfigurationAPI());
        registry.registerClass(SimpleLogger.class, new SimpleLogger());

        registry.registerClass(DataSource.class, new DataSource());
        registry.registerClass(Redis.class, new Redis());
        registry.registerClass(DatabaseAPI.class, new DatabaseAPI());

        registry.registerClass(Updater.class, new Updater());
        registry.registerClass(TextSerializer.class, new TextSerializer());
    }

    public void initializeDependencies() {
        try {
            registry.resolveClass(ConfigurationAPI.class).saveConfiguration();
            registry.resolveClass(DatabaseAPI.class).initialize();

            Runnable updaterTasks = () -> {
                Updater updater = registry.resolveClass(Updater.class);
                updater.checkOnStart();
                updater.checkForNotification();
            };
            updaterTasks.run();
        } catch (Exception exception) {
            registry.resolveClass(SimpleLogger.class).log(Level.SEVERE, "Error initializing dependencies", exception);
        }
    }

    public void terminate() {
        try {
            registry.resolveClass(ConfigurationAPI.class).clearAllCache();
            registry.resolveClass(DatabaseAPI.class).terminate();
        } finally {
            registry.clearCache();
        }
    }
}
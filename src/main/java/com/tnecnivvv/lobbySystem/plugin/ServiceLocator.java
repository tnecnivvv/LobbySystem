package com.tnecnivvv.lobbySystem.plugin;

import com.tnecnivvv.api.config.ConfigurationAPI;
import com.tnecnivvv.lobbySystem.Registry;

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
}
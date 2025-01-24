package com.tnecnivvv.lobbySystem.plugin;

import com.tnecnivvv.api.config.ConfigurationAPI;
import com.tnecnivvv.lobbySystem.Registry;

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

    public void initialize() {
        registry.registerClass(ConfigurationAPI.class, new ConfigurationAPI());
    }

    public void terminate() {
        registry.clearCache();
    }
}
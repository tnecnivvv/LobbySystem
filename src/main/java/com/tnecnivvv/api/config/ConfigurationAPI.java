package com.tnecnivvv.api.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.yaml.YAMLFactory;
import com.tnecnivvv.lobbySystem.LobbySystem;
import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.io.IOException;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class ConfigurationAPI {

    private final LobbySystem lobbySystem;
    private final ObjectMapper objectMapper = new ObjectMapper(new YAMLFactory());
    private final Map<ConfigurationTypes, Object> cache = new ConcurrentHashMap<>();

    public ConfigurationAPI() {
        this.lobbySystem = LobbySystem.getInstance();
    }

    /**
     * Saves the configuration files for each config type defined in the ConfigurationTypes
     */
    public void saveConfiguration() {
        for (ConfigurationTypes configType : ConfigurationTypes.values()) {
            File configFile = new File(lobbySystem.getDataFolder(), configType.getResourcePath());
            if (!configFile.exists()) {
                lobbySystem.saveResource(configType.getResourcePath(), false);
            }
        }
    }

    /**
     * Retrieves a configuration of the specified type and class.
     *
     * @param configurationTypes the type of the configuration to retrieve
     * @param configurationClass the class representing the configuration structure
     * @param <T>                the type of the configuration class
     * @return the configuration object of the specified type
     * @throws RuntimeException if there is an error reading the configuration file
     */
    @SuppressWarnings("unchecked")
    public <T> T getConfiguration(@NotNull ConfigurationTypes configurationTypes, Class<T> configurationClass) {
        if (cache.containsKey(configurationTypes)) {
            return (T) cache.get(configurationTypes);
        }

        File configurationFile = new File(lobbySystem.getDataFolder(), configurationTypes.getResourcePath());

        if (!configurationFile.exists()) {
            lobbySystem.saveResource(configurationTypes.getResourcePath(), false);
        }

        try {
            T configuration = objectMapper.readValue(configurationFile, configurationClass);
            cache.put(configurationTypes, configuration);
            return configuration;
        } catch (IOException exception) {
            throw new RuntimeException(exception);
        }
    }

    /**
     * Clears the cache for a specific configuration type.
     *
     * @param configurationTypes the type of configuration to clear from cache
     */
    public void clearCache(@NotNull ConfigurationTypes configurationTypes) {
        cache.remove(configurationTypes);
    }

    /**
     * Clears the entire configuration cache.
     */
    public void clearAllCache() {
        cache.clear();
    }
}
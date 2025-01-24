package com.tnecnivvv.api.config;

public enum ConfigurationTypes {

    SETTINGS("configuration/settings.yml");

    private final String resourcePath;

    /**
     * Constructor to initialize the enum
     *
     * @param resourcePath The path to the configuration file within the plugin's resources
     */
    ConfigurationTypes(String resourcePath) {
        this.resourcePath = resourcePath;
    }

    /**
     * Gets the resource path for the configuration file
     *
     * @return The path to the configuration file
     */
    public String getResourcePath() {
        return resourcePath;
    }
}

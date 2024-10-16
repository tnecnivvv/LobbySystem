package de.vinnie.network.config;

public enum ConfigTypes {

    MYSQL("mysql.yml", "config/mysql.yml"),
    SETTINGS("settings.yml", "config/settings.yml"),
    PLAYER_DATA("player_data.yml", "config/player_data.yml");

    private final String fileName;
    private final String resourcePath;

    ConfigTypes(String fileName, String resourcePath) {
        this.fileName = fileName;
        this.resourcePath = resourcePath;
    }

    public String getFileName() {
        return fileName;
    }

    public String getResourcePath() {
        return resourcePath;
    }
}
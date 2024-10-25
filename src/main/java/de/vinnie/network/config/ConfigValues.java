package de.vinnie.network.config;

import com.fasterxml.jackson.annotation.JsonProperty;

public class ConfigValues {

    // --------------------------
    // DATABASE CONFIGURATION
    // --------------------------
    @JsonProperty("database")
    public DatabaseConfig databaseConfig;

    public static class DatabaseConfig {
        public String user;
        public String password;
        public String host;
        public int port;
        public String name;
    }

    // --------------------------
    // SSL CONFIGURATION SETTINGS
    // --------------------------
    @JsonProperty("ssl")
    public SSLConfig sslConfig;

    public static class SSLConfig {
        public boolean requireSSL;
        public boolean verifyServerCertificate;
        public String sslMode;
    }

    // --------------------------------
    // HIKARI CONNECTION POOL SETTINGS
    // --------------------------------
    @JsonProperty("hikari")
    public HikariConfig hikariConfig;

    public static class HikariConfig {
        public boolean cachePrepStmts;
        public int prepStmtCacheSize;
        public int prepStmtCacheSqlLimit;

        public int maximumPoolSize;
        public int minimumIdle;
        public int poolLifeTime;

        public int connectionTimeOut;
        public int idleTimeout;

        // Leak Detection Settings
        public int threshold;
    }

    // --------------------------
    // WORLD, SERVER AND PLUGIN SETTINGS
    // --------------------------
    public WorldConfig world;
    public ServerConfig server;
    public PluginConfig plugin;

    public static class WorldConfig {

        @JsonProperty("spawn-location")
        public SpawnLocation spawnLocation;

        @JsonProperty("set-auto-spawnlocation")
        public boolean setAutoSpawnLocation;

        @JsonProperty("do-daylight-cycle")
        public boolean doDaylightCycle;

        @JsonProperty("do-weather-cycle")
        public boolean doWeatherCycle;

        @JsonProperty("world-time")
        public int worldTime;

        @JsonProperty("clear-weather")
        public boolean clearWeather;

        public static class SpawnLocation {
            public String world;
            public double x;
            public double y;
            public double z;
            public float yaw;
            public float pitch;
        }
    }

    public static class ServerConfig {
        public String motd;

        @JsonProperty("max-players")
        public int maxPlayers;
    }

    public static class PluginConfig {
        @JsonProperty("enable-logging")
        public boolean loggingConfigure;
        @JsonProperty("check-for-updates")
        public boolean checkForUpdates;
    }

    // --------------------------------
    // PLAYER DATA FOR DATABASE
    // --------------------------------
    public PlayerDataConfig playerData;

    public static class PlayerDataConfig {
        public int networklevel;
        public String networkrank;
        public int sushi;
        public boolean termsofservice;
        public String language;
    }
}
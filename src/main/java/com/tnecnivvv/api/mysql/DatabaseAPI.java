package com.tnecnivvv.api.mysql;

import com.tnecnivvv.api.mysql.redis.Redis;
import com.tnecnivvv.api.mysql.source.DataSource;
import com.tnecnivvv.lobbySystem.LobbySystem;
import com.tnecnivvv.lobbySystem.dependencies.SimpleLogger;
import com.tnecnivvv.lobbySystem.plugin.ServiceLocator;
import org.bukkit.Bukkit;

import java.util.concurrent.CompletableFuture;
import java.util.logging.Level;

public class DatabaseAPI {

    private final LobbySystem lobbySystem;

    private final DataSource dataSource;
    private final Redis redis;
    private final SimpleLogger simpleLogger;

    public DatabaseAPI() {
        lobbySystem = LobbySystem.getInstance();

        dataSource = ServiceLocator.getInstance().getDataSource();
        redis = ServiceLocator.getInstance().getRedis();
        simpleLogger = ServiceLocator.getInstance().getSimpleLogger();
    }

    /**
     * Initializes the database-related services asynchronously.
     */
    public void initialize() {
        CompletableFuture.runAsync(() -> {
            try {
                dataSource.initializeHikari();
                redis.initializeRedis();

                Bukkit.getScheduler().runTask(lobbySystem, () ->
                        simpleLogger.log(Level.INFO, "DatabaseAPI initialized successfully.", null));
            } catch (Exception exception) {
                Bukkit.getScheduler().runTask(lobbySystem, () ->
                        simpleLogger.log(Level.SEVERE, "Error initializing DatabaseAPI.", exception));
            }
        });
    }

    /**
     * Terminates the database-related services asynchronously.
     */
    public void terminate() {
        CompletableFuture.runAsync(() -> {
            try {
                dataSource.closeHikariConnection();
                redis.terminateRedis();

                Bukkit.getScheduler().runTask(lobbySystem, () ->
                        simpleLogger.log(Level.INFO, "DatabaseAPI terminated successfully.", null));
            } catch (Exception exception) {
                Bukkit.getScheduler().runTask(lobbySystem, () ->
                        simpleLogger.log(Level.WARNING, "Error terminating DatabaseAPI.", exception));
            }
        });
    }
}

package com.tnecnivvv.lobbySystem;

import com.tnecnivvv.lobbySystem.plugin.PluginHelper;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.logging.Level;

public final class LobbySystem extends JavaPlugin {

    private static LobbySystem instance;
    private final PluginHelper pluginHelper;

    public LobbySystem() {
        pluginHelper = PluginHelper.getInstance();
    }

    public static LobbySystem getInstance() {
        return instance;
    }

    @Override
    public void onEnable() {
        try {
            instance = this;
        } finally {
            pluginHelper.initializeHelper();
            pluginHelper.initializeDependencies();
        }
    }

    @Override
    public void onDisable() {
        try {
            instance = null;
            pluginHelper.terminate();
        } catch (Exception exception) {
            getLogger().log(Level.SEVERE, "Failure on Disabling", exception.getMessage());
        }
    }
}
package de.vinnie;

import de.vinnie.network.ConfigManager;
import de.vinnie.network.SystemManager;
import org.bukkit.plugin.java.JavaPlugin;

public final class LobbySystem extends JavaPlugin {

    private static LobbySystem instance;

    public static LobbySystem getInstance() {
        return instance;
    }

    @Override
    public void onLoad() {
        instance = this;
        ConfigManager.loadConfigs();
    }

    @Override
    public void onEnable() {
        SystemManager.getSystemManager().initialize();
    }

    @Override
    public void onDisable() {

    }
}

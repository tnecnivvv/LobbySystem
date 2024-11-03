package de.vinnie;

import com.github.retrooper.packetevents.PacketEvents;
import com.github.retrooper.packetevents.event.PacketListenerPriority;
import de.vinnie.network.ConfigManager;
import de.vinnie.network.DatabaseManager;
import de.vinnie.network.SystemManager;
import de.vinnie.network.event.packet.ScoreboardPacketListener;
import de.vinnie.network.plugin.Updater;
import io.github.retrooper.packetevents.factory.spigot.SpigotPacketEventsBuilder;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.logging.Level;

public final class LobbySystem extends JavaPlugin {

    private static LobbySystem lobbySystem;
    private static DatabaseManager databaseManager;

    public static LobbySystem getLobbySystem() {
        return lobbySystem;
    }

    @Override
    public void onLoad() {
        PacketEvents.setAPI(SpigotPacketEventsBuilder.build(this));
        PacketEvents.getAPI().load();
        PacketEvents.getAPI().getSettings().reEncodeByDefault(false)
                .checkForUpdates(false)
                .bStats(false);
        PacketEvents.getAPI().getEventManager().registerListener(new ScoreboardPacketListener(), PacketListenerPriority.HIGHEST);
    }

    @Override
    public void onEnable() {
        lobbySystem = this;

        databaseManager = DatabaseManager.getDatabaseManager();
        SystemManager systemManager = SystemManager.getSystemManager();
        Updater updater = Updater.getUpdater();
        try {
            ConfigManager.loadConfigs();
            databaseManager.initialize();
            systemManager.initialize();
            PacketEvents.getAPI().init();
        } catch (Exception exception) {
            systemManager.log(Level.WARNING, "Failed to initialize components.", exception);
            this.getServer().getPluginManager().disablePlugin(this);
        }
        updater.checkForUpdates();
    }

    @Override
    public void onDisable() {
        databaseManager.cleanupConnections();
        PacketEvents.getAPI().terminate();
    }
}
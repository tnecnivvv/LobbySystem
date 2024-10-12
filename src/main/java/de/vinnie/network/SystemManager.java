package de.vinnie.network;

import de.vinnie.LobbySystem;
import de.vinnie.network.event.block.BlockBreakEvent;
import de.vinnie.network.event.block.BlockPlaceEvent;
import de.vinnie.network.event.entity.EntityDamageEvent;
import de.vinnie.network.event.entity.FoodLevelChangeEvent;
import de.vinnie.network.event.inventory.InventoryClickEvent;
import de.vinnie.network.event.player.PlayerJoinEvent;
import de.vinnie.network.event.player.PlayerQuitEvent;
import org.bukkit.ChatColor;
import org.bukkit.GameRule;
import org.bukkit.World;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryOpenEvent;

import java.util.Arrays;
import java.util.List;

public class SystemManager {

    private static SystemManager systemManager;
    private final LobbySystem lobbySystem;

    public SystemManager() {
        this.lobbySystem = LobbySystem.getInstance();
    }

    public static SystemManager getSystemManager() {
        if (systemManager == null) {
            systemManager = new SystemManager();
        }
        return systemManager;
    }

    private final int SERVER_MAX_PLAYERS = 50;
    private final String SERVER_MOTD = ChatColor.LIGHT_PURPLE + "               Aetheria Network" + ChatColor.GRAY + " [1.21+] \n" +
    ChatColor.GRAY + "                  Currently in Beta";
    private final List<Listener> listeners = Arrays.asList(
            new PlayerJoinEvent(),
            new PlayerQuitEvent(),
            new BlockBreakEvent(),
            new BlockPlaceEvent(),
            new EntityDamageEvent(),
            new FoodLevelChangeEvent(),
            new InventoryClickEvent()
    );

    public void initialize() {
        registerListeners();
        configureServer();
        configureWorlds();
    }

    private void registerListeners() {
        this.listeners.forEach(listener -> lobbySystem.getServer().getPluginManager().registerEvents(listener, lobbySystem));
        lobbySystem.getLogger().info("Successfully registered listeners");
    }

    private void configureServer() {
        lobbySystem.getServer().setMotd(SERVER_MOTD);
        lobbySystem.getServer().setMaxPlayers(SERVER_MAX_PLAYERS);
        lobbySystem.getServer().getMessenger().registerOutgoingPluginChannel(lobbySystem, "BungeeCord");
        lobbySystem.getLogger().info("Successfully configured server");
    }

    private void configureWorlds() {
        for (World world : lobbySystem.getServer().getWorlds()) {
            world.setGameRule(GameRule.DO_DAYLIGHT_CYCLE, false);
            world.setGameRule(GameRule.DO_WEATHER_CYCLE, false);
            world.setTime(18000);
        }
        lobbySystem.getLogger().info("Successfully configured worlds");
    }
}

package de.vinnie.network;

import de.vinnie.LobbySystem;
import de.vinnie.network.config.ConfigTypes;
import de.vinnie.network.config.ConfigValues;
import de.vinnie.network.event.block.BlockBreakEvent;
import de.vinnie.network.event.block.BlockPlaceEvent;
import de.vinnie.network.event.entity.EntityDamageEvent;
import de.vinnie.network.event.entity.FoodLevelChangeEvent;
import de.vinnie.network.event.inventory.InventoryClickEvent;
import de.vinnie.network.event.player.PlayerJoinEvent;
import de.vinnie.network.event.player.PlayerQuitEvent;
import org.bukkit.ChatColor;
import org.bukkit.GameRule;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.event.Listener;

import java.util.Arrays;
import java.util.List;
import java.util.Objects;

public class SystemManager {

    private static volatile SystemManager systemManager;
    private final LobbySystem lobbySystem;

    private final List<Listener> listeners = Arrays.asList(
            new PlayerJoinEvent(),
            new PlayerQuitEvent(),
            new BlockBreakEvent(),
            new BlockPlaceEvent(),
            new EntityDamageEvent(),
            new FoodLevelChangeEvent(),
            new InventoryClickEvent()
    );

    /*
      __  __
     |  \/  | __ _ _ __   __ _  __ _  ___ _ __
     | |\/| |/ _` | '_ \ / _` |/ _` |/ _ \ '__|
     | |  | | (_| | | | | (_| | (_| |  __/ |
     |_|  |_|\__,_|_| |_|\__,_|\__, |\___|_|
                               |___/
    */
    public SystemManager() {
        this.lobbySystem = LobbySystem.getInstance();
    }

    public static SystemManager getSystemManager() {
        if (systemManager == null) {
            synchronized (SystemManager.class) {
                if (systemManager == null) {
                    systemManager = new SystemManager();
                }
            }
        }
        return systemManager;
    }

    public void initialize() {
        configureListeners();
        configureServer();
        configureWorlds();
    }

    /*
      _____         _
     |_   _|_ _ ___| | _____
       | |/ _` / __| |/ / __|
       | | (_| \__ \   <\__ \
       |_|\__,_|___/_|\_\___/
    */
    private void configureListeners() {
        this.listeners.forEach(listener -> lobbySystem.getServer().getPluginManager().registerEvents(listener, lobbySystem));
        logInfo("Successfully registered listeners");
    }

    private void configureServer() {
        ConfigValues.ServerConfig configValues = Objects.requireNonNull(ConfigManager.getConfig(ConfigTypes.SETTINGS)).server;
        lobbySystem.getServer().setMotd(ChatColor.translateAlternateColorCodes('&', configValues.motd));
        lobbySystem.getServer().setMaxPlayers(configValues.maxPlayers);
        lobbySystem.getServer().getMessenger().registerOutgoingPluginChannel(lobbySystem, "BungeeCord");
        logInfo("Successfully configured server");
    }

    private void configureWorlds() {
        ConfigValues.WorldConfig worldConfig = Objects.requireNonNull(ConfigManager.getConfig(ConfigTypes.SETTINGS)).world;
        for (World world : lobbySystem.getServer().getWorlds()) {
            applyWorldGameRules(world, worldConfig);
            applyWorldWeather(world, worldConfig);
            applySpawnLocation(world, worldConfig);
        }
        logInfo("Successfully configured worlds");
    }

    /*
      _   _      _                   __  __      _   _               _
     | | | | ___| |_ __   ___ _ __  |  \/  | ___| |_| |__   ___   __| |___
     | |_| |/ _ \ | '_ \ / _ \ '__| | |\/| |/ _ \ __| '_ \ / _ \ / _` / __|
     |  _  |  __/ | |_) |  __/ |    | |  | |  __/ |_| | | | (_) | (_| \__ \
     |_| |_|\___|_| .__/ \___|_|    |_|  |_|\___|\__|_| |_|\___/ \__,_|___/
                  |_|
    */
    public void logInfo(String message) {
        ConfigValues.PluginConfig pluginConfig = Objects.requireNonNull(ConfigManager.getConfig(ConfigTypes.SETTINGS)).plugin;
        if(pluginConfig.loggingConfigure) {
            lobbySystem.getLogger().info(message);
        }
    }

    private void applyWorldGameRules(World world, ConfigValues.WorldConfig worldConfig) {
        world.setGameRule(GameRule.DO_DAYLIGHT_CYCLE, worldConfig.doDaylightCycle);
        world.setGameRule(GameRule.DO_WEATHER_CYCLE, worldConfig.doWeatherCycle);
        world.setTime(worldConfig.worldTime);
    }

    private void applyWorldWeather(World world, ConfigValues.WorldConfig worldConfig) {
        if (worldConfig.clearWeather) {
            world.setStorm(false);
            world.setThundering(false);
            world.setWeatherDuration(12000);
            logInfo("Successfully enabled clear weather.");
        } else {
            world.setStorm(true);
            world.setThundering(true);
        }
    }

    private void applySpawnLocation(World world, ConfigValues.WorldConfig worldConfig) {
        if (worldConfig.setAutoSpawnLocation) {
            ConfigValues.WorldConfig.SpawnLocation spawnLocation = worldConfig.spawnLocation;

            if (spawnLocation != null && spawnLocation.world != null) {
                Location location = new Location(
                        lobbySystem.getServer().getWorld(spawnLocation.world),
                        spawnLocation.x,
                        spawnLocation.y,
                        spawnLocation.z,
                        spawnLocation.yaw,
                        spawnLocation.pitch
                );

                world.setSpawnLocation(location);
                logInfo("Successfully set Spawnlocation.");
            } else {
                logInfo("Invalid spawn location in configuration.");
            }
        }
    }
}

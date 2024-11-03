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
import de.vinnie.network.event.player.PlayerLoginEvent;
import de.vinnie.network.event.player.PlayerQuitEvent;
import de.vinnie.network.scoreboard.ScoreboardTask;
import org.bukkit.GameRule;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.event.Listener;

import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.logging.Level;

public class SystemManager {

    /*
      __  __
     |  \/  | __ _ _ __   __ _  __ _  ___ _ __
     | |\/| |/ _` | '_ \ / _` |/ _` |/ _ \ '__|
     | |  | | (_| | | | | (_| | (_| |  __/ |
     |_|  |_|\__,_|_| |_|\__,_|\__, |\___|_|
                               |___/
    */
    private static volatile SystemManager systemManager;
    private final LobbySystem lobbySystem;

    private SystemManager() {
        this.lobbySystem = LobbySystem.getLobbySystem();
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
        registerListeners();
        configureServer();
        adjustWorlds();
        scheduleTasks();
    }

    /*
      _____         _
     |_   _|_ _ ___| | _____
       | |/ _` / __| |/ / __|
       | | (_| \__ \   <\__ \
       |_|\__,_|___/_|\_\___/
    */
    private final List<Listener> listeners = Arrays.asList(
            new PlayerLoginEvent(),
            new PlayerJoinEvent(),
            new PlayerQuitEvent(),
            new BlockBreakEvent(),
            new BlockPlaceEvent(),
            new EntityDamageEvent(),
            new FoodLevelChangeEvent(),
            new InventoryClickEvent()
    );

    private void registerListeners() {
        this.listeners.forEach(listener -> lobbySystem.getServer().getPluginManager().registerEvents(listener, lobbySystem));
    }

    private void configureServer() {
        ConfigValues.ServerConfig configValues = Objects.requireNonNull(ConfigManager.getConfig(ConfigTypes.SETTINGS)).server;
        lobbySystem.getServer().setMotd(ComponentManager.getComponentManager().serializeToLegacy(configValues.motd));
        lobbySystem.getServer().setMaxPlayers(configValues.maxPlayers);
    }

    private void adjustWorlds() {
        ConfigValues.WorldConfig worldConfig = Objects.requireNonNull(ConfigManager.getConfig(ConfigTypes.SETTINGS)).world;

        for (World world : lobbySystem.getServer().getWorlds()) {
            applyWorldGameRules(world, worldConfig);
            applyWorldWeather(world, worldConfig);
            applySpawnLocation(world, worldConfig);
        }
    }

    private void scheduleTasks() {
        new ScoreboardTask().runTaskTimer(lobbySystem, 0L, 30L);
    }

    /*
      _   _      _                   __  __      _   _               _
     | | | | ___| |_ __   ___ _ __  |  \/  | ___| |_| |__   ___   __| |___
     | |_| |/ _ \ | '_ \ / _ \ '__| | |\/| |/ _ \ __| '_ \ / _ \ / _` / __|
     |  _  |  __/ | |_) |  __/ |    | |  | |  __/ |_| | | | (_) | (_| \__ \
     |_| |_|\___|_| .__/ \___|_|    |_|  |_|\___|\__|_| |_|\___/ \__,_|___/
                  |_|
    */
    public void log(Level level, String message, Throwable exception) {
        ConfigValues.PluginConfig pluginConfig = Objects.requireNonNull(ConfigManager.getConfig(ConfigTypes.SETTINGS)).plugin;
        if (pluginConfig.loggingConfigure) {
            if (exception != null) {
                lobbySystem.getLogger().log(level, message, exception);
            } else {
                lobbySystem.getLogger().log(level, message);
            }
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
                log(Level.INFO,"successfully set spawn location.", null);
            } else {
                log(Level.WARNING,"Invalid spawn location in configuration.", null);
            }
        }
    }
}

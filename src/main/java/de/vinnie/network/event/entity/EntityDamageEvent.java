package de.vinnie.network.event.entity;

import de.vinnie.network.ConfigManager;
import de.vinnie.network.config.ConfigTypes;
import de.vinnie.network.config.ConfigValues;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.jetbrains.annotations.NotNull;

public class EntityDamageEvent implements Listener {

    @EventHandler
    public void onEntityDamage(org.bukkit.event.entity.EntityDamageEvent event) {
        Player player = (Player) event.getEntity();
        if (event.getCause() == org.bukkit.event.entity.EntityDamageEvent.DamageCause.VOID) {
            ConfigValues configValues = ConfigManager.getConfig(ConfigTypes.SETTINGS);
                event.setCancelled(true);
            if (configValues != null) {
                player.teleport(location(configValues));
            }
        }
        event.setCancelled(true);
    }

    private static @NotNull Location location(ConfigValues configValues) {
        World world = Bukkit.getWorld(configValues.world.spawnLocation.world);
        double adjustX = configValues.world.spawnLocation.x + 0.5;
        double adjustY = configValues.world.spawnLocation.y;
        double adjustZ = configValues.world.spawnLocation.z + 0.5;
        float yaw = configValues.world.spawnLocation.yaw;
        float pitch = configValues.world.spawnLocation.pitch;
        return new Location(world, adjustX, adjustY, adjustZ, yaw, pitch);
    }
}

package de.vinnie.network.event.entity;

import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

public class EntityDamageEvent implements Listener {

    @EventHandler
    public void onEntityDamage(org.bukkit.event.entity.EntityDamageEvent event) {
        Player player = (Player) event.getEntity();
        if(event.getCause() == org.bukkit.event.entity.EntityDamageEvent.DamageCause.VOID) {
            Location loc = player.getWorld().getSpawnLocation();
            double adjustX = loc.getX() + 0.5;
            double adjustY = loc.getY();
            double adjustZ = loc.getZ() + 0.5;
            int yaw = 90;
            int pitch = 0;
            Location spawnLocation = new Location(player.getWorld(), adjustX, adjustY, adjustZ, yaw, pitch);

            event.setCancelled(true);
            player.teleport(spawnLocation);
        }
        event.setCancelled(true);
    }
}

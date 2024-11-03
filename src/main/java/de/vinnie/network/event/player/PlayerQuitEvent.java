package de.vinnie.network.event.player;

import de.vinnie.mysql.cache.DataCache;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

public class PlayerQuitEvent implements Listener {

    @EventHandler
    public void onPlayerQuit(org.bukkit.event.player.PlayerQuitEvent event) {
        event.setQuitMessage(null);
        DataCache.getDataCache().removeAll(event.getPlayer().getUniqueId());
    }
}

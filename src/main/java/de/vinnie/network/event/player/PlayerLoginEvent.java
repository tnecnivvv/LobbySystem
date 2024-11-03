package de.vinnie.network.event.player;

import de.vinnie.mysql.cache.DataCache;
import de.vinnie.mysql.utility.tables.NetworkPlayerProfileTable;
import de.vinnie.network.SystemManager;
import org.apache.logging.log4j.core.util.SystemNanoClock;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

import java.util.logging.Level;

public class PlayerLoginEvent implements Listener {

    @EventHandler
    public void onPlayerLogin(org.bukkit.event.player.PlayerLoginEvent event) {
        NetworkPlayerProfileTable.getNetworkPlayerProfileTable().createPlayer(event.getPlayer());
        DataCache.getDataCache().receiveAll(event.getPlayer().getUniqueId());
    }
}
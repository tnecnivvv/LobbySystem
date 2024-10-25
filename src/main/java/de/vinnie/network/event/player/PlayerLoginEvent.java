package de.vinnie.network.event.player;

import de.vinnie.mysql.utility.tables.PlayerProfileTable;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

public class PlayerLoginEvent implements Listener {

    @EventHandler
    public void onPlayerLogin(org.bukkit.event.player.PlayerLoginEvent event) {
        PlayerProfileTable.createPlayer(event.getPlayer());
    }
}
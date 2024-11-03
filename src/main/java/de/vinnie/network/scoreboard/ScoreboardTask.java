package de.vinnie.network.scoreboard;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

public class ScoreboardTask extends BukkitRunnable {

    @Override
    public void run() {
        for(Player onlinePlayers : Bukkit.getOnlinePlayers()) {
            Scoreboard scoreboard = new Scoreboard(onlinePlayers);
            scoreboard.createScoreboard();
        }
    }
}
package de.vinnie.network.scoreboard;

import de.vinnie.localization.controller.ScoreboardController;
import de.vinnie.mysql.cache.DataCache;
import de.vinnie.network.LocalizationManager;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

import java.text.DecimalFormat;
import java.util.UUID;

public class Scoreboard extends ScoreboardUtil {

    public Scoreboard(Player player) {
        super(player, "");
    }

    @Override
    public void createScoreboard() {
        UUID uuid = player.getUniqueId();

        ScoreboardController scoreboardController = LocalizationManager.getLocalizationManager().getController(uuid, "ScoreboardController", ScoreboardController.class);
        String networkrank = DataCache.getDataCache().getRank().get(uuid);
        Integer Sushi = DataCache.getDataCache().getSushi().get(uuid);
        Integer minutes = DataCache.getDataCache().getPlaytime().get(uuid);

        String playerProfile = scoreboardController.getPlayerProfile();
        String playerRank = scoreboardController.getPlayerRank();
        String playerRankTranslated = scoreboardController.translateRank(uuid);
        String playerSushi = scoreboardController.getPlayerSushi();
        String playerPlaytime = scoreboardController.getPlayerPlaytime();
        String playerPlayTimeHour = scoreboardController.getPlayerPlaytimeHour();
        String playerTabHeader = scoreboardController.getPlayerTabHeader();
        String playerTabFooter = scoreboardController.getPlayerTabFooter();

        DecimalFormat df = new DecimalFormat("#.##");
        df.setGroupingUsed(true);
        df.setGroupingSize(3);
        String SushiFormatted = df.format(Sushi);
        String formattedSushi = SushiFormatted.replace(",", ChatColor.GRAY + "," + ChatColor.GRAY);

        double convertedPlaytime = minutes / 60.0;
        String playtime = String.format("%.2f", convertedPlaytime);

        String[] playtimeParts = playtime.split("\\.");
        String formattedPlaytime = ChatColor.GRAY + playtimeParts[0] + ChatColor.GRAY + "." + ChatColor.GRAY + playtimeParts[1];

        setTitle(ChatColor.LIGHT_PURPLE + "" + ChatColor.BOLD + "Aetheria.net");

        setScore("    ", 11);
        setScore(playerProfile, 10);
        setScore(ChatColor.GRAY + player.getName(), 9);
        setScore("   ", 8);
        setScore(playerRank, 7);
        setScore(ChatColor.GRAY + playerRankTranslated,6);
        setScore("  ", 5);
        setScore(playerSushi, 4);
        setScore(ChatColor.GRAY + formattedSushi, 3);
        setScore(" ", 2);
        setScore(playerPlaytime, 1);
        setScore(ChatColor.GRAY + formattedPlaytime + " " + playerPlayTimeHour, 0);
        sortAllPlayerTeams(player);

        setPlayerListHeaderFooter(player,
                ChatColor.LIGHT_PURPLE + "Aetheria" + ChatColor.GRAY + "." + ChatColor.LIGHT_PURPLE +"net \n"
                        + playerTabHeader,
                playerTabFooter);
    }
}
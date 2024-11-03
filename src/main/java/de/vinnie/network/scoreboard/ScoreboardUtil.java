package de.vinnie.network.scoreboard;

import de.vinnie.mysql.cache.DataCache;
import de.vinnie.network.ComponentManager;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.TextColor;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.scoreboard.DisplaySlot;
import org.bukkit.scoreboard.Objective;
import org.bukkit.scoreboard.Scoreboard;
import org.bukkit.scoreboard.Team;

import java.util.Objects;

public abstract class ScoreboardUtil {

    private static ComponentManager componentManager;

    private final Scoreboard scoreboard;
    private final Objective objective;

    public final Player player;

    public ScoreboardUtil(Player player, String title) {
        componentManager = ComponentManager.getComponentManager();
        this.player = player;

        if(player.getScoreboard().equals(Bukkit.getScoreboardManager().getMainScoreboard())) {
            player.setScoreboard(Bukkit.getScoreboardManager().getNewScoreboard());
        }
        this.scoreboard = player.getScoreboard();

        if(this.scoreboard.getObjective("sidebar") != null) {
            Objects.requireNonNull(this.scoreboard.getObjective("sidebar")).unregister();
        }
        this.objective = this.scoreboard.registerNewObjective("sidebar", "dummy", title);
        this.objective.setDisplaySlot(DisplaySlot.SIDEBAR);
    }

    public abstract void createScoreboard();

    public void setTitle(String title) {
        this.objective.setDisplayName(title);
    }

    public void setScore(String content, int score) {
        this.objective.getScore(content).setScore(score);
    }

    public void removeScore(String content) {
        this.scoreboard.resetScores(content);
    }

    public void sortAllPlayerTeams(Player player) {
        Bukkit.getOnlinePlayers().forEach(this::createPlayerTeams);
    }

    public void setPlayerListHeaderFooter(Player player, String header, String footer) {
        player.setPlayerListHeaderFooter(
                header,
                footer
        );
    }

    private void createPlayerTeams(Player player) {
        Scoreboard scoreboard = this.scoreboard;
        String[] teamNames = {
                "01admin", "02team", "03player"
        };
        for(String teamName : teamNames) {
            if(scoreboard.getTeam(teamName) == null) {
                scoreboard.registerNewTeam(teamName);
            }
        }
        Team adminTeam = scoreboard.getTeam("01admin");
        Team teamTeam = scoreboard.getTeam("02team");
        Team playerTeam = scoreboard.getTeam("03player");

        for (Player p : Bukkit.getOnlinePlayers()) {
            if (Objects.equals(DataCache.getDataCache().getRank().get(p.getUniqueId()), "Admin")) {
                if (adminTeam != null) {
                    adminTeam.setColor(ChatColor.RED);
                    adminTeam.setPrefix(componentManager.serializeToLegacy("<red>Admin <gray>| "));
                    adminTeam.addEntry(p.getName());
                    adminTeam.setOption(Team.Option.NAME_TAG_VISIBILITY, Team.OptionStatus.ALWAYS);
                    adminTeam.setOption(Team.Option.COLLISION_RULE, Team.OptionStatus.NEVER);
                }
            } else if (Objects.equals(DataCache.getDataCache().getRank().get(p.getUniqueId()), "Team")) {
                if (teamTeam != null) {
                    teamTeam.setColor(ChatColor.RED);
                    teamTeam.setPrefix(componentManager.serializeToLegacy("<red>Team <gray>| "));
                    teamTeam.addEntry(p.getName());
                    teamTeam.setOption(Team.Option.NAME_TAG_VISIBILITY, Team.OptionStatus.ALWAYS);
                    teamTeam.setOption(Team.Option.COLLISION_RULE, Team.OptionStatus.NEVER);
                }
            } else if (Objects.equals(DataCache.getDataCache().getRank().get(p.getUniqueId()), "Player")) {
                if (playerTeam != null) {
                    playerTeam.setColor(ChatColor.GRAY);
                    playerTeam.addEntry(p.getName());
                    playerTeam.setOption(Team.Option.NAME_TAG_VISIBILITY, Team.OptionStatus.ALWAYS);
                    playerTeam.setOption(Team.Option.COLLISION_RULE, Team.OptionStatus.NEVER);
                }
            }
        }
    }
}
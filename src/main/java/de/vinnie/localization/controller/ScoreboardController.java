package de.vinnie.localization.controller;

import de.vinnie.mysql.cache.DataCache;
import de.vinnie.network.ComponentManager;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.minimessage.MiniMessage;
import net.kyori.adventure.text.serializer.legacy.LegacyComponentSerializer;

import java.util.Properties;
import java.util.UUID;

public class ScoreboardController {

    private final String playerProfile;
    private final String playerRank;
    private final String playerRankAdmin;
    private final String playerRankTeam;
    private final String playerRankDefault;
    private final String playerRankError;
    private final String playerSushi;
    private final String playerPlaytime;
    private final String playerPlaytimeHour;
    private final String playerTabHeader;
    private final String playerTabFooter;

    public ScoreboardController(Properties properties) {
        ComponentManager componentManager = ComponentManager.getComponentManager();
        this.playerProfile = componentManager.serializeToLegacy(properties.getProperty("playerProfile"));
        this.playerRank = componentManager.serializeToLegacy(properties.getProperty("playerRank"));
        this.playerRankAdmin = componentManager.serializeToLegacy(properties.getProperty("playerRank.admin"));
        this.playerRankTeam = componentManager.serializeToLegacy(properties.getProperty("playerRank.team"));
        this.playerRankDefault = componentManager.serializeToLegacy(properties.getProperty("playerRank.default"));
        this.playerRankError = componentManager.serializeToLegacy(properties.getProperty("playerRank.error"));
        this.playerSushi = componentManager.serializeToLegacy(properties.getProperty("playerSushi"));
        this.playerPlaytime = componentManager.serializeToLegacy(properties.getProperty("playerPlaytime"));
        this.playerPlaytimeHour = componentManager.serializeToLegacy(properties.getProperty("playerPlaytimeHour"));
        this.playerTabHeader = componentManager.serializeToLegacy(properties.getProperty("playerTabHeader"));
        this.playerTabFooter = componentManager.serializeToLegacy(properties.getProperty("playerTabFooter"));
    }

    public String translateRank(UUID uuid) {
        String cachedRank = DataCache.getDataCache().getRank().get(uuid);
        return switch (cachedRank) {
            case "Admin" -> playerRankAdmin;
            case "Team" -> playerRankTeam;
            case "Player" -> playerRankDefault;
            default -> playerRankError;
        };
    }

    public String getPlayerProfile() {
        return playerProfile;
    }

    public String getPlayerRank() { return playerRank; }

    public String getPlayerSushi() {
        return playerSushi;
    }

    public String getPlayerPlaytime() {
        return playerPlaytime;
    }

    public String getPlayerPlaytimeHour() {
        return playerPlaytimeHour;
    }

    public String getPlayerTabHeader() {
        return playerTabHeader;
    }

    public String getPlayerTabFooter() {
        return playerTabFooter;
    }
}
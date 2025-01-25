package com.tnecnivvv.lobbySystem.dependencies;

import com.tnecnivvv.api.config.ConfigurationAPI;
import com.tnecnivvv.api.config.ConfigurationTypes;
import com.tnecnivvv.api.config.types.SettingConfiguration;
import com.tnecnivvv.lobbySystem.LobbySystem;
import com.tnecnivvv.lobbySystem.plugin.ServiceLocator;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URI;
import java.net.URL;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Updater {

    private final LobbySystem lobbySystem;
    private final Logger logger;
    private final ConfigurationAPI configurationAPI;

    public Updater() {
        lobbySystem = LobbySystem.getInstance();
        logger = lobbySystem.getLogger();
        configurationAPI = ServiceLocator.getInstance().getConfigurationAPI();
    }

    /**
     * Check for updates on server start
     */
    public void checkOnStart() {
        boolean checkOnStart = configurationAPI.getConfiguration(ConfigurationTypes.SETTINGS, SettingConfiguration.class).getUpdates().doCheckOnStartup();

        if (checkOnStart) {
            try {
                String githubPluginYmlContent = this.getPluginYmlContent();
                String githubVersion = this.getVersionFromPluginYml(githubPluginYmlContent);
                String currentVersion = lobbySystem.getPluginMeta().getVersion();

                if (githubVersion != null && !currentVersion.equals(githubVersion)) {
                    logger.warning("Plugin Version Outdated!");
                    logger.warning("Newest Version: " + githubVersion);
                    logger.warning("Your Version: " + currentVersion);
                } else {
                    logger.info("Plugin is up to date.");
                }

            } catch (IOException exception) {
                logger.log(Level.SEVERE, "Failed to check for updates.", exception);
            }
        }
    }

    /**
     * Check for updates notification
     */
    public void checkForNotification() {
        boolean doUpdateNotification = configurationAPI.getConfiguration(ConfigurationTypes.SETTINGS, SettingConfiguration.class).getUpdates().doNotifications();

        if (doUpdateNotification) {
            new BukkitRunnable() {
                @Override
                public void run() {
                    try {
                        String githubPluginYmlContent = getPluginYmlContent();
                        String githubVersion = getVersionFromPluginYml(githubPluginYmlContent);
                        String currentVersion = lobbySystem.getPluginMeta().getVersion();

                        for (Player onlinePlayer : Bukkit.getOnlinePlayers()) {
                            if(onlinePlayer.hasPermission("lobbysystem.update.notification")) {
                                if (githubVersion != null && !currentVersion.equals(githubVersion)) {
                                    onlinePlayer.sendMessage("Plugin Version Outdated!");
                                }
                            }
                        }
                    } catch (IOException exception) {
                        logger.log(Level.SEVERE, "Failed to fetch update information", exception);
                    }
                }
            }.runTaskTimerAsynchronously(lobbySystem, 0, 36000);
        }
    }

    /**
     * Fetches the content of the plugin.yml file from the GitHub repository
     *
     * @return A String containing the content of the plugin.yml file
     * @throws IOException If there is an issue reading from the URL
     */
    private String getPluginYmlContent() throws IOException {
        String apiUrl = "https://raw.githubusercontent.com/tnecnivvv/LobbySystem/beta/src/main/resources/plugin.yml";
        URL url = URI.create(apiUrl).toURL();
        BufferedReader in = new BufferedReader(new InputStreamReader(url.openStream()));
        StringBuilder content = new StringBuilder();
        String inputLine;

        while ((inputLine = in.readLine()) != null) {
            content.append(inputLine).append("\n");
        }
        in.close();

        return content.toString();
    }

    /**
     * Extracts the version information from the plugin.yml content using regex
     *
     * @param pluginYmlContent The content of the plugin.yml file as a string
     * @return The version of the plugin, or null if the version could not be found
     */
    private String getVersionFromPluginYml(String pluginYmlContent) {
        Pattern pattern = Pattern.compile("version:\\s*'?([^'\n]+)'?");
        Matcher matcher = pattern.matcher(pluginYmlContent);

        if (matcher.find()) {
            return matcher.group(1);
        }

        return null;
    }
}
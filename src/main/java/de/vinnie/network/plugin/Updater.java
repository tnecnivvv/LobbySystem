package de.vinnie.network.plugin;

import de.vinnie.LobbySystem;
import de.vinnie.network.ConfigManager;
import de.vinnie.network.config.ConfigTypes;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.Objects;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Updater {

    public static Updater updater;

    private Updater(){}

    public static Updater getUpdater() {
        if (updater == null) {
            updater = new Updater();
        }
        return updater;
    }

    public void checkForUpdates() {
        if (Objects.requireNonNull(ConfigManager.getConfig(ConfigTypes.SETTINGS)).plugin.checkForUpdates) {
            try {
                String githubPluginYmlContent = this.getPluginYmlContent();
                String githubVersion = this.getVersionFromPluginYml(githubPluginYmlContent);

                if (githubVersion != null && isPluginOutdated(githubVersion)) {
                    LobbySystem.getInstance().getLogger().severe("Plugin Version Outdated!");
                    LobbySystem.getInstance().getLogger().severe("Newest Version: " + githubVersion);
                    LobbySystem.getInstance().getLogger().severe("Your Version: " + LobbySystem.getInstance().getDescription().getVersion());
                } else {
                    LobbySystem.getInstance().getLogger().info("Plugin is up to date.");
                }

            } catch (IOException e) {
                LobbySystem.getInstance().getLogger().severe("Failed to check for updates: " + e.getMessage());
            }
        }
    }

    private String getPluginYmlContent() throws IOException {
        String apiUrl = "https://raw.githubusercontent.com/tnecnivvv/LobbySystem/beta/src/main/resources/plugin.yml";
        URL url = new URL(apiUrl);
        BufferedReader in = new BufferedReader(new InputStreamReader(url.openStream()));
        StringBuilder content = new StringBuilder();
        String inputLine;
        while ((inputLine = in.readLine()) != null) {
            content.append(inputLine).append("\n");
        }
        in.close();
        return content.toString();
    }

    private String getVersionFromPluginYml(String pluginYmlContent) {
        Pattern pattern = Pattern.compile("version:\\s*'?([^'\n]+)'?");
        Matcher matcher = pattern.matcher(pluginYmlContent);
        if (matcher.find()) {
            return matcher.group(1);
        }
        return null;
    }

    private boolean isPluginOutdated(String githubVersion) {
        String currentVersion = LobbySystem.getInstance().getDescription().getVersion();
        return !currentVersion.equals(githubVersion);
    }
}
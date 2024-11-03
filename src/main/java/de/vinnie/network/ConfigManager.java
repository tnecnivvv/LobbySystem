package de.vinnie.network;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.yaml.YAMLFactory;
import de.vinnie.LobbySystem;
import de.vinnie.network.config.ConfigTypes;
import de.vinnie.network.config.ConfigValues;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;

public class ConfigManager {

    private static final ObjectMapper mapper = new ObjectMapper(new YAMLFactory());

    private ConfigManager() {}

    public static void loadConfigs() {
        for (ConfigTypes config : ConfigTypes.values()) {
            saveConfigs(config);
        }
    }

    private static void saveConfigs(ConfigTypes configTypes) {
        File file = new File(LobbySystem.getLobbySystem().getDataFolder(), configTypes.getFileName());

        if (!file.exists()) {
            LobbySystem.getLobbySystem().getDataFolder().mkdirs();
            try (InputStream stream = LobbySystem.getLobbySystem().getResource(configTypes.getResourcePath())) {
                if (stream != null) {
                    Files.copy(stream, file.toPath(), StandardCopyOption.REPLACE_EXISTING);
                    LobbySystem.getLobbySystem().getLogger().info(configTypes.getFileName() + " copied to data folder.");
                } else {
                    LobbySystem.getLobbySystem().getLogger().warning(configTypes.getResourcePath() + " not found in resources.");
                }
            } catch (IOException e) {
                LobbySystem.getLobbySystem().getLogger().severe("Error copying " + configTypes.getFileName() + ": " + e.getMessage());
            }
        } else {
            LobbySystem.getLobbySystem().getLogger().info(configTypes.getFileName() + " successfully verified.");
        }
    }

    public static ConfigValues getConfig(ConfigTypes configTypes) {
        File configFile = new File(LobbySystem.getLobbySystem().getDataFolder(), configTypes.getFileName());

        if (!configFile.exists()) {
            LobbySystem.getLobbySystem().getLogger().warning("Configuration file " + configTypes.getFileName() + " does not exist.");
            return null;
        }

        try {
            return mapper.readValue(configFile, ConfigValues.class);
        } catch (IOException e) {
            LobbySystem.getLobbySystem().getLogger().severe("Error loading " + configTypes.getFileName() + ": " + e.getMessage());
            return null;
        }
    }
}
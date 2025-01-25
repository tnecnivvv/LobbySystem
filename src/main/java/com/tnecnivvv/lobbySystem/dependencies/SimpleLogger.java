package com.tnecnivvv.lobbySystem.dependencies;

import com.tnecnivvv.api.config.ConfigurationTypes;
import com.tnecnivvv.api.config.types.SettingConfiguration;
import com.tnecnivvv.lobbySystem.LobbySystem;
import com.tnecnivvv.lobbySystem.plugin.ServiceLocator;

import java.util.logging.Level;

public class SimpleLogger {

    private final LobbySystem lobbySystem;
    private final SettingConfiguration settingsConfig;

    public SimpleLogger() {
        lobbySystem = LobbySystem.getInstance();
        settingsConfig = ServiceLocator.getInstance().
                getConfigurationAPI().getConfiguration(
                        ConfigurationTypes.SETTINGS,
                        SettingConfiguration.class
                );
    }

    /**
     * Logs a message with the specified log level, including an optional exception
     *
     * @param level     The log level to use (e.g., INFO, WARNING, SEVERE)
     * @param message   The message to log
     * @param exception The exception to log, or null if no exception is provided
     * @see Level
     */
    public void log(Level level, String message, Throwable exception) {
        boolean doVerboselogging = settingsConfig.getLogging().doVerboseLogging();
        if (doVerboselogging) {
            if (exception != null) {
                lobbySystem.getLogger().log(level, message, exception);
            } else {
                lobbySystem.getLogger().log(level, message);
            }
        }
    }
}
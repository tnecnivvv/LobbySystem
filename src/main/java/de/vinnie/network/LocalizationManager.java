package de.vinnie.network;

import de.vinnie.localization.controller.ScoreboardController;
import de.vinnie.localization.properties.PropertyFileHandler;
import de.vinnie.mysql.cache.DataCache;

import java.util.*;

public class LocalizationManager {

    private static volatile LocalizationManager localizationManager;
    private final Map<String, String> localizations = Map.of(
            "English", "en_US",
            "German", "de_DE",
            "French", "fr_FR",
            "Spanish", "es_ES"
    );

    private final Map<String, Properties> propertiesCache = new HashMap<>();
    private final Map<UUID, Map<String, Object>> controllerCache = new HashMap<>();

    private final Map<String, Class<?>> controllerClasses = new HashMap<>();
    private final Map<String, List<String>> propertiesFilePrefixes = new HashMap<>();
    private final Map<String, List<String>> controllerPaths = new HashMap<>();

    private LocalizationManager() {
        registerController(
                "ScoreboardController",
                ScoreboardController.class,
                Collections.singletonList("network_scoreboard"),
                Collections.singletonList("properties/localization/scoreboard")
        );
    }

    public static LocalizationManager getLocalizationManager() {
        if (localizationManager == null) {
            synchronized (LocalizationManager.class) {
                if (localizationManager == null) {
                    localizationManager = new LocalizationManager();
                }
            }
        }
        return localizationManager;
    }

    public String getIdentifier(String languageIdentifier) {
        return localizations.get(languageIdentifier);
    }

    public String getName(String languageIdentifier) {
        return localizations.entrySet().stream()
                .filter(entry -> entry.getValue().equals(languageIdentifier))
                .map(Map.Entry::getKey)
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("Unknown language: " + languageIdentifier));
    }

    public String convertIdentifier(UUID uuid) {
        return getName(DataCache.getDataCache().getLanguage().get(uuid));
    }

    private void registerController(String controllerName, Class<?> controllerClass, List<String> prefixes, List<String> paths) {
        controllerClasses.put(controllerName, controllerClass);
        propertiesFilePrefixes.put(controllerName, prefixes);
        controllerPaths.put(controllerName, paths);
    }

    public <T> T getController(UUID uuid, String controllerName, Class<T> controllerClass) {
        String playerLanguageCode = DataCache.getDataCache().getLanguage().get(uuid);
        List<String> prefixes = propertiesFilePrefixes.get(controllerName);
        List<String> paths = controllerPaths.get(controllerName);

        if (prefixes == null || paths == null) {
            throw new IllegalArgumentException("No properties file found for controller: " + controllerName);
        }

        Properties properties = new Properties();
        for (int i = 0; i < prefixes.size(); i++) {
            String prefix = prefixes.get(i);
            String path = paths.get(i);
            String propertiesFileName = prefix + "_" + playerLanguageCode + ".properties";
            String cacheKey = path + "/" + propertiesFileName;

            Properties loadedProperties = propertiesCache.computeIfAbsent(cacheKey, key ->
                    PropertyFileHandler.getPropertyFileHandler().loadProperties(path, propertiesFileName));
            properties.putAll(loadedProperties);
        }

        try {
            T controller = controllerClass.getConstructor(Properties.class).newInstance(properties);
            controllerCache.computeIfAbsent(uuid, k -> new HashMap<>()).put(controllerName, controller);
            return controller;
        } catch (Exception e) {
            throw new RuntimeException("Failed to instantiate controller: " + controllerName, e);
        }
    }
}
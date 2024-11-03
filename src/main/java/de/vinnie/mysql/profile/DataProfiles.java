package de.vinnie.mysql.profile;

public enum DataProfiles {

    NETWORK_PLAYER_PROFILES("network_player_profiles"),
    NETWORK_SETTINGS_PROFILES("network_settings_profiles"),

    NETWORKLEVEL("networklevel"),
    NETWORKRANK("networkrank"),
    PLAYTIME("playtime"),
    SUSHI("sushi"),
    LANGUAGE("language");

    private final String key;

    DataProfiles(String key) {
        this.key = key;
    }

    public String getKey() {
        return key;
    }
}

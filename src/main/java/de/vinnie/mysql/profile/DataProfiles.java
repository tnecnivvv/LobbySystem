package de.vinnie.mysql.profile;

public enum DataProfiles {

    /*
      ____       _
     / ___|  ___| |__   ___ _ __ ___   __ _
     \___ \ / __| '_ \ / _ \ '_ ` _ \ / _` |
      ___) | (__| | | |  __/ | | | | | (_| |
     |____/ \___|_| |_|\___|_| |_| |_|\__,_|
    */
    NETWORK_PLAYER_PROFILES("network_player_profiles"),
    NETWORK_SETTINGS_PROFILES("network_settings_profiles"),

    /*
     __     __    _
     \ \   / /_ _| |_   _  ___  ___
      \ \ / / _` | | | | |/ _ \/ __|
       \ V / (_| | | |_| |  __/\__ \
        \_/ \__,_|_|\__,_|\___||___/
    */
    NETWORKLEVEL("networklevel"),
    NETWORKRANK("networkrank"),
    PLAYTIME("playtime"),
    SUSHI("sushi"),
    LANGUAGE("language");

    private final String value;

    DataProfiles(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }
}

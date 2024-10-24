package de.vinnie.mysql.profile;

public enum DataProfiles {

    /*
      _____     _     _
     |_   _|_ _| |__ | | ___
       | |/ _` | '_ \| |/ _ \
       | | (_| | |_) | |  __/
       |_|\__,_|_.__/|_|\___|
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
    GAMELEVEL("gamelevel"),
    GAMERANK("gamerank"),
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

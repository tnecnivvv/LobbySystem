package de.vinnie.mysql.cache;

import de.vinnie.LobbySystem;
import de.vinnie.mysql.profile.DataProfiles;
import de.vinnie.mysql.source.DataSource;
import de.vinnie.network.SystemManager;
import org.bukkit.entity.Player;

import java.sql.*;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import java.util.logging.Level;

public class DataCache {

    private static volatile DataCache dataCache;
    private final SystemManager systemManager;

    private DataCache() {
        systemManager = SystemManager.getSystemManager();
    }

    public static DataCache getDataCache() {
        if (dataCache == null) {
            synchronized (DataCache.class) {
                if (dataCache == null) {
                    dataCache = new DataCache();
                }
            }
        }
        return dataCache;
    }

    /*
       ____           _            __  __
      / ___|__ _  ___| |__   ___  |  \/  | __ _ _ __  ___
     | |   / _` |/ __| '_ \ / _ \ | |\/| |/ _` | '_ \/ __|
     | |__| (_| | (__| | | |  __/ | |  | | (_| | |_) \__ \
      \____\__,_|\___|_| |_|\___| |_|  |_|\__,_| .__/|___/
                                               |_|
    */
    private final ConcurrentHashMap<UUID, Integer> level = new ConcurrentHashMap<>();
    private final ConcurrentHashMap<UUID, String> rank = new ConcurrentHashMap<>();
    private final ConcurrentHashMap<UUID, Integer> playtime = new ConcurrentHashMap<>();
    private final ConcurrentHashMap<UUID, Integer> sushi = new ConcurrentHashMap<>();
    private final ConcurrentHashMap<UUID, String> language = new ConcurrentHashMap<>();

    public ConcurrentHashMap<UUID, Integer> getLevel() {
        return level;
    }

    public ConcurrentHashMap<UUID, String> getRank() {
        return rank;
    }

    public ConcurrentHashMap<UUID, Integer> getPlaytime() {
        return playtime;
    }

    public ConcurrentHashMap<UUID, Integer> getSushi() {
        return sushi;
    }

    public ConcurrentHashMap<UUID, String> getLanguage() {
        return language;
    }


    /*
       ____           _            __  __      _   _               _
     / ___|__ _  ___| |__   ___  |  \/  | ___| |_| |__   ___   __| |___
    | |   / _` |/ __| '_ \ / _ \ | |\/| |/ _ \ __| '_ \ / _ \ / _` / __|
    | |__| (_| | (__| | | |  __/ | |  | |  __/ |_| | | | (_) | (_| \__ \
     \____\__,_|\___|_| |_|\___| |_|  |_|\___|\__|_| |_|\___/ \__,_|___/
    */
    public <T> void add(ConcurrentHashMap<UUID, T> map, UUID key, T value) {
        map.putIfAbsent(key, value);
    }

    public <T> void remove(ConcurrentHashMap<UUID, T> map, UUID uuid) {
        map.remove(uuid);
    }

    public <T> void update(ConcurrentHashMap<UUID, T> map, UUID uuid, T value) {
        map.put(uuid, value);
    }

    private <T> void execute(DataProfiles tableProfile, DataProfiles columnProfile,
                             ConcurrentHashMap<UUID, T> map, Class<T> clazz, boolean isUpdate) {
        String column = columnProfile.getKey();
        String query = isUpdate
                ? "UPDATE " + tableProfile.getKey() + " SET " + column + " = ? WHERE uuid = ?"
                : "SELECT uuid, " + column + " FROM " + tableProfile.getKey();

        try (Connection connection = DataSource.getDataSource().getConnection();
             PreparedStatement statement = connection.prepareStatement(query)) {

            if (isUpdate) {
                for (Map.Entry<UUID, T> entry : map.entrySet()) {
                    UUID uuid = entry.getKey();
                    T value = entry.getValue();
                    if (clazz.isInstance(value)) {
                        statement.setObject(1, value);
                        statement.setString(2, uuid.toString());
                        statement.addBatch();
                    } else {
                        throw new ClassCastException("Type mismatch: Expected " + clazz.getName() + " but got " + value.getClass().getName());
                    }
                }
                statement.executeBatch();
            } else {
                ResultSet resultSet = statement.executeQuery();
                while (resultSet.next()) {
                    UUID uuid = UUID.fromString(resultSet.getString("uuid"));
                    Object result = resultSet.getObject(column);
                    if (clazz.isInstance(result)) {
                        map.put(uuid, clazz.cast(result));
                    } else {
                        throw new ClassCastException("Type mismatch: Expected " + clazz.getName() + " but got " + result.getClass().getName());
                    }
                }
            }
        } catch (SQLException e) {
            systemManager.log(Level.SEVERE, "Database operation failed.", e);
        } catch (ClassCastException e) {
            systemManager.log(Level.WARNING, "Type mismatch for column.", e);
        }
    }


    /*
      _   _      _                   __  __      _   _               _
     | | | | ___| |_ __   ___ _ __  |  \/  | ___| |_| |__   ___   __| |___
     | |_| |/ _ \ | '_ \ / _ \ '__| | |\/| |/ _ \ __| '_ \ / _ \ / _` / __|
     |  _  |  __/ | |_) |  __/ |    | |  | |  __/ |_| | | | (_) | (_| \__ \
     |_| |_|\___|_| .__/ \___|_|    |_|  |_|\___|\__|_| |_|\___/ \__,_|___/
                  |_|
    */
    public void receiveAll(UUID uuid) {
        LobbySystem lobbySystem = LobbySystem.getLobbySystem();
        try {
            execute(DataProfiles.NETWORK_PLAYER_PROFILES, DataProfiles.NETWORKLEVEL, level, Integer.class, false);
            execute(DataProfiles.NETWORK_PLAYER_PROFILES, DataProfiles.NETWORKRANK, rank, String.class, false);
            execute(DataProfiles.NETWORK_PLAYER_PROFILES, DataProfiles.PLAYTIME, playtime, Integer.class, false);
            execute(DataProfiles.NETWORK_PLAYER_PROFILES, DataProfiles.SUSHI, sushi, Integer.class, false);
            execute(DataProfiles.NETWORK_PLAYER_PROFILES, DataProfiles.LANGUAGE, language, String.class, false);
        } catch (ClassCastException e) {
            systemManager.log(Level.SEVERE, "Failed to load data for player " + uuid, e);
            lobbySystem.getServer().getScheduler().runTask(lobbySystem, () -> {
                Player player = lobbySystem.getServer().getPlayer(uuid);
                if (player != null && player.isOnline()) {
                    player.kickPlayer("Failed to load your data, please try again later.");
                }
            });
        } catch (Exception e) {
            systemManager.log(Level.SEVERE, "Unexpected error while loading data for player " + uuid, e);
            lobbySystem.getServer().getScheduler().runTask(lobbySystem, () -> {
                Player player = lobbySystem.getServer().getPlayer(uuid);
                if (player != null && player.isOnline()) {
                    player.kickPlayer("An unexpected error occurred, please try again later.");
                }
            });
        }
    }

    public void saveAll() {
        execute(DataProfiles.NETWORK_PLAYER_PROFILES, DataProfiles.NETWORKLEVEL, level, Integer.class, true);
        execute(DataProfiles.NETWORK_PLAYER_PROFILES, DataProfiles.NETWORKRANK, rank, String.class, true);
        execute(DataProfiles.NETWORK_PLAYER_PROFILES, DataProfiles.PLAYTIME, playtime, Integer.class, true);
        execute(DataProfiles.NETWORK_PLAYER_PROFILES, DataProfiles.SUSHI, sushi, Integer.class, true);
        execute(DataProfiles.NETWORK_PLAYER_PROFILES, DataProfiles.LANGUAGE, language, String.class, true);
    }

    public void removeAll(UUID uuid) {
        ConcurrentHashMap<UUID, ?>[] maps = new ConcurrentHashMap[]{level, rank, playtime, sushi, language};
        for (ConcurrentHashMap<UUID, ?> map : maps) {
            map.remove(uuid);
        }
    }

    public void clearAllCaches() {
        ConcurrentHashMap<?, ?>[] maps = new ConcurrentHashMap[]{level, rank, playtime, sushi, language};
        for (ConcurrentHashMap<?, ?> map : maps) {
            map.clear();
        }
    }
}
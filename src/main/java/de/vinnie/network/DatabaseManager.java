package de.vinnie.network;

import de.vinnie.mysql.cache.DataCache;
import de.vinnie.mysql.source.DataSource;
import de.vinnie.mysql.utility.tables.NetworkPlayerProfileTable;

import java.sql.Connection;
import java.sql.SQLException;

public class DatabaseManager {

    /*
      __  __
     |  \/  | __ _ _ __   __ _  __ _  ___ _ __
     | |\/| |/ _` | '_ \ / _` |/ _` |/ _ \ '__|
     | |  | | (_| | | | | (_| | (_| |  __/ |
     |_|  |_|\__,_|_| |_|\__,_|\__, |\___|_|
                               |___/
    */
    private static volatile DatabaseManager databaseManager;

    private DatabaseManager() {}

    public static DatabaseManager getDatabaseManager() {
        if (databaseManager == null) {
            synchronized (DatabaseManager.class) {
                if (databaseManager == null) {
                    databaseManager = new DatabaseManager();
                }
            }
        }
        return databaseManager;
    }

    /*
      _____         _
     |_   _|_ _ ___| | _____
       | |/ _` / __| |/ / __|
       | | (_| \__ \   <\__ \
       |_|\__,_|___/_|\_\___/
    */
    public void initialize() {
        openDatabaseConnection();
        createTables();
        // ...
    }

    public void cleanupConnections() {
        closeDatabaseConnection();
        // ...
    }


    /*
      _   _      _                   __  __      _   _               _
     | | | | ___| |_ __   ___ _ __  |  \/  | ___| |_| |__   ___   __| |___
     | |_| |/ _ \ | '_ \ / _ \ '__| | |\/| |/ _ \ __| '_ \ / _ \ / _` / __|
     |  _  |  __/ | |_) |  __/ |    | |  | |  __/ |_| | | | (_) | (_| \__ \
     |_| |_|\___|_| .__/ \___|_|    |_|  |_|\___|\__|_| |_|\___/ \__,_|___/
                  |_|
    */
    private void openDatabaseConnection() {
        DataSource.getInstance().openDataSource();
        DataCache.getDataCache().clearAllCaches();
    }

    private void closeDatabaseConnection() {
        DataSource.getInstance().closeDataSource();
    }

    private void createTables() {
        try (Connection conn = DataSource.getDataSource().getConnection()) {
            NetworkPlayerProfileTable.getNetworkPlayerProfileTable().createTable(conn);
        } catch (SQLException e) {
            e.fillInStackTrace();
        }
    }
}
package de.vinnie.network;

import de.vinnie.mysql.source.DataSource;
import de.vinnie.mysql.utility.tables.PlayerProfileTable;

import java.sql.Connection;
import java.sql.SQLException;

public class DatabaseManager {

    private static volatile DatabaseManager instance;
    /*
      __  __
     |  \/  | __ _ _ __   __ _  __ _  ___ _ __
     | |\/| |/ _` | '_ \ / _` |/ _` |/ _ \ '__|
     | |  | | (_| | | | | (_| | (_| |  __/ |
     |_|  |_|\__,_|_| |_|\__,_|\__, |\___|_|
                               |___/
    */
    private DatabaseManager() {}

    public static DatabaseManager getDatabaseManager() {
        if (instance == null) {
            synchronized (DatabaseManager.class) {
                if (instance == null) {
                    instance = new DatabaseManager();
                }
            }
        }
        return instance;
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
    }

    private void closeDatabaseConnection() {
        DataSource.getInstance().closeDataSource();
    }

    private void createTables() {
        try (Connection conn = DataSource.getDataSource().getConnection()) {
            PlayerProfileTable.createTable(conn);
        } catch (SQLException e) {
            e.fillInStackTrace();
        }
    }
}
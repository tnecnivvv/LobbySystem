package de.vinnie.mysql.utility.tables;

import de.vinnie.mysql.profile.DataProfiles;
import de.vinnie.mysql.source.DataSource;
import de.vinnie.network.ConfigManager;
import de.vinnie.network.config.ConfigTypes;
import de.vinnie.network.config.ConfigValues;
import org.bukkit.entity.Player;

import java.sql.*;
import java.text.SimpleDateFormat;
import java.util.Objects;
import java.util.UUID;

public class PlayerProfileTable {

    public PlayerProfileTable() {}

    public static void createTable(Connection conn) throws SQLException {
        String sql = "CREATE TABLE IF NOT EXISTS network_player_profiles ("
                + "uuid VARCHAR(36) PRIMARY KEY,"
                + "username VARCHAR(50) NOT NULL,"
                + "networklevel INT NOT NULL,"
                + "networkrank VARCHAR(20) NOT NULL,"
                + "sushi INT NOT NULL,"
                + "playtime INT NOT NULL,"
                + "termsofservice BOOLEAN NOT NULL,"
                + "language VARCHAR(20) NOT NULL,"
                + "first_login TIMESTAMP NOT NULL,"
                + "last_login TIMESTAMP NOT NULL"
                + ")";
        try (Statement stmt = conn.createStatement()) {
            stmt.executeUpdate(sql);
        }
    }

    public static void createPlayer(Player player) {
        ConfigValues.PlayerDataConfig playerDataConfig = Objects.requireNonNull(ConfigManager.getConfig(ConfigTypes.PLAYER_DATA)).playerData;
        UUID uuid = player.getUniqueId();
        String name = player.getName();

        if (playerDataConfig != null) {
            int networklevel = playerDataConfig.networklevel;
            String networkrank = playerDataConfig.networkrank;
            int sushi = playerDataConfig.sushi;
            int playtime = 0;
            boolean termsofservice = playerDataConfig.termsofservice;
            String language = playerDataConfig.language;
            Timestamp currentTime = new Timestamp(System.currentTimeMillis());
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            String formattedDate = sdf.format(new Date(currentTime.getTime()));

            try (Connection conn = DataSource.getDataSource().getConnection()) {
                String checkSql = "SELECT COUNT(*) FROM " + DataProfiles.NETWORK_PLAYER_PROFILES + " WHERE uuid = ?";
                try (PreparedStatement checkStmt = conn.prepareStatement(checkSql)) {
                    checkStmt.setString(1, uuid.toString());
                    try (ResultSet rs = checkStmt.executeQuery()) {
                        rs.next();
                        int count = rs.getInt(1);
                        if (count > 0) {
                            return;
                        }
                    }
                }

                try (PreparedStatement pstmt = conn.prepareStatement(insertSql)) {
                    pstmt.setString(1, uuid.toString());
                    pstmt.setString(2, name);
                    pstmt.setInt(3, networklevel);
                    pstmt.setString(4, networkrank);
                    pstmt.setInt(5, sushi);
                    pstmt.setInt(6, playtime);
                    pstmt.setBoolean(7, termsofservice);
                    pstmt.setString(8, language);
                    pstmt.setString(9, formattedDate);
                    pstmt.setString(10, formattedDate);
                    pstmt.executeUpdate();
                }
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        }
    }
}
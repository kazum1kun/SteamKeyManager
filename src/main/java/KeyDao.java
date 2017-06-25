import Utils.DBTool;

import java.sql.*;
import java.util.ArrayList;

/**
 * Key Database Access Operation
 *
 * @author Dontcampy
 */
public class KeyDao {
    private int dbType;
    private ArrayList<Key> keys;

    /**
     *
     * @param dbType = Utils.DBTool.MYSQL, Utils.DBTool.SQLITE ......
     * @throws SQLException
     */
    public KeyDao(int dbType) throws SQLException {
        this.dbType = dbType;
        init();
    }

    /**
     * Create the table if it has not been created.
     * @throws SQLException
     */

    private synchronized void init() throws SQLException {
        Connection conn = DBTool.getConnection(dbType);
        Statement stmt = conn.createStatement();
        String sqlStr = "";
        switch (dbType) {
            // There are some differences between SQLite and MySQL, like autoincrement and AUTO_INCREMENT.
            case DBTool.SQLITE:
                sqlStr = "CREATE TABLE IF NOT EXISTS gamekeys\n" +
                        "(\n" +
                        "    gamename VARCHAR(255) NOT NULL,\n" +
                        "    gamekey VARCHAR(50) NOT NULL PRIMARY KEY ,\n" +
                        "    note VARCHAR(255)\n" +
                        ");";
                break;
            case DBTool.MYSQL:
                sqlStr = "CREATE TABLE IF NOT EXISTS gamekeys\n" +
                        "(\n" +
                        "    gamename VARCHAR(255) NOT NULL,\n" +
                        "    gamekey VARCHAR(50) NOT NULL PRIMARY KEY ,\n" +
                        "    note VARCHAR(255)\n" +
                        ");";
                break;
            default:
                break;
        }
        stmt.executeUpdate(sqlStr);
        stmt.close();
        DBTool.closeDB();
    }

    /**
     * Select Methods
     * @throws SQLException
     */

    public synchronized ArrayList<Key> getAllKeys() throws SQLException {
        Connection conn = DBTool.getConnection(dbType);
        Statement stmt = conn.createStatement();
        String sqlStr = "select * from gamekeys";
        ResultSet rs = stmt.executeQuery(sqlStr);
        keys = new ArrayList<>();

        while (rs.next()) {
            Key key = new Key(rs.getString("gamename"),
                    rs.getString("gamekey"),
                    rs.getString("note"));
            keys.add(key);
            System.out.println(key);
        }
        rs.close();
        stmt.close();
        DBTool.closeDB();
        return keys;
    }

    // The method has not been tested yet.
    public synchronized ArrayList<Key> getKeysByGame(String game) throws SQLException {
        Connection conn = DBTool.getConnection(dbType);
        String sqlStr = "select * from gamekeys where gamename = ?";
        PreparedStatement prestmt = conn.prepareStatement(sqlStr);
        prestmt.setString(1, game);
        ResultSet rs = prestmt.executeQuery(sqlStr);
        keys = new ArrayList<>();

        while (rs.next()) {
            Key key = new Key(rs.getString("gamename"),
                    rs.getString("gamekey"),
                    rs.getString("note"));
            keys.add(key);
            System.out.println(key);
        }
        rs.close();
        prestmt.close();
        DBTool.closeDB();
        return keys;
    }

    /**
     * Insert Methods
     * @throws SQLException
     */

    public synchronized void insertKey(String game, String key, String note) throws SQLException {
        Connection conn = DBTool.getConnection(dbType);
        String sqlStr = "insert into gamekeys(gamename, gamekey, note) values(?, ?, ?)";
        PreparedStatement prestmt = conn.prepareStatement(sqlStr);

        prestmt.setString(1, game);
        prestmt.setString(2, key);
        prestmt.setString(3, note);

        prestmt.executeUpdate();
        prestmt.close();
        DBTool.closeDB();
    }

    // Insert function overload for Key.class
    public synchronized void insertKey(Key key) throws SQLException {
        Connection conn = DBTool.getConnection(dbType);
        String sqlStr = "insert into gamekeys(gamename, gamekey, note) values(?, ?, ?)";
        PreparedStatement prestmt = conn.prepareStatement(sqlStr);

        prestmt.setString(1, key.getGame());
        prestmt.setString(2, key.getKey());
        prestmt.setString(3, key.getNotes());

        prestmt.executeUpdate();
        prestmt.close();
        DBTool.closeDB();
    }

    /**
     * Delete Methods
     * @throws SQLException
     */

    public synchronized void delKey(String key) throws SQLException {
        Connection conn = DBTool.getConnection(dbType);
        String sqlStr = "delete from gamekeys where gamekey = ?";
        PreparedStatement prestmt = conn.prepareStatement(sqlStr);

        prestmt.setString(1, key);

        prestmt.executeUpdate();
        prestmt.close();
        DBTool.closeDB();
    }

    // Insert function overload for Key.class
    public synchronized void delKey(Key key) throws SQLException {
        Connection conn = DBTool.getConnection(dbType);
        String sqlStr = "delete from gamekeys where gamekey = ?";
        PreparedStatement prestmt = conn.prepareStatement(sqlStr);

        prestmt.setString(1, key.getKey());

        prestmt.executeUpdate();
        prestmt.close();
        DBTool.closeDB();
    }

    /**
     * Update Methods
     * @throws SQLException
     */

    public synchronized void updateGame(String game, String key) throws SQLException {
        Connection conn = DBTool.getConnection(dbType);
        String sqlStr = "update gamekeys set gamename = ? where gamekey = ?";
        PreparedStatement prestmt = conn.prepareStatement(sqlStr);

        prestmt.setString(1, game);
        prestmt.setString(2, key);

        prestmt.executeUpdate();
        prestmt.close();
        DBTool.closeDB();
    }

    public synchronized void updateKey(String oldKey, String newKey) throws SQLException {
        Connection conn = DBTool.getConnection(dbType);
        String sqlStr = "update gamekeys set gamekey = ? where gamekey = ?";
        PreparedStatement prestmt = conn.prepareStatement(sqlStr);

        prestmt.setString(1, newKey);
        prestmt.setString(2, oldKey);

        prestmt.executeUpdate();
        prestmt.close();
        DBTool.closeDB();
    }

    public synchronized void updateNote(String note, String key) throws SQLException {
        Connection conn = DBTool.getConnection(dbType);
        String sqlStr = "update gamekeys set note = ? where gamekey = ?";
        PreparedStatement prestmt = conn.prepareStatement(sqlStr);

        prestmt.setString(1, note);
        prestmt.setString(2, key);

        prestmt.executeUpdate();
        prestmt.close();
        DBTool.closeDB();
    }

    // DO NOT USE THIS FUNC TO UPDATE gamekey
    public synchronized void updateData(Key key) throws SQLException {
        Connection conn = DBTool.getConnection(dbType);
        String sqlStr = "update gamekeys set gamename = ?, note = ? where gamekey = ?";
        PreparedStatement prestmt = conn.prepareStatement(sqlStr);

        prestmt.setString(1, key.getGame());
        prestmt.setString(2, key.getNotes());
        prestmt.setString(3, key.getKey());

        prestmt.executeUpdate();
        prestmt.close();
        DBTool.closeDB();
    }
}

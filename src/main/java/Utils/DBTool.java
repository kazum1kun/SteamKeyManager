package Utils;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/**
 * MySQL and SQLite support
 *
 * @author Dontcampy
 */
public final class DBTool {
    public static final int SQLITE = 0;
    public static final int MYSQL = 1;

    /** The values are used for SQLite. */
    private static String sqlite_driver = "org.sqlite.JDBC";
    private static String sqlite_uri = "jdbc:sqlite:sample.db";

    /** The values are used for MySQL. */
    private static String mysql_username = "";
    private static String mysql_password = "";
    private static String mysql_driver = "com.mysql.jdbc.Driver";
    private static String mysql_uri = "jdbc:mysql://localhost:3306/key_manager";

    private static Connection connection = null;

    private DBTool(){}

    public static Connection getConnection(int type) {
        try {
            switch (type) {
                case SQLITE:
                    Class.forName(sqlite_driver);
                    connection = DriverManager.getConnection(sqlite_uri);
                    break;
                case MYSQL:
                    Class.forName(mysql_driver);
                    connection = DriverManager.getConnection(mysql_uri, mysql_username, mysql_password);
                    break;
                default:
                    break;
            }
            System.out.println("Connect to database successfully. " + connection);
        } catch (ClassNotFoundException | SQLException e) {
            System.out.println("Connect to database unsuccessfully.");
            e.printStackTrace();
        }
        return connection;
    }

    public static void closeDB() {
        try {
            connection.close();
        } catch (SQLException e) {
            System.out.println("Close database unsuccessfully.");
            e.printStackTrace();
        }
    }
}

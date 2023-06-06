package datastorage;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/**
 * The ConnectionBuilder class provides a connection to the database.
 * It follows the Singleton pattern to ensure only one connection is established.
 */
public class ConnectionBuilder {
    private static Connection conn;

    /**
     * Private constructor to prevent instantiation from outside the class.
     * Initializes the database connection.
     */
    private ConnectionBuilder() {
        try {
            // Load the JDBC driver
            Class.forName("org.hsqldb.jdbc.JDBCDriver");
            System.out.println("Working Directory = " + System.getProperty("user.dir"));

            // Establish the database connection
            conn = DriverManager.getConnection("jdbc:hsqldb:db/nursingHomeDB;shutdown=true;user=SA;password=SA");
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
            System.out.println("Treiberklasse konnte nicht gefunden werden!");
        } catch (SQLException e) {
            System.out.println("Verbindung zur Datenbank konnte nicht aufgebaut werden!");
            e.printStackTrace();
        }
    }

    /**
     * Returns the connection to the database.
     * If the connection is not already established, it creates a new connection.
     *
     * @return The connection object.
     */
    public static Connection getConnection() {
        if (conn == null) {
            new ConnectionBuilder();
        }
        return conn;
    }

    /**
     * Closes the database connection.
     * If the connection is already closed or null, this method does nothing.
     */
    public static void closeConnection() {
        try {
            if (conn != null) {
                conn.close();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
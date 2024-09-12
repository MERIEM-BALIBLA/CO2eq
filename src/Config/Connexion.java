package Config;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class Connexion {
    private static Connexion instance;
    private Connection connection;

    private String dbName = "Brief2";
    private  String user = "GreenPulse";
    private String password = "";
    public static Connexion getInstance() {
        if (instance == null) {
            instance = new Connexion();
        }
        return instance;
    }
    // Method to connect to the database
    public Connection connectToDB() {
        if (connection == null) {
            try {
                Class.forName("org.postgresql.Driver");
                String url = "jdbc:postgresql://localhost:5432/" + dbName;
                connection = DriverManager.getConnection(url, user, password);
                if (connection != null) {
                    System.out.println("Connected successfully");
                } else {
                    System.out.println("Connection failed");
                }
            } catch (Exception e) {
                System.out.println("Connection error: " + e.getMessage());
                e.printStackTrace();
            }
        } else {
            System.out.println("Using existing connection.");
        }
        return connection;
    }
    // Method to close the connection
    public void closeConnection() {
        if (connection != null) {
            try {
                connection.close();
                connection = null;
                System.out.println("Connection closed.");
            } catch (SQLException e) {
                System.out.println("Failed to close connection: " + e.getMessage());
                e.printStackTrace();
            }
        }
    }

}

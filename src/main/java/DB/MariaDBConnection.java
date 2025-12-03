package DB;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class MariaDBConnection implements iDatabase {
    private static MariaDBConnection instance;
    private Connection connection;
    private final String url = "jdbc:mariadb://localhost:3306/nombre_base_datos";
    private final String user = "user_mariadb";
    private final String password = "pwd";

    private MariaDBConnection(){
        try{
            // Cargar explicitamente el Driver
            Class.forName("org.mariadb.jdbc.Driver");
            connection = DriverManager.getConnection(url, user, password);
            System.out.println("MariaDB connection established");
        } catch (ClassNotFoundException e) {
            System.err.println("MariaDB Driver not found: " + e.getMessage());
        } catch (SQLException e) {
            System.err.println("MariaDB failed to establish connection:  " + e.getMessage());
        }
    }

    public Connection getConnection() {
        return connection;
    }

    public void disconnect() {
        try {
            if (connection != null && !connection.isClosed()) {
                connection.close();
                System.out.println("Disconnected from MariaDB successfully");
            }
        } catch (SQLException e) {
            System.err.println("MariaDB connection could not be closed: " + e.getMessage());
        }
    }

    public synchronized MariaDBConnection getDatabase(){
        if (instance == null) {
            instance = new MariaDBConnection();
        } else {
            try {
                if (instance.connection.isClosed()) {
                    instance = new MariaDBConnection();
                }
            } catch (SQLException e) {
                instance = new MariaDBConnection();
            }
        }
        return instance;
    }
}

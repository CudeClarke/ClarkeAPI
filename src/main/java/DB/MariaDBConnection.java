package DB;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class MariaDBConnection implements iDatabase {
    private Connection connection;

    @Override
    public Connection getConnection() {
        if (connection != null) return connection;

        try{
            // Cargar explicitamente el Driver
            Class.forName("org.mariadb.jdbc.Driver");

            String url = "jdbc:mariadb://localhost:3306/nombre_base_datos";
            String user = "user_mariadb";
            String password = "pwd";

            // Si ya existe una conexion valida que no esta cerrada la devuelvo
            if (connection != null && !connection.isClosed()) {
                return connection;
            }

            connection = DriverManager.getConnection(url, user, password);
            System.out.println("MariaDB connection established");
            return connection;
        } catch (ClassNotFoundException e) {
            System.err.println("MariaDB Driver not found: " + e.getMessage());
            return null;
        } catch (SQLException e) {
            System.err.println("MariaDB failed to establish connection:  " + e.getMessage());
            return null;
        }
    }

    @Override
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
}

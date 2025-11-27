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

            this.connection = DriverManager.getConnection(url, user, password);
            System.out.println("MariaDB connection established");
        } catch (ClassNotFoundException e) {
            System.err.println("MariaDB Driver not found");
            e.printStackTrace();
        } catch (SQLException e) {
            System.err.println("MariaDB failed to establish connection");
            e.printStackTrace();
        }
        return this.connection;
    }

    @Override
    public void disconnect() {
        if (connection != null) {
            try {
                connection.close();
                connection = null;
                System.out.println("Disconnected from MariaDB successfully");
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }
}

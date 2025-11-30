package DB;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class MySQLConnection implements iDatabase{
    private Connection connection;
    private final String url = "jdbc:mysql://localhost:3306/ClarkeDB";
    private final String user = "clarke";
    private final String password = "cudeca";

    @Override
    public Connection getConnection() {
        try {
            if(this.connection == null) {
                this.connection = DriverManager.getConnection(url, user, password);
                System.out.println("Connected to MySQL successfully");
            }
            return this.connection;
        } catch (SQLException e) {
            System.err.println("MySQL failed to establish connection:  " + e.getMessage());
            return null;
        }
    }

    @Override
    public void disconnect() {
        try {
            if(this.connection != null && !this.connection.isClosed()) {
                this.connection.close();
                System.out.println("Disconnected from MySQL successfully");
            }
        } catch (SQLException e) {
            System.err.println("MySQL connection could not be close:  " + e.getMessage());
        }
    }
}

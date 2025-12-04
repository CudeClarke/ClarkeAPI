package DB;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public final class MySQLConnection implements iDatabase{
    private static MySQLConnection instance;
    private Connection connection;
    private final String url = "jdbc:mysql://localhost:3306/ClarkeDB";
    private final String user = "clarke";
    private final String password = "cudeca";


    private MySQLConnection(){
        connect();
    }

    public void connect(){
        try {
            this.connection = DriverManager.getConnection(url, user, password);
            System.out.println("Connected to MySQL successfully");
        } catch (SQLException e) {
            System.err.println("MySQL failed to establish connection:  " + e.getMessage());
        }
    }

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

    public Connection getConnection() {
        return connection;
    }

    public static synchronized MySQLConnection getInstance() {
        if (instance == null) {
            instance = new MySQLConnection();
        } else {
            try {
                if (instance.connection.isClosed()) {
                    instance = new MySQLConnection();
                }
            } catch (SQLException e) {
                instance = new MySQLConnection();
            }
        }
        return instance;
    }
}

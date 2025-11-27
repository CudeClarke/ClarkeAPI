package DB;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class MySQLConnection implements iDatabase{
    private Connection connection;

    @Override
    public Connection getConnection() {
        try {
            String url = "jdbc:mysql://localhost:3306/clarkeca";
            String user = "user_mysql";
            String password = "pwd";
            this.connection = DriverManager.getConnection(url,user,password);
            System.out.println("Connected to MySQL successfully");
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return this.connection;
    }

    @Override
    public void disconnect() {
        if (this.connection != null) {
            try {
                this.connection.close();
                this.connection = null;
                System.out.println("Disconnected from MySQL successfully");
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }
}

package DB;

public class DatabaseFactory {

    public enum DbType {
        MYSQL, MARIADB
    }

    public static iDatabase getDatabase(DbType type) {
        return switch (type) {
            case MYSQL -> new MySQLConnection();
            case MARIADB -> new MariaDBConnection();
            // Aqui se pueden añadir tantas conexiones como queramos
            default -> throw new IllegalArgumentException("Unsupported database type");
        };
    }
}

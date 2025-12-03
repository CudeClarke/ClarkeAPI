package DB;

public class DatabaseFactory {

    public enum DbType {
        MYSQL, MARIADB
    }

    public static iDatabase getDatabase(DbType type) {
        return switch (type) {
            case MYSQL -> MySQLConnection.getDatabase();
            case MARIADB -> MariaDBConnection.getDatabase();
            // Aqui se pueden añadir tantas conexiones como queramos
            default -> throw new IllegalArgumentException("Unsupported database type");
        };
    }
}

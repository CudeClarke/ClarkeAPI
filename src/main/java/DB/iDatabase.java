package DB;

import java.sql.Connection;

public interface iDatabase {
    /**
     * Metodo comun para conectarse a una base datos
     * @return la propia conexion
     */
    Connection getConnection();

    /**
     * Metodo para manejar la logica de dexconexion
     */
    void disconnect();

    /**
     * Crea una instancia de la BD solicitada.
     * @param tipo "MySQL" o "MariaDB"
     * @return Una instancia de iDatabase lista para usa
     */
    static iDatabase create(String tipo) {
        if (tipo == null) return null;

        return switch (tipo.toLowerCase()) {
            case "mysql" -> new MySQLConnection();
            case "mariadb" -> new MariaDBConnection();
            // Aqui se agregan las que bd que hagan falta
            default -> {
                System.err.println("Error: " + tipo + " non supported");
                yield null;
            }
        };
    }
}

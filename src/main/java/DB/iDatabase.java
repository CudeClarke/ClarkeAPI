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
}

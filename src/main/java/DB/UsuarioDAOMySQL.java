package DB;

import Usuario.*;
import java.sql.*;

public class UsuarioDAOMySQL implements iUsuarioDAO{
    private Connection connection;

    public UsuarioDAOMySQL(Connection connection) {
        this.connection = connection;
    }

    /**
     * Metodo para registrar un usuario en la base de datos.
     * @param usuario Objeto con los datos a introducir en la bd.
     * @return True si se inserto correctamente o false si, o ya estaba en la bd u ocurrio un error.
     */
    public boolean register(UsuarioBase usuario) {
        String sql = "INSERT INTO usuario (nombre, email, dni, spam) VALUES (?, ?, ?, ?)";

        try(PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, usuario.getNombre());
            stmt.setString(2, usuario.getEmail());
            stmt.setString(3, usuario.getDni());
            stmt.setBoolean(4, usuario.isSpam());

            int rowsAffected = stmt.executeUpdate();
            return (rowsAffected > 0);
        } catch (SQLException e){
            System.err.println("Error registering an user: " + e.getMessage());
            return false;
        }
    }

    /**
     * Metodo para actualizar un usuario partiendo del dni
     * @param usuario Objeto con la informacion del usuario
     * @return True si se actualiza o false en otro caso
     */
    public boolean update(UsuarioBase usuario) {
        String sql = "UPDATE usuario SET nombre=?, email=?, spam=? WHERE dni=?";

        try (PreparedStatement stmt = connection.prepareStatement(sql)){
            stmt.setString(1, usuario.getNombre());
            stmt.setString(2, usuario.getEmail());
            stmt.setBoolean(3, usuario.isSpam());
            stmt.setString(4, usuario.getDni());

            int rowsAffected = stmt.executeUpdate();
            return (rowsAffected > 0);

        } catch (SQLException e){
            System.err.println("Error updating an user: " + e.getMessage());
            return false;
        }
    }

    /**
     * Metodo para buscar por el dni de un usuario.
     * @param dni String que buscamos.
     * @return El usuario si existe o null en otro caso.
     */
    public UsuarioBase searchByDni(String dni) {
        String sql = "SELECT * FROM usuario WHERE dni = ?";
        UsuarioBase user = null;

        try(PreparedStatement stmt = connection.prepareStatement(sql)){
            stmt.setString(1, dni);
            try (ResultSet rs = stmt.executeQuery()){
                if (rs.next()) {
                    user = new UsuarioBase(rs.getString("nombre"), rs.getString("email"), rs.getString("dni"));
                }
            }
        } catch (SQLException e){
            System.err.println("Error searching for user: " + e.getMessage());
        }
        return user;
    }

    /**
     * Metodo para borrar un usuario de la base de datos.
     * @param dni String del usuario a borrar.
     * @return True si se borro correctamente o False en otro caso.
     */
    public boolean delete(String dni) {
        String sql = "DELETE FROM usuarios WHERE dni = ?";

        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, dni);

            int rowsAffected = stmt.executeUpdate();
            return (rowsAffected > 0);

        } catch (SQLException e){
            System.err.println("Error deleting an user: " + e.getMessage());
            return false;
        }
    }
}

package DB.UserDAO;

import DB.MySQLConnection;
import Datos.Usuario.*;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class UsuarioDAOMySQL implements iUsuarioDAO{
    private static UsuarioDAOMySQL instance;
    private final Connection connection;

    private UsuarioDAOMySQL() {
        this.connection = MySQLConnection.getInstance().getConnection();
    }

    public static UsuarioDAOMySQL getInstance() {
        if (instance == null) instance = new UsuarioDAOMySQL();
        return instance;
    }

    /**
     * Metodo para registrar un usuario en la base de datos, aprovechando la herencia.
     * @param usuario Objeto con los datos a introducir en la bd, puede ser base o registrado.
     * @return True si se inserto correctamente o false si, o ya estaba en la bd u ocurrio un error.
     */
    public boolean registerUsuario(iUsuario usuario) {
        String sqlUsuario = "INSERT INTO usuario (DNI, Nombre, Apellidos, Email, SPAM) VALUES (?, ?, ?, ?, ?)";
        String sqlRegistrado = "INSERT INTO registrado (DNI_USUARIO, Telefono, Direccion_postal) VALUES (?, ?, ?)";

        PreparedStatement psUser = null;
        PreparedStatement psReg = null;

        try {
            // Desactivado para hacer la transacción manualmente
            connection.setAutoCommit(false);

            // Inserta en la tabla padre
            psUser = connection.prepareStatement(sqlUsuario);
            psUser.setString(1, usuario.getDni());
            psUser.setString(2, usuario.getNombre());
            psUser.setString(3, usuario.getApellidos());
            psUser.setString(4, usuario.getEmail());
            psUser.setBoolean(5, usuario.isSpam());
            psUser.executeUpdate();

            // Si es registrado, entonces inserta en la tabla hija
            if (usuario instanceof UsuarioRegistrado) {
                UsuarioRegistrado ur = (UsuarioRegistrado) usuario;
                psReg = connection.prepareStatement(sqlRegistrado);

                psReg.setString(1, ur.getDni());
                psReg.setString(2, ur.getTlf());
                psReg.setString(3, ur.getDireccion());
                psReg.executeUpdate();
            }

            // Confirma los cambios si ha salido bien
            connection.commit();
            return true;
        } catch (SQLException e) {
            System.err.println("Error resgistering an user" + e.getMessage());
            try {
                // Si falla, se deshace
                connection.rollback();
            } catch (SQLException ex) {
                ex.printStackTrace();
            }
            return false;
        } finally {
            try {
                // Restaura el autocommit
                connection.setAutoCommit(true);
                // Cierra los statements
                if (psUser != null) psUser.close();
                if (psReg != null) psReg.close();
            } catch (SQLException ex) {
                ex.printStackTrace();
            }
        }
    }

    /**
     * Metodo para actualizar un usuario partiendo del dni
     * @param usuario Objeto con la informacion del usuario
     * @return True si se actualiza o false en otro caso
     */
    public boolean updateUsuario(iUsuario usuario) {
        // SQL para datos comunes
        String sqlUpdateUsuario = "UPDATE usuario SET Nombre=?, Apellidos=?, Email=?, SPAM=? WHERE DNI=?";
        // SQL para datos específicos
        String sqlUpdateRegistrado = "UPDATE registrado SET Telefono=?, Direccion_postal=? WHERE DNI_USUARIO=?";

        PreparedStatement psUser = null;
        PreparedStatement psReg = null;

        try {
            // Misma lógica que en registerUsuario
            connection.setAutoCommit(false);

            // Actualizamos tabla padre
            psUser = connection.prepareStatement(sqlUpdateUsuario);
            psUser.setString(1, usuario.getNombre());
            psUser.setString(2, usuario.getApellidos());
            psUser.setString(3, usuario.getEmail());
            psUser.setBoolean(4, usuario.isSpam());

            psUser.setString(5, usuario.getDni());

            int filasUsuario = psUser.executeUpdate();

            // // Si hay 0 filas afectadas, el DNI no existia
            if (filasUsuario == 0) {
                connection.rollback();
                return false;
            }

            // Si es un UsuarioRegistrado, actualizamos también la tabla hija
            if (usuario instanceof UsuarioRegistrado) {
                UsuarioRegistrado ur = (UsuarioRegistrado) usuario;
                psReg = connection.prepareStatement(sqlUpdateRegistrado);

                // Cuidado con el orden de los interrogantes ?
                psReg.setString(1, ur.getTlf());         // 1º ? (Telefono)
                psReg.setString(2, ur.getDireccion());   // 2º ? (Direccion)
                psReg.setString(3, ur.getDni());         // 3º ? (WHERE DNI...)

                psReg.executeUpdate();
            }

            connection.commit();
            return true;

        } catch (SQLException e) {
            System.err.println("Error al actualizar usuario: " + e.getMessage());
            try {
                connection.rollback();
            } catch (SQLException ex) {
                ex.printStackTrace();
            }
            return false;
        } finally {
            try {
                connection.setAutoCommit(true);
                if (psUser != null) psUser.close();
                if (psReg != null) psReg.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * Metodo para buscar por el dni de un usuario.
     * @param dni String que buscamos.
     * @return El usuario (base/registrado) si existe o null en otro caso.
     */
    public iUsuario searchByDni(String dni) {
        String sql = "SELECT u.*, r.Telefono, r.Direccion_postal " +
                "FROM usuario u " +
                "LEFT JOIN registrado r ON u.DNI = r.DNI_USUARIO " +
                "WHERE u.DNI = ?";

        UsuarioBase user = null;

        try(PreparedStatement stmt = connection.prepareStatement(sql)){
            stmt.setString(1, dni);
            try (ResultSet rs = stmt.executeQuery()){
                if (rs.next()) {
                    user = mapUsuario(rs);
                }
            }
        } catch (SQLException e){
            System.err.println("Error searching for user: " + e.getMessage());
        }
        return user;
    }

    public List<iUsuario> getAllUsuarios() {
        List<iUsuario> list = new ArrayList<>();
        String sql = "SELECT u.*, r.Telefono, r.Direccion_postal " +
                "FROM usuario u " +
                "LEFT JOIN registrado r ON u.DNI = r.DNI_USUARIO";

        try (Statement stmt = connection.createStatement(); ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                list.add(mapUsuario(rs));
            }
        } catch (SQLException e){
            System.err.println("Error listing users: " + e.getMessage());
        }
        return list;
    }

    /**
     * Metodo para borrar un usuario (base/registrado) de la base de datos.
     * @param dni String del usuario a borrar.
     * @return True si se borro correctamente o False en otro caso.
     */
    public boolean deleteUsuario(String dni) {
        String sqlDeleteRegistrado = "DELETE FROM registrado WHERE DNI_USUARIO = ?";
        String sqlDeleteUsuario = "DELETE FROM usuario WHERE DNI = ?";

        PreparedStatement psReg = null;
        PreparedStatement psUser = null;

        try {
            // Misma lógica que en registerUsuario
            connection.setAutoCommit(false);

            // Intenta borrar de la tabla hija
            psReg = connection.prepareStatement(sqlDeleteRegistrado);
            psReg.setString(1, dni);
            psReg.executeUpdate();

            // Borra de la tabla padre
            psUser = connection.prepareStatement(sqlDeleteUsuario);
            psUser.setString(1, dni);
            int filasAfectadas = psUser.executeUpdate();

            // Si hay 0 filas afectadas, el usuario no existia
            if (filasAfectadas == 0) {
                connection.rollback(); // Deshacemos por seguridad
                return false;
            }

            connection.commit();
            return true;

        } catch (SQLException e) {
            System.err.println("Error al eliminar usuario: " + e.getMessage());
            try {
                connection.rollback();
            } catch (SQLException ex) {
                ex.printStackTrace();
            }
            return false;
        } finally {
            try {
                connection.setAutoCommit(true);
                if (psReg != null) psReg.close();
                if (psUser != null) psUser.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * Metodo auxiliar para mapear resultados
     * @param rs Set del que obtener los datos
     * @return Un usuario (base/registrado) en funcion de los datos del rs.
     * @throws SQLException
     */
    private UsuarioBase mapUsuario(ResultSet rs) throws SQLException {
        String dni = rs.getString("DNI");
        String nombre = rs.getString("Nombre");
        String apellidos = rs.getString("Apellidos");
        String email = rs.getString("Email");
        boolean spam = rs.getBoolean("SPAM");

        String direccion = rs.getString("Direccion_postal");

        if (direccion == null) {
            return new UsuarioBase(nombre, apellidos, email, dni, spam);
        } else {
            String tlf = rs.getString("Telefono");
            return new UsuarioRegistrado(nombre, apellidos, email, dni, spam, direccion, tlf);
        }
    }
}

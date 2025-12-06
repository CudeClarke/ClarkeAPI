package DB.UserDAO;

import DB.MySQLConnection;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

import static org.junit.jupiter.api.Assertions.*;

class UsuarioDAOMySQLTest {
    private Connection connection;
    private UsuarioDAOMySQL usuarioDAO;

    private final String DNI_TEST = "12345678A";

    @BeforeEach
    void setUp() {
        connection = MySQLConnection.getInstance().getConnection();
        usuarioDAO = UsuarioDAOMySQL.getInstance();
    }

    @AfterEach
    void tearDown() throws SQLException {
        String sqlDeleteRegistrado = "DELETE FROM registrado WHERE DNI_USUARIO = ?";
        String sqlDeleteUsuario = "DELETE FROM usuario WHERE DNI = ?";

        try (PreparedStatement psReg = connection.prepareStatement(sqlDeleteRegistrado);
             PreparedStatement psUser = connection.prepareStatement(sqlDeleteUsuario)) {

            // Primero tabla hija
            psReg.setString(1, DNI_TEST);
            psReg.executeUpdate();

            // Luego tabla padre
            psUser.setString(1, DNI_TEST);
            psUser.executeUpdate();
        }
    }
}
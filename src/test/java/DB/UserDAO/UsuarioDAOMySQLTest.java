package DB.UserDAO;

import DB.MySQLConnection;
import Datos.Usuario.UsuarioBase;
import Datos.Usuario.UsuarioRegistrado;
import Datos.Usuario.iUsuario;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

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

    @Test
    @DisplayName("Registrar y Buscar Usuario Registrado")
    void testRegisterSearch() {
        // GIVEN
        UsuarioRegistrado nuevo = new UsuarioRegistrado(
                "TestNombre", "TestApellido", "test@junit.com", DNI_TEST, true,
                "Calle Test 123", "600000000"
        );

        // WHEN
        boolean registrado = usuarioDAO.registerUsuario(nuevo);

        // THEN
        assertTrue(registrado, "El registro debería devolver true");

        iUsuario recuperado = usuarioDAO.searchByDni(DNI_TEST);
        assertNotNull(recuperado, "El usuario recuperado no debe ser null");

        // Verificamos que recupera la subclase correcta
        assertInstanceOf(UsuarioRegistrado.class, recuperado, "Debe ser instancia de UsuarioRegistrado");

        UsuarioRegistrado ur = (UsuarioRegistrado) recuperado;
        assertEquals("Calle Test 123", ur.getDireccion());
        assertEquals("600000000", ur.getTlf());
    }
}
package DB.UserDAO;

import Datos.Usuario.UsuarioBase;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

import Datos.Usuario.*;

@ExtendWith(MockitoExtension.class)
class UsuarioDAOMySQLTest {
    // Create false actors
    @Mock
    private Connection connectionMock;
    @Mock
    private PreparedStatement preparedStatementMock;
    @Mock
    private ResultSet resultSetMock;

    private UsuarioDAOMySQL usuarioDAO;

    @BeforeEach
    void setUp() throws SQLException {
        // Before each test, initialize a false connection
        usuarioDAO = new UsuarioDAOMySQL(connectionMock);
    }

    @Test
    void testRegisterUsuario() throws SQLException {
        UsuarioRegistrado usuario = new UsuarioRegistrado(
                "Juan", "Perez", "juan@test.com", "12345678A", false, "Calle Falsa 123", "600000000"
        );
        // When DAO ask for a PreparedStatement, we give a false one.
        when(connectionMock.prepareStatement(anyString())).thenReturn(preparedStatementMock);
        // When update is executed, return 1 (rows affected)
        when(preparedStatementMock.executeUpdate()).thenReturn(1);

        boolean result = usuarioDAO.registerUsuario(usuario);

        assertTrue(result, "Register should return true");

        // A few extra verifications
        // Verify that autocommit has been deactivated
        verify(connectionMock).setAutoCommit(false);
        // Verify that 2 statements where prepared (one for base, one for registrado)
        verify(connectionMock, times(2)).prepareStatement(anyString());
        // Verify that commit was done
        verify(connectionMock).commit();
    }

    @Test
    void testRegisterUsuarioFailRollback() throws SQLException {
        UsuarioBase usuario = new UsuarioBase("Ana", "Gomez", "ana@test.com", "87654321B", true);

        when(connectionMock.prepareStatement(anyString())).thenReturn(preparedStatementMock);
        // Simulates throwing an exception when update is executed
        when(preparedStatementMock.executeUpdate()).thenThrow(new SQLException("Conexion error"));

        boolean resultado = usuarioDAO.registerUsuario(usuario);
        assertFalse(resultado, "Register should fail and return false");

        // Verify that rollback attempt was made
        verify(connectionMock).rollback();
    }

    @Test
    void testSearchByDniFound() throws SQLException {
        String dniBuscado = "12345678A";

        // Configuration of all the calls that must be done
        // connection -> prepareStatement -> executeQuery -> ResultSet
        when(connectionMock.prepareStatement(anyString())).thenReturn(preparedStatementMock);
        when(preparedStatementMock.executeQuery()).thenReturn(resultSetMock);

        // Simulates the behavior of the ResultSet
        when(resultSetMock.next()).thenReturn(true); // Hay un resultado
        when(resultSetMock.getString("DNI")).thenReturn(dniBuscado);
        when(resultSetMock.getString("Nombre")).thenReturn("Carlos");
        when(resultSetMock.getString("Apellidos")).thenReturn("Sainz");
        when(resultSetMock.getString("Email")).thenReturn("carlos@test.com");
        when(resultSetMock.getBoolean("SPAM")).thenReturn(false);

        when(resultSetMock.getString("Direccion_postal")).thenReturn("Calle Circuito 55");
        when(resultSetMock.getString("Telefono")).thenReturn("600111222");

        var resultado = usuarioDAO.searchByDni(dniBuscado);

        assertNotNull(resultado);
        // Verifies the inheritance
        assertInstanceOf(UsuarioRegistrado.class, resultado);

        // Checks specific data
        UsuarioRegistrado ur = (UsuarioRegistrado) resultado;
        assertEquals("Calle Circuito 55", ur.getDireccion());
        assertEquals("Carlos", ur.getNombre());
    }

    @Test
    void testDeleteUsuario() throws SQLException {
        String dni = "12345678A";
        when(connectionMock.prepareStatement(anyString())).thenReturn(preparedStatementMock);
        // Simulates removing a row
        when(preparedStatementMock.executeUpdate()).thenReturn(1);

        boolean resultado = usuarioDAO.deleteUsuario(dni);

        assertTrue(resultado);
        verify(connectionMock).commit();
    }
}
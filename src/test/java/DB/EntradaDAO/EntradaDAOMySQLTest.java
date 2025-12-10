package DB.EntradaDAO;

import Datos.Entrada.Entrada;
import Datos.Entrada.iEntrada;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.sql.*;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class EntradaDAOMySQLTest {
    @Mock
    private Connection connectionMock;
    @Mock
    private PreparedStatement preparedStatementMock;
    @Mock
    private ResultSet resultSetMock;

    private EntradaDAOMySQL entradaDAO;

    @BeforeEach
    void setUp() {
        entradaDAO = new EntradaDAOMySQL(connectionMock);
    }

    @Test
    void testRegisterEntrada_Success() throws SQLException {
        // Create one entrada for testing
        iEntrada entrada = new Entrada(50, 25.50f, "VIP", "Entrada zona VIP");
        int idEvento = 1;

        when(connectionMock.prepareStatement(anyString())).thenReturn(preparedStatementMock);

        // Simulates that insert returns 1 (row affecter)
        when(preparedStatementMock.executeUpdate()).thenReturn(1);

        boolean resultado = entradaDAO.registerEntrada(entrada, idEvento);

        assertTrue(resultado, "Register should be true");

        // Verifies the parameters were in the same order as the SQL sentence
        verify(preparedStatementMock).setInt(1, idEvento);
        verify(preparedStatementMock).setFloat(2, 25.50f); // Precio
        verify(preparedStatementMock).setInt(3, 50);       // Cantidad (SubAforo)
        verify(preparedStatementMock).setString(4, "VIP"); // Nombre
    }

    @Test
    void testRegisterEntradaSQLFail() throws SQLException {
        iEntrada entrada = new Entrada(50, 25.50f, "VIP", "Desc");

        when(connectionMock.prepareStatement(anyString())).thenReturn(preparedStatementMock);
        // Simulates a SQL error
        when(preparedStatementMock.executeUpdate()).thenThrow(new SQLException("Error simulado"));

        boolean resultado = entradaDAO.registerEntrada(entrada, 1);

        assertFalse(resultado, "Debería devolver false si hay excepción SQL");
    }

    @Test
    void testSearchByEvento_EntradaFound() throws SQLException {
        int idEvento = 5;

        // Configure the sequence: connection -> statement -> resultSet
        when(connectionMock.prepareStatement(anyString())).thenReturn(preparedStatementMock);
        when(preparedStatementMock.executeQuery()).thenReturn(resultSetMock);

        // Simulates that there 1 result inside the list
        when(resultSetMock.next()).thenReturn(true, false); // Primera vez true, segunda false (fin loop)

        // Simulates the exact data used in the columns used in buildEntrada(ResultSet rs)
        when(resultSetMock.getInt("Id_entrada")).thenReturn(33);
        when(resultSetMock.getInt("Cantidad")).thenReturn(100);
        when(resultSetMock.getFloat("Precio")).thenReturn(15.0f);
        when(resultSetMock.getString("Nombre")).thenReturn("General");
        when(resultSetMock.getString("Descripcion")).thenReturn("Acceso general");

        List<iEntrada> lista = entradaDAO.searchByEvento(idEvento);

        assertNotNull(lista);
        assertEquals(1, lista.size());

        iEntrada entradaRecuperada = lista.getFirst();
        assertEquals("General", entradaRecuperada.getNombre());
        assertEquals(100, entradaRecuperada.getSubAforo());
    }

    @Test
    void testDeleteEntrada() throws SQLException {
        int idEntrada = 10;

        when(connectionMock.prepareStatement(anyString())).thenReturn(preparedStatementMock);
        when(preparedStatementMock.executeUpdate()).thenReturn(1);

        boolean resultado = entradaDAO.deleteEntrada(idEntrada);

        assertTrue(resultado);
        verify(preparedStatementMock).setInt(1, idEntrada);
    }

    @Test
    void testGetIDFound() throws SQLException {
        iEntrada entrada = new Entrada(100, 20.0f, "Test", "Desc");
        int idEvento = 1;

        when(connectionMock.prepareStatement(anyString())).thenReturn(preparedStatementMock);
        when(preparedStatementMock.executeQuery()).thenReturn(resultSetMock);

        // Simulate that it founds a result
        when(resultSetMock.next()).thenReturn(true);
        when(resultSetMock.getInt("ID_ENTRADA")).thenReturn(999);

        int idRecuperado = entradaDAO.getID(entrada, idEvento);

        assertEquals(999, idRecuperado);
    }

    @Test
    void testGetIDNotFound() throws SQLException {
        iEntrada entrada = new Entrada(100, 20.0f, "Test", "Desc");

        when(connectionMock.prepareStatement(anyString())).thenReturn(preparedStatementMock);
        when(preparedStatementMock.executeQuery()).thenReturn(resultSetMock);

        // Simulates it doesnt found anything
        when(resultSetMock.next()).thenReturn(false);

        int idRecuperado = entradaDAO.getID(entrada, 1);

        assertEquals(-1, idRecuperado);
    }
}
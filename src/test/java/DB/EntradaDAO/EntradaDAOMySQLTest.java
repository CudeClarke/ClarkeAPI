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
        // Usamos el constructor package-private para inyectar el mock de conexión
        entradaDAO = new EntradaDAOMySQL(connectionMock);
    }

    @Test
    void testRegisterEntrada_Success() throws SQLException {
        // subAforo=50, Precio=25.50, Nombre="VIP"
        iEntrada entrada = new Entrada(50, 25.50f, "VIP", "Entrada zona VIP");
        int idEvento = 1;

        when(connectionMock.prepareStatement(anyString())).thenReturn(preparedStatementMock);
        // Simula que el insert afecta a 1 fila
        when(preparedStatementMock.executeUpdate()).thenReturn(1);

        boolean resultado = entradaDAO.registerEntrada(entrada, idEvento);

        assertTrue(resultado, "El registro debería devolver true");

        // Verificamos que se pasan los parámetros en el orden correcto según el SQL
        // SQL: INSERT INTO ... VALUES (?, ?, ?, ?, ?)
        // 1: ID_EVENTO, 2: Precio, 3: Cantidad, 4: Nombre, 5: Descripcion
        verify(preparedStatementMock).setInt(1, idEvento);
        verify(preparedStatementMock).setFloat(2, 25.50f);
        verify(preparedStatementMock).setInt(3, 50);
        verify(preparedStatementMock).setString(4, "VIP");
        verify(preparedStatementMock).setString(5, "Entrada zona VIP");
    }

    @Test
    void testRegisterEntradaSQLFail() throws SQLException {
        iEntrada entrada = new Entrada(50, 25.50f, "VIP", "Desc");

        when(connectionMock.prepareStatement(anyString())).thenReturn(preparedStatementMock);
        // Simula un error de SQL al ejecutar el update
        when(preparedStatementMock.executeUpdate()).thenThrow(new SQLException("Error simulado de base de datos"));

        boolean resultado = entradaDAO.registerEntrada(entrada, 1);

        assertFalse(resultado, "Debería devolver false si salta una SQLException");
    }

    @Test
    void testSearchByEvento_EntradaFound() throws SQLException {
        int idEvento = 5;

        when(connectionMock.prepareStatement(anyString())).thenReturn(preparedStatementMock);
        when(preparedStatementMock.executeQuery()).thenReturn(resultSetMock);

        // Simula el bucle del ResultSet: encuentra uno (true) y luego termina (false)
        when(resultSetMock.next()).thenReturn(true, false);

        // Mockeamos las columnas que lee buildEntrada
        when(resultSetMock.getInt("Id_entrada")).thenReturn(33);
        when(resultSetMock.getInt("Cantidad")).thenReturn(100);
        when(resultSetMock.getFloat("Precio")).thenReturn(15.0f);
        when(resultSetMock.getString("Nombre")).thenReturn("General");
        when(resultSetMock.getString("Descripcion")).thenReturn("Acceso general");

        List<iEntrada> lista = entradaDAO.searchByEvento(idEvento);

        assertNotNull(lista);
        assertEquals(1, lista.size());

        iEntrada entradaRecuperada = lista.getFirst(); // Requiere Java 21+, si usas anterior usa .get(0)
        assertEquals("General", entradaRecuperada.getNombre());
        assertEquals(100, entradaRecuperada.getSubAforo());
        assertEquals(15.0f, entradaRecuperada.getPrecio());
        assertEquals(33, entradaRecuperada.getId());
    }

    @Test
    void testSearchById_Found() throws SQLException {
        int idEntrada = 99;

        when(connectionMock.prepareStatement(anyString())).thenReturn(preparedStatementMock);
        when(preparedStatementMock.executeQuery()).thenReturn(resultSetMock);
        when(resultSetMock.next()).thenReturn(true); // Encuentra resultado

        // Mockeamos datos
        when(resultSetMock.getInt("Id_entrada")).thenReturn(idEntrada);
        when(resultSetMock.getString("Nombre")).thenReturn("Entrada Unica");
        // ... resto de valores por defecto (0 o null) si no se especifican

        iEntrada resultado = entradaDAO.searchById(idEntrada);

        assertNotNull(resultado);
        assertEquals("Entrada Unica", resultado.getNombre());
        assertEquals(99, resultado.getId());
    }

    @Test
    void testDeleteEntrada_Success() throws SQLException {
        int idEntrada = 10;

        when(connectionMock.prepareStatement(anyString())).thenReturn(preparedStatementMock);
        when(preparedStatementMock.executeUpdate()).thenReturn(1); // 1 fila eliminada

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

        // Simula que NO encuentra nada
        when(resultSetMock.next()).thenReturn(false);

        int idRecuperado = entradaDAO.getID(entrada, 1);

        assertEquals(-1, idRecuperado);
    }
}
package DB.TicketDAO;

import Datos.Ticket.iTicket;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.sql.*;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.contains;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class TicketDAOMySQLTest {

    @Mock private Connection connectionMock;

    // Usaremos mocks específicos dentro de cada test cuando sea necesario
    // para evitar conflictos en llamadas anidadas.

    private TicketDAOMySQL ticketDAO;

    @BeforeEach
    void setUp() {
        ticketDAO = new TicketDAOMySQL(connectionMock);
    }

    @Test
    void testRegisterTicket() throws SQLException {
        PreparedStatement psMock = mock(PreparedStatement.class);

        // Configura para cualquier SQL (registro es simple, solo 1 llamada)
        when(connectionMock.prepareStatement(anyString())).thenReturn(psMock);
        when(psMock.executeUpdate()).thenReturn(1);

        iTicket ticketInput = mock(iTicket.class);
        when(ticketInput.getDniAsistente()).thenReturn("55555555X");
        when(ticketInput.getPagoExtra()).thenReturn(5.5f);

        boolean resultado = ticketDAO.registerTicket(ticketInput, "12345678Z", 10, "Info");

        assertTrue(resultado);
        verify(psMock).executeUpdate();
    }

    @Test
    void testDeleteTicket() throws SQLException {
        PreparedStatement psMock = mock(PreparedStatement.class);

        when(connectionMock.prepareStatement(anyString())).thenReturn(psMock);
        when(psMock.executeUpdate()).thenReturn(1);

        boolean resultado = ticketDAO.deleteTicket("T-100");

        assertTrue(resultado);
    }

    @Test
    void testSearchByUser_NestedCalls() throws SQLException {
        PreparedStatement psMain = mock(PreparedStatement.class);
        ResultSet rsMain = mock(ResultSet.class);

        PreparedStatement psType = mock(PreparedStatement.class);
        ResultSet rsType = mock(ResultSet.class);

        // Configura la llamada principal (Busca tickets por usuario)
        // Usa contains para detectar la query principal
        when(connectionMock.prepareStatement(contains("SELECT * FROM ticket")))
                .thenReturn(psMain);
        when(psMain.executeQuery()).thenReturn(rsMain);

        // Simula que encuentra 1 ticket
        when(rsMain.next()).thenReturn(true, false);
        when(rsMain.getInt("ID_TICKET")).thenReturn(100);
        when(rsMain.getString("Dni_asistente")).thenReturn("87654321B");
        when(rsMain.getString("Informacion")).thenReturn("InfoTicket");
        when(rsMain.getFloat("Pago_extra")).thenReturn(0.0f);

        // Configura la llamada anidada (ticketType)
        // Esta se llama DENTRO del bucle while(rsMain.next())
        when(connectionMock.prepareStatement(contains("ID_TIPO_EVENTO")))
                .thenReturn(psType);
        when(psType.executeQuery()).thenReturn(rsType);

        // Simula que el tipo es 1 (Carrera)
        when(rsType.next()).thenReturn(true);
        when(rsType.getInt("tipo")).thenReturn(1);

        List<iTicket> lista = ticketDAO.searchByUser("12345678A");

        assertNotNull(lista);
        assertFalse(lista.isEmpty());
        // Esperamos size 2, aunque sea el mismo objeto.
        assertEquals(2, lista.size());
        assertEquals("87654321B", lista.getFirst().getDniAsistente());

        // Verifica que se llamó a ambas consultas
        verify(psMain).executeQuery();
        verify(psType).executeQuery();
    }
}
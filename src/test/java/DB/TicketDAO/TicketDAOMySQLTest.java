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
    @Mock private PreparedStatement psMock;
    @Mock private ResultSet rsMock;

    private TicketDAOMySQL ticketDAO;

    @BeforeEach
    void setUp() throws SQLException {
        ticketDAO = new TicketDAOMySQL(connectionMock);
    }

    @Test
    void testRegisterTicket() throws SQLException {
        // --- GIVEN ---
        // Configuración simple: cualquier SQL devuelve el PreparedStatement mockeado
        when(connectionMock.prepareStatement(anyString())).thenReturn(psMock);
        when(psMock.executeUpdate()).thenReturn(1);

        iTicket ticketInput = mock(iTicket.class);
        when(ticketInput.getDniAsistente()).thenReturn("55555555X");
        when(ticketInput.getPagoExtra()).thenReturn(5.5f);

        // --- WHEN ---
        boolean resultado = ticketDAO.registerTicket(ticketInput, "12345678Z", 10, "Info");

        // --- THEN ---
        assertTrue(resultado);
        verify(psMock).executeUpdate();
    }

    @Test
    void testDeleteTicket() throws SQLException {
        // --- GIVEN ---
        when(connectionMock.prepareStatement(anyString())).thenReturn(psMock);
        when(psMock.executeUpdate()).thenReturn(1);

        // --- WHEN ---
        boolean resultado = ticketDAO.deleteTicket("T-100");

        // --- THEN ---
        assertTrue(resultado);
    }

    @Test
    void testSearchByUser_2consultations() throws SQLException {
        PreparedStatement psMain = mock(PreparedStatement.class);
        ResultSet rsMain = mock(ResultSet.class);

        PreparedStatement psType = mock(PreparedStatement.class);
        ResultSet rsType = mock(ResultSet.class);

        // If SQL statement contains "SELECT *", then return main mock
        when(connectionMock.prepareStatement(contains("SELECT *"))).thenReturn(psMain);
        when(psMain.executeQuery()).thenReturn(rsMain);

        // Simulamos datos del Ticket principal
        // Simulates data for main Ticket
        when(rsMain.next()).thenReturn(true, false);
        when(rsMain.getInt("ID_TICKET")).thenReturn(100);
        when(rsMain.getString("Dni_Asistente")).thenReturn("87654321B");
        when(rsMain.getString("Informacion")).thenReturn("10");
        when(rsMain.getFloat("Pago_extra")).thenReturn(0.0f);

        // If SQL statement ask for type, then return the side mock (Type 1 = Carrera)
        when(connectionMock.prepareStatement(contains("ID_TIPO_EVENTO"))).thenReturn(psType);
        when(psType.executeQuery()).thenReturn(rsType);

        // Simulates is type 1
        when(rsType.next()).thenReturn(true);
        when(rsType.getInt("tipo")).thenReturn(1);

        List<iTicket> lista = ticketDAO.searchByUser("12345678A");

        assertFalse(lista.isEmpty());
        assertEquals("87654321B", lista.getFirst().getDniAsistente());
    }
}
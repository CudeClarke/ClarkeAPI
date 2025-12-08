package Managers;

import DB.iDatabaseAccessFactory;
import DB.TicketDAO.iTicketDAO;
import Datos.Ticket.iTicket;
import Datos.Usuario.iUsuario;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class TicketManagerTest {

    @Mock
    private iDatabaseAccessFactory factory;

    @Mock
    private iTicketDAO ticketDAO;

    @Mock
    private iTicket ticket;

    @Mock
    private iUsuario usuario;

    private TicketManager ticketManager;

    @BeforeEach
    void setUp() {
        when(factory.getTicketDAO()).thenReturn(ticketDAO);
        ticketManager = new TicketManager(factory);
    }

    // --- searchByUser Tests ---

    @Test
    void searchByUser_ValidDni_ReturnsList() {
        String dni = "12345678A";
        List<iTicket> expectedList = List.of(ticket);
        when(ticketDAO.searchByUser(dni)).thenReturn(expectedList);

        List<iTicket> result = ticketManager.searchByUser(dni);

        assertEquals(expectedList, result);
        verify(ticketDAO).searchByUser(dni);
    }

    @Test
    void searchByUser_InvalidDni_ReturnsEmptyList() {
        String dni = "invalid";
        when(ticketDAO.searchByUser(dni)).thenReturn(Collections.emptyList());

        List<iTicket> result = ticketManager.searchByUser(dni);

        assertTrue(result.isEmpty());
        verify(ticketDAO).searchByUser(dni);
    }

    // --- searchByEntrada Tests ---

    @Test
    void searchByEntrada_ValidId_ReturnsList() {
        int idEntrada = 1;
        List<iTicket> expectedList = List.of(ticket);
        when(ticketDAO.searchByEntrada(idEntrada)).thenReturn(expectedList);

        List<iTicket> result = ticketManager.searchByEntrada(idEntrada);

        assertEquals(expectedList, result);
        verify(ticketDAO).searchByEntrada(idEntrada);
    }

    @Test
    void searchByEntrada_InvalidId_ReturnsEmptyList() {
        int idEntrada = -1;
        when(ticketDAO.searchByEntrada(idEntrada)).thenReturn(Collections.emptyList());

        List<iTicket> result = ticketManager.searchByEntrada(idEntrada);

        assertTrue(result.isEmpty());
        verify(ticketDAO).searchByEntrada(idEntrada);
    }

    // --- registerTicket Tests ---

    @Test
    void registerTicket_ValidTicket_ReturnsTrue() {
        String dniComprador = "12345678A";
        int idEntrada = 1;
        String info = "Info";
        String dniAsistente = "87654321B";

        when(ticket.getUsuario()).thenReturn(usuario);
        when(ticket.getDniAsistente()).thenReturn(dniAsistente);
        when(ticketDAO.registerTicket(ticket, dniComprador, idEntrada, info)).thenReturn(true);

        boolean result = ticketManager.registerTicket(ticket, dniComprador, idEntrada, info);

        assertTrue(result);
        verify(ticketDAO).registerTicket(ticket, dniComprador, idEntrada, info);
    }

    @Test
    void registerTicket_NullTicket_ReturnsFalse() {
        boolean result = ticketManager.registerTicket(null, "dni", 1, "info");

        assertFalse(result);
        verify(ticketDAO, never()).registerTicket(any(), anyString(), anyInt(), anyString());
    }

    @Test
    void registerTicket_TicketNullUser_ReturnsFalse() {
        when(ticket.getUsuario()).thenReturn(null);

        boolean result = ticketManager.registerTicket(ticket, "dni", 1, "info");

        assertFalse(result);
        verify(ticketDAO, never()).registerTicket(any(), anyString(), anyInt(), anyString());
    }

    @Test
    void registerTicket_TicketNullDniAsistente_ReturnsFalse() {
        when(ticket.getUsuario()).thenReturn(usuario);
        when(ticket.getDniAsistente()).thenReturn(null);

        boolean result = ticketManager.registerTicket(ticket, "dni", 1, "info");

        assertFalse(result);
        verify(ticketDAO, never()).registerTicket(any(), anyString(), anyInt(), anyString());
    }

    @Test
    void registerTicket_TicketBlankDniAsistente_ReturnsFalse() {
        when(ticket.getUsuario()).thenReturn(usuario);
        when(ticket.getDniAsistente()).thenReturn("   ");

        boolean result = ticketManager.registerTicket(ticket, "dni", 1, "info");

        assertFalse(result);
        verify(ticketDAO, never()).registerTicket(any(), anyString(), anyInt(), anyString());
    }

    @Test
    void registerTicket_DaoFails_ReturnsFalse() {
        String dniComprador = "12345678A";
        int idEntrada = 1;
        String info = "Info";
        String dniAsistente = "87654321B";

        when(ticket.getUsuario()).thenReturn(usuario);
        when(ticket.getDniAsistente()).thenReturn(dniAsistente);
        when(ticketDAO.registerTicket(ticket, dniComprador, idEntrada, info)).thenReturn(false);

        boolean result = ticketManager.registerTicket(ticket, dniComprador, idEntrada, info);

        assertFalse(result);
        verify(ticketDAO).registerTicket(ticket, dniComprador, idEntrada, info);
    }

    // --- deleteTicket Tests ---

    @Test
    void deleteTicket_ValidId_ReturnsTrue() {
        String id = "ticket123";
        when(ticketDAO.deleteTicket(id)).thenReturn(true);

        boolean result = ticketManager.deleteTicket(id);

        assertTrue(result);
        verify(ticketDAO).deleteTicket(id);
    }

    @Test
    void deleteTicket_NullId_ReturnsFalse() {
        boolean result = ticketManager.deleteTicket(null);

        assertFalse(result);
        verify(ticketDAO, never()).deleteTicket(anyString());
    }

    @Test
    void deleteTicket_BlankId_ReturnsFalse() {
        boolean result = ticketManager.deleteTicket("   ");

        assertFalse(result);
        verify(ticketDAO, never()).deleteTicket(anyString());
    }

    @Test
    void deleteTicket_DaoFails_ReturnsFalse() {
        String id = "ticket123";
        when(ticketDAO.deleteTicket(id)).thenReturn(false);

        boolean result = ticketManager.deleteTicket(id);

        assertFalse(result);
        verify(ticketDAO).deleteTicket(id);
    }
}

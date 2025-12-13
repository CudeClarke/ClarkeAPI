package Managers;

import DB.EntradaDAO.iEntradaDAO;
import DB.EventoDAO.iEventoDAO;
import DB.TicketDAO.iTicketDAO;
import DB.iDatabaseAccessFactory;
import Datos.Entrada.Entrada;
import Datos.Entrada.iEntrada;
import Datos.Evento.EventoConcierto;
import Datos.Evento.iEvento;
import Datos.Ticket.Ticket;
import Datos.Ticket.iTicket;
import Datos.Usuario.UsuarioBase;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CompraManagerTest {

    @Mock private iDatabaseAccessFactory factoryMock;
    @Mock private iTicketDAO ticketDAOMock;
    @Mock private iEntradaDAO entradaDAOMock;
    @Mock private iEventoDAO eventoDAOMock;

    // Usaremos objetos REALES para los datos para que funcionen las listas internas
    private iEvento eventoReal;
    private iEntrada entradaReal;

    @Mock private iTicket ticketMock;

    private CompraManager compraManager;

    @BeforeEach
    void setUp() {
        when(factoryMock.getTicketDAO()).thenReturn(ticketDAOMock);
        when(factoryMock.getEntradaDAO()).thenReturn(entradaDAOMock);
        when(factoryMock.getEventoDAO()).thenReturn(eventoDAOMock);

        compraManager = new CompraManager(factoryMock);

        eventoReal = new EventoConcierto("Evento Test", "Lugar", 0, 0, "Desc", "2025-01-01", "url", 1,"Art");

        entradaReal = new Entrada(10, 100, 20.0f, "Entrada Test", "Desc");
    }

    @Test
    void testStartTransaction() {
        UsuarioBase usuario = new UsuarioBase("Juan", "Perez", "juan@mail.com", "12345678A");
        int id = compraManager.startTransaction(usuario);

        assertNotNull(compraManager.getTransaction(id));
        assertEquals(usuario, compraManager.getTransaction(id).getComprador());
    }

    @Test
    void testDeleteTransaction() {
        UsuarioBase usuario = new UsuarioBase("Juan", "Perez", "juan@mail.com", "12345678A");
        int id = compraManager.startTransaction(usuario);
        assertNotNull(compraManager.getTransaction(id));

        compraManager.deleteTransaction(id);
        assertNull(compraManager.getTransaction(id));
    }

    @Test
    void testCheckAvailability_Success() {
        int idEvento = 1;
        int idEntrada = 10;
        int cantidadSolicitada = 2;

        // Simula búsqueda devolviendo la entrada real (Aforo 100)
        when(entradaDAOMock.searchById(idEntrada)).thenReturn(entradaReal);

        // Simula 50 vendidos en BD
        List<iTicket> vendidosBD = new ArrayList<>();
        for(int i=0; i<50; i++) vendidosBD.add(mock(iTicket.class));
        when(ticketDAOMock.searchByEntrada(idEntrada)).thenReturn(vendidosBD);

        boolean result = compraManager.checkAvailabity(idEvento, idEntrada, cantidadSolicitada);
        assertTrue(result);
    }

    @Test
    void testCheckAvailability_Fail() {
        int idEvento = 1;
        int idEntrada = 10;
        int cantidadSolicitada = 10;

        when(entradaDAOMock.searchById(idEntrada)).thenReturn(entradaReal); // Aforo 100

        // Simula 95 vendidos en BD
        List<iTicket> vendidosBD = new ArrayList<>();
        for(int i=0; i<95; i++) vendidosBD.add(mock(iTicket.class));
        when(ticketDAOMock.searchByEntrada(idEntrada)).thenReturn(vendidosBD);

        boolean result = compraManager.checkAvailabity(idEvento, idEntrada, cantidadSolicitada);
        assertFalse(result);
    }

    @Test
    void testAddTicketToTransaction_Success() {
        UsuarioBase usuario = new UsuarioBase("Juan", "Perez", "juan@mail.com", "12345678A");
        int idTrans = compraManager.startTransaction(usuario);
        int idEvento = 1;
        int idEntrada = 10;

        // Mockea la búsqueda para que devuelva nuestros objetos reales (inicializados en setUp)
        when(eventoDAOMock.searchById(idEvento)).thenReturn(eventoReal);
        when(entradaDAOMock.searchById(idEntrada)).thenReturn(entradaReal);

        boolean result = compraManager.addTicketToTransaction(idTrans, idEvento, idEntrada, ticketMock);

        assertTrue(result, "El método debería devolver true si todo va bien");

        // Verificación robusta:
        Transaction t = compraManager.getTransaction(idTrans);
        assertNotNull(t, "La transacción debe existir");

        // Verifica que se ha añadido algo a la lista de eventos
        assertFalse(t.getEventos().isEmpty(), "La lista de eventos de la transacción no debería estar vacía");

        // Verifica que el evento añadido es el correcto (por ID)
        assertEquals(1, t.getEventos().getFirst().getID(), "El evento añadido debería tener el ID 1");
    }

    @Test
    void testConfirmTransaction_Success() {
        UsuarioBase usuario = new UsuarioBase("Juan", "Perez", "juan@mail.com", "12345678A");
        int idTrans = compraManager.startTransaction(usuario);

        // Configura DAOs
        when(eventoDAOMock.searchById(1)).thenReturn(eventoReal);
        when(entradaDAOMock.searchById(10)).thenReturn(entradaReal);
        when(ticketDAOMock.getNextTicketID()).thenReturn(500);
        when(ticketDAOMock.registerTicket(any(), anyString(), anyInt(), any())).thenReturn(true);

        // Usa un ticket REAL
        iTicket realTicket = new Ticket(usuario, "12345678A");
        // realTicket.getInformacion() devolverá "" por defecto, lo cual es válido.

        // Añade el ticket a la transacción
        compraManager.addTicketToTransaction(idTrans, 1, 10, realTicket);

        boolean result = compraManager.confirmTransaction(idTrans);

        assertTrue(result);

        // Verifica que se llamó al registro con los datos correctos
        verify(ticketDAOMock).registerTicket(eq(realTicket), eq("12345678A"), eq(10), any());
        assertEquals(500, realTicket.getId());
    }

    @Test
    void testGetTicketFactoryByEventType() {
        EventoConcierto concierto = new EventoConcierto();
        assertNotNull(compraManager.getTicketFactoryByEventType(concierto));
    }
}
package DB.EventoDAO;

import Datos.Evento.EventoConcierto;
import Datos.Evento.iEvento;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;

import java.sql.*;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class EventoDAOMySQLTest {
    @Mock private Connection connectionMock;
    @Mock private PreparedStatement preparedStatementMock;
    @Mock private ResultSet resultSetMock;
    @Mock private Statement statementMock;

    private EventoDAOMySQL eventoDAO;

    private static final int ID_CONCIERTO = 3;

    @BeforeEach
    void setUp() {
        eventoDAO = new EventoDAOMySQL(connectionMock);
    }

    @Test
    void testRegisterEvento_Success() throws SQLException {
        Date fechaEvento = Date.valueOf("2025-07-15");

        // Creamos un evento de prueba
        // Ajusta el constructor a tu clase real EventoConcierto
        iEvento evento = new EventoConcierto(
                "MadCool", "Madrid", 0, 100000,
                "Festival Verano", fechaEvento, "img.png", "Metallica"
        );
        int tipoEvento = ID_CONCIERTO;

        when(connectionMock.prepareStatement(anyString())).thenReturn(preparedStatementMock);
        when(preparedStatementMock.executeUpdate()).thenReturn(1); // 1 fila insertada

        boolean resultado = eventoDAO.registerEvento(evento, tipoEvento);

        assertTrue(resultado, "El registro debería devolver true");

        // Verify parameters were in right order
        verify(preparedStatementMock).setInt(1, tipoEvento);
        verify(preparedStatementMock).setString(2, "MadCool");
        verify(preparedStatementMock).setDate(3, fechaEvento);
        // ... (Lugar, Descripcion, Recaudacion, Objetivo, Informacion, Imagen)
        verify(preparedStatementMock).setDouble(6, 0.0);
        verify(preparedStatementMock).setDouble(7, 100000.0);
        verify(preparedStatementMock).executeUpdate();
    }

    @Test
    void testRegisterEvento_WithNulls() throws SQLException {
        // Tests logic of setOptionalString giving nulls
        Date fecha = Date.valueOf("2025-01-01");
        iEvento evento = new EventoConcierto("Evento Null", null, 0, 0, null, fecha, null, "Artista");

        when(connectionMock.prepareStatement(anyString())).thenReturn(preparedStatementMock);
        when(preparedStatementMock.executeUpdate()).thenReturn(1);

        eventoDAO.registerEvento(evento, ID_CONCIERTO);

        // Verifies the call setNull for index 4 and 5
        verify(preparedStatementMock).setNull(4, Types.VARCHAR);
        verify(preparedStatementMock).setNull(5, Types.VARCHAR);
    }

    @Test
    void testSearchByName_FoundConcierto() throws SQLException {
        String nombreBuscado = "MadCool";

        when(connectionMock.prepareStatement(anyString())).thenReturn(preparedStatementMock);
        when(preparedStatementMock.executeQuery()).thenReturn(resultSetMock);
        when(resultSetMock.next()).thenReturn(true); // Encuentra resultado

        // --- MOCKEO EXHAUSTIVO DEL RESULTSET ---
        // Esto es crucial porque tu método buildEvento lee todas estas columnas

        // Establish which event type to use (factory)
        when(resultSetMock.getInt("ID_TIPO_EVENTO")).thenReturn(ID_CONCIERTO);

        // Common data
        when(resultSetMock.getString("Nombre")).thenReturn("MadCool");
        when(resultSetMock.getInt("Objetivo")).thenReturn(50000);
        when(resultSetMock.getString("Lugar")).thenReturn("Ifema");
        when(resultSetMock.getString("Descripcion")).thenReturn("Festival");
        when(resultSetMock.getDate("Fecha")).thenReturn(Date.valueOf("2025-07-10"));
        when(resultSetMock.getString("Imagen")).thenReturn("logo.png");
        when(resultSetMock.getInt("Recaudacion")).thenReturn(1000);
        when(resultSetMock.getString("Informacion")).thenReturn("Info Extra"); // Usado por concierto para Artista/Orquesta

        iEvento resultado = eventoDAO.searchByName(nombreBuscado);

        assertNotNull(resultado);
        // Verifies the correct type of Factory was created
        assertInstanceOf(EventoConcierto.class, resultado);
        assertEquals("MadCool", resultado.getNombre());
        assertEquals("Ifema", resultado.getUbicacion());

        // verifies info extra was correctly assigned
        EventoConcierto concierto = (EventoConcierto) resultado;

        assertEquals("Info Extra", concierto.getArtista());
    }

    @Test
    void testGetAllEventos() throws SQLException {
        when(connectionMock.prepareStatement(anyString())).thenReturn(preparedStatementMock);
        when(preparedStatementMock.executeQuery()).thenReturn(resultSetMock);

        // Simulates 2 results
        when(resultSetMock.next()).thenReturn(true, true, false);

        // Minimum configuration
        when(resultSetMock.getInt("ID_TIPO_EVENTO")).thenReturn(ID_CONCIERTO);
        when(resultSetMock.getString("Nombre")).thenReturn("Ev1", "Ev2");
        when(resultSetMock.getDate("Fecha")).thenReturn(Date.valueOf("2024-01-01"));
        // The remaining should be null or 0

        List<iEvento> lista = eventoDAO.getAllEventos();

        assertEquals(2, lista.size());
        assertEquals("Ev1", lista.get(0).getNombre());
        assertEquals("Ev2", lista.get(1).getNombre());
    }

    @Test
    void testUpdateEvento() throws SQLException {
        int idEvento = 10;
        Date fecha = Date.valueOf("2025-01-01");
        iEvento evento = new EventoConcierto("Nuevo Nombre", "Lugar", 10, 100, "Desc", fecha, "url", "Art");

        when(connectionMock.prepareStatement(anyString())).thenReturn(preparedStatementMock);
        when(preparedStatementMock.executeUpdate()).thenReturn(1);

        boolean resultado = eventoDAO.updateEvento(idEvento, evento);

        assertTrue(resultado);
        verify(preparedStatementMock).setString(1, "Nuevo Nombre");
        // ID is the 9th parameter in SQL update
        verify(preparedStatementMock).setInt(9, idEvento);
    }

    @Test
    void testDeleteEvento() throws SQLException {
        int idEvento = 5;
        when(connectionMock.prepareStatement(anyString())).thenReturn(preparedStatementMock);
        when(preparedStatementMock.executeUpdate()).thenReturn(1);

        boolean resultado = eventoDAO.deleteEvento(idEvento);

        assertTrue(resultado);
        verify(preparedStatementMock).setInt(1, idEvento);
    }

    @Test
    void testGetID() throws SQLException {
        Date fecha = Date.valueOf("2025-10-10");
        // Creates a dummy (minimum data)
        iEvento eventoDummy = new EventoConcierto("Evento X", null, 0, 0, null, fecha, null, null);

        when(connectionMock.prepareStatement(anyString())).thenReturn(preparedStatementMock);
        when(preparedStatementMock.executeQuery()).thenReturn(resultSetMock);

        when(resultSetMock.next()).thenReturn(true);
        when(resultSetMock.getInt("ID_EVENTO")).thenReturn(99);

        int id = eventoDAO.getID(eventoDummy);

        assertEquals(99, id);
        // Verifies that it was searched by Nombre and Fecha
        verify(preparedStatementMock).setString(1, "Evento X");
        verify(preparedStatementMock).setDate(2, fecha);
    }
}
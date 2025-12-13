package DB.EventoDAO;

import Datos.Evento.EventoConcierto;
import Datos.Evento.Patrocinador;
import Datos.Evento.iEvento;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.sql.*;
import java.util.List;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class EventoDAOMySQLTest {

    @Mock private Connection connectionMock;
    @Mock private PreparedStatement preparedStatementMock;
    @Mock private ResultSet resultSetMock;

    private EventoDAOMySQL eventoDAO;
    private static final int ID_CONCIERTO = 3;

    @BeforeEach
    void setUp() {
        eventoDAO = new EventoDAOMySQL(connectionMock);
    }

    @Test
    void testSearchByName_FoundConcierto() throws SQLException {
        String nombreBuscado = "MadCool";

        when(connectionMock.prepareStatement(anyString())).thenReturn(preparedStatementMock);
        when(preparedStatementMock.executeQuery()).thenReturn(resultSetMock);
        when(resultSetMock.next()).thenReturn(true);

        // IDs
        when(resultSetMock.getInt("ID_EVENTO")).thenReturn(10);
        when(resultSetMock.getInt("ID_TIPO_EVENTO")).thenReturn(ID_CONCIERTO);

        // Data basics
        when(resultSetMock.getString("Nombre")).thenReturn(nombreBuscado);
        when(resultSetMock.getFloat("Objetivo")).thenReturn(50000f); // getFloat
        when(resultSetMock.getString("Lugar")).thenReturn("Madrid");
        when(resultSetMock.getString("Descripcion")).thenReturn("Festival");
        when(resultSetMock.getString("Fecha")).thenReturn("2025-07-10");
        when(resultSetMock.getString("Imagen")).thenReturn("img.png");
        when(resultSetMock.getFloat("Recaudacion")).thenReturn(0f); // getFloat
        when(resultSetMock.getString("Informacion")).thenReturn("Metallica");

        iEvento resultado = eventoDAO.searchByName(nombreBuscado);

        assertNotNull(resultado);
        assertInstanceOf(EventoConcierto.class, resultado);

        EventoConcierto concierto = (EventoConcierto) resultado;
        assertEquals("MadCool", concierto.getNombre());
        assertEquals(10, concierto.getID());
    }

    @Test
    void testGetAllEventos() throws SQLException {
        when(connectionMock.prepareStatement(anyString())).thenReturn(preparedStatementMock);
        when(preparedStatementMock.executeQuery()).thenReturn(resultSetMock);

        when(resultSetMock.next()).thenReturn(true, true, false);

        // Mockeo de columnas
        when(resultSetMock.getInt("ID_EVENTO")).thenReturn(1, 2);
        when(resultSetMock.getInt("ID_TIPO_EVENTO")).thenReturn(ID_CONCIERTO);
        when(resultSetMock.getString("Nombre")).thenReturn("Evento A", "Evento B");
        when(resultSetMock.getString("Fecha")).thenReturn("2025-01-01");
        when(resultSetMock.getString("Descripcion")).thenReturn("Descripción Genérica");
        when(resultSetMock.getString("Lugar")).thenReturn("Lugar Genérico");
        when(resultSetMock.getString("Imagen")).thenReturn("default.png");
        when(resultSetMock.getString("Informacion")).thenReturn("Info Extra");

        when(resultSetMock.getFloat("Objetivo")).thenReturn(1000f);
        when(resultSetMock.getFloat("Recaudacion")).thenReturn(0f);

        List<iEvento> lista = eventoDAO.getAllEventos();

        assertEquals(2, lista.size());
        assertEquals("Evento A", lista.getFirst().getNombre());
    }

    @Test
    void testRegisterEvento_Full() throws SQLException {
        iEvento evento = new EventoConcierto(
                "RockFest", "Barcelona", 0, 20000, "Rock", "2025-08-01", "url.png", "AC/DC"
        );
        when(connectionMock.prepareStatement(anyString())).thenReturn(preparedStatementMock);
        when(preparedStatementMock.executeUpdate()).thenReturn(1);

        assertTrue(eventoDAO.registerEvento(evento, ID_CONCIERTO));
        verify(preparedStatementMock).setFloat(6, 0.0f); // Recaudacion
        verify(preparedStatementMock).setFloat(7, 20000.0f); // Objetivo
    }

    @Test
    void testRegisterEvento_WithNulls() throws SQLException {
        iEvento evento = new EventoConcierto("Evento Null", null, 0, 100, null, "2025-01-01", null, "Artista");
        when(connectionMock.prepareStatement(anyString())).thenReturn(preparedStatementMock);
        when(preparedStatementMock.executeUpdate()).thenReturn(1);
        eventoDAO.registerEvento(evento, ID_CONCIERTO);
        verify(preparedStatementMock).setNull(4, Types.VARCHAR);
    }

    @Test
    void testGetTags() throws SQLException {
        int idEvento = 5;
        PreparedStatement psTags = mock(PreparedStatement.class);
        ResultSet rsTags = mock(ResultSet.class);
        lenient().when(connectionMock.prepareStatement(contains("etiquetas"))).thenReturn(psTags);
        when(psTags.executeQuery()).thenReturn(rsTags);
        when(rsTags.next()).thenReturn(true, true, false);
        when(rsTags.getString("nombre")).thenReturn("Verano", "Musica");

        Set<String> tags = eventoDAO.getTags(idEvento);
        assertEquals(2, tags.size());
    }

    @Test
    void testGetPatrocinadores() throws SQLException {
        int idEvento = 1;
        PreparedStatement psPatro = mock(PreparedStatement.class);
        ResultSet rsPatro = mock(ResultSet.class);
        lenient().when(connectionMock.prepareStatement(contains("FROM patrocinador"))).thenReturn(psPatro);
        when(psPatro.executeQuery()).thenReturn(rsPatro);
        when(rsPatro.next()).thenReturn(true, false);
        when(rsPatro.getString("Nombre")).thenReturn("CocaCola");
        when(rsPatro.getString("Imagen")).thenReturn("coke.png");

        Set<Patrocinador> resultado = eventoDAO.getPatrocinadores(idEvento);
        assertEquals(1, resultado.size());
    }

    @Test
    void testUpdateEvento() throws SQLException {
        iEvento evento = new EventoConcierto("N", "L", 0, 0, "D", "2025", "u", "A");
        when(connectionMock.prepareStatement(anyString())).thenReturn(preparedStatementMock);
        when(preparedStatementMock.executeUpdate()).thenReturn(1);
        assertTrue(eventoDAO.updateEvento(10, evento));
    }

    @Test
    void testDeleteEvento() throws SQLException {
        when(connectionMock.prepareStatement(anyString())).thenReturn(preparedStatementMock);
        when(preparedStatementMock.executeUpdate()).thenReturn(1);
        assertTrue(eventoDAO.deleteEvento(5));
    }

    @Test
    void testGetID() throws SQLException {
        String fecha = "2025-12-31";
        iEvento eventoDummy = new EventoConcierto("Fin", null, 0, 0, null, fecha, null, null);
        when(connectionMock.prepareStatement(anyString())).thenReturn(preparedStatementMock);
        when(preparedStatementMock.executeQuery()).thenReturn(resultSetMock);
        when(resultSetMock.next()).thenReturn(true);
        when(resultSetMock.getInt("ID_EVENTO")).thenReturn(999);
        assertEquals(999, eventoDAO.getID(eventoDummy));
    }
}
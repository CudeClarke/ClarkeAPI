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

    @Mock
    private Connection connectionMock;
    @Mock
    private PreparedStatement preparedStatementMock;
    @Mock
    private ResultSet resultSetMock;

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
        when(resultSetMock.next()).thenReturn(true); // Encuentra 1 resultado

        // IDs
        when(resultSetMock.getInt("ID_EVENTO")).thenReturn(10);
        when(resultSetMock.getInt("ID_TIPO_EVENTO")).thenReturn(ID_CONCIERTO); // Tipo 3 = Concierto

        // Data basics
        when(resultSetMock.getString("Nombre")).thenReturn(nombreBuscado);
        when(resultSetMock.getInt("Objetivo")).thenReturn(50000);
        when(resultSetMock.getString("Lugar")).thenReturn("Madrid");
        when(resultSetMock.getString("Descripcion")).thenReturn("Festival");

        when(resultSetMock.getString("Fecha")).thenReturn("2025-07-10");

        when(resultSetMock.getString("Imagen")).thenReturn("img.png");
        when(resultSetMock.getInt("Recaudacion")).thenReturn(0);

        when(resultSetMock.getString("Informacion")).thenReturn("Metallica");

        iEvento resultado = eventoDAO.searchByName(nombreBuscado);

        assertNotNull(resultado);
        assertInstanceOf(EventoConcierto.class, resultado);

        EventoConcierto concierto = (EventoConcierto) resultado;
        assertEquals("MadCool", concierto.getNombre());
        assertEquals("Metallica", concierto.getArtista());
        assertEquals(10, concierto.getID());
        assertEquals("2025-07-10", concierto.getDate());
    }

    @Test
    void testGetAllEventos() throws SQLException {
        // Configures the chain: Connection -> Statement -> ResultSet
        when(connectionMock.prepareStatement(anyString())).thenReturn(preparedStatementMock);
        when(preparedStatementMock.executeQuery()).thenReturn(resultSetMock);

        // Simulates 2 iterations
        when(resultSetMock.next()).thenReturn(true, true, false);
        when(resultSetMock.getInt("ID_EVENTO")).thenReturn(1, 2);
        when(resultSetMock.getInt("ID_TIPO_EVENTO")).thenReturn(ID_CONCIERTO);
        when(resultSetMock.getString("Nombre")).thenReturn("Evento A", "Evento B");
        when(resultSetMock.getString("Fecha")).thenReturn("2025-01-01"); // Fecha como String

        when(resultSetMock.getString("Descripcion")).thenReturn("Descripción Genérica");
        when(resultSetMock.getString("Lugar")).thenReturn("Lugar Genérico");
        when(resultSetMock.getString("Imagen")).thenReturn("default.png");
        when(resultSetMock.getString("Informacion")).thenReturn("Info Extra");

        when(resultSetMock.getInt("Objetivo")).thenReturn(1000);
        when(resultSetMock.getInt("Recaudacion")).thenReturn(0);

        List<iEvento> lista = eventoDAO.getAllEventos();

        assertNotNull(lista);
        assertEquals(2, lista.size(), "La lista debería tener 2 eventos");

        // Verifications
        assertEquals("Evento A", lista.get(0).getNombre());
        assertEquals(1, lista.get(0).getID());

        assertEquals("Evento B", lista.get(1).getNombre());
        assertEquals(2, lista.get(1).getID());
    }

    @Test
    void testRegisterEvento_Full() throws SQLException {
        iEvento evento = new EventoConcierto(
                "RockFest", "Barcelona", 0, 20000, "Rock", "2025-08-01", "url.png", "AC/DC"
        );

        when(connectionMock.prepareStatement(anyString())).thenReturn(preparedStatementMock);
        when(preparedStatementMock.executeUpdate()).thenReturn(1);

        boolean result = eventoDAO.registerEvento(evento, ID_CONCIERTO);

        assertTrue(result);

        // Verifies
        verify(preparedStatementMock).setInt(1, ID_CONCIERTO);
        verify(preparedStatementMock).setString(2, "RockFest");
        verify(preparedStatementMock).setString(3, "2025-08-01"); // Fecha String
        verify(preparedStatementMock).setString(8, "AC/DC"); // Informacion/Artista
    }

    @Test
    void testRegisterEvento_WithNulls() throws SQLException {
        iEvento evento = new EventoConcierto(
                "Evento Null", null, 0, 100, null, "2025-01-01", null, "Artista"
        );

        when(connectionMock.prepareStatement(anyString())).thenReturn(preparedStatementMock);
        when(preparedStatementMock.executeUpdate()).thenReturn(1);

        eventoDAO.registerEvento(evento, ID_CONCIERTO);

        // Verifies that setNull was called
        verify(preparedStatementMock).setNull(4, Types.VARCHAR);
        verify(preparedStatementMock).setNull(5, Types.VARCHAR);
        verify(preparedStatementMock).setNull(9, Types.VARCHAR);
    }

    @Test
    void testGetTags() throws SQLException {
        int idEvento = 5;

        PreparedStatement psTags = mock(PreparedStatement.class);
        ResultSet rsTags = mock(ResultSet.class);

        lenient().when(connectionMock.prepareStatement(contains("etiquetas"))).thenReturn(psTags);
        when(psTags.executeQuery()).thenReturn(rsTags);

        // Simulates 2 results
        when(rsTags.next()).thenReturn(true, true, false);
        when(rsTags.getString("nombre")).thenReturn("Verano", "Musica");

        Set<String> tags = eventoDAO.getTags(idEvento);

        assertNotNull(tags);
        assertEquals(2, tags.size());
        assertTrue(tags.contains("Verano"));
        assertTrue(tags.contains("Musica"));

        verify(psTags).setInt(1, idEvento);
    }

    @Test
    void testGetPatrocinadores() throws SQLException {
        int idEvento = 1;

        PreparedStatement psPatro = mock(PreparedStatement.class);
        ResultSet rsPatro = mock(ResultSet.class);

        lenient().when(connectionMock.prepareStatement(contains("FROM patrocinador"))).thenReturn(psPatro);
        when(psPatro.executeQuery()).thenReturn(rsPatro);

        // Simulates 1 result
        when(rsPatro.next()).thenReturn(true, false);
        when(rsPatro.getString("Nombre")).thenReturn("CocaCola");
        when(rsPatro.getString("Imagen")).thenReturn("coke.png");

        Set<Patrocinador> resultado = eventoDAO.getPatrocinadores(idEvento);

        assertEquals(1, resultado.size());
        Patrocinador p = resultado.iterator().next();
        assertEquals("CocaCola", p.getNombre());
    }

    @Test
    void testUpdateEvento() throws SQLException {
        iEvento evento = new EventoConcierto("NuevoNom", "Lugar", 0, 0, "Desc", "2025-01-01", "url", "Art");

        when(connectionMock.prepareStatement(anyString())).thenReturn(preparedStatementMock);
        when(preparedStatementMock.executeUpdate()).thenReturn(1);

        boolean result = eventoDAO.updateEvento(10, evento);

        assertTrue(result);
        verify(preparedStatementMock).setString(2, "2025-01-01"); // Fecha
        verify(preparedStatementMock).setInt(9, 10); // ID es el último parámetro
    }

    @Test
    void testDeleteEvento() throws SQLException {
        when(connectionMock.prepareStatement(anyString())).thenReturn(preparedStatementMock);
        when(preparedStatementMock.executeUpdate()).thenReturn(1);

        boolean result = eventoDAO.deleteEvento(5);

        assertTrue(result);
        verify(preparedStatementMock).setInt(1, 5);
    }

    @Test
    void testGetID() throws SQLException {
        String fecha = "2025-12-31";
        iEvento eventoDummy = new EventoConcierto("FinDeAño", null, 0, 0, null, fecha, null, null);

        when(connectionMock.prepareStatement(anyString())).thenReturn(preparedStatementMock);
        when(preparedStatementMock.executeQuery()).thenReturn(resultSetMock);

        when(resultSetMock.next()).thenReturn(true);
        when(resultSetMock.getInt("ID_EVENTO")).thenReturn(999);

        int id = eventoDAO.getID(eventoDummy);

        assertEquals(999, id);
        verify(preparedStatementMock).setString(1, "FinDeAño");
        verify(preparedStatementMock).setString(2, fecha);
    }
}
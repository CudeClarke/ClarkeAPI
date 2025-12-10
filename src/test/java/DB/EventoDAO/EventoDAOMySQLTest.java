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

    // --- TEST: Búsqueda y Construcción de Objetos ---

    @Test
    void testSearchByName_FoundConcierto() throws SQLException {
        // GIVEN
        String nombreBuscado = "MadCool";

        when(connectionMock.prepareStatement(anyString())).thenReturn(preparedStatementMock);
        when(preparedStatementMock.executeQuery()).thenReturn(resultSetMock);
        when(resultSetMock.next()).thenReturn(true); // Encuentra 1 resultado

        // --- MOCKEO DE COLUMNAS (Para buildEvento) ---
        // 1. IDs
        when(resultSetMock.getInt("ID_EVENTO")).thenReturn(10);
        when(resultSetMock.getInt("ID_TIPO_EVENTO")).thenReturn(ID_CONCIERTO); // Tipo 3 = Concierto

        // 2. Datos básicos
        when(resultSetMock.getString("Nombre")).thenReturn(nombreBuscado);
        when(resultSetMock.getInt("Objetivo")).thenReturn(50000);
        when(resultSetMock.getString("Lugar")).thenReturn("Madrid");
        when(resultSetMock.getString("Descripcion")).thenReturn("Festival");

        // CAMBIO IMPORTANTE: Ahora la fecha es String
        when(resultSetMock.getString("Fecha")).thenReturn("2025-07-10");

        when(resultSetMock.getString("Imagen")).thenReturn("img.png");
        when(resultSetMock.getInt("Recaudacion")).thenReturn(0);

        // 3. Dato polimórfico: 'Informacion' se mapea a 'Artista' en Concierto
        when(resultSetMock.getString("Informacion")).thenReturn("Metallica");

        // WHEN
        iEvento resultado = eventoDAO.searchByName(nombreBuscado);

        // THEN
        assertNotNull(resultado);
        assertInstanceOf(EventoConcierto.class, resultado); // Verifica que la Factory funcionó

        EventoConcierto concierto = (EventoConcierto) resultado;
        assertEquals("MadCool", concierto.getNombre());
        assertEquals("Metallica", concierto.getArtista()); // Verifica el mapeo de Informacion
        assertEquals(10, concierto.getID());
        assertEquals("2025-07-10", concierto.getDate());
    }

    @Test
    void testGetAllEventos() throws SQLException {
        // --- GIVEN ---
        // 1. Configuramos la cadena: Connection -> Statement -> ResultSet
        when(connectionMock.prepareStatement(anyString())).thenReturn(preparedStatementMock);
        when(preparedStatementMock.executeQuery()).thenReturn(resultSetMock);

        // 2. Simulamos el bucle: 2 vueltas (true, true) y luego termina (false)
        when(resultSetMock.next()).thenReturn(true, true, false);

        // --- MOCKEO EXHAUSTIVO DE COLUMNAS (Para satisfacer a Mockito Estricto) ---

        // A. Identificadores (Distintos para cada vuelta)
        when(resultSetMock.getInt("ID_EVENTO")).thenReturn(1, 2);

        // B. Tipo de evento (Usamos Concierto para ambos)
        when(resultSetMock.getInt("ID_TIPO_EVENTO")).thenReturn(ID_CONCIERTO);

        // C. Datos de Texto Básicos
        when(resultSetMock.getString("Nombre")).thenReturn("Evento A", "Evento B");
        when(resultSetMock.getString("Fecha")).thenReturn("2025-01-01"); // Fecha como String

        // D. Datos Opcionales/Extra (Añadidos para evitar el error "Strict stubbing mismatch")
        when(resultSetMock.getString("Descripcion")).thenReturn("Descripción Genérica");
        when(resultSetMock.getString("Lugar")).thenReturn("Lugar Genérico");
        when(resultSetMock.getString("Imagen")).thenReturn("default.png");
        when(resultSetMock.getString("Informacion")).thenReturn("Info Extra");

        // E. Datos Numéricos Extra
        when(resultSetMock.getInt("Objetivo")).thenReturn(1000);
        when(resultSetMock.getInt("Recaudacion")).thenReturn(0);

        // --- WHEN ---
        List<iEvento> lista = eventoDAO.getAllEventos();

        // --- THEN ---
        assertNotNull(lista);
        assertEquals(2, lista.size(), "La lista debería tener 2 eventos");

        // Verificaciones
        assertEquals("Evento A", lista.get(0).getNombre());
        assertEquals(1, lista.get(0).getID());

        assertEquals("Evento B", lista.get(1).getNombre());
        assertEquals(2, lista.get(1).getID());
    }

    // --- TEST: Registro (INSERT) ---

    @Test
    void testRegisterEvento_Full() throws SQLException {
        // GIVEN
        // (nombre, ubicacion, recaudacion, objetivo, descripcion, date, url, artista)
        // NOTA: Asegúrate de usar el constructor que acepta String en la fecha
        iEvento evento = new EventoConcierto(
                "RockFest", "Barcelona", 0, 20000, "Rock", "2025-08-01", "url.png", "AC/DC"
        );

        when(connectionMock.prepareStatement(anyString())).thenReturn(preparedStatementMock);
        when(preparedStatementMock.executeUpdate()).thenReturn(1);

        // WHEN
        boolean result = eventoDAO.registerEvento(evento, ID_CONCIERTO);

        // THEN
        assertTrue(result);

        // Verificamos los parámetros clave
        verify(preparedStatementMock).setInt(1, ID_CONCIERTO);
        verify(preparedStatementMock).setString(2, "RockFest");
        verify(preparedStatementMock).setString(3, "2025-08-01"); // Fecha String
        verify(preparedStatementMock).setString(8, "AC/DC"); // Informacion/Artista
    }

    @Test
    void testRegisterEvento_WithNulls() throws SQLException {
        // GIVEN: Evento con campos opcionales nulos (Lugar, Descripcion)
        iEvento evento = new EventoConcierto(
                "Evento Null", null, 0, 100, null, "2025-01-01", null, "Artista"
        );

        when(connectionMock.prepareStatement(anyString())).thenReturn(preparedStatementMock);
        when(preparedStatementMock.executeUpdate()).thenReturn(1);

        // WHEN
        eventoDAO.registerEvento(evento, ID_CONCIERTO);

        // THEN
        // Verificamos que se llamó a setNull para los campos opcionales
        verify(preparedStatementMock).setNull(4, Types.VARCHAR); // Lugar
        verify(preparedStatementMock).setNull(5, Types.VARCHAR); // Descripcion
        verify(preparedStatementMock).setNull(9, Types.VARCHAR); // Imagen
    }

    @Test
    void testGetTags() throws SQLException {
        // GIVEN
        int idEvento = 5;

        // Usamos mocks específicos para aislar esta consulta
        PreparedStatement psTags = mock(PreparedStatement.class);
        ResultSet rsTags = mock(ResultSet.class);

        // Usamos lenient() + contains para interceptar la SQL de etiquetas
        lenient().when(connectionMock.prepareStatement(contains("etiquetas"))).thenReturn(psTags);
        when(psTags.executeQuery()).thenReturn(rsTags);

        // Simulamos 2 resultados
        when(rsTags.next()).thenReturn(true, true, false);
        when(rsTags.getString("nombre")).thenReturn("Verano", "Musica");

        // WHEN
        Set<String> tags = eventoDAO.getTags(idEvento);

        // THEN
        assertNotNull(tags);
        assertEquals(2, tags.size());
        assertTrue(tags.contains("Verano"));
        assertTrue(tags.contains("Musica"));

        verify(psTags).setInt(1, idEvento);
    }

    @Test
    void testGetPatrocinadores() throws SQLException {
        // GIVEN
        int idEvento = 1;

        PreparedStatement psPatro = mock(PreparedStatement.class);
        ResultSet rsPatro = mock(ResultSet.class);

        lenient().when(connectionMock.prepareStatement(contains("FROM patrocinador"))).thenReturn(psPatro);
        when(psPatro.executeQuery()).thenReturn(rsPatro);

        // Simulamos 1 resultado
        when(rsPatro.next()).thenReturn(true, false);
        when(rsPatro.getString("Nombre")).thenReturn("CocaCola");
        when(rsPatro.getString("Imagen")).thenReturn("coke.png");

        // WHEN
        Set<Patrocinador> resultado = eventoDAO.getPatrocinadores(idEvento);

        // THEN
        assertEquals(1, resultado.size());
        Patrocinador p = resultado.iterator().next();
        assertEquals("CocaCola", p.getNombre());
    }

    @Test
    void testUpdateEvento() throws SQLException {
        // GIVEN
        iEvento evento = new EventoConcierto("NuevoNom", "Lugar", 0, 0, "Desc", "2025-01-01", "url", "Art");

        when(connectionMock.prepareStatement(anyString())).thenReturn(preparedStatementMock);
        when(preparedStatementMock.executeUpdate()).thenReturn(1);

        // WHEN
        boolean result = eventoDAO.updateEvento(10, evento);

        // THEN
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
        // GIVEN
        String fecha = "2025-12-31";
        iEvento eventoDummy = new EventoConcierto("FinDeAño", null, 0, 0, null, fecha, null, null);

        when(connectionMock.prepareStatement(anyString())).thenReturn(preparedStatementMock);
        when(preparedStatementMock.executeQuery()).thenReturn(resultSetMock);

        when(resultSetMock.next()).thenReturn(true);
        when(resultSetMock.getInt("ID_EVENTO")).thenReturn(999);

        // WHEN
        int id = eventoDAO.getID(eventoDummy);

        // THEN
        assertEquals(999, id);
        verify(preparedStatementMock).setString(1, "FinDeAño");
        verify(preparedStatementMock).setString(2, fecha);
    }
}
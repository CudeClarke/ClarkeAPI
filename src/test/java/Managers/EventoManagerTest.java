package Managers;

import DB.EntradaDAO.iEntradaDAO;
import DB.EventoDAO.iEventoDAO;
import DB.iDatabaseAccessFactory;
import Datos.Entrada.iEntrada;
import Datos.Evento.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class EventoManagerTest {

    @Mock private iDatabaseAccessFactory factoryMock;
    @Mock private iEventoDAO eventoDAOMock;
    @Mock private iEntradaDAO entradaDAOMock;

    // Usa mocks para los objetos de datos para facilitar la configuración
    @Mock private iEvento eventoMock;
    @Mock private iEntrada entradaMock;

    private EventoManager eventoManager;

    @BeforeEach
    void setUp() {
        // Configura la factory para devolver nuestros DAOs mockeados
        when(factoryMock.getEventoDAO()).thenReturn(eventoDAOMock);
        when(factoryMock.getEntradaDAO()).thenReturn(entradaDAOMock);

        eventoManager = new EventoManager(factoryMock);
    }

    @Test
    void testGetEventType() {
        // Prueba con instancias reales vacías o mocks de clases concretas
        EventoCarrera carrera = mock(EventoCarrera.class);
        EventoRifa rifa = mock(EventoRifa.class);
        EventoConcierto concierto = mock(EventoConcierto.class);

        assertEquals(1, eventoManager.getEventType(carrera));
        assertEquals(2, eventoManager.getEventType(rifa));
        assertEquals(3, eventoManager.getEventType(concierto));
    }

    @Test
    void testGetAllEventos() {
        List<iEvento> listaEventos = new ArrayList<>();
        listaEventos.add(eventoMock);

        when(eventoDAOMock.getAllEventos()).thenReturn(listaEventos);
        when(eventoMock.getID()).thenReturn(1);

        // Simula la recuperación de tags y patrocinadores
        when(eventoDAOMock.getTags(1)).thenReturn(new HashSet<>());
        when(eventoDAOMock.getPatrocinadores(1)).thenReturn(new HashSet<>());

        List<iEvento> resultado = eventoManager.getAllEventos();

        assertNotNull(resultado);
        assertEquals(1, resultado.size());
        // Verifica que se llamaron a los setters de datos extra
        verify(eventoMock).setTags(any());
        verify(eventoMock).setPatrocinadores(any());
    }

    @Test
    void testSearchById() {
        int id = 5;
        when(eventoDAOMock.searchById(id)).thenReturn(eventoMock);
        when(eventoMock.getID()).thenReturn(id);

        iEvento resultado = eventoManager.searchById(id);

        assertNotNull(resultado);
        verify(eventoMock).setTags(any());
    }

    @Test
    void testRegisterEvento_Success_Completo() {
        // Un evento válido con Tags y Patrocinadores nuevos
        when(eventoMock.getNombre()).thenReturn("Evento Top");
        when(eventoMock.getDate()).thenReturn("2025-01-01"); // ValidEvent check

        // Configuración de Tags
        Set<String> tags = new HashSet<>(Collections.singletonList("Musica"));
        when(eventoMock.getTags()).thenReturn(tags);

        // Configuración de Patrocinadores
        Patrocinador patro = new Patrocinador("CocaCola", "logo.png");
        Set<Patrocinador> patrocinadores = new HashSet<>(Collections.singletonList(patro));
        when(eventoMock.getPatrocinadores()).thenReturn(patrocinadores);

        // Configuración de Entradas
        List<iEntrada> entradas = Collections.singletonList(entradaMock);
        when(eventoMock.getEntradas()).thenReturn(entradas);

        // ==== MOCKEO DEL DAO ====
        // ID del nuevo evento
        when(eventoDAOMock.getNextEventoID()).thenReturn(100);
        // Registro del evento base
        when(eventoDAOMock.registerEvento(eq(eventoMock), anyInt())).thenReturn(true);

        // Lógica de Tags (Tag "Musica" no existe -> se crea)
        when(eventoDAOMock.getTagID("Musica")).thenReturn(-1);
        when(eventoDAOMock.registerTag("Musica")).thenReturn(true);
        when(eventoDAOMock.getNextTagID()).thenReturn(11); // ID devuelto será 11-1 = 10
        when(eventoDAOMock.setRelationEventoTag(100, 10)).thenReturn(true);

        // Lógica de Patrocinadores
        when(eventoDAOMock.getPatrocinadorID("CocaCola")).thenReturn(-1);
        when(eventoDAOMock.registerPatrocinador(patro)).thenReturn(true);
        when(eventoDAOMock.getNextPatrocinadorID()).thenReturn(21); // ID será 20
        when(eventoDAOMock.setRelationEventoPatrocinador(100, 20)).thenReturn(true);

        // Registro de Entradas
        when(entradaDAOMock.registerEntrada(entradaMock, 100)).thenReturn(true);

        boolean result = eventoManager.registerEvento(eventoMock);

        assertTrue(result);
        verify(eventoDAOMock).registerEvento(eq(eventoMock), anyInt());
        verify(eventoDAOMock).registerTag("Musica");
        verify(eventoDAOMock).setRelationEventoTag(100, 10);
        verify(eventoDAOMock).setRelationEventoPatrocinador(100, 20);
        verify(entradaDAOMock).registerEntrada(entradaMock, 100);
    }

    @Test
    void testRegisterEvento_Invalid() {
        // Evento sin nombre
        when(eventoMock.getNombre()).thenReturn(null);

        boolean result = eventoManager.registerEvento(eventoMock);

        assertFalse(result);
        verify(eventoDAOMock, never()).registerEvento(any(), anyInt());
    }

    @Test
    void testUpdateEvento_Success() {
        when(eventoMock.getNombre()).thenReturn("Valid");
        when(eventoMock.getDate()).thenReturn("date");
        when(eventoDAOMock.getID(eventoMock)).thenReturn(10);
        when(eventoDAOMock.updateEvento(10, eventoMock)).thenReturn(true);

        boolean result = eventoManager.updateEvento(eventoMock);

        assertTrue(result);
        verify(eventoDAOMock).updateEvento(10, eventoMock);
    }

    @Test
    void testDeleteEvento_Success() {
        when(eventoMock.getNombre()).thenReturn("Valid");
        when(eventoMock.getDate()).thenReturn("date");
        when(eventoDAOMock.getID(eventoMock)).thenReturn(10);
        when(eventoDAOMock.deleteEvento(10)).thenReturn(true);

        boolean result = eventoManager.deleteEvento(eventoMock);

        assertTrue(result);
        verify(eventoDAOMock).deleteEvento(10);
    }
}
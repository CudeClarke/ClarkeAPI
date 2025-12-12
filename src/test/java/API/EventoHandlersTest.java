package API;

import Datos.Evento.EventoConcierto;
import Datos.Evento.iEvento;
import Managers.EventoManager;
import io.javalin.http.Context;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.MockedStatic;
import org.mockito.junit.jupiter.MockitoExtension;
import utils.json_utils;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.argThat;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class EventoHandlersTest {

    @Mock
    private Context ctxMock;

    @Mock
    private EventoManager eventoManagerMock;

    private MockedStatic<json_utils> jsonGeneratorStaticMock;
    private Object originalManager;

    @BeforeEach
    void setUp() throws Exception {
        jsonGeneratorStaticMock = mockStatic(json_utils.class);

        Field field = EventoHandlers.class.getDeclaredField("eventoManager");
        field.setAccessible(true);

        originalManager = field.get(null);
        field.set(null, eventoManagerMock);
    }

    @AfterEach
    void tearDown() throws Exception {
        jsonGeneratorStaticMock.close();

        // Restores original manager
        Field field = EventoHandlers.class.getDeclaredField("eventoManager");
        field.setAccessible(true);
        field.set(null, originalManager);
    }

    @Test
    void testGetEvents_Success() throws Exception {
        List<iEvento> listEventos = new ArrayList<>();
        // Creates dummy event
        EventoConcierto eventoDummy = new EventoConcierto(
                "MadCool", "Madrid", 0, 0, "Desc", "2025-07-01", "url", "Artista"
        );
        listEventos.add(eventoDummy);

        when(eventoManagerMock.getAllEventos()).thenReturn(listEventos);
        // Simulates the event type to return
        when(eventoManagerMock.getEventType(eventoDummy)).thenReturn(3);

        EventoHandlers.getEvents.handle(ctxMock);

        // Verifies JSON is properly built
        verify(ctxMock).json(argThat(jsonStr ->
                jsonStr.toString().contains("\"idEvento\":1") &&
                        jsonStr.toString().contains("\"tipo\":3") &&
                        jsonStr.toString().contains("MadCool")
        ));
    }

    @Test
    void testGetEvents_Empty() throws Exception {
        when(eventoManagerMock.getAllEventos()).thenReturn(new ArrayList<>());

        jsonGeneratorStaticMock.when(() -> json_utils.status_response(1, "No events found in database"))
                .thenReturn("ERROR_EMPTY");

        EventoHandlers.getEvents.handle(ctxMock);

        verify(ctxMock).json("ERROR_EMPTY");
    }

    @Test
    void testGetEventByName_Success() throws Exception {
        String nombre = "MadCool";
        iEvento eventoDummy = mock(iEvento.class);

        when(ctxMock.pathParam("nombre")).thenReturn(nombre);
        when(eventoManagerMock.searchByName(nombre)).thenReturn(eventoDummy);

        jsonGeneratorStaticMock.when(() -> json_utils.Java_to_json_string(eventoDummy))
                .thenReturn("{ \"nombre\": \"MadCool\" }");

        EventoHandlers.getEventByName.handle(ctxMock);

        verify(ctxMock).json("{ \"nombre\": \"MadCool\" }");
    }

    @Test
    void testGetEventByName_NotFound() throws Exception {
        String nombre = "NoExiste";
        when(ctxMock.pathParam("nombre")).thenReturn(nombre);
        when(eventoManagerMock.searchByName(nombre)).thenReturn(null);

        jsonGeneratorStaticMock.when(() -> json_utils.status_response(1, "Could not find event in database"))
                .thenReturn("ERROR_NOT_FOUND");

        EventoHandlers.getEventByName.handle(ctxMock);

        verify(ctxMock).json("ERROR_NOT_FOUND");
    }

    @Test
    void testGetEventByName_InvalidName() throws Exception {
        when(ctxMock.pathParam("nombre")).thenReturn(""); // Nombre vacío

        jsonGeneratorStaticMock.when(() -> json_utils.status_response(1, "Invalid event name format"))
                .thenReturn("ERROR_FORMAT");

        EventoHandlers.getEventByName.handle(ctxMock);

        verify(ctxMock).json("ERROR_FORMAT");
        verify(eventoManagerMock, never()).searchByName(anyString());
    }

    @Test
    void testAddEvent_Success() throws Exception {
        EventoConcierto nuevoEvento = mock(EventoConcierto.class);

        // Simulates proper validation
        when(nuevoEvento.getNombre()).thenReturn("Nuevo Concierto");
        when(nuevoEvento.getDate()).thenReturn("2025-01-01");

        when(ctxMock.bodyAsClass(any())).thenAnswer(invocation -> {
            Class<?> clazz = invocation.getArgument(0);
            if (clazz.equals(EventoConcierto.class)) {
                return nuevoEvento;
            }
            throw new Exception("Parsing error simulado");
        });

        // Simulates successful register
        when(eventoManagerMock.registerEvento(nuevoEvento)).thenReturn(true);

        jsonGeneratorStaticMock.when(() -> json_utils.status_response(0, "Evento recibido correctamente."))
                .thenReturn("SUCCESS");

        EventoHandlers.addEvent.handle(ctxMock);

        verify(ctxMock).json("SUCCESS");
    }

    @Test
    void testAddEvent_MissingData() throws Exception {
        EventoConcierto eventoIncompleto = mock(EventoConcierto.class);

        // Simulates null nombre to force validation error
        when(eventoIncompleto.getNombre()).thenReturn(null);

        when(ctxMock.bodyAsClass(any())).thenAnswer(invocation -> {
            if (invocation.getArgument(0).equals(EventoConcierto.class)) return eventoIncompleto;
            throw new Exception("Parsing error");
        });

        jsonGeneratorStaticMock.when(() -> json_utils.status_response(1, "Nombre y Fecha son obligatorios."))
                .thenReturn("ERROR_VALIDATION");

        EventoHandlers.addEvent.handle(ctxMock);

        verify(ctxMock).json("ERROR_VALIDATION");
        verify(eventoManagerMock, never()).registerEvento(any());
    }

    @Test
    void testAddEvent_FailBD() throws Exception {
        EventoConcierto evento = mock(EventoConcierto.class);
        when(evento.getNombre()).thenReturn("Test");
        when(evento.getDate()).thenReturn("2025-01-01");

        when(ctxMock.bodyAsClass(any())).thenAnswer(invocation -> {
            if (invocation.getArgument(0).equals(EventoConcierto.class)) return evento;
            throw new Exception("Parsing error");
        });

        // Simulates a DB error
        when(eventoManagerMock.registerEvento(evento)).thenReturn(false);

        jsonGeneratorStaticMock.when(() -> json_utils.status_response(1, "Fallo al añadir evento (DB Error/Duplicado)."))
                .thenReturn("ERROR_DB");

        EventoHandlers.addEvent.handle(ctxMock);

        verify(ctxMock).json("ERROR_DB");
    }

    @Test
    void testAddEvent_InvalidBody() throws Exception {
        // Simulates that all attempts fail
        when(ctxMock.bodyAsClass(any())).thenThrow(new RuntimeException("Json invalido"));

        jsonGeneratorStaticMock.when(() -> json_utils.status_response(1, "Request body does not hold Evento data"))
                .thenReturn("ERROR_BODY");

        EventoHandlers.addEvent.handle(ctxMock);

        verify(ctxMock).json("ERROR_BODY");
    }
}
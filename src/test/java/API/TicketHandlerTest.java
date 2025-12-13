package API;

import DB.MySQLAccessFactory;
import Datos.Ticket.iTicket;
import Datos.Usuario.UsuarioBase;
import Managers.TicketManager;
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

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class TicketHandlerTest {

    @Mock
    private Context ctxMock;

    @Mock
    private TicketManager ticketManagerMock;

    // Mocks estáticos
    private MockedStatic<json_utils> jsonUtilsStaticMock;
    private MockedStatic<MySQLAccessFactory> dbFactoryStaticMock;

    private Object originalManager;

    @BeforeEach
    void setUp() throws Exception {
        // Mockea json_utils para controlar las respuestas
        jsonUtilsStaticMock = mockStatic(json_utils.class);

        // Mockea la BD para evitar error de conexión al cargar la clase
        dbFactoryStaticMock = mockStatic(MySQLAccessFactory.class);
        MySQLAccessFactory dummyFactory = mock(MySQLAccessFactory.class);
        dbFactoryStaticMock.when(MySQLAccessFactory::getInstance).thenReturn(dummyFactory);

        // Fuerza la carga de la clase TicketHandler
        try {
            Class.forName("API.TicketHandler");
        } catch (ClassNotFoundException e) {
            throw new RuntimeException("No se encontró la clase API.TicketHandler", e);
        }

        // Inyecta el Mock del Manager por Reflexión
        Field field = TicketHandler.class.getDeclaredField("ticketManager");
        field.setAccessible(true);
        originalManager = field.get(null);
        field.set(null, ticketManagerMock);
    }

    @AfterEach
    void tearDown() throws Exception {
        jsonUtilsStaticMock.close();
        dbFactoryStaticMock.close();

        // Restaura el manager original
        Field field = TicketHandler.class.getDeclaredField("ticketManager");
        field.setAccessible(true);
        field.set(null, originalManager);
    }

    @Test
    void testGetTickets_ByDni_Success() throws Exception {
        String dni = "12345678A";
        List<iTicket> listaTickets = new ArrayList<>();
        iTicket ticketDummy = mock(iTicket.class);
        listaTickets.add(ticketDummy);

        when(ctxMock.queryParam("dni")).thenReturn(dni);
        when(ticketManagerMock.searchByUser(dni)).thenReturn(listaTickets);

        // Simula la conversión a JSON
        jsonUtilsStaticMock.when(() -> json_utils.Java_to_json_string(listaTickets))
                .thenReturn("[{\"id\": 1}]");

        TicketHandler.getTickets.handle(ctxMock);

        verify(ctxMock).json("[{\"id\": 1}]");
    }

    @Test
    void testGetTickets_ByDni_Empty() throws Exception {
        String dni = "12345678A";
        when(ctxMock.queryParam("dni")).thenReturn(dni);
        // Devuelve null o lista vacía
        when(ticketManagerMock.searchByUser(dni)).thenReturn(new ArrayList<>());

        jsonUtilsStaticMock.when(() -> json_utils.status_response(1, "No tickets found for user with DNI: " + dni))
                .thenReturn("NOT_FOUND");

        TicketHandler.getTickets.handle(ctxMock);

        verify(ctxMock).json("NOT_FOUND");
    }

    @Test
    void testGetTickets_ByType_NotImplemented() throws Exception {
        when(ctxMock.queryParam("dni")).thenReturn(null);
        when(ctxMock.queryParam("type")).thenReturn("Concierto");

        jsonUtilsStaticMock.when(() -> json_utils.status_response(1, "Search by type not implemented yet"))
                .thenReturn("NOT_IMPLEMENTED");

        TicketHandler.getTickets.handle(ctxMock);

        verify(ctxMock).json("NOT_IMPLEMENTED");
    }

    @Test
    void testGetTickets_MissingParams() throws Exception {
        when(ctxMock.queryParam("dni")).thenReturn(null);
        when(ctxMock.queryParam("type")).thenReturn(null);

        jsonUtilsStaticMock.when(() -> json_utils.status_response(1, "Please specify 'dni' or 'type' query parameter"))
                .thenReturn("MISSING_PARAMS");

        TicketHandler.getTickets.handle(ctxMock);

        verify(ctxMock).json("MISSING_PARAMS");
    }

    @Test
    void testAddTicket_Success() throws Exception {
        // Crea el objeto DatosTicket que espera el handler
        TicketHandler.DatosTicket datos = new TicketHandler.DatosTicket();
        datos.nombre = "Juan";
        datos.dni = "12345678A";
        datos.id = 100;
        datos.info = "Detalles";

        when(ctxMock.bodyAsClass(TicketHandler.DatosTicket.class)).thenReturn(datos);

        // Simula registro exitoso
        // Usa any() porque el handler crea una instancia nueva de Ticket dentro
        when(ticketManagerMock.registerTicket(any(iTicket.class), eq(datos.dni), eq(datos.id), eq(datos.info)))
                .thenReturn(true);

        jsonUtilsStaticMock.when(() -> json_utils.Java_to_json_string(any(iTicket.class)))
                .thenReturn("TICKET_JSON");

        TicketHandler.addTicket.handle(ctxMock);

        verify(ctxMock).json("TICKET_JSON");
        verify(ticketManagerMock).registerTicket(any(), eq("12345678A"), eq(100), eq("Detalles"));
    }

    @Test
    void testAddTicket_MissingData() throws Exception {
        TicketHandler.DatosTicket datosIncompletos = new TicketHandler.DatosTicket();
        datosIncompletos.nombre = null; // Falta nombre

        when(ctxMock.bodyAsClass(TicketHandler.DatosTicket.class)).thenReturn(datosIncompletos);

        jsonUtilsStaticMock.when(() -> json_utils.status_response(1, "Nombre y DNI son obligatorios."))
                .thenReturn("ERROR_VALIDATION");

        TicketHandler.addTicket.handle(ctxMock);

        verify(ctxMock).json("ERROR_VALIDATION");
        verify(ticketManagerMock, never()).registerTicket(any(), anyString(), anyInt(), anyString());
    }

    @Test
    void testAddTicket_DbError() throws Exception {
        TicketHandler.DatosTicket datos = new TicketHandler.DatosTicket();
        datos.nombre = "Ana";
        datos.dni = "88888888B";

        when(ctxMock.bodyAsClass(TicketHandler.DatosTicket.class)).thenReturn(datos);

        // Simula fallo en BD
        when(ticketManagerMock.registerTicket(any(), any(), anyInt(), any()))
                .thenReturn(false);

        jsonUtilsStaticMock.when(() -> json_utils.status_response(1, "Fallo al registrar ticket (DB Error/Duplicado)."))
                .thenReturn("ERROR_DB");

        TicketHandler.addTicket.handle(ctxMock);

        verify(ctxMock).json("ERROR_DB");
    }

    @Test
    void testDeleteTicket_Success() throws Exception {
        String idParam = "T-50";
        when(ctxMock.pathParam("id")).thenReturn(idParam);
        when(ticketManagerMock.deleteTicket(idParam)).thenReturn(true);

        jsonUtilsStaticMock.when(() -> json_utils.status_response(0, "Ticket eliminado correctamente."))
                .thenReturn("DELETED");

        TicketHandler.deleteTicket.handle(ctxMock);

        verify(ctxMock).json("DELETED");
    }

    @Test
    void testDeleteTicket_Failure() throws Exception {
        String idParam = "T-99";
        when(ctxMock.pathParam("id")).thenReturn(idParam);
        when(ticketManagerMock.deleteTicket(idParam)).thenReturn(false);

        jsonUtilsStaticMock.when(() -> json_utils.status_response(1, "No se pudo eliminar el ticket (no existe o DB error)."))
                .thenReturn("ERROR_DELETE");

        TicketHandler.deleteTicket.handle(ctxMock);

        verify(ctxMock).json("ERROR_DELETE");
    }
}
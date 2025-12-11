package API;

import DB.MySQLAccessFactory;
import Datos.Ticket.TicketFactory;
import Datos.Ticket.iTicket;
import Datos.Usuario.UsuarioBase;
import Managers.CompraManager;
import Managers.EventoManager;
import Managers.Transaction;
import Managers.UserManager;
import Datos.Evento.iEvento;
import io.javalin.http.Context;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.MockedStatic;
import org.mockito.junit.jupiter.MockitoExtension;
import utils.json_generator;

import java.lang.reflect.Field;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CompraHandlersTest {

    @Mock private Context ctxMock;
    @Mock private CompraManager compraManagerMock;
    @Mock private EventoManager eventoManagerMock;
    @Mock private UserManager userManagerMock;

    // Auxiliary mocks
    @Mock private TicketFactory ticketFactoryMock;
    @Mock private iTicket ticketMock;
    @Mock private iEvento eventoMock;
    @Mock private Transaction transactionMock;

    // Static mocks
    private MockedStatic<json_generator> jsonGeneratorStaticMock;
    private MockedStatic<MySQLAccessFactory> dbFactoryStaticMock;

    private Object originalCompraManager;
    private Object originalEventoManager;
    private Object originalUserManager;

    @BeforeEach
    void setUp() throws Exception {
        // Control responses
        jsonGeneratorStaticMock = mockStatic(json_generator.class);

        // DB mocking
        dbFactoryStaticMock = mockStatic(MySQLAccessFactory.class);
        MySQLAccessFactory dummyFactory = mock(MySQLAccessFactory.class);
        dbFactoryStaticMock.when(MySQLAccessFactory::getInstance).thenReturn(dummyFactory);

        try {
            Class.forName("API.CompraHandlers");
        } catch (ClassNotFoundException e) {
            throw new RuntimeException("No se encontró la clase API.CompraHandlers", e);
        }

        // 4. Inyectamos nuestros Mocks específicos en los campos estáticos
        injectMock("compraManager", compraManagerMock);
        injectMock("eventoManager", eventoManagerMock);
        injectMock("userManager", userManagerMock);
    }

    @AfterEach
    void tearDown() throws Exception {
        // Close all static mocks
        jsonGeneratorStaticMock.close();
        dbFactoryStaticMock.close();

        // Restores the managers
        if (originalCompraManager != null) restoreMock("compraManager", originalCompraManager);
        if (originalEventoManager != null) restoreMock("eventoManager", originalEventoManager);
        if (originalUserManager != null) restoreMock("userManager", originalUserManager);
    }

    // Some auxiliary methods

    private void injectMock(String fieldName, Object mock) throws Exception {
        Field field = CompraHandlers.class.getDeclaredField(fieldName);
        field.setAccessible(true);
        // Saves the original just the 1st time
        if (fieldName.equals("compraManager")) originalCompraManager = field.get(null);
        if (fieldName.equals("eventoManager")) originalEventoManager = field.get(null);
        if (fieldName.equals("userManager")) originalUserManager = field.get(null);

        field.set(null, mock);
    }

    private void restoreMock(String fieldName, Object original) throws Exception {
        Field field = CompraHandlers.class.getDeclaredField(fieldName);
        field.setAccessible(true);
        field.set(null, original);
    }

    @Test
    void testCheckTicketsAvailability_Available() throws Exception {
        // GIVEN
        String jsonBody = "[ { \"idEvento\": 1, \"idEntrada\": 10, \"amount\": 2 } ]";
        when(ctxMock.body()).thenReturn(jsonBody);

        when(compraManagerMock.checkAvailabity(1, 10, 2)).thenReturn(true);

        jsonGeneratorStaticMock.when(() -> json_generator.status_response(0, ""))
                .thenReturn("AVAILABLE");

        CompraHandlers.checkTicketsAvailability.handle(ctxMock);

        verify(ctxMock).json("AVAILABLE");
    }

    @Test
    void testCheckTicketsAvailability_NotAvailable() throws Exception {
        String jsonBody = "[ { \"idEvento\": 1, \"idEntrada\": 10, \"amount\": 500 } ]";
        when(ctxMock.body()).thenReturn(jsonBody);

        when(compraManagerMock.checkAvailabity(1, 10, 500)).thenReturn(false);

        jsonGeneratorStaticMock.when(() -> json_generator.status_response(1, "Tickets not available"))
                .thenReturn("NOT_AVAILABLE");

        CompraHandlers.checkTicketsAvailability.handle(ctxMock);

        verify(ctxMock).json("NOT_AVAILABLE");
    }

    @Test
    void testSetTransaction_Success() throws Exception {
        String jsonBody = """
            {
                "comprador": { "nombre": "Juan", "dni": "12345678A" },
                "lista_entrada": [
                    {
                        "idEvento": 5,
                        "idEntrada": 10,
                        "pago_extra": 0.0,
                        "asistente": { "dni": "87654321B" },
                        "boleto": "InfoExtra"
                    }
                ]
            }
            """;
        when(ctxMock.body()).thenReturn(jsonBody);

        // Simulates the transaction start
        when(compraManagerMock.startTransaction(any(UsuarioBase.class))).thenReturn(999);

        // Simulates event and factory recovery
        when(eventoManagerMock.searchById(5)).thenReturn(eventoMock);
        when(compraManagerMock.getTicketFactoryByEventType(eventoMock)).thenReturn(ticketFactoryMock);

        // Simulates ticket creation
        when(ticketFactoryMock.createTicket(any(), eq("87654321B"), eq(0.0f), eq("InfoExtra")))
                .thenReturn(ticketMock);

        // Simulates adding a ticket transaction (succesfully)
        when(compraManagerMock.addTicketToTransaction(999, 5, 10, ticketMock)).thenReturn(true);

        jsonGeneratorStaticMock.when(() -> json_generator.status_response(0, "999"))
                .thenReturn("SUCCESS_ID_999");

        CompraHandlers.setTransaction.handle(ctxMock);

        verify(ctxMock).json("SUCCESS_ID_999");
        verify(compraManagerMock, never()).deleteTransaction(anyInt());
    }

    @Test
    void testSetTransaction_Fail_Rollback() throws Exception {
        String jsonBody = """
            {
                "comprador": { "nombre": "Juan" },
                "lista_entrada": [ { "idEvento": 1, "idEntrada": 1, "pago_extra": 0, "asistente": {"dni":"X"} } ]
            }
            """;
        when(ctxMock.body()).thenReturn(jsonBody);
        when(compraManagerMock.startTransaction(any())).thenReturn(100);

        // Simulates a specific configuration to achieve a failure
        when(eventoManagerMock.searchById(1)).thenReturn(eventoMock);
        when(compraManagerMock.getTicketFactoryByEventType(eventoMock)).thenReturn(ticketFactoryMock);

        // Simulates failure adding a ticket
        when(compraManagerMock.addTicketToTransaction(anyInt(), anyInt(), anyInt(), any())).thenReturn(false);

        jsonGeneratorStaticMock.when(() -> json_generator.status_response(1, "Error while processing transaction"))
                .thenReturn("ERROR_PROCESS");

        CompraHandlers.setTransaction.handle(ctxMock);

        verify(ctxMock).json("ERROR_PROCESS");
        // Verifies that deleteTransaction was made
        verify(compraManagerMock).deleteTransaction(100);
    }

    @Test
    void testProcessPayment_Success() throws Exception {
        when(ctxMock.pathParam("idTransaction")).thenReturn("50");
        when(ctxMock.body()).thenReturn("{}");

        // Simulates a valid transaction
        when(compraManagerMock.getTransaction(50)).thenReturn(transactionMock);
        UsuarioBase comprador = mock(UsuarioBase.class);
        when(transactionMock.getComprador()).thenReturn(comprador);

        jsonGeneratorStaticMock.when(() -> json_generator.status_response(0, "Transaction Confirmed"))
                .thenReturn("CONFIRMED");

        CompraHandlers.processPayment.handle(ctxMock);

        verify(userManagerMock).registerUsuario(comprador);
        verify(compraManagerMock).confirmTransaction(50);
        verify(ctxMock).json("CONFIRMED");
    }

    @Test
    void testProcessPayment_InvalidId() throws Exception {
        when(ctxMock.pathParam("idTransaction")).thenReturn("abc"); // ID no numérico

        jsonGeneratorStaticMock.when(() -> json_generator.status_response(1, "Invalid Transaction ID"))
                .thenReturn("INVALID_ID");

        CompraHandlers.processPayment.handle(ctxMock);

        verify(ctxMock).json("INVALID_ID");
        verify(compraManagerMock, never()).confirmTransaction(anyInt());
    }

    @Test
    void testCancelTransaction_Success() throws Exception {
        when(ctxMock.pathParam("idTransaction")).thenReturn("123");

        jsonGeneratorStaticMock.when(() -> json_generator.status_response(0, "Transaction Cancelled"))
                .thenReturn("CANCELLED");

        CompraHandlers.cancelTransaction.handle(ctxMock);

        verify(compraManagerMock).deleteTransaction(123);
        verify(ctxMock).json("CANCELLED");
    }
}
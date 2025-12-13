package API;

import DB.MySQLAccessFactory;
import Datos.Entrada.EntradaConcierto;
import Datos.Entrada.iEntrada;
import Managers.EntradaManager;
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
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class EntradaHandlersTest {

    @Mock
    private Context ctxMock;

    @Mock
    private EntradaManager entradaManagerMock;

    // Mocks estáticos
    private MockedStatic<json_utils> jsonGeneratorStaticMock;
    private MockedStatic<MySQLAccessFactory> dbFactoryStaticMock; // NECESARIO

    private Object originalManager;

    @BeforeEach
    void setUp() throws Exception {
        // Mock de json_utils
        jsonGeneratorStaticMock = mockStatic(json_utils.class);

        // BLINDAJE: Mock de MySQLAccessFactory para evitar conexión real
        dbFactoryStaticMock = mockStatic(MySQLAccessFactory.class);
        MySQLAccessFactory dummyFactory = mock(MySQLAccessFactory.class);
        dbFactoryStaticMock.when(MySQLAccessFactory::getInstance).thenReturn(dummyFactory);

        // Fuerza la carga de la clase con el mock de BD activo
        try {
            Class.forName("API.EntradaHandlers");
        } catch (ClassNotFoundException e) {
            throw new RuntimeException("Error cargando API.EntradaHandlers", e);
        }

        // Inyección del Mock del Manager por Reflexión
        Field field = EntradaHandlers.class.getDeclaredField("manager");
        field.setAccessible(true);

        originalManager = field.get(null);
        field.set(null, entradaManagerMock);
    }

    @AfterEach
    void tearDown() throws Exception {
        jsonGeneratorStaticMock.close();
        dbFactoryStaticMock.close(); // Cerramos el mock de BD

        // Restaurar el manager original
        Field field = EntradaHandlers.class.getDeclaredField("manager");
        field.setAccessible(true);
        field.set(null, originalManager);
    }

    @Test
    void testGetEntradasByEvento_Success() throws Exception {
        String idParam = "1";
        List<iEntrada> lista = new ArrayList<>();
        // ID=100, Precio=50.0, Nombre="General"
        EntradaConcierto entradaDummy = new EntradaConcierto(100, 50.0f, "General", "Desc");
        lista.add(entradaDummy);

        when(ctxMock.pathParam("id")).thenReturn(idParam);
        when(entradaManagerMock.getEntradasByEvento(1)).thenReturn(lista);

        EntradaHandlers.getEntradasByEvento.handle(ctxMock);

        // Verificamos que el JSON contiene los datos que Jackson serializaría
        verify(ctxMock).json(argThat(jsonStr ->
                jsonStr.toString().contains("\"idEvento\":1") &&
                        jsonStr.toString().contains("General") && // Nombre de la entrada
                        jsonStr.toString().contains("50.0")       // Precio
        ));
    }

    @Test
    void testGetEntradasByEvento_IvalidID() throws Exception {
        when(ctxMock.pathParam("id")).thenReturn("abc");

        jsonGeneratorStaticMock.when(() -> json_utils.status_response(1, "Tipo de ID incorrecto"))
                .thenReturn("ERROR_ID");

        EntradaHandlers.getEntradasByEvento.handle(ctxMock);

        verify(ctxMock).json("ERROR_ID");
        verify(entradaManagerMock, never()).getEntradasByEvento(anyInt());
    }

    @Test
    void testGetEntradasByEvento_Empty() throws Exception {
        when(ctxMock.pathParam("id")).thenReturn("1");
        // Retorna lista vacía
        when(entradaManagerMock.getEntradasByEvento(1)).thenReturn(new ArrayList<>());

        jsonGeneratorStaticMock.when(() -> json_utils.status_response(1, "No hay entradas"))
                .thenReturn("EMPTY");

        EntradaHandlers.getEntradasByEvento.handle(ctxMock);

        verify(ctxMock).json("EMPTY");
    }
/*
    @Test
    void testAddEntrada_Success() throws Exception {
        // JSON válido que coincide con la estructura esperada por tu 'entradaFromJson'
        String jsonBody = """
            {
                "idEvento": 5,
                "entrada": {
                    "nombre": "Entrada VIP",
                    "precio": 100.0,
                    "subAforo": 50,
                    "descripcion": "Acceso total"
                }
            }
            """;

        when(ctxMock.body()).thenReturn(jsonBody);

        // Simulamos éxito al guardar
        when(entradaManagerMock.addEntrada(any(iEntrada.class), eq(5))).thenReturn(true);

        jsonGeneratorStaticMock.when(() -> json_utils.status_response(0, "Entrada creada correctamente"))
                .thenReturn("SUCCESS");

        EntradaHandlers.addEntrada.handle(ctxMock);

        verify(ctxMock).json("SUCCESS");
    }

    @Test
    void testAddEntrada_MissingNombre() throws Exception {
        String jsonBody = """
            {
                "idEvento": 5,
                "entrada": {
                    "nombre": "", 
                    "precio": 50.0
                }
            }
            """;
        when(ctxMock.body()).thenReturn(jsonBody);

        jsonGeneratorStaticMock.when(() -> json_utils.status_response(1, "Se necesita nombre"))
                .thenReturn("ERROR_NOMBRE");

        EntradaHandlers.addEntrada.handle(ctxMock);

        verify(ctxMock).json("ERROR_NOMBRE");
        verify(entradaManagerMock, never()).addEntrada(any(), anyInt());
    }
 */

    @Test
    void testAddEntrada_EmptyBody() throws Exception {
        when(ctxMock.body()).thenReturn("{}");

        jsonGeneratorStaticMock.when(() -> json_utils.status_response(1, "Cuerpo vacío"))
                .thenReturn("ERROR_EMPTY");

        EntradaHandlers.addEntrada.handle(ctxMock);

        verify(ctxMock).json("ERROR_EMPTY");
    }

    @Test
    void testDeleteEntrada_Success() throws Exception {
        when(ctxMock.pathParam("id")).thenReturn("10");
        when(entradaManagerMock.deleteEntrada(10)).thenReturn(true);

        jsonGeneratorStaticMock.when(() -> json_utils.status_response(0, "Entrada eliminada"))
                .thenReturn("DELETED");

        EntradaHandlers.deleteEntrada.handle(ctxMock);

        verify(ctxMock).json("DELETED");
    }

    @Test
    void testDeleteEntrada_IncorrectID() throws Exception {
        when(ctxMock.pathParam("id")).thenReturn("text");

        jsonGeneratorStaticMock.when(() -> json_utils.status_response(1, "Formtao de ID incorrecto"))
                .thenReturn("ERROR_ID_FORMAT");

        EntradaHandlers.deleteEntrada.handle(ctxMock);

        verify(ctxMock).json("ERROR_ID_FORMAT");
        verify(entradaManagerMock, never()).deleteEntrada(anyInt());
    }
}
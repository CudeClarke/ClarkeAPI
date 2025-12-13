package API;

import DB.MySQLAccessFactory;
import Managers.UserManager;
import Datos.Usuario.UsuarioBase;
import Datos.Usuario.iUsuario;
import com.fasterxml.jackson.databind.node.ObjectNode; // Necesario para el cast
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

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UserHandlersTest {

    @Mock
    private Context ctxMock;

    @Mock
    private UserManager userManagerMock;

    // Mocks estáticos
    private MockedStatic<json_utils> jsonGeneratorStaticMock;
    private MockedStatic<MySQLAccessFactory> dbFactoryStaticMock; // Necesario para evitar conexión real

    private Object originalUserManager;

    @BeforeEach
    void setUp() throws Exception {
        // Mock de utilidades JSON
        jsonGeneratorStaticMock = mockStatic(json_utils.class);

        // Blindaje contra conexión a BD (Igual que en los otros handlers)
        dbFactoryStaticMock = mockStatic(MySQLAccessFactory.class);
        MySQLAccessFactory dummyFactory = mock(MySQLAccessFactory.class);
        dbFactoryStaticMock.when(MySQLAccessFactory::getInstance).thenReturn(dummyFactory);

        // Fuerza carga de la clase
        try {
            Class.forName("API.UserHandlers");
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        }

        // Inyección del Manager
        Field field = UserHandlers.class.getDeclaredField("userManager");
        field.setAccessible(true);
        originalUserManager = field.get(null);
        field.set(null, userManagerMock);
    }

    @AfterEach
    void tearDown() throws Exception {
        jsonGeneratorStaticMock.close();
        dbFactoryStaticMock.close();

        // Restaura manager original
        Field field = UserHandlers.class.getDeclaredField("userManager");
        field.setAccessible(true);
        field.set(null, originalUserManager);
    }

    @Test
    void testGetUserByDni_Encontrado() throws Exception {
        String dni = "12345678Z";
        // Usa UsuarioBase para que el switch del handler entre en el caso correcto
        UsuarioBase usuarioFalso = mock(UsuarioBase.class);

        when(ctxMock.pathParam("dni")).thenReturn(dni);
        when(userManagerMock.searchByDni(dni)).thenReturn(usuarioFalso);

        ObjectNode nodeMock = mock(ObjectNode.class);
        when(nodeMock.toString()).thenReturn("{ \"ok\": true }"); // Simular el toString final

        jsonGeneratorStaticMock.when(() -> json_utils.Java_to_json_node(usuarioFalso))
                .thenReturn(nodeMock);

        UserHandlers.getUserByDni.handle(ctxMock);

        verify(ctxMock).json("{ \"ok\": true }");
        // Verifica que el handler intentó añadir el tipo (parte de su lógica)
        verify(nodeMock).put(eq("tipo"), anyString());
    }

    @Test
    void testGetUserByDni_NoEncontrado() throws Exception {
        String dni = "12345678Z";
        when(ctxMock.pathParam("dni")).thenReturn(dni);
        when(userManagerMock.searchByDni(dni)).thenReturn(null);

        jsonGeneratorStaticMock.when(() -> json_utils.status_response(1, "Could not find user in database"))
                .thenReturn("NOT_FOUND");

        UserHandlers.getUserByDni.handle(ctxMock);

        verify(ctxMock).json("NOT_FOUND");
    }

    @Test
    void testGetUserByDni_FormatoIncorrecto() throws Exception {
        // DNI inválido (corto)
        when(ctxMock.pathParam("dni")).thenReturn("123");

        jsonGeneratorStaticMock.when(() -> json_utils.status_response(1, "Incorrect DNI format"))
                .thenReturn("ERROR_FORMATO");

        UserHandlers.getUserByDni.handle(ctxMock);

        verify(ctxMock).json("ERROR_FORMATO");
        verify(userManagerMock, never()).searchByDni(anyString());
    }

    @Test
    void testRegisterUser_Exitoso() throws Exception {
        UsuarioBase usuarioInput = mock(UsuarioBase.class);

        when(ctxMock.body()).thenReturn("{JSON}");
        jsonGeneratorStaticMock.when(() -> json_utils.json_string_to_iUsuario(anyString()))
                .thenReturn(usuarioInput);

        when(userManagerMock.registerUsuario(usuarioInput)).thenReturn(true);

        jsonGeneratorStaticMock.when(() -> json_utils.status_response(0, "User added to database"))
                .thenReturn("SUCCESS");
        UserHandlers.registerUser.handle(ctxMock);

        verify(ctxMock).json("SUCCESS");
    }

    @Test
    void testRegisterUser_FalloBD() throws Exception {
        UsuarioBase usuarioInput = mock(UsuarioBase.class);

        when(ctxMock.body()).thenReturn("{JSON}");
        jsonGeneratorStaticMock.when(() -> json_utils.json_string_to_iUsuario(anyString()))
                .thenReturn(usuarioInput);

        // Simula fallo en registro
        when(userManagerMock.registerUsuario(usuarioInput)).thenReturn(false);

        jsonGeneratorStaticMock.when(() -> json_utils.status_response(1, "Could not add user to database"))
                .thenReturn("ERROR_BD");

        UserHandlers.registerUser.handle(ctxMock);

        verify(ctxMock).json("ERROR_BD");
    }

    @Test
    void testRegisterUser_InvalidBody() throws Exception {
        when(ctxMock.body()).thenReturn("{BAD}");
        // Simula que el parseo falla devuelve null
        jsonGeneratorStaticMock.when(() -> json_utils.json_string_to_iUsuario(anyString()))
                .thenReturn(null);

        jsonGeneratorStaticMock.when(() -> json_utils.status_response(1, "Request body does not hold user data"))
                .thenReturn("ERROR_BODY");

        UserHandlers.registerUser.handle(ctxMock);

        verify(ctxMock).json("ERROR_BODY");
        verify(userManagerMock, never()).registerUsuario(any());
    }
}
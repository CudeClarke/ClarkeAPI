package API;

import Managers.UserManager;
import Datos.Usuario.UsuarioBase;
import Datos.Usuario.iUsuario;
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

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UserHandlersTest {

    @Mock
    private Context ctxMock;

    @Mock
    private UserManager userManagerMock;

    private MockedStatic<json_generator> jsonGeneratorStaticMock;

    // Guardamos el manager original para restaurarlo después del test
    private Object originalUserManager;

    @BeforeEach
    void setUp() throws Exception {
        // Mocking json_generator
        jsonGeneratorStaticMock = mockStatic(json_generator.class);

        Field field = UserHandlers.class.getDeclaredField("userManager");
        field.setAccessible(true);

        originalUserManager = field.get(null);

        field.set(null, userManagerMock);
    }

    @AfterEach
    void tearDown() throws Exception {
        jsonGeneratorStaticMock.close();

        // Restores manager so it won't affect other tests
        Field field = UserHandlers.class.getDeclaredField("userManager");
        field.setAccessible(true);
        field.set(null, originalUserManager);
    }

    @Test
    void testGetUserByDni_Encontrado() throws Exception {
        String dni = "12345678Z";
        iUsuario usuarioFalso = mock(UsuarioBase.class);

        when(ctxMock.pathParam("dni")).thenReturn(dni);
        // El mock devuelve un usuario
        when(userManagerMock.searchByDni(dni)).thenReturn(usuarioFalso);

        jsonGeneratorStaticMock.when(() -> json_generator.Java_to_json_string(usuarioFalso))
                .thenReturn("{ \"ok\": true }");

        // Exec handler
        UserHandlers.getUserByDni.handle(ctxMock);

        verify(ctxMock).json("{ \"ok\": true }");
    }

    @Test
    void testGetUserByDni_FormatoIncorrecto() throws Exception {
        // Invalid DNI
        when(ctxMock.pathParam("dni")).thenReturn("123");

        jsonGeneratorStaticMock.when(() -> json_generator.status_response(1, "Incorrect DNI format"))
                .thenReturn("ERROR_FORMATO");

        UserHandlers.getUserByDni.handle(ctxMock);

        verify(ctxMock).json("ERROR_FORMATO");
        // Asures that the manager WASN'T called
        verify(userManagerMock, never()).searchByDni(anyString());
    }

    @Test
    void testRegisterUser_Exitoso() throws Exception {
        UsuarioBase usuarioInput = mock(UsuarioBase.class);

        // Simulates it reads
        when(ctxMock.bodyAsClass(UsuarioBase.class)).thenReturn(usuarioInput);

        // Simulates that the register is actually working
        when(userManagerMock.registerUsuario(usuarioInput)).thenReturn(true);

        jsonGeneratorStaticMock.when(() -> json_generator.status_response(0, "User added to database"))
                .thenReturn("SUCCESS");

        UserHandlers.registerUser.handle(ctxMock);

        verify(ctxMock).json("SUCCESS");
    }

    @Test
    void testRegisterUser_FalloBD() throws Exception {
        UsuarioBase usuarioInput = mock(UsuarioBase.class);
        when(ctxMock.bodyAsClass(UsuarioBase.class)).thenReturn(usuarioInput);

        // Simulates that the register fail
        when(userManagerMock.registerUsuario(usuarioInput)).thenReturn(false);

        jsonGeneratorStaticMock.when(() -> json_generator.status_response(1, "Could not add user to database"))
                .thenReturn("ERROR_BD");

        UserHandlers.registerUser.handle(ctxMock);

        verify(ctxMock).json("ERROR_BD");
    }
}
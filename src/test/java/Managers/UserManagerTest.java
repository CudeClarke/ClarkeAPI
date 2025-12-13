package Managers;

import DB.UserDAO.iUsuarioDAO;
import DB.iDatabaseAccessFactory;
import Datos.Usuario.UsuarioBase;
import Datos.Usuario.UsuarioRegistrado;
import Datos.Usuario.iUsuario;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UserManagerTest {

    @Mock
    private iDatabaseAccessFactory factoryMock;

    @Mock
    private iUsuarioDAO usuarioDAOMock;

    private UserManager userManager;

    // Dummies
    private UsuarioBase usuarioBase;
    private UsuarioRegistrado usuarioRegistrado;

    @BeforeEach
    void setUp() {
        when(factoryMock.getUsuarioDAO()).thenReturn(usuarioDAOMock);
        userManager = new UserManager(factoryMock);

        // Inicializa datos de prueba
        usuarioBase = new UsuarioBase("Pepe", "Lopez", "pepe@mail.com", "12345678A");
        usuarioRegistrado = new UsuarioRegistrado("Pepe", "Lopez", "pepe@mail.com", "12345678A", false, "Calle 1", "600");
    }

    @Test
    void testGetAllUsuarios() {
        List<iUsuario> lista = Collections.singletonList(usuarioBase);
        when(usuarioDAOMock.getAllUsuarios()).thenReturn(lista);

        List<iUsuario> resultado = userManager.getAllUsuarios();

        assertEquals(lista, resultado);
        verify(usuarioDAOMock).getAllUsuarios();
    }

    @Test
    void testSearchByDni_Valido() {
        String dni = "12345678A";
        when(usuarioDAOMock.searchByDni(dni)).thenReturn(usuarioBase);

        iUsuario resultado = userManager.searchByDni(dni);

        assertNotNull(resultado);
        verify(usuarioDAOMock).searchByDni(dni);
    }

    @Test
    void testSearchByDni_Invalido() {
        String dni = "123"; // Corto
        iUsuario resultado = userManager.searchByDni(dni);

        assertNull(resultado);
        verify(usuarioDAOMock, never()).searchByDni(anyString());
    }

    @Test
    void testRegisterUsuario_Nuevo_Success() {
        // El usuario NO existe en BD
        when(usuarioDAOMock.searchByDni("12345678A")).thenReturn(null);
        when(usuarioDAOMock.registerUsuario(usuarioBase)).thenReturn(true);

        boolean result = userManager.registerUsuario(usuarioBase);

        assertTrue(result);
        verify(usuarioDAOMock).registerUsuario(usuarioBase);
        verify(usuarioDAOMock, never()).updateUsuario(any());
        verify(usuarioDAOMock, never()).upgradUsuarioToRegistrado(any());
    }

    @Test
    void testRegisterUsuario_Upgrade_BaseToRegistrado() {
        // En BD hay un UsuarioBase, y queremos registrar un UsuarioRegistrado
        when(usuarioDAOMock.searchByDni("12345678A")).thenReturn(usuarioBase); // Devuelve base
        when(usuarioDAOMock.upgradUsuarioToRegistrado(usuarioRegistrado)).thenReturn(true);

        boolean result = userManager.registerUsuario(usuarioRegistrado);

        assertTrue(result);
        verify(usuarioDAOMock).upgradUsuarioToRegistrado(usuarioRegistrado); // Debe hacer upgrade
        verify(usuarioDAOMock, never()).registerUsuario(any());
        verify(usuarioDAOMock, never()).updateUsuario(any());
    }

    @Test
    void testRegisterUsuario_Existe_UpdateNormal() {
        // En BD hay un UsuarioRegistrado, y registramos otro igual (Update)
        when(usuarioDAOMock.searchByDni("12345678A")).thenReturn(usuarioRegistrado);
        when(usuarioDAOMock.updateUsuario(usuarioRegistrado)).thenReturn(true);

        boolean result = userManager.registerUsuario(usuarioRegistrado);

        assertTrue(result);
        verify(usuarioDAOMock).updateUsuario(usuarioRegistrado); // Debe hacer update normal
        verify(usuarioDAOMock, never()).upgradUsuarioToRegistrado(any());
    }

    @Test
    void testRegisterUsuario_DniInvalido() {
        UsuarioBase usuarioMalo = new UsuarioBase("N", "A", "E", "123"); // DNI Malo

        boolean result = userManager.registerUsuario(usuarioMalo);

        assertFalse(result);
        verify(usuarioDAOMock, never()).searchByDni(anyString());
    }

    @Test
    void testUpdateUsuario_Existe_Success() {
        when(usuarioDAOMock.searchByDni("12345678A")).thenReturn(usuarioBase);
        when(usuarioDAOMock.updateUsuario(usuarioBase)).thenReturn(true);

        boolean result = userManager.updateUsuario(usuarioBase);

        assertTrue(result);
        verify(usuarioDAOMock).updateUsuario(usuarioBase);
    }

    @Test
    void testUpdateUsuario_NoExiste_Fail() {
        when(usuarioDAOMock.searchByDni("12345678A")).thenReturn(null);

        boolean result = userManager.updateUsuario(usuarioBase);

        assertFalse(result);
        verify(usuarioDAOMock, never()).updateUsuario(any());
    }

    @Test
    void testDeleteUsuario_Existe_Success() {
        String dni = "12345678A";
        when(usuarioDAOMock.searchByDni(dni)).thenReturn(usuarioBase);
        when(usuarioDAOMock.deleteUsuario(dni)).thenReturn(true);

        boolean result = userManager.deleteUsuario(dni);

        assertTrue(result);
        verify(usuarioDAOMock).deleteUsuario(dni);
    }

    @Test
    void testDeleteUsuario_NoExiste_Fail() {
        String dni = "12345678A";
        when(usuarioDAOMock.searchByDni(dni)).thenReturn(null);

        boolean result = userManager.deleteUsuario(dni);

        assertFalse(result);
        verify(usuarioDAOMock, never()).deleteUsuario(anyString());
    }
}
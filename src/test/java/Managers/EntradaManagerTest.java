package Managers;

import DB.EntradaDAO.iEntradaDAO;
import DB.iDatabaseAccessFactory;
import Datos.Entrada.iEntrada;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class EntradaManagerTest {

    @Mock
    private iDatabaseAccessFactory factoryMock;

    @Mock
    private iEntradaDAO entradaDAOMock;

    @Mock
    private iEntrada entradaMock;

    private EntradaManager entradaManager;

    @BeforeEach
    void setUp() {
        // Configuramos la factory para que devuelva nuestro DAO falso
        when(factoryMock.getEntradaDAO()).thenReturn(entradaDAOMock);

        // Inicializamos el manager
        entradaManager = new EntradaManager(factoryMock);
    }

    @Test
    void testGetEntradasByEvento() {
        int idEvento = 1;
        List<iEntrada> listaEsperada = Collections.singletonList(entradaMock);
        when(entradaDAOMock.searchByEvento(idEvento)).thenReturn(listaEsperada);

        List<iEntrada> resultado = entradaManager.getEntradasByEvento(idEvento);

        assertEquals(listaEsperada, resultado);
        verify(entradaDAOMock).searchByEvento(idEvento);
    }

    @Test
    void testAddEntrada_Valida_Success() {
        // Una entrada válida (Aforo > 0 y Precio > 0)
        when(entradaMock.getSubAforo()).thenReturn(100);
        when(entradaMock.getPrecio()).thenReturn(50.0f);

        when(entradaDAOMock.registerEntrada(entradaMock, 1)).thenReturn(true);

        boolean result = entradaManager.addEntrada(entradaMock, 1);

        assertTrue(result);
        verify(entradaDAOMock).registerEntrada(entradaMock, 1);
    }

    @Test
    void testAddEntrada_Invalida_PrecioCero() {
        // Entrada con precio 0 (Inválida)
        when(entradaMock.getSubAforo()).thenReturn(100);
        when(entradaMock.getPrecio()).thenReturn(0.0f);

        boolean result = entradaManager.addEntrada(entradaMock, 1);

        // THEN
        assertFalse(result, "Debe fallar si el precio es 0");
        // Verifica que NUNCA se llamó al DAO
        verify(entradaDAOMock, never()).registerEntrada(any(), anyInt());
    }

    @Test
    void testAddEntrada_Invalida_AforoNegativo() {
        // Entrada con aforo negativo (Inválida)
        when(entradaMock.getSubAforo()).thenReturn(-1);

        boolean result = entradaManager.addEntrada(entradaMock, 1);

        assertFalse(result, "Debe fallar si el aforo es <= 0");
        verify(entradaDAOMock, never()).registerEntrada(any(), anyInt());
    }

    @Test
    void testUpdateEntrada_Valida() {
        when(entradaMock.getSubAforo()).thenReturn(50);
        when(entradaMock.getPrecio()).thenReturn(10.0f);
        when(entradaDAOMock.updateEntrada(10, entradaMock)).thenReturn(true);

        boolean result = entradaManager.updateEntrada(10, entradaMock);

        assertTrue(result);
        verify(entradaDAOMock).updateEntrada(10, entradaMock);
    }

    @Test
    void testUpdateEntrada_Invalida() {
        // Datos inválidos
        when(entradaMock.getSubAforo()).thenReturn(0);

        boolean result = entradaManager.updateEntrada(10, entradaMock);

        assertFalse(result);
        verify(entradaDAOMock, never()).updateEntrada(anyInt(), any());
    }

    @Test
    void testDeleteEntrada() {
        when(entradaDAOMock.deleteEntrada(5)).thenReturn(true);

        boolean result = entradaManager.deleteEntrada(5);

        assertTrue(result);
        verify(entradaDAOMock).deleteEntrada(5);
    }
}
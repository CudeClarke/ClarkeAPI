package Managers;

import DB.iDatabaseAccessFactory;
import DB.EntradaDAO.iEntradaDAO;
import Datos.Entrada.*;
import java.util.List;

public class EntradaManager {
    
    private final iEntradaDAO entradaDAO;


    public EntradaManager(iDatabaseAccessFactory factory) {
        this.entradaDAO = factory.getEntradaDAO();
    }

    public List<iEntrada> getEntradasByEvento(int idEvento) {
        return entradaDAO.searchByEvento(idEvento);
    }

    public boolean addEntrada(int idEvento, int cantidad, float precio, String nombre, String descripcion) {
        if (cantidad < 0 || precio < 0) {
            return false;
        }
        
        Entrada nuevaEntrada = new Entrada(cantidad, precio, nombre, descripcion);
        return entradaDAO.registerEntrada(nuevaEntrada, idEvento);
    }

    public boolean updateEntrada(int idEntrada, int cantidad, float precio, String nombre, String descripcion) {
        if (cantidad < 0 || precio < 0) return false;

        Entrada entradaEditada = new Entrada(cantidad, precio, nombre, descripcion);
        return entradaDAO.updateEntrada(idEntrada, entradaEditada);
    }

    public boolean deleteEntrada(int idEntrada) {
        return entradaDAO.deleteEntrada(idEntrada);
    }
}

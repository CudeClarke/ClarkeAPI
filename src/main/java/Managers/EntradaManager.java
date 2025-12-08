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

    private boolean validEntrada(iEntrada entrada){
        return entrada.getSubAforo() > 0 && entrada.getPrecio() > 0;
    }

    public List<iEntrada> getEntradasByEvento(int idEvento) {
        return entradaDAO.searchByEvento(idEvento);
    }

    public boolean addEntrada(iEntrada entrada, int idEvento) {
        if (!validEntrada(entrada)) {
            return false;
        }
        return entradaDAO.registerEntrada(entrada, idEvento);
    }

    public boolean updateEntrada(int idEntrada, iEntrada entrada) {
        if (!validEntrada(entrada)) {
            return false;
        }
        return entradaDAO.updateEntrada(idEntrada, entrada);
    }

    public boolean deleteEntrada(int idEntrada) {
        return entradaDAO.deleteEntrada(idEntrada);
    }
}

package DB.EntradaDAO;

import Datos.Entrada.iEntrada;
import java.util.List;

public interface iEntradaDAO {
    List<iEntrada> searchByEvento(int idEvento);
    boolean registerEntrada(iEntrada entrada, int idEvento);
    boolean updateEntrada(int id, iEntrada entrada);
    boolean deleteEntrada(int id);

    int getID(iEntrada entrada, int idEvento);
}

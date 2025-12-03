package DB.EntradaDAO;

import Datos.Entrada.iEntrada;
import java.util.List;

public interface iEntradaDAO {
    List<iEntrada> searchByEvento(int idEvento);
    boolean registerEntrada(iEntrada entrada, int idEvento);
    boolean updateEntrada(iEntrada entrada);
    boolean deleteEntrada(iEntrada entrada);

    int getID(iEntrada entrada, int idEvento);
}

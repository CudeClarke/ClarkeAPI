package DB.EventoDAO;

import Evento.*;

import java.sql.Date;
import java.util.List;
import java.util.Map;

public interface iEventoDAO {
    iEvento searchByName(String nombre);
    List<iEvento> searchByTag(String tag);
    List<iEvento> searchByPatrocinador(String patrocinador);
    List<iEvento> getAllEventos();
    Map<String, Integer> countEntradasPorEvento(int idEvento);
    boolean AñadirEvento();
}

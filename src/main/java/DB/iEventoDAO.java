package DB;

import Evento.*;
import java.util.List;
import java.util.Map;

public interface iEventoDAO {
    EventoAbstracto searchByName(String nombre);
    List<EventoAbstracto> searchByTag(String tag);
    List<EventoAbstracto> searchByPatrocinador(String patrocinador);
    List<EventoAbstracto> getAllEventos();
    Map<String, Integer> countEntradasPorEvento(int idEvento);
}

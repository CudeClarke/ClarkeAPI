package DB.EventoDAO;

import Datos.Evento.*;
import java.util.List;
import java.util.Map;

public interface iEventoDAO {
    List<iEvento> getAllEventos();
    List<iEvento> searchByTag(String tag);
    List<iEvento> searchByPatrocinador(String patrocinador);
    iEvento searchByName(String nombre);
    boolean registerEvento(iEvento evento, int tipo);
    boolean updateEvento(iEvento evento);
    boolean deleteEvento(iEvento evento);
}

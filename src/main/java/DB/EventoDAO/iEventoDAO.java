package DB.EventoDAO;

import Datos.Evento.*;
import java.util.List;
import java.util.Set;


public interface iEventoDAO {
    List<iEvento> getAllEventos();
    List<iEvento> searchByTag(String tag);
    List<iEvento> searchByPatrocinador(String patrocinador);
    iEvento searchByName(String nombre);
    boolean registerEvento(iEvento evento, int tipo);
    boolean updateEvento(int id, iEvento evento);
    boolean deleteEvento(int idEvento);

    int getID(iEvento evento);
    Set<String> getTags(int idEvento);
    Set<Patrocinador> getPatrocinadores(int idEvento);
}

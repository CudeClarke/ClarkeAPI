package DB.EventoDAO;

import Datos.Evento.*;
import java.util.List;


public interface iEventoDAO {
    List<iEvento> getAllEventos();
    List<iEvento> searchByTag(String tag);
    List<iEvento> searchByPatrocinador(String patrocinador);
    iEvento searchByName(String nombre);
    boolean registerEvento(iEvento evento, int tipo);
    boolean updateEvento(int id, iEvento evento);
    boolean deleteEvento(int idEvento);

    int getID(iEvento evento);
}

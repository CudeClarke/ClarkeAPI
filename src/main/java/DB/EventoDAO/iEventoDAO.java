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
    boolean AñadirEvento(int idevento,int id_tipo_evento,String nombre,Date fecha,int aforo,
                         double recaudacion, double objetivo,String lugar,
                         String descripcion);
}

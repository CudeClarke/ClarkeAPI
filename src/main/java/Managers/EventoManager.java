package Managers;

import DB.iDatabaseAccessFactory;
import DB.EventoDAO.iEventoDAO;
import Datos.Evento.*;

import java.util.List;

public class EventoManager {
    private iEventoDAO eventoDAO;

    public EventoManager(iDatabaseAccessFactory factory){
        eventoDAO = factory.getEventoDAO();
    }

    private boolean validEvent(iEvento evento){return (evento.getNombre() != null && !evento.getNombre().isBlank() && evento.getDate() != null);}
    public int getEventType(iEvento evento){
        return switch (evento) {
            case EventoCarrera _ -> 1;
            case EventoRifa _ -> 2;
            case EventoConcierto _ -> 3;
            default -> 4;
        };
    }

    public List<iEvento> getAllEventos(){
        List<iEvento> eventos = eventoDAO.getAllEventos();
        for (int i = 0; i<eventos.size(); i++){
            iEvento current = eventos.get(i);
            current.setTags(eventoDAO.getTags(i+1));
            current.setPatrocinadores(eventoDAO.getPatrocinadores(i+1));
        }
        return eventos;
    }

    public iEvento searchByName(String name){
        return eventoDAO.searchByName(name);
    }

    public List<iEvento> searchByTag(String tag){
        return eventoDAO.searchByTag(tag);
    }

    public List<iEvento> searchByPatrocinador(String patrocinador){
        return eventoDAO.searchByPatrocinador(patrocinador);
    }

    public boolean registerEvento(iEvento evento){
        boolean res = false;
        if (validEvent(evento)){
            res = eventoDAO.registerEvento(evento, getEventType(evento));
        }
        return res;
    }

    public boolean updateEvento(iEvento evento){
        boolean res = false;
        if (validEvent(evento)){
            int id = eventoDAO.getID(evento);
            if (id > 0){
                res = eventoDAO.updateEvento(id, evento);
            }
        }
        return res;
    }

    public boolean deleteEvento(iEvento evento){
        boolean res = false;
        if (validEvent(evento)){
            int id = eventoDAO.getID(evento);
            if (id > 0){
                res = eventoDAO.deleteEvento(id);
            }
        }
        return res;
    }
}

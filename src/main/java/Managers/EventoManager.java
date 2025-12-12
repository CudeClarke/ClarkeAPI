package Managers;

import java.util.List;

import DB.EntradaDAO.iEntradaDAO;
import DB.iDatabaseAccessFactory;
import DB.EventoDAO.iEventoDAO;
import Datos.Evento.*;
import Datos.Entrada.iEntrada;

public class EventoManager {
    private iEventoDAO eventoDAO;
    private iEntradaDAO entradaDAO;

    public EventoManager(iDatabaseAccessFactory factory){
        eventoDAO = factory.getEventoDAO();
        entradaDAO = factory.getEntradaDAO();
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
        for (iEvento current : eventos) {
            current.setTags(eventoDAO.getTags(current.getID()));
            current.setPatrocinadores(eventoDAO.getPatrocinadores(current.getID()));
        }
        return eventos;
    }

    public iEvento searchByName(String name){
        iEvento evento = eventoDAO.searchByName(name);
        evento.setTags(eventoDAO.getTags(evento.getID()));
        evento.setPatrocinadores(eventoDAO.getPatrocinadores(evento.getID()));
        return evento;
    }

    public iEvento searchById(int idEvento){
        iEvento evento = eventoDAO.searchById(idEvento);
        evento.setTags(eventoDAO.getTags(evento.getID()));
        evento.setPatrocinadores(eventoDAO.getPatrocinadores(evento.getID()));
        return evento;
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
            /*List<iEntrada> entradas = evento.getEntradas();
            if (res && entradas != null && !entradas.isEmpty()){
                for (iEntrada entrada : entradas){
                    res = res && entradaDAO.registerEntrada(entrada);
                }
            }*/
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

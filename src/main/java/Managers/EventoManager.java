package Managers;

import java.util.List;
import java.util.Set;

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
        if (evento != null) {
            evento.setTags(eventoDAO.getTags(evento.getID()));
            evento.setPatrocinadores(eventoDAO.getPatrocinadores(evento.getID()));
        }
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
            int idEvento = eventoDAO.getNextEventoID();
            res = eventoDAO.registerEvento(evento, getEventType(evento));
            Set<String> tags = evento.getTags();
            if (res && tags != null && !tags.isEmpty()){
               for (String tag : tags){
                    int idTag = registerTag(tag);
                    if (idTag > 0){
                        res = res && eventoDAO.setRelationEventoTag(idEvento, idTag);
                    }
               }
            }

            Set<Patrocinador> patrocinadores = evento.getPatrocinadores();
            if (res && patrocinadores != null && !patrocinadores.isEmpty()){
                for (Patrocinador patrocinador : patrocinadores){
                    int idPatrocinador = registerPatrocinador(patrocinador);
                    if (idPatrocinador > 0){
                        res = res && eventoDAO.setRelationEventoPatrocinador(idEvento, idPatrocinador);
                    }
                }
            }

            List<iEntrada> entradas = evento.getEntradas();
            if (res && entradas != null && !entradas.isEmpty()){
                for (iEntrada entrada : entradas){
                    res = res && entradaDAO.registerEntrada(entrada, idEvento);
                }
            }
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

    public int registerTag(String tag){
        int idTag;
        idTag = eventoDAO.getTagID(tag);
        if (idTag < 0){
            if (eventoDAO.registerTag(tag)){
                idTag = eventoDAO.getNextTagID()-1;
            }
        }
        return idTag;
    }

    public int registerPatrocinador(Patrocinador patrocinador){
        int idPatrocinador;
        idPatrocinador = eventoDAO.getPatrocinadorID(patrocinador.getNombre());
        if (idPatrocinador < 0){
            if (eventoDAO.registerPatrocinador(patrocinador)){
                idPatrocinador = eventoDAO.getNextPatrocinadorID()-1;
            }
        }
        return idPatrocinador;
    }
}

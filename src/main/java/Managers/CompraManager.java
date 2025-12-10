package Managers;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import Datos.Ticket.*;
import Datos.Entrada.iEntrada;
import Datos.Usuario.iUsuario;
import Datos.Evento.*;
import DB.TicketDAO.iTicketDAO;
import DB.EntradaDAO.iEntradaDAO;
import DB.EventoDAO.iEventoDAO;
import DB.iDatabaseAccessFactory;

public class CompraManager {
    private static iTicketDAO ticketDAO;
    private static  iEntradaDAO entradaDAO;
    private static iEventoDAO eventoDAO;
    List<iTicket> tickets;
    Map<Integer, Transaction> transacciones_pendientes;

    public CompraManager(iDatabaseAccessFactory factory){
        ticketDAO = factory.getTicketDAO();
        entradaDAO = factory.getEntradaDAO();
        eventoDAO = factory.getEventoDAO();
        tickets = new ArrayList<>();
    }

    public TicketFactory getTicketFactoryByEventType(iEvento evento){
        return switch (evento) {
            case EventoConcierto _ -> new TicketConciertoFactory();
            case EventoCarrera _ -> new TicketCarreraFactory();
            case EventoRifa _ -> new TicketRifaFactory();
            default -> new TicketDefaultFactory();
        };
    }

    public Transaction getTransaction(int idTransaction){
        return transacciones_pendientes.get(idTransaction);
    }

    public boolean checkAvailabity(int idEvento, int idEntrada, int number){
        boolean res = false;

        iEntrada entrada = entradaDAO.searchByEvento(idEvento).get(idEntrada);
        int absoluteIDEntrada = entradaDAO.getID(entrada, idEvento);
        int soldTickets = ticketDAO.searchByEntrada(absoluteIDEntrada).size();

        res = ((entrada.getSubAforo() - soldTickets - number) >= 0);
        return res;
    }

    public int startTransaction(iUsuario comprador){
        Transaction transaction = new Transaction(comprador);
        transacciones_pendientes.put(transaction.getID(), transaction);
        return transaction.getID();
    }

    public void deleteTransaction(int idTransaction){
        transacciones_pendientes.remove(idTransaction);
    }

    public boolean addTicketToTransaction(int idTransation, int idEvento, int idEntrada, iTicket ticket){
        boolean exito = false;
        Transaction transaction = transacciones_pendientes.get(idTransation);
        if (transaction != null){
            iEvento evento = transaction.eventoIncluded(idEvento);
            if (evento == null){
                evento = eventoDAO.searchById(idEvento);
                if (evento != null){
                    transaction.addEvento(evento);
                }
            }
            if (evento != null){
                iEntrada entrada = transaction.entradaIncluded(evento, idEntrada);
                if (entrada == null){
                    entrada = entradaDAO.searchById(idEntrada);
                    if (entrada != null){
                        evento.addEntrada(entrada);
                    }
                    if (entrada != null){
                        entrada.addTicket(ticket);
                        exito = true;
                    }
                }
            }
        }
        return exito;
    }

    public boolean confirmTransaction(int idTransaction){
        boolean exito = true;
        Transaction transaction = transacciones_pendientes.get(idTransaction);
        if (transaction != null){
            for (iEvento evento : transaction.eventos) {
                for (iEntrada entrada : evento.getEntradas()){
                    for (iTicket ticket : entrada.getTickets()){
                        exito = exito && ticketDAO.registerTicket(ticket, ticket.getUsuario().getDni(), entrada.getId(), ticket.getInformacion());
                    }
                }
            }
        }
        if (exito){
            deleteTransaction(idTransaction);
        }
        return exito;
    }

}

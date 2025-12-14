package Managers;

import java.util.HashMap;
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
    Map<Integer, Transaction> transacciones_pendientes;

    public CompraManager(iDatabaseAccessFactory factory){
        ticketDAO = factory.getTicketDAO();
        entradaDAO = factory.getEntradaDAO();
        eventoDAO = factory.getEventoDAO();
        transacciones_pendientes = new HashMap<>();
    }

    /**
     * Metodo para conseguir una factory de tickets apropiada para el tipo de evento.
     * @param evento Evento cuyo tipo usaremos para determinar el tipo de los tickets.
     * @return TicketFactory del tipo específico para dicho evento.
     */
    public TicketFactory getTicketFactoryByEventType(iEvento evento){
        return switch (evento) {
            case EventoConcierto _ -> new TicketConciertoFactory();
            case EventoCarrera _ -> new TicketCarreraFactory();
            case EventoRifa _ -> new TicketRifaFactory();
            default -> new TicketDefaultFactory();
        };
    }

    /**
     * Metodo para conseguir una transaccion a partir de su ID.
     * @param idTransaction ID de la transaccion que queremos obtener.
     * @return Objeto Transaction si existe una transaccion pendiente con ese ID, null en caso contrario.
     */
    public Transaction getTransaction(int idTransaction){
        return transacciones_pendientes.get(idTransaction);
    }

    /**
     * Metodo para obtener el numero de tickets vendidos de un tipo de entrada de un evento.
     * @param idEvento ID del evento al que le pertenece el ticket.
     * @param idEntrada ID del tipo de entrada dentro del evento.
     * @return Numero de tickets vendidos de ese tipo de entrada, contando aquellos almacenados en la base de datos y aquellos en transacciones pendientes aun no confirmadas.
     */
    public int soldTicktets(int idEvento, int idEntrada){
        int soldTickets = ticketDAO.searchByEntrada(idEntrada).size();

        for (Transaction transaction : transacciones_pendientes.values()){
            iEvento eventoTransaction = transaction.eventoIncluded(idEvento);
            if (eventoTransaction != null){
                iEntrada entradaTransaction = transaction.entradaIncluded(eventoTransaction, idEntrada);
                if (entradaTransaction != null){
                    soldTickets += entradaTransaction.getTickets().size();
                }
            }
        }

        return soldTickets;
    }

    /**
     * Metodo para conocer si se puede adquirir una cantidad de tickets.
     * @param idEvento ID del evento al que le pertenece el ticket.
     * @param idEntrada ID del tipo de entrada dentro del evento.
     * @return True si se pueden adquirir esos tickets, False en caso contrario.
     */
    public boolean checkAvailabity(int idEvento, int idEntrada, int number){
        boolean res;

        iEntrada entrada = entradaDAO.searchById(idEntrada);
        int soldTickets = soldTicktets(idEvento, idEntrada);

        res = ((entrada.getSubAforo() - soldTickets - number) >= 0);
        return res;
    }

    /**
     * Metodo para conocer si los tickets de una transaccion pendiente siguen estando disponibles. Pensado para realizar una comprobacion final antes de procesar la transaccion.
     * @param idTransaction ID de la transaccion que queremos comprobar
     * @return True si se pueden adquirir esos tickets, False en caso contrario.
     */
    public boolean checkAvailabilityTransaction(int idTransaction){
        boolean res = false;
        Transaction transaction = transacciones_pendientes.get(idTransaction);
        if (transaction != null){
            res = true;
            for (iEvento evento : transaction.getEventos()){
                for (iEntrada entrada : evento.getEntradas()){
                    res = res && ((entrada.getSubAforo() - soldTicktets(evento.getID(), entrada.getId())) >= 0);
                }
            }
        }
        return res;
    }

    /**
     * Metodo para inicializar una transaccion.
     * @param comprador Comprador de las entradas.
     * @return ID de la transaccion.
     */
    public int startTransaction(iUsuario comprador){
        Transaction transaction = new Transaction(comprador);
        transacciones_pendientes.put(transaction.getID(), transaction);
        return transaction.getID();
    }

    /**
     * Metodo para cancelar una transaccion.
     * @param idTransaction ID de la transaccion a cancelar.
     */
    public void deleteTransaction(int idTransaction){
        transacciones_pendientes.remove(idTransaction);
    }

    /**
     * Metodo para añadir los tickets que se quieren comprar a la transaccion
     * @param idTransation ID de la transaccion a la que se van a añadir los tickets
     * @param idEvento ID del evento al que le pertenece el ticket.
     * @param idEntrada ID del tipo de entrada dentro del evento.
     * @param ticket Objeto ticket que queremos añadir.
     * @return True si se añaden los tickets correctamente, False en caso contrario
     */
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
                if (entrada == null) {
                    entrada = entradaDAO.searchById(idEntrada);
                    if (entrada != null) {
                        evento.addEntrada(entrada);
                    }
                }
                if (entrada != null) {
                    entrada.addTicket(ticket);
                    exito = true;
                }
            }
        }
        return exito;
    }

    /**
     * Metodo para confirmar una transaccion y almacenar sus tickets permanentemente en la base de datos.
     * @param idTransaction ID de la transaccion que queremos confirmar.
     * @return True si se pueden adquirir esos tickets, False en caso contrario.
     */
    public boolean confirmTransaction(int idTransaction){
        boolean exito = true;
        Transaction transaction = transacciones_pendientes.get(idTransaction);
        if (transaction != null){
            for (iEvento evento : transaction.eventos) {
                for (iEntrada entrada : evento.getEntradas()){
                    for (iTicket ticket : entrada.getTickets()){
                        int nextID = ticketDAO.getNextTicketID();
                        transaction.addTicketIDs(nextID);
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

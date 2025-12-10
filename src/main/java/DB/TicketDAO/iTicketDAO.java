package DB.TicketDAO;

import Datos.Ticket.*;
import java.util.List;

public interface iTicketDAO {

    /**
     * Method to retrieve all tickets purchased by a user with a given DNI
     * @param dni the dni of the user whose tickets are to be retrieved
     */
    List<iTicket> searchByUser(String dni);

    /**
     * Method to retrieve all tickets linked to the given entry
     * @param idEntrada The id of the entry whose tickets we want to list
     */
    List<iTicket> searchByEntrada(int idEntrada);
    
    /**
     * Method for registering certain ticket in the database
     */
    boolean registerTicket(iTicket ticket, String dniComprador, int idEntrada, String informacion);

    /**
     * Method to delete a ticket from de database
     * @param id the id of the ticket that we want to delete from the database
     */
    boolean deleteTicket(String id);
}

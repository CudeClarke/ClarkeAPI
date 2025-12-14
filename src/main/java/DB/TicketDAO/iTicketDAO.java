package DB.TicketDAO;

import Datos.Ticket.*;
import java.util.List;

public interface iTicketDAO {

    /**
     * Metodo para obtener todos los tickets pertenecientes a una persona
     * @param dni dni de la persona cuyos tickets queremos sacar
     * @return Lista de tickets de ese usuario
     */
    List<iTicket> searchByUser(String dni);

    /**
     * Metodo para obtener todos los tickets asociados a una entrada
     * @param idEntrada id de la entrada asociada a esos tickets
     * @return Lista de tickets asociados a esa entrada
     */
    List<iTicket> searchByEntrada(int idEntrada);

    /**
     * Metodo para buscar un ticket a partir de su id.
     * @param idTicket Id del ticket que se desea buscar.
     * @return Objeto iTicket si existe, o null en caso contrario.
     */
    iTicket searchById(int idTicket);

    /**
     * Metodo para buscar el ID de la entrada a la que pertenece el Ticket.
     * @param idTicket Id del ticket.
     * @return ID de la entrada a la que pertenece el Ticket, -1 en caso de fallo.
     */
    int getEntradaID(int idTicket);
    
    /**
     * Metodo para registrar un ticket en la base de datos
     * @param ticket ticket que se quiere registrar en la base de datos
     * @param dniComprador dni del comprador del ticket
     * @param idEntrada id de la entrada asociada al ticket
     * @param informacion informacion especial del ticket
     * @return True o False dependiendo de si la consulta SQL se ejecuta correctamente
     */
    boolean registerTicket(iTicket ticket, String dniComprador, int idEntrada, String informacion);

    /**
     * Metodo para eliminar un ticket de la base de datos
     * @param id el id del ticket a eliminar
     * @return True o False dependiendo de si la consulta SQL se ejecuta correctamente
     */
    boolean deleteTicket(String id);

    /**
     * Metodo para conseguir el proximo el ID de ticket que se asignará en la base de datos.
     * @return Int proximo ID de ticket.
     */
    int getNextTicketID();
}

package Managers;

import DB.iDatabaseAccessFactory;
import DB.TicketDAO.iTicketDAO;
import Datos.Ticket.*;

import java.util.List;

/**
 * Clase que gestiona la lógica de negocio relacionada con los Tickets.
 * Utiliza el patrón DAO para acceder a la base de datos.
 */
public class TicketManager {
    private iTicketDAO ticketDAO;

    /**
     * Constructor de la clase TicketManager.
     * 
     * @param factory Factoría de acceso a datos para obtener el DAO de Tickets.
     */
    public TicketManager(iDatabaseAccessFactory factory) {
        ticketDAO = factory.getTicketDAO();
    }

    /**
     * Valida si un ticket es válido para ser procesado.
     * 
     * @param ticket El ticket a validar.
     * @return true si el ticket es válido, false en caso contrario.
     */
    private boolean validTicket(iTicket ticket) {
        return (ticket != null && ticket.getUsuario() != null && ticket.getDniAsistente() != null
                && !ticket.getDniAsistente().isBlank());
    }

    /**
     * Busca tickets asociados a un usuario por su DNI.
     * 
     * @param dni DNI del usuario.
     * @return Lista de tickets asociados al usuario.
     */
    public List<iTicket> searchByUser(String dni) {
        return ticketDAO.searchByUser(dni);
    }

    /**
     * Busca tickets asociados a una entrada específica.
     * 
     * @param idEntrada ID de la entrada.
     * @return Lista de tickets asociados a la entrada.
     */
    public List<iTicket> searchByEntrada(int idEntrada) {
        return ticketDAO.searchByEntrada(idEntrada);
    }

    public iTicket searchByID(int idTicket) {return ticketDAO.searchById(idTicket);}

    public int searchEntradaID(int idTicket) {return  ticketDAO.getEntradaID(idTicket);}

    /**
     * Registra un nuevo ticket en el sistema.
     * 
     * @param ticket       El objeto ticket a registrar.
     * @param dniComprador DNI del comprador del ticket.
     * @param idEntrada    ID de la entrada asociada.
     * @param informacion  Información adicional del ticket.
     * @return true si el registro fue exitoso, false en caso contrario.
     */
    public boolean registerTicket(iTicket ticket, String dniComprador, int idEntrada, String informacion) {
        boolean res = false;
        if (validTicket(ticket)) {
            res = ticketDAO.registerTicket(ticket, dniComprador, idEntrada, informacion);
        }
        return res;
    }

    /**
     * Elimina un ticket del sistema.
     * 
     * @param id ID del ticket a eliminar (en este caso parece ser un String según
     *           la interfaz DAO).
     * @return true si la eliminación fue exitosa, false en caso contrario.
     */
    public boolean deleteTicket(String id) {
        boolean res = false;
        if (id != null && !id.isBlank()) {
            res = ticketDAO.deleteTicket(id);
        }
        return res;
    }
}

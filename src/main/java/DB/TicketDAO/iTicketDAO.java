package DB.TicketDAO;

import Datos.Ticket.*;
import java.util.List;

public interface iTicketDAO {
    List<iTicket> searchByUser(String dni);
    List<iTicket> searchByEntrada(int idEntrada);
    boolean registerTicket(iTicket ticket, String dniComprador, int idEntrada, String informacion);
    boolean deleteTicket(String id);
}

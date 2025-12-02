package DB.TicketDAO;

import Ticket.*;
import Usuario.*;
import java.util.List;

public interface iTicketDAO {
    boolean registerTicket(String nombre, String DNI_Beneficiario, int id, String type);
    boolean deleteTicket(String id);
    List<iTicket> searchByUser(iUsuario user);
    List<iTicket> searchByType(int type);
    int ticketType (int id);
    TicketFactory getFactoryByType(int type);
}

package DB.TicketDAO;

import Datos.Ticket.*;
import Datos.Usuario.iUsuario;

import java.util.List;

public interface iTicketDAO {
    List<iTicket> searchByUser(iUsuario user);
    List<iTicket> searchByType(int type);
    boolean registerTicket(String DNI_Beneficiario, int id, String type);
    boolean deleteTicket(String id);
}

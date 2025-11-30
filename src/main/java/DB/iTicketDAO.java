package DB;

import Ticket.*;
import Usuario.UsuarioBase;

import java.util.List;

public interface iTicketDAO {
    boolean registerTicket(Ticket ticket);
    boolean deleteTicket(String id);
    List<Ticket> searchByUser(UsuarioBase user);
    List<Ticket> searchByType(String type);
}

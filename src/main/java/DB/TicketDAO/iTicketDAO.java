package DB.TicketDAO;

import Datos.Ticket.*;
import Datos.Usuario.UsuarioBase;

import java.sql.ResultSet;
import java.util.List;

public interface iTicketDAO {
    boolean registerTicket(String nombre, String DNI_Beneficiario, int id, String type);
    boolean deleteTicket(String id);
    List<Ticket> searchByUser(UsuarioBase user);
    List<Ticket> searchByType(String type);
    List<String> ticketNameAndType (int id);
}

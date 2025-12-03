package DB.TicketDAO;

<<<<<<< HEAD
import Datos.Ticket.*;
import Datos.Usuario.UsuarioBase;

import java.sql.ResultSet;
=======
import Ticket.*;
import Usuario.*;
>>>>>>> develop
import java.util.List;

public interface iTicketDAO {
    boolean registerTicket(String nombre, String DNI_Beneficiario, int id, String type);
    boolean deleteTicket(String id);
    List<iTicket> searchByUser(iUsuario user);
    List<iTicket> searchByType(int type);
    int ticketType (int id);
    TicketFactory getFactoryByType(int type);
}

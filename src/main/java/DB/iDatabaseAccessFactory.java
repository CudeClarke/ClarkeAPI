package DB;

import DB.UserDAO.iUsuarioDAO;
import DB.EventoDAO.iEventoDAO;
import DB.EntradaDAO.iEntradaDAO;
import DB.TicketDAO.iTicketDAO;

public interface iDatabaseAccessFactory {
    iUsuarioDAO getUsuarioDAO();
    iEventoDAO getEventoDAO();
    iEntradaDAO getEntradaDAO();
    iTicketDAO getTicketDAO();
}

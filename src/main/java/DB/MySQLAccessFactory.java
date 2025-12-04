package DB;

import DB.EntradaDAO.iEntradaDAO;
import DB.EventoDAO.EventoDAOMySQL;
import DB.EventoDAO.iEventoDAO;
import DB.TicketDAO.TicketDAOMySQL;
import DB.TicketDAO.iTicketDAO;
import DB.UserDAO.UsuarioDAOMySQL;
import DB.UserDAO.iUsuarioDAO;

public class MySQLAccessFactory implements DatabaseAccessFactory{
    MySQLConnection mySQLConnection = MySQLConnection.getDatabase();
    
    @Override
    public iUsuarioDAO getUsuarioDAO() {
        return new UsuarioDAOMySQL(mySQLConnection.getConnection());
    }

    @Override
    public iEventoDAO getEventoDAO() {
        return new EventoDAOMySQL(mySQLConnection.getConnection());
    }

    @Override
    public iEntradaDAO getEntradaDAO() {
        //No EntradaDAOMySQL implemented
        return null;
    }

    @Override
    public iTicketDAO getTicketDAO() {
        return new TicketDAOMySQL(mySQLConnection.getConnection());
    }
}

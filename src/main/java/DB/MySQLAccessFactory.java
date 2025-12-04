package DB;

import DB.EntradaDAO.iEntradaDAO;
import DB.EventoDAO.EventoDAOMySQL;
import DB.EventoDAO.iEventoDAO;
import DB.TicketDAO.TicketDAOMySQL;
import DB.TicketDAO.iTicketDAO;
import DB.UserDAO.UsuarioDAOMySQL;
import DB.UserDAO.iUsuarioDAO;

public class MySQLAccessFactory implements iDatabaseAccessFactory {
    private static MySQLAccessFactory instance;
    private MySQLConnection mySQLConnection;

    private MySQLAccessFactory(){
        mySQLConnection = MySQLConnection.getInstance();
    }

    public static MySQLAccessFactory getInstance() {
        if (instance == null) instance = new MySQLAccessFactory();
        return instance;
    }

    @Override
    public iUsuarioDAO getUsuarioDAO() {
        return UsuarioDAOMySQL.getInstance();
    }

    @Override
    public iEventoDAO getEventoDAO() {
        return EventoDAOMySQL.getInstance();
    }

    @Override
    public iEntradaDAO getEntradaDAO() {
        //No EntradaDAOMySQL implemented
        return null;
    }

    @Override
    public iTicketDAO getTicketDAO() {
        return TicketDAOMySQL.getInstance();
    }
}

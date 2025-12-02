package DB.EventoDAO;

import DB.DatabaseFactory;
import DB.iDatabase;

public class EventoDAOMySQLFactory extends EventoDAOFactory{
    @Override
    public iEventoDAO createEventoDAO() {
        iDatabase db = DatabaseFactory.getDatabase(DatabaseFactory.DbType.MYSQL);
        return new EventoDAOMySQL(db.getConnection());
    }
}

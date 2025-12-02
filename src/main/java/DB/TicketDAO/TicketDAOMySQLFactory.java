package DB.TicketDAO;

import DB.DatabaseFactory;
import DB.iDatabase;

public class TicketDAOMySQLFactory extends TicketDAOFactory {
    @Override
    public iTicketDAO createTicketDAO() {
        iDatabase db = DatabaseFactory.getDatabase(DatabaseFactory.DbType.MYSQL);
        return new TicketDAOMySQL(db.getConnection());
    }
}

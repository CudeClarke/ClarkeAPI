package DB.EntradaDAO;

import DB.DatabaseFactory;
import DB.iDatabase;

public class EntradaDAOMySQLFactory extends EntradaDAOFactory {

    @Override
    public iEntradaDAO createEntradaDAO() {

        iDatabase db = DatabaseFactory.getDatabase(DatabaseFactory.DbType.MYSQL);   
        return new EntradaDAOMySQL(db.getConnection());
    }
}

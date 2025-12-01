package DB.UserDAO;

import DB.DatabaseFactory;
import DB.iDatabase;

public class UsuarioDAOMySQLFactory extends UsuarioDAOFactory{
    @Override
    public iUsuarioDAO createUsuarioDAO() {
        iDatabase db = DatabaseFactory.getDatabase(DatabaseFactory.DbType.MYSQL);
        return new UsuarioDAOMySQL(db.getConnection());
    }
}

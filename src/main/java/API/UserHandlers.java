package API;

import Usuario.UsuarioBase;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.javalin.http.Handler;
import DB.*;
import Usuario.*;

public class UserHandlers {
    private final static DatabaseFactory.DbType DB_TYPE = DatabaseFactory.DbType.MYSQL;
    private final static iDatabase db = DatabaseFactory.getDatabase(DB_TYPE);
    private final static iUsuarioDAO userDAO = new UsuarioDAOMySQL(db.getConnection());
    
    public static Handler getUser = ctx -> {
        iUsuario user = userDAO.searchByDni(ctx.pathParam("dni"));
        ctx.json(user);
    };

    public static Handler storeUser = ctx -> {
        UsuarioBase user = ctx.bodyAsClass(UsuarioBase.class);
        String status = userDAO.register(user)? "OK" : "ERROR";
        String jsonString = String.format("{\"Status\":\"%s\"}", status);
        ctx.json(new ObjectMapper().readTree(jsonString));
    };
}

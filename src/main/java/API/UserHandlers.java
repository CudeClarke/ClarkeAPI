package API;

import io.javalin.validation.ValidationException;
import io.javalin.http.Handler;
import DB.UserDAO.UsuarioDAOFactory;
import DB.UserDAO.UsuarioDAOMySQLFactory;
import DB.UserDAO.iUsuarioDAO;
import Usuario.*;
import utils.json_generator;

public class UserHandlers {
    private final static UsuarioDAOFactory factory = new UsuarioDAOMySQLFactory();
    private final static iUsuarioDAO userDAO = factory.createUsuarioDAO();
    
    public static Handler getUser = ctx -> {
        String dni = ctx.pathParam("dni");
        String res = json_generator.status_response(1, "Incorrect DNI format");
        if (dni.length() == 9 && Character.isLetter(dni.charAt(dni.length() - 1))) {
            iUsuario user = userDAO.searchByDni(ctx.pathParam("dni"));
            if (user != null){
                res = json_generator.Java_to_json(user);
            }else{
                res = json_generator.status_response(1, "Could not find user in database");
            }
        }
        ctx.json(res);
    };

    public static Handler storeUser = ctx -> {
        iUsuario user;
        String res = "";
        try {
            user = ctx.bodyValidator(UsuarioBase.class).get();
        } catch (ValidationException e){
            user = null;
        }
        if (user == null){
            try {
                user = ctx.bodyValidator(UsuarioRegistrado.class).get();
            } catch (ValidationException e) {
                res = json_generator.status_response(1, "Request body does not hold user data");
            }
        }
        if (user != null){
            if (userDAO.register(user)){
                res = json_generator.status_response(0, "User added to database");
            }else{
                res = json_generator.status_response(1, "Could not add user to database");
            }
        }
        ctx.json(res);
    };
}

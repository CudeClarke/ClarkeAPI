package API;

import io.javalin.http.Handler;
import Datos.Usuario.*;
import DB.MySQLAccessFactory;
import utils.json_generator;
import Managers.UserManager;

public class UserHandlers {
    private static final UserManager userManager = new UserManager(MySQLAccessFactory.getInstance());
    
    public static Handler getUserByDni = ctx -> {
        String dni = ctx.pathParam("dni");
        String res = json_generator.status_response(1, "Incorrect DNI format");
        if (dni.length() == 9 && Character.isLetter(dni.charAt(dni.length() - 1))) {
            iUsuario user = userManager.searchByDni(ctx.pathParam("dni"));
            if (user != null){
                res = json_generator.Java_to_json(user);
            }else{
                res = json_generator.status_response(1, "Could not find user in database");
            }
        }
        ctx.json(res);
    };

    public static Handler registerUser = ctx -> {
        iUsuario user;
        String res = "";
        try {
            user = ctx.bodyAsClass(UsuarioBase.class);
        } catch (Exception e){
            user = null;
        }
        if (user == null){
            try {
                user = ctx.bodyAsClass(UsuarioRegistrado.class);
            } catch (Exception e) {
                res = json_generator.status_response(1, "Request body does not hold user data");
            }
        }
        if (user != null){
            if (userManager.registerUsuario(user)){
                res = json_generator.status_response(0, "User added to database");
            }else{
                res = json_generator.status_response(1, "Could not add user to database");
            }
        }
        ctx.json(res);
    };
}

package API;

import com.fasterxml.jackson.databind.node.ObjectNode;
import io.javalin.http.Handler;
import Datos.Usuario.*;
import DB.MySQLAccessFactory;
import utils.json_utils;
import Managers.UserManager;

public class UserHandlers {
    private static UserManager userManager = new UserManager(MySQLAccessFactory.getInstance());
    
    public static Handler getUserByDni = ctx -> {
        String dni = ctx.pathParam("dni");
        String res = json_utils.status_response(1, "Incorrect DNI format");
        if (dni.length() == 9 && Character.isLetter(dni.charAt(dni.length() - 1))) {
            iUsuario user = userManager.searchByDni(ctx.pathParam("dni"));
            if (user != null){
                ObjectNode jsonObject = (ObjectNode) json_utils.Java_to_json_node(user);
                switch (user) {
                    case UsuarioRegistrado _ : jsonObject.put("tipo", "REGISTRADO"); break;
                    case UsuarioBase _ : jsonObject.put("tipo", "BASE"); break;
                    default : jsonObject.put("tipo", "ERROR");
                }
                res = jsonObject.toString();
            }else{
                res = json_utils.status_response(1, "Could not find user in database");
            }
        }
        ctx.json(res);
    };

    public static Handler registerUser = ctx -> {
        iUsuario user;
        String res = "";
        user = json_utils.json_string_to_iUsuario(ctx.body());
        if (user != null){
            if (userManager.registerUsuario(user)){
                res = json_utils.status_response(0, "User added to database");
            }else{
                res = json_utils.status_response(1, "Could not add user to database");
            }
        } else {
            res = json_utils.status_response(1, "Request body does not hold user data");
        }
        ctx.json(res);
    };
}

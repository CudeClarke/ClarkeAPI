package Managers;

import DB.iDatabaseAccessFactory;
import DB.MySQLAccessFactory;
import DB.UserDAO.iUsuarioDAO;
import Datos.Usuario.*;

import java.util.List;

public class UserManager {
    private final iDatabaseAccessFactory factory = MySQLAccessFactory.getInstance();
    private final iUsuarioDAO userDAO = factory.getUsuarioDAO();

    public UserManager(){}

    private boolean validDni(String dni){ return dni.length() == 9 && Character.isLetter(dni.charAt(dni.length() - 1));}

    public List<iUsuario> getAllUsuarios(){
        return userDAO.getAllUsuarios();
    }

    public iUsuario searchByDni(String dni){
        iUsuario res = null;
        if (validDni(dni))
            res = userDAO.searchByDni(dni);
        return res;
    }

    public boolean registerUsuario(iUsuario user){
        boolean res = false;
        if (validDni(user.getDni())){
            res = userDAO.registerUsuario(user);
        }
        return res;
    }

    public boolean updateUsuario(iUsuario user){
        boolean res = false;
        if (validDni(user.getDni())){
            iUsuario exists = userDAO.searchByDni(user.getDni());
            if (exists != null){
                res = userDAO.updateUsuario(user);
            }
        }
        return res;
    }

    public boolean deleteUsuario(String dni){
        boolean res = false;
        if (validDni(dni)){
            iUsuario exists = userDAO.searchByDni(dni);
            if (exists != null){
                res = userDAO.deleteUsuario(dni);
            }
        }
        return res;
    }
}

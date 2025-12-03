package DB.UserDAO;

import Datos.Usuario.*;

public interface iUsuarioDAO {
    iUsuario searchByDni(String DNI);
    boolean registerUsuario(iUsuario usuario);
    boolean update(iUsuario usuario);
    boolean deleteUsuario(String DNI);
}

package DB.UserDAO;

import Datos.Usuario.*;

public interface iUsuarioDAO {
    boolean register(iUsuario usuario);
    boolean update(iUsuario usuario);
    boolean delete(String DNI);
    iUsuario searchByDni(String DNI);
}

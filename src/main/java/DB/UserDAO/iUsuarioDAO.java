package DB.UserDAO;

import Datos.Usuario.*;

import java.util.List;

public interface iUsuarioDAO {
    List<UsuarioBase> listAllUsuarios();
    UsuarioBase searchByDni(String DNI);
    boolean registerUsuarioBase(UsuarioBase usuario);
    boolean updateUsuario(iUsuario usuario);
    boolean deleteUsuario(String DNI);
}

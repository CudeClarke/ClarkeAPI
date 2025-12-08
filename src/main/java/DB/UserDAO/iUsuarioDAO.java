package DB.UserDAO;

import Datos.Usuario.*;

import java.util.List;

public interface iUsuarioDAO {
    List<iUsuario> getAllUsuarios();
    iUsuario searchByDni(String DNI);
    boolean registerUsuario(iUsuario usuario);
    boolean upgradUsuarioToRegistrado(UsuarioRegistrado usuario);
    boolean updateUsuario(iUsuario usuario);
    boolean deleteUsuario(String DNI);
}

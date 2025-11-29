package DB;

import Usuario.*;

public interface iUsuarioDAO {
    boolean register(UsuarioBase usuario);
    boolean update(UsuarioBase usuario);
    boolean delete(String DNI);
    iUsuario searchByDni(String DNI);
}

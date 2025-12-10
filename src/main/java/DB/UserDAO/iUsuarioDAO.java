package DB.UserDAO;

import Datos.Usuario.*;

import java.util.List;

public interface iUsuarioDAO {

    /**
     * Metodo para recuperar todos los usuarios registrados en la base de datos.
     * @return Lista que contiene todos los objetos iUsuarios almacenados.
     */
    List<iUsuario> getAllUsuarios();

    /**
     * Metodo para buscar por el dni de un usuario.
     * @param DNI String que buscamos.
     * @return El usuario (base/registrado) si existe o null en otro caso.
     */
    iUsuario searchByDni(String DNI);

    /**
     * Metodo para registrar un usuario en la base de datos, aprovechando la herencia.
     * @param usuario Objeto con los datos a introducir en la bd, puede ser base o registrado.
     * @return True si se inserto correctamente o false si, o ya estaba en la bd u ocurrio un error.
     */
    boolean registerUsuario(iUsuario usuario);

    /**
     * Metodo para convertir a un usuario ya existente en la base de datos en un usario registrado
     * @param usuario UsuarioRegistrado a introducir en la base de datos
     * @return True si se inserto correctamente o false si, o ya estaba en la bd u ocurrio un error.
     */
    boolean upgradUsuarioToRegistrado(UsuarioRegistrado usuario);

    /**
     * Metodo para actualizar un usuario partiendo del dni
     * @param usuario Objeto con la informacion del usuario
     * @return True si se actualiza o false en otro caso
     */
    boolean updateUsuario(iUsuario usuario);

    /**
     * Metodo para borrar un usuario (base/registrado) de la base de datos.
     * @param DNI String del usuario a borrar.
     * @return True si se borro correctamente o False en otro caso.
     */
    boolean deleteUsuario(String DNI);
}

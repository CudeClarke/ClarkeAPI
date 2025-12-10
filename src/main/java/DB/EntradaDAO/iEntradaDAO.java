package DB.EntradaDAO;

import Datos.Entrada.iEntrada;
import java.util.List;

public interface iEntradaDAO {

    /**
     * Metodo para buscar todas las entradas asociadas a un evento concreto.
     * @param idEvento Identificador del evento.
     * @return Lista de entradas disponibles para ese evento.
     */
    List<iEntrada> searchByEvento(int idEvento);

    /**
     * Metodo para buscar una entrada a partir de su id.
     * @param idEntrada Id de la entrada que se desea buscar.
     * @return Objeto iEntrada si existe, o null en caso contrario.
     */
    iEntrada searchById(int idEntrada);

    /**
     * Metodo para registrar un nuevo tipo de entrada en la base de datos.
     * @param entrada Objeto con los datos de la entrada.
     * @param idEvento ID del evento al que pertenece.
     * @return True si se inserta correctamente o False si ocurre un error.
     */
    boolean registerEntrada(iEntrada entrada, int idEvento);

    /**
     * Metodo para actualizar una entrada existente dado su ID.
     * @param id Identificador unico de la entrada.
     * @param entrada Objeto con los nuevos datos.
     * @return True si se actualiza correctamente.
     */
    boolean updateEntrada(int id, iEntrada entrada);

    /**
     * Metodo para eliminar una entrada de la base de datos por su ID.
     * @param id Identificador unico de la entrada.
     * @return True si se elimina correctamente.
     */
    boolean deleteEntrada(int id);

    /**
     * Metodo auxiliar para recuperar el ID de una entrada.
     * @param entrada Objeto entrada.
     * @param idEvento ID del evento asociado.
     * @return ID de la entrada o -1 si no existe.
     */
    int getID(iEntrada entrada, int idEvento);
}

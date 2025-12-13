package DB.EntradaDAO;

import Datos.Entrada.iEntrada;
import java.util.List;

public interface iEntradaDAO {

    /**
     * Method to search all "entradas" related to a certain "evento"
     * @param idEvento identifier of the "evento"
     * @return List of "entradas" available for that "evento"
     */
    List<iEntrada> searchByEvento(int idEvento);

    /**
     * Method for searching a "entrada" based on its id
     * @param idEntrada id of the "entrada" you are searching for
     * @return an object "iEntrada" if it exists, otherwise it returns null
     */
    iEntrada searchById(int idEntrada);

    /**
     * Method to register a new type of "entrada" in the database
     * @param entrada Object with the "entrada" data
     * @param idEvento id of the "evento" to which it belongs
     * @return returns true or false based on the correct execution of the sql query
     */
    boolean registerEntrada(iEntrada entrada, int idEvento);

    /**
     * Method to update an existent "entrada" based on its id
     * @param id identifier of the "entrada"
     * @param entrada Object with the new data which will replace the old one
     * @return true or false based on the correct execution of the sql query.
     */
    boolean updateEntrada(int id, iEntrada entrada);

    /**
     * Method for deleting a "entrada" from the database
     * @param id unique identifier of the "entrada".
     * @return true or false based on the correct execution of the sql query.
     */
    boolean deleteEntrada(int id);

    /**
     * Auxiliar method to retrieve the id of a "entrada"
     * @param entrada Object "entrada" which we want to retrieve"
     * @param idEvento id of the associated "evento".
     * @return id of the "entrada" if it exists, -1 otherwise.
     */
    int getID(iEntrada entrada, int idEvento);
}

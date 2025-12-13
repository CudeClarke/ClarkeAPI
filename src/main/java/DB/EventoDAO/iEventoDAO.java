package DB.EventoDAO;

import Datos.Evento.*;
import java.util.List;
import java.util.Set;


public interface iEventoDAO {

    /**
     * Method to retrieve all "eventos" from the database
     * @return a list of type "iEvento" ordered by ID
     */
    List<iEvento> getAllEventos();

    /**
     * Method for searching all "eventos" which have a certain "tag"
     * @param tag Name of the tag associated to the "eventos"
     * @return List of type "iEvento" with that certain "tag" ordered by id
     */
    List<iEvento> searchByTag(String tag);

    /**
     * Method for searching "eventos" sponsored by a certain "patrocinador"
     * @param patrocinador Name of the "patrocinador"
     * @return List of type iEvento with "eventos" associated to a certain "patrocinador" ordered by id
     */
    List<iEvento> searchByPatrocinador(String patrocinador);

    /**
     * Method for searching all "eventos" based on its name
     * @param nombre Name of the "evento" we want to search
     * @return Object of type "iEvento" with that certain "name"
     */
    iEvento searchByName(String nombre);

    /**
     * Method for searching all "eventos" based on its id
     * @param idEvento Identificator of the "evento" we want to search
     * @return Object of type "iEvento" with that certain id if it exists, NULL otherwise
     */
    iEvento searchById(int idEvento);

    /**
     * Method for registering a new "evento" on the database
     * The fields "LUGAR" and "DESCRIPCION" are optional
     * so they might be received and set as NULL
     * @param evento "Evento" to be added to the database
     * @param tipo Type of "evento"
     * @return True or false based on the correct execution of the sql query.
     */
    boolean registerEvento(iEvento evento, int tipo);

    /**
     * Method for updating the information of an existent "evento"
     * @param id Identifier of the "evento" to be updated
     * @param evento "Evento" to be updated
     * NOTA: it is taken for granted that the "evento" type is something not to be changed
     * @return True or false based on the correct execution of the sql query.
     */
    boolean updateEvento(int id, iEvento evento);

    /**
     * Method for deleting the information of an existent "evento"
     * @param idEvento identifier of the "evento" to be eliminated
     * @return True or false based on the correct execution of the sql query.
     */
    boolean deleteEvento(int idEvento);

    /**
     * Method for getting the identifier of a certain existent event
     * @param evento Object of type iEvento whose id we are going to get
     * @return The identifier of the "evento". In case of an error, it returns 0
     */
    int getID(iEvento evento);

    /**
     * Method for getting all the "tags" from a certain existent event
     * @param idEvento identificator of the specified event
     * @return Set of the "tags"
     */
    Set<String> getTags(int idEvento);

    /**
     * Method for getting all the "patrocinadores" from a certain existent event
     * @param idEvento identificator of the specified event
     * @return Set of the "patrocinadores"
     */
    Set<Patrocinador> getPatrocinadores(int idEvento);

    /**
     * Method for searching the next id that will be assigned to a "evento" in the database
     * @return Int of the next "evento" id
     */
    int getNextEventoID();

    /**
     * Method for registering a "tag" on the database
     * @param tag Object "tag" that will be added
     * @return true or false based on the correct execution of the sql query.
     */
    boolean registerTag(String tag);

    /**
     * Method for getting the "tag" id based on its name
     * @param tag Name of the "tag"
     * @return ID of the "tag"
     */
    int getTagID(String tag);

    /**
     * Method for searching the next id that will be assigned to a "tag" in the database
     * @return Int of the next "tag" id
     */
    int getNextTagID();

    /**
     * Method for registering a "patrocinador" on the database
     * @param patrocinador Object "patrocinador" that will be added
     * @return True or false based on the correct execution of the sql query.
     */
    boolean registerPatrocinador(Patrocinador patrocinador);

    /**
     * Method for getting the "patrocinador" id based on its name
     * @param nombre Name of the "patrocinador"
     * @return ID of the "patrocinador"
     */
    int getPatrocinadorID(String nombre);

    /**
     * Method for searching the next id that will be assigned to a "patrocinador" in the database
     * @return Int of the next "patrocinador" id
     */
    int getNextPatrocinadorID();

    /**
     * Method for setting the relation between "evento" and "tag" based on their ids
     * @return True or false based on the correct execution of the sql query.
     */
    boolean setRelationEventoTag(int idEvento, int idTag);

    /**
     * Method for setting the relation between "evento" and "patrocinador" based on their ids
     * @return True or false based on the correct execution of the sql query.
     */
    boolean setRelationEventoPatrocinador(int idEvento, int idPatrocinador);
}

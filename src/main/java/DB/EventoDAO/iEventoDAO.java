package DB.EventoDAO;

import Datos.Evento.*;
import java.util.List;
import java.util.Set;


public interface iEventoDAO {

    /**
     * Metodo para recuperar todos los eventos registrados en la base de datos.
     * @return Lista que contiene todos los objetos iEvento almacenados ordenados por ID.
     */
    List<iEvento> getAllEventos();

    /**
     * Metodo para buscar eventos que contengan una etiqueta concreta.
     * @param tag Nombre de la etiqueta asociada a los eventos.
     * @return Lista de eventos que tienen dicha etiqueta ordenados por ID.
     */
    List<iEvento> searchByTag(String tag);

    /**
     * Metodo para buscar eventos patrocinados por un patrocinador concreto.
     * @param patrocinador Nombre del patrocinador.
     * @return Lista de eventos asociados al patrocinador indicado ordenados por ID.
     */
    List<iEvento> searchByPatrocinador(String patrocinador);

    /**
     * Metodo para buscar un evento a partir de su nombre.
     * @param nombre Nombre del evento que se desea buscar.
     * @return Objeto iEvento si existe, o null en caso contrario.
     */
    iEvento searchByName(String nombre);

    /**
     * Metodo para buscar un evento a partir de su id.
     * @param idEvento Id del evento que se desea buscar.
     * @return Objeto iEvento si existe, o null en caso contrario.
     */
    iEvento searchById(int idEvento);

    /**
     * Metodo para añadir un nuevo evento a la base de datos.
     * Los campos LUGAR y DESCRIPCION son opcionales, por lo que
     * pueden recibirse como null y se insertarán como NULL.
     * @param evento Evento a añdir a la base de datos.
     * @param tipo Tipo del evento.
     * @return True si el evento se insertó correctamente, false en caso contrario.
     */
    boolean registerEvento(iEvento evento, int tipo);

    /**
     * Metodo para actualizar la información de un evento existente.
     * @param id Identificador del evento a actualizar.
     * @param evento Evento a actualizar.
     * NOTA: Se entiende que el tipo de evento no es algo que se deba de cambiar.
     * @return True si el evento se actualizo correctamente, false en caso contrario.
     */
    boolean updateEvento(int id, iEvento evento);

    /**
     * Metodo para eliminar la información de un evento existente.
     * @param idEvento Identificador del evento a eliminar.
     * @return True si el evento se actualizo correctamente, false en caso contrario.
     */
    boolean deleteEvento(int idEvento);

    /**
     * Metodo para conseguir el identificador de un evento existente.
     * @param evento Evento al que queremos obtener el identifador.
     * @return El identificador del evento. En caso de error devuelve 0.
     */
    int getID(iEvento evento);

    /**
     * Metodo para conseguir las etiquetas de un evento existente.
     * @param idEvento Identificador del evento al que vamos a obtener las etiquetas.
     * @return Lista de etiquetas del evento.
     */
    Set<String> getTags(int idEvento);

    /**
     * Metodo para conseguir los patrocinadores de un evento existente.
     * @param idEvento Identificador del evento al que vamos a obtener los patrocinadores.
     * @return Lista de patrocinadores del evento.
     */
    Set<Patrocinador> getPatrocinadores(int idEvento);

    /**
     * Metodo para conseguir el proximo el ID de evento que se asignará en la base de datos.
     * @return Int proximo ID de evento.
     */
    int getNextEventoID();

    /**
     * Metodo para añadir un nuevo tag a la base de datos.
     * @param tag String del tag.
     * @return True si el tag se insertó correctamente, false en caso contrario.
     */
    boolean registerTag(String tag);

    /**
     * Metodo para conseguir el ID de un tag dado su String.
     * @param tag String del tag.
     * @return ID del tag.
     */
    int getTagID(String tag);

    /**
     * Metodo para conseguir el proximo el ID de tag que se asignará en la base de datos.
     * @return Int proximo ID de tag.
     */
    int getNextTagID();

    /**
     * Metodo para añadir un nuevo patrocinador a la base de datos.
     * @param patrocinador String del tag.
     * @return True si el patrocinador se insertó correctamente, false en caso contrario.
     */
    boolean registerPatrocinador(Patrocinador patrocinador);

    /**
     * Metodo para conseguir el ID de un patrocinador dado su nombre.
     * @param nombre Nombre del patrocinador
     * @return ID del patrocinador.
     */
    int getPatrocinadorID(String nombre);

    /**
     * Metodo para conseguir el proximo el ID de patrocinador que se asignará en la base de datos.
     * @return Int proximo ID de patrocinador.
     */
    int getNextPatrocinadorID();
}

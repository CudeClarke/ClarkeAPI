package DB.EventoDAO;

import DB.MySQLConnection;
import Datos.Evento.*;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class EventoDAOMySQL implements iEventoDAO {
    private static EventoDAOMySQL instance;
    private final Connection connection;

    private static final int ID_CARRERA = 1;
    private static final int ID_RIFA = 2;
    private static final int ID_CONCIERTO = 3;

    private EventoDAOMySQL() {
        this.connection = MySQLConnection.getInstance().getConnection();
    }

    public static EventoDAOMySQL getInstance() {
        if (instance == null) instance = new EventoDAOMySQL();
        return instance;
    }

    public EventoFactory getFactoryByID(int type){
        return switch(type){
            case ID_CARRERA -> new EventoCarreraFactory();
            case ID_RIFA -> new EventoRifaFactory();
            case ID_CONCIERTO -> new EventoConciertoFactory();
            default -> throw new IllegalArgumentException("Unknown event type ID: " + type);
        };
    }

    private void setOptionalString(PreparedStatement stmt, int index, String value) throws SQLException {
        if (value != null && !value.isBlank()) {
            stmt.setString(index, value);
        } else {
            stmt.setNull(index, Types.VARCHAR);
        }
    }

    private iEvento buildEvento(ResultSet rs) throws SQLException {

        int tipoID = rs.getInt("ID_TIPO_EVENTO");
        String nombre = rs.getString("Nombre");
        int objetivo = rs.getInt("Objetivo");
        String ubicacion = rs.getString("Lugar");
        String descripcion = rs.getString("Descripcion");
        Date fecha = rs.getDate("Fecha");
        String url = rs.getString("Imagen");
        int recaudacion = rs.getInt("Recaudacion");
        String informacionExtra = rs.getString("Informacion");

        EventoFactory factory = getFactoryByID(tipoID);

        iEvento evento = factory.createEvento(
                nombre, ubicacion, objetivo, descripcion, fecha, url, informacionExtra
        );
        evento.setRecaudacion(recaudacion);

        return evento;
    }

    /**
     * Metodo para buscar un evento a partir de su nombre.
     * @param nombreBuscado Nombre del evento que se desea buscar.
     * @return Objeto iEvento si existe, o null en caso contrario.
     */
    public iEvento searchByName(String nombreBuscado) {
        String sql = "SELECT e.* FROM evento e WHERE e.Nombre = ?";

        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, nombreBuscado);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) return buildEvento(rs);
            }
        } catch (SQLException e) {
            System.err.println("Error searching event by name: " + e.getMessage());
        }
        return null;
    }

    /**
     * Metodo para buscar eventos que contengan una etiqueta concreta.
     * @param tag Nombre de la etiqueta asociada a los eventos.
     * @return Lista de eventos que tienen dicha etiqueta.
     */
    @Override
    public List<iEvento> searchByTag(String tag) {
        List<iEvento> lista = new ArrayList<>();

        String sql = """
            SELECT e.*
            FROM evento e
            JOIN clasifica c ON c.id_evento = e.id_evento
            JOIN etiquetas t ON t.id_etiqueta = c.id_etiqueta
            WHERE t.nombre = ?
            """;

        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, tag);
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) lista.add(buildEvento(rs));
            }
        } catch (SQLException e) {
            System.err.println("Error searching event by tag: " + e.getMessage());
        }
        return lista;
    }

    /**
     * Metodo para buscar eventos patrocinados por un patrocinador concreto.
     * @param patrocinador Nombre del patrocinador.
     * @return Lista de eventos asociados al patrocinador indicado.
     */
    @Override
    public List<iEvento> searchByPatrocinador(String patrocinador) {
        List<iEvento> lista = new ArrayList<>();

        String sql = """
            SELECT e.*
            FROM evento e
            JOIN patrocinio p ON p.id_evento = e.id_evento
            JOIN patrocinador pa ON pa.id_patrocinador = p.id_patrocinador
            WHERE pa.nombre = ?
            """;

        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, patrocinador);
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) lista.add(buildEvento(rs));
            }
        } catch (SQLException e) {
            System.err.println("Error searching event by sponsoring: " + e.getMessage());
        }
        return lista;
    }

    /**
     * Metodo para recuperar todos los eventos registrados en la base de datos.
     * @return Lista que contiene todos los objetos iEvento almacenados.
     */
    @Override
    public List<iEvento> getAllEventos() {
        List<iEvento> lista = new ArrayList<>();

        String sql = "SELECT * FROM evento";

        try (PreparedStatement stmt = connection.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {
             while (rs.next()) lista.add(buildEvento(rs));
        } catch (SQLException e) {
            System.err.println("Error getting all events: " + e.getMessage());
        }
        return lista;
    }

    /**
     * Metodo para añadir un nuevo evento a la base de datos.
     * Los campos LUGAR y DESCRIPCION son opcionales, por lo que
     * pueden recibirse como null y se insertarán como NULL.
     * @param evento Evento a añdir a la base de datos.
     * @param tipo Tipo del evento.
     * @return True si el evento se insertó correctamente, false en caso contrario.
     */

    @Override
    public boolean registerEvento(iEvento evento, int tipo) {
        String sql = """
        INSERT INTO evento
        (ID_TIPO_EVENTO, Nombre, Fecha, Lugar, Descripcion, Recaudacion, Objetivo, Informacion, Imagen)
        VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)
        """;

        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, tipo);
            stmt.setString(2, evento.getNombre());
            stmt.setDate(3, evento.getDate());
            setOptionalString(stmt, 4, evento.getUbicacion());
            setOptionalString(stmt, 5, evento.getDescripcion());
            stmt.setDouble(6, evento.getRecaudacion());
            stmt.setDouble(7, evento.getObjetivoRecaudacion());
            setOptionalString(stmt, 8, evento.getInformacion());
            setOptionalString(stmt, 9, evento.getUrl());


            return stmt.executeUpdate() > 0;

        } catch (SQLException e) {
            System.err.println("Error inserting the event: " + e.getMessage());
            return false;
        }
    }

    /**
     * Metodo para actualizar la información de un evento existente.
     * @param id Identificador del evento a actualizar.
     * @param evento Evento a actualizar.
     * NOTA: Se entiende que el tipo de evento no es algo que se deba de cambiar.
     * @return True si el evento se actualizo correctamente, false en caso contrario.
     */

    @Override
    public boolean updateEvento(int id, iEvento evento) {
        if (id <= 0) return false;

        String sql = """
            UPDATE evento SET
            Nombre = ?, Fecha = ?, Lugar = ?, Descripcion = ?,
            Recaudacion = ?, Objetivo = ?, Informacion = ?, Imagen = ?
            WHERE ID_EVENTO = ?
            """;

        try (PreparedStatement stmt = connection.prepareStatement(sql)) {

            stmt.setString(1, evento.getNombre());
            stmt.setDate(2, evento.getDate());
            setOptionalString(stmt, 3, evento.getUbicacion());
            setOptionalString(stmt, 4, evento.getDescripcion());
            stmt.setDouble(5, evento.getRecaudacion());
            stmt.setDouble(6, evento.getObjetivoRecaudacion());
            setOptionalString(stmt, 7, evento.getInformacion());
            setOptionalString(stmt, 8, evento.getUrl());
            stmt.setInt(9, id);

            return stmt.executeUpdate() > 0;

        } catch (SQLException e) {
            System.err.println("Error updating the event: " + e.getMessage());
            return false;
        }
    }

    /**
     * Metodo para eliminar la información de un evento existente.
     * @param idEvento Identificador del evento a eliminar.
     * @return True si el evento se actualizo correctamente, false en caso contrario.
     */

    @Override
    public boolean deleteEvento(int idEvento) {
        if (idEvento <= 0) return false;

        String sql = "DELETE FROM evento WHERE ID_EVENTO = ?";

        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, idEvento);

            // Retorna true si se elimino al menos una fila
            return stmt.executeUpdate() > 0;

        } catch (SQLException e) {
            System.err.println("Error deleting the event: " + e.getMessage());
            return false;
        }
    }

    /**
     * Metodo para conseguir el identificador de un evento existente.
     * @param evento Evento al que queremos obtener el identifador.
     * @return El identificador del evento. En caso de error devuelve 0.
     */

    @Override
    public int getID(iEvento evento) {
        String nombre = evento.getNombre();
        Date fecha = evento.getDate();  // Añado fecha ya que cabe la posibilidad de tener un evento con el mismo nombre
        String sql = "SELECT ID_EVENTO FROM evento WHERE Nombre = ? AND Fecha = ?";

        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, nombre);
            stmt.setDate(2, fecha);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) return rs.getInt("ID_EVENTO");
            }
        } catch (SQLException e) {
            System.err.println("Error getting event ID: " + e.getMessage());
        }
        return 0;
    }
}

package DB.EventoDAO;

import Datos.Evento.*;
import java.sql.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class EventoDAOMySQL implements iEventoDAO {

    private Connection connection;

    public EventoDAOMySQL(Connection connection) {
        this.connection = connection;
    }

    // -------------------------------------------------------
    // Sujeto a cambios por el factory
    // -------------------------------------------------------
    private iEvento buildEvento(ResultSet rs) throws SQLException {

        String nombre = rs.getString("nombre");
        int objetivo = rs.getInt("objetivo");
        int aforo = rs.getInt("aforo");
        String tipo = rs.getString("tipo_evento");

        return switch (tipo.toUpperCase()) {
            case "CARRERA" -> new EventoCarrera(nombre, objetivo, aforo,null,0);
            case "CONCIERTO" -> new EventoConcierto(nombre, objetivo, aforo, null, null, null);
            case "RIFA" -> new EventoRifa(nombre, objetivo, aforo,null);
            default -> new Evento(nombre, objetivo, aforo);
        };
    }

    /**
     * Metodo para buscar un evento a partir de su nombre.
     * @param nombreBuscado Nombre del evento que se desea buscar.
     * @return Objeto iEvento si existe, o null en caso contrario.
     */
    public iEvento searchByName(String nombreBuscado) {
        String sql = """
            SELECT e.*, te.nombre AS tipo_evento
            FROM evento e
            JOIN tipo_evento te ON te.id_tipo_evento = e.id_tipo_evento
            WHERE e.nombre = ?
            """;

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
            SELECT e.*, te.nombre AS tipo_evento
            FROM evento e
            JOIN clasifica c ON c.id_evento = e.id_evento
            JOIN etiquetas t ON t.id_etiqueta = c.id_etiqueta
            JOIN tipo_evento te ON te.id_tipo_evento = e.id_tipo_evento
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
            SELECT e.*, te.nombre AS tipo_evento
            FROM evento e
            JOIN patrocinio p ON p.id_evento = e.id_evento
            JOIN patrocinador pa ON pa.id_patrocinador = p.id_patrocinador
            JOIN tipo_evento te ON te.id_tipo_evento = e.id_tipo_evento
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
     * Metodo para añadir un nuevo evento a la base de datos.
     * Los campos LUGAR y DESCRIPCION son opcionales, por lo que
     * pueden recibirse como null y se insertarán como NULL.
     *
     * @param idevento        Identificador del evento.
     * @param id_tipo_evento    Identificador del tipo de evento.
     * @param nombre          Nombre del evento.
     * @param fecha           Fecha del evento.
     * @param aforo           Aforo del evento.
     * @param recaudacion     Recaudación inicial del evento.
     * @param objetivo        Objetivo de recaudación del evento.
     * @param lugar           (Opcional) Lugar donde se realiza el evento.
     * @param descripcion     (Opcional) Descripción del evento.
     * @return True si el evento se insertó correctamente, false en caso contrario.
     */

    public boolean AñadirEvento(int idevento,int id_tipo_evento,String nombre,Date fecha,int aforo,
                                double recaudacion, double objetivo,String lugar,
                                String descripcion) {
        String sql = """
        INSERT INTO evento
        (id_evento, id_tipo_evento, nombre, fecha, lugar, descripcion, aforo, recaudacion, objetivo)
        VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)
        """;
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, idevento);
            stmt.setInt(2, id_tipo_evento);
            stmt.setString(3, nombre);
            stmt.setDate(4, new java.sql.Date(fecha.getTime()));
            if (lugar != null && !lugar.isBlank()) {
                stmt.setString(5, lugar);
            } else {
                stmt.setNull(5, Types.VARCHAR);
            }
            if (descripcion != null && !descripcion.isBlank()) {
                stmt.setString(6, descripcion);
            } else {
                stmt.setNull(6, Types.VARCHAR);
            }
            stmt.setInt(7, aforo);
            stmt.setDouble(8, recaudacion);
            stmt.setDouble(9, objetivo);

            return stmt.executeUpdate() > 0;

        } catch (SQLException e) {
            System.err.println("Error inserting the event: " + e.getMessage());
            return false;
        }
    }

    /**
     * Metodo para recuperar todos los eventos registrados en la base de datos.
     * @return Lista que contiene todos los objetos iEvento almacenados.
     */
    @Override
    public List<iEvento> getAllEventos() {
        List<iEvento> lista = new ArrayList<>();

        String sql = """
            SELECT e.*, te.nombre AS tipo_evento
            FROM evento e
            JOIN tipo_evento te ON te.id_tipo_evento = e.id_tipo_evento
            """;

        try (PreparedStatement stmt = connection.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {
             while (rs.next()) lista.add(buildEvento(rs));
        } catch (SQLException e) {
            System.err.println("Error getting all events: " + e.getMessage());
        }
        return lista;
    }

    /**
     * Metodo para recuperar el numero de entradas disponibles agrupadas por tipo.
     * Este metodo lee los datos de la tabla ENTRADA asociada al evento.
     *
     * @param idEvento Identificador del evento del que se quieren obtener las entradas.
     * @return Mapa donde la clave es el tipo/descripcion de la entrada y el valor es la cantidad disponible.
     */

    public Map<String, Integer> countEntradasPorEvento(int idEvento) {
        Map<String, Integer> mapa = new HashMap<>();

        String sql = """
        SELECT informacion, cantidad
        FROM entrada
        WHERE id_evento = ?
        """;

        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, idEvento);
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    String tipo = rs.getString("informacion");
                    int cantidad = rs.getInt("cantidad");
                    mapa.put(tipo, cantidad);
                }
            }
        } catch (SQLException e) {
            System.err.println("Error getting ticket types: " + e.getMessage());
        }
        return mapa;
    }

    @Override
    public boolean registerEvento(iEvento evento, int tipo) {
        return false;
    }

    @Override
    public boolean updateEvento(iEvento evento) {
        return false;
    }

    @Override
    public boolean deleteEvento(iEvento evento) {
        return false;
    }

    @Override
    public int getID(iEvento evento) {
        return 0;
    }
}

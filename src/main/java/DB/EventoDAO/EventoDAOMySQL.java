package DB.EventoDAO;

import DB.MySQLConnection;
import Datos.Evento.*;
import java.sql.*;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

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
            default -> new EventoDefaultFactory();
        };
    }

    /**
     * Constructor package-visible, needed for testing
     * @param connection
     */
    EventoDAOMySQL(Connection connection) {
        this.connection = connection;
    }

    private void setOptionalString(PreparedStatement stmt, int index, String value) throws SQLException {
        if (value != null && !value.isBlank()) {
            stmt.setString(index, value);
        } else {
            stmt.setNull(index, Types.VARCHAR);
        }
    }

    private iEvento buildEvento(ResultSet rs) throws SQLException {
        int idEvento = rs.getInt("ID_EVENTO");
        int tipoID = rs.getInt("ID_TIPO_EVENTO");
        String nombre = rs.getString("Nombre");
        float objetivo = rs.getFloat("Objetivo");
        String ubicacion = rs.getString("Lugar");
        String descripcion = rs.getString("Descripcion");
        String fecha = rs.getString("Fecha");
        String url = rs.getString("Imagen");
        float recaudacion = rs.getFloat("Recaudacion");
        String informacionExtra = rs.getString("Informacion");

        ubicacion = (ubicacion == null)? "" : ubicacion;
        descripcion = (descripcion == null)? "" : descripcion;
        informacionExtra = (informacionExtra == null)? "" : informacionExtra;
        url = (url == null)? "default.png" : url;

        EventoFactory factory = getFactoryByID(tipoID);

        iEvento evento = factory.createEvento(
                nombre, ubicacion,recaudacion, objetivo, descripcion, fecha, url,idEvento, informacionExtra
        );

        return evento;
    }

    @Override
    public List<iEvento> getAllEventos() {
        List<iEvento> lista = new ArrayList<>();

        String sql = "SELECT * FROM evento ORDER BY ID_EVENTO ASC";

        try (PreparedStatement stmt = connection.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {
            while (rs.next()) lista.add(buildEvento(rs));
        } catch (SQLException e) {
            System.err.println("Error getting all events: " + e.getMessage());
        }
        return lista;
    }

    @Override
    public List<iEvento> searchByTag(String tag) {
        List<iEvento> lista = new ArrayList<>();

        String sql = """
            SELECT e.*
            FROM evento e
            JOIN clasifica c ON c.id_evento = e.id_evento
            JOIN etiquetas t ON t.id_etiqueta = c.id_etiqueta
            WHERE t.nombre = ?
            ORDER BY e.ID_EVENTO ASC
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

    @Override
    public List<iEvento> searchByPatrocinador(String patrocinador) {
        List<iEvento> lista = new ArrayList<>();

        String sql = """
            SELECT e.*
            FROM evento e
            JOIN patrocinio p ON p.id_evento = e.id_evento
            JOIN patrocinador pa ON pa.id_patrocinador = p.id_patrocinador
            WHERE pa.nombre = ?
            ORDER BY e.ID_EVENTO ASC
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

    @Override
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

    @Override
    public iEvento searchById(int idEvento) {
        String sql = "SELECT e.* FROM evento e WHERE e.ID_EVENTO = ?";

        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, idEvento);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) return buildEvento(rs);
            }
        } catch (SQLException e) {
            System.err.println("Error searching event by id: " + e.getMessage());
        }
        return null;
    }

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
            stmt.setString(3, evento.getDate());
            setOptionalString(stmt, 4, evento.getUbicacion());
            setOptionalString(stmt, 5, evento.getDescripcion());
            stmt.setFloat(6, evento.getRecaudacion());
            stmt.setFloat(7, evento.getObjetivoRecaudacion());
            setOptionalString(stmt, 8, evento.getInformacion());
            setOptionalString(stmt, 9, evento.getUrl());


            return stmt.executeUpdate() > 0;

        } catch (SQLException e) {
            System.err.println("Error inserting the event: " + e.getMessage());
            return false;
        }
    }

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
            stmt.setString(2, evento.getDate());
            setOptionalString(stmt, 3, evento.getUbicacion());
            setOptionalString(stmt, 4, evento.getDescripcion());
            stmt.setFloat(5, evento.getRecaudacion());
            stmt.setFloat(6, evento.getObjetivoRecaudacion());
            setOptionalString(stmt, 7, evento.getInformacion());
            setOptionalString(stmt, 8, evento.getUrl());
            stmt.setInt(9, id);

            return stmt.executeUpdate() > 0;

        } catch (SQLException e) {
            System.err.println("Error updating the event: " + e.getMessage());
            return false;
        }
    }

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

    @Override
    public int getID(iEvento evento) {
        String nombre = evento.getNombre();
        String fecha = evento.getDate();  // Añado fecha ya que cabe la posibilidad de tener un evento con el mismo nombre
        String sql = "SELECT ID_EVENTO FROM evento WHERE Nombre = ? AND Fecha = ?";

        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, nombre);
            stmt.setString(2, fecha);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) return rs.getInt("ID_EVENTO");
            }
        } catch (SQLException e) {
            System.err.println("Error getting event ID: " + e.getMessage());
        }
        return 0;
    }

    @Override
    public Set<String> getTags(int idEvento) {
        Set <String> tags = new HashSet<>();
        String sql = """
            SELECT e.nombre
            FROM etiquetas e
            JOIN clasifica c ON e.id_etiqueta = c.id_etiqueta
            WHERE c.id_evento = ?
            """;
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, idEvento);
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    tags.add(rs.getString("nombre"));
                }
            }
        } catch (SQLException e) {
            System.err.println("Error getting tags for event ID: " + e.getMessage());
        }
        return tags;
    }

    @Override
    public Set<Patrocinador> getPatrocinadores(int idEvento) {
        Set<Patrocinador> patrocinadores = new HashSet<>();
        String sql = """
            SELECT pa.Nombre, pa.Imagen
            FROM patrocinador pa
            JOIN patrocinio p ON pa.id_patrocinador = p.id_patrocinador
            WHERE p.id_evento = ?
            """;

        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, idEvento);
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    String nombre = rs.getString("Nombre");
                    String logo = rs.getString("Imagen");

                    logo = (logo == null)? "default_logo.png" : logo;
                    Patrocinador patrocinador = new Patrocinador(nombre, logo);
                    patrocinadores.add(patrocinador);
                }
            }
        } catch (SQLException e) {
            System.err.println("Error getting sponsors for event ID " + idEvento + ": " + e.getMessage());
        }
        return patrocinadores;
    }

    @Override
    public int getNextEventoID() {
        int nextID = -1;
        String sql = "SELECT COUNT(*) count FROM evento";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            try (ResultSet rs = stmt.executeQuery()){
                if (rs.next()) {
                    nextID = rs.getInt("count") + 1;
                }
            }
        } catch (SQLException e) {
            System.err.println("Error getting next event ID: " + e.getMessage());
        }

        return nextID;
    }

    @Override
    public boolean registerTag(String tag) {
        String sql = "INSERT INTO etiquetas (Nombre) VALUES (?)";
        try(PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, tag);
            int rowsAffected = stmt.executeUpdate();
            return (rowsAffected > 0);
        } catch (SQLException e){
            System.err.println("Error registering a tag: " + e.getMessage());
            return false;
        }
    }

    @Override
    public int getTagID(String tag) {
        String sql = "SELECT ID_ETIQUETA FROM etiquetas WHERE Nombre = ?";
        try(PreparedStatement stmt = connection.prepareStatement(sql)){
            stmt.setString(1, tag);
            try(ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt("ID_ETIQUETA");
                } else {
                    return -1;
                }
            }
        }catch (SQLException e) {
            System.err.println("Error getting tag ID: " + e.getMessage());
            return -1;
        }
    }

    @Override
    public int getNextTagID() {
        String sql = "SELECT MAX(ID_ETIQUETA) AS max_id FROM etiquetas";
        try (PreparedStatement st = connection.prepareStatement(sql);
             ResultSet rs = st.executeQuery()) {
            if (rs.next()) {
                return rs.getInt("max_id") + 1;
            }
        } catch (SQLException e) {
            System.err.println("Error getting next tag ID: " + e.getMessage());
        }
        return 1;
    }

    @Override
    public boolean registerPatrocinador(Patrocinador patrocinador) {
        String sql = "INSERT INTO patrocinador (Nombre, Imagen) VALUES (?, ?)";
        try(PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, patrocinador.getNombre());
            stmt.setString(2, patrocinador.getLogo());
            int rowsAffected = stmt.executeUpdate();
            return (rowsAffected > 0);
        } catch (SQLException e){
            System.err.println("Error registering a patrocinador: " + e.getMessage());
            return false;
        }
    }

    @Override
    public int getPatrocinadorID(String nombre) {
        String sql = "SELECT ID_PATROCINADOR FROM patrocinador WHERE Nombre = ?";
                try(PreparedStatement stmt = connection.prepareStatement(sql)){
                    stmt.setString(1, nombre);
                    try(ResultSet rs = stmt.executeQuery()) {
                        if (rs.next()) {
                            return rs.getInt("ID_PATROCINADOR");
                        } else {
                            return -1;
                        }
                    }
                }catch (SQLException e) {
                    System.err.println("Error getting patrocinador ID: " + e.getMessage());
                    return -1;
                }
    }

    @Override
    public int getNextPatrocinadorID() {
        String sql = "SELECT MAX(ID_PATROCINADOR) AS max_id FROM patrocinador";
        try (PreparedStatement st = connection.prepareStatement(sql);
             ResultSet rs = st.executeQuery()) {
            if (rs.next()) {
                return rs.getInt("max_id") + 1;
            }
        } catch (SQLException e) {
            System.err.println("Error getting next patrocinador ID: " + e.getMessage());
        }
        return 1;
    }


    @Override
    public boolean setRelationEventoTag(int idEvento, int idTag){
        String sql = "INSERT INTO clasifica (ID_ETIQUETA, ID_EVENTO) VALUES (?, ?)";
        try(PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, idTag);
            stmt.setInt(2, idEvento);
            int rowsAffected = stmt.executeUpdate();
            return (rowsAffected > 0);
        } catch (SQLException e){
            System.err.println("Error setting a relation between a evento and tag: " + e.getMessage());
            return false;
        }
    }
    @Override
    public boolean setRelationEventoPatrocinador(int idEvento, int idPatrocinador){
        String sql = "INSERT INTO patrocinio (ID_PATROCINADOR, ID_EVENTO) VALUES (?, ?)";
        try(PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, idPatrocinador);
            stmt.setInt(2, idEvento);
            int rowsAffected = stmt.executeUpdate();
            return (rowsAffected > 0);
        } catch (SQLException e){
            System.err.println("Error setting a relation between evento and patrocinador: " + e.getMessage());
            return false;
        }
    }
}

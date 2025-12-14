package DB.EntradaDAO;

import DB.MySQLConnection;
import Datos.Entrada.*;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class EntradaDAOMySQL implements iEntradaDAO {

    private static EntradaDAOMySQL instance;
    private final Connection connection;

    private EntradaDAOMySQL() {
        this.connection = MySQLConnection.getInstance().getConnection();
    }

    public static EntradaDAOMySQL getInstance() {
        if (instance == null) instance = new EntradaDAOMySQL();
        return instance;
    }

    /**
     * IMPORTANTE: SOLO SE USA EN TESTING
     * @param connection
     */
    EntradaDAOMySQL(Connection connection) {
        this.connection = connection;
    }
    
    private iEntrada buildEntrada(ResultSet rs) throws SQLException {
        int id = rs.getInt("Id_entrada");
        int subAforo = rs.getInt("Cantidad");
        float precio = rs.getFloat("Precio");
        String nombre = rs.getString("Nombre");
        String descripcion = rs.getString("Descripcion");
        return new Entrada(id, subAforo, precio, nombre, descripcion);
    }

    @Override
    public List<iEntrada> searchByEvento(int idEvento) {
        List<iEntrada> lista = new ArrayList<>();
        String sql = "SELECT * FROM entrada WHERE ID_EVENTO = ?";

        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, idEvento);
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) lista.add(buildEntrada(rs));
            }
        } catch (SQLException e) {
            System.err.println("Error searching entries by event: " + e.getMessage());
        }
        return lista;
    }

    @Override
    public iEntrada searchById(int idEntrada) {
        String sql = "SELECT * FROM entrada WHERE ID_ENTRADA = ?";

        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, idEntrada);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) return buildEntrada(rs);
            }
        } catch (SQLException e) {
            System.err.println("Error searching entries by id: " + e.getMessage());
        }
        return null;

    }

    @Override
    public int getEventoID(int idEntrada) {
        int evento = -1;
        String sql = "SELECT ID_EVENTO FROM entrada WHERE ID_ENTRADA = ?";
        try(PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, idEntrada);
            try(ResultSet rs = stmt.executeQuery()){
                while(rs.next()){
                    evento = rs.getInt("ID_EVENTO");
                }
            }
        } catch (SQLException e) {
            System.err.println("Error obtaining the evento id: " + e.getMessage());
        }
        return evento;
    }

    @Override
    public boolean registerEntrada(iEntrada entrada, int idEvento) {
        String sql = "INSERT INTO entrada (ID_EVENTO, Precio, Cantidad, Nombre, Descripcion) VALUES (?, ?, ?, ?, ?)";

        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, idEvento);
            stmt.setFloat(2, entrada.getPrecio());
            stmt.setInt(3, entrada.getSubAforo());
            stmt.setString(4, entrada.getNombre());
            stmt.setString(5, entrada.getDescripcion());

            return stmt.executeUpdate() > 0;

        } catch (SQLException e) {
            System.err.println("Error registering entry: " + e.getMessage());
            return false;
        }
    }

    @Override
    public boolean updateEntrada(int id, iEntrada entrada) {
        String sql = "UPDATE entrada SET Precio = ?, Cantidad = ?, Nombre = ?, Descripcion = ? WHERE ID_ENTRADA = ?";

        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setFloat(1, entrada.getPrecio());
            stmt.setInt(2, entrada.getSubAforo());
            stmt.setString(3, entrada.getNombre());
            stmt.setString(4, entrada.getDescripcion());
            stmt.setInt(5, id);

            return stmt.executeUpdate() > 0;

        } catch (SQLException e) {
            System.err.println("Error updating entry: " + e.getMessage());
            return false;
        }
    }

    @Override
    public boolean deleteEntrada(int id) {
        String sql = "DELETE FROM entrada WHERE ID_ENTRADA = ?";

        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, id);
            return stmt.executeUpdate() > 0;

        } catch (SQLException e) {
            System.err.println("Error deleting entry: " + e.getMessage());
            return false;
        }
    }

    @Override
    public int getID(iEntrada entrada, int idEvento) {
        String sql = "SELECT ID_ENTRADA FROM entrada WHERE ID_EVENTO = ? AND Precio = ? AND Nombre = ? AND Descripcion = ?";
        
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, idEvento);
            stmt.setFloat(2, entrada.getPrecio());
            stmt.setString(3, entrada.getNombre());
            stmt.setString(4, entrada.getDescripcion());
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) return rs.getInt("ID_ENTRADA");
            }
        } catch (SQLException e) {
            System.err.println("Error getting entry ID: " + e.getMessage());
        }
        return -1; 
    }
}

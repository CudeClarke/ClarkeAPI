package DB.TicketDAO;

<<<<<<< HEAD
import Datos.Ticket.*;
import Datos.Usuario.UsuarioBase;
=======
import Ticket.*;
import Usuario.*;
>>>>>>> develop

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

//MUST DELETE NOMBRE ONCE TICKET CLASS IS CHANGED
public class TicketDAOMySQL implements iTicketDAO {
    private Connection connection;
    public TicketDAOMySQL(Connection connection){
        this.connection = connection;
    }

    /**
     * Method to the retrieve the type of a certain ticket, based on the event type
     * @param id the id of the ticket whose type you want to confirm
     */
    @Override
    public int ticketType(int id) {
        String sql = "SELECT e.ID_TIPO_EVENTO as tipo FROM ticket t JOIN entrada en ON en.ID_ENTRADA = t.ID_ENTRADA JOIN evento e ON e.ID_EVENTO = en.ID_EVENTO where t.ID_TICKET = ?";
        try (PreparedStatement st = connection.prepareStatement(sql)) {
            st.setInt(1, id);
            try (ResultSet rs = st.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt("tipo");
                } else {
                    System.err.println("No type found for ticket id: " + id);
                }
            }
        } catch (SQLException e) {
            System.err.println("ERROR: " + e.getMessage());
        }
        return 0;
    }

    @Override
    public TicketFactory getFactoryByType(int type){
        switch(type){
            case 1 -> {
                return new TicketCarreraFactory();
            }
            case 2 -> {
                return new TicketRifaFactory();
            }
            case 3 -> {
                return new TicketConciertoFactory();
            }
            default -> {
                throw new IllegalArgumentException("Unknown ticket type: " + type);
            }
        }
    }

    /**
     * Method to retrieve all tickets purchased by a user with a given DNI
     * @param user the user whose tickets are to be retrieved
     */
    @Override
    public List<iTicket>  searchByUser(iUsuario user) {
        List<iTicket> ticketList = new ArrayList<>();
        String sql = "SELECT * FROM ticket WHERE DNI_USUARIO = ?";
        try(PreparedStatement st = connection.prepareStatement(sql)) {
            st.setString(1, user.getDni());
            try(ResultSet rs = st.executeQuery()){
                while(rs.next()){
                    int id = rs.getInt("ID_TICKET");
                    int type = ticketType(id);
                    String nombre = rs.getString(""); //nombre must be eliminated once changed ticket class
                    String dni = rs.getString("DNI_USUARIO");
                    int info = rs.getInt("Informacion");
                    TicketFactory factory = getFactoryByType(type);
                    iTicket ticket = factory.createTicket(nombre, dni, id, info);
                    ticketList.add((ticket));
                }
            }catch(SQLException e){
                System.err.println("Error searching ticket/s for user: " + e.getMessage());
            }
        }catch (SQLException e){
            System.err.println("Error searching ticket/s for user:" + e.getMessage());
        }
        return ticketList;
    }

    /**
     * Method to retrieve all tickets that are from the same type
     * @param type the type of the tickets we are retrieving
     */
    public List<iTicket>  searchByType(int type) {
        List<iTicket> ticketList = new ArrayList<>();
        String sql = "SELECT * FROM ticket t JOIN entrada en ON en.ID_ENTRADA = t.ID_ENTRADA JOIN evento e ON e.ID_EVENTO = en.ID_EVENTO where e.ID_TIPO_EVENTO = ?";
        try(PreparedStatement st = connection.prepareStatement(sql)) {
            try(ResultSet rs = st.executeQuery()){
                while(rs.next()) {
                        String nombre = rs.getString("");
                        String dni = rs.getString("DNI_USUARIO");
                        int id = rs.getInt("ID_TICKET");
                        int info = rs.getInt("informacion");
                        TicketFactory factory = getFactoryByType(type);
                        iTicket ticket = factory.createTicket(nombre, dni, id, info);
                        ticketList.add(ticket);
                }

            }catch(SQLException e){
                System.err.println("Error searching ticket/s for user: " + e.getMessage());
            }
        }catch (SQLException e){
            System.err.println("Error searching ticket/s for user:" + e.getMessage());
        }
        return ticketList;
    }

    /**
     * Method for registering certain ticket in the database
     */
    @Override
    public boolean registerTicket(String nombre, String dni, int id, String info) {
        String sql = "INSERT INTO ticket (nombre, DNI_USUARIO, ID_TICKET, Informacion) VALUES (?, ?, ?, ?)";
        try(PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, nombre);
            stmt.setString(2, dni);
            stmt.setInt(3, id);
            stmt.setString(6,  info);
            int rowsAffected = stmt.executeUpdate();
            return (rowsAffected > 0);
        } catch (SQLException e){
            System.err.println("Error registering an ticket: " + e.getMessage());
            return false;
        }
    }

    /**
     * Method to delete a ticket from de database
     * @param id the id of the ticket that we want to delete from the database
     */
    @Override
    public boolean deleteTicket(String id) {
        String sql = "DELETE FROM ticket WHERE ID_TICKET = ?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, id);

            int rowsAffected = stmt.executeUpdate();
            return (rowsAffected > 0);

        } catch (SQLException e){
            System.err.println("Error deleting an user: " + e.getMessage());
            return false;
        }
    }
}


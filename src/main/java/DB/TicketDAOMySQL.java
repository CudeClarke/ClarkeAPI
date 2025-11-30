package DB;

import Ticket.*;
import Usuario.UsuarioBase;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class TicketDAOMySQL implements iTicketDAO {
    private Connection connection;
    public TicketDAOMySQL(Connection connection){
        this.connection = connection;
    }

    /**
     * Method to retrieve all tickets purchased by a user with a given DNI
     * @param user the user whose tickets are to be retrieved
     */
    @Override
    public List<Ticket>  searchByUser(UsuarioBase user) {
        List<Ticket> ticketList = new ArrayList<>();
        String sql = "SELECT * FROM tickets WHERE DNI_Beneficiario = ?";
        try(PreparedStatement st = connection.prepareStatement(sql)) {
            st.setString(1, user.getDni());
            try(ResultSet rs = st.executeQuery()){
                while(rs.next()){
                    String type = rs.getString("tipo");
                    Ticket t = null;
                    switch(type){
                        case "Carrera" -> {
                            t = new TicketCarrera(
                                    rs.getString("Nombre"),
                                    rs.getString("DNI_Beneficiario"),
                                    rs.getInt("id"),
                                    rs.getInt("Dorsal")
                            );
                        }
                        case "Rifa" -> {
                            t = new TicketRifa(
                                    rs.getString("Nombre"),
                                    rs.getString("DNI_Beneficiario"),
                                    rs.getInt("id"),
                                    rs.getInt("id_Boleto")
                            );
                        }
                        case "Concierto" -> {
                            t = new TicketConcierto(
                                    rs.getString("Nombre"),
                                    rs.getString("DNI_Beneficiario"),
                                    rs.getInt("id"),
                                    rs.getInt("asiento")
                            );
                        }
                    }
                    ticketList.add(t);
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
    public List<Ticket>  searchByType(String type) {
        List<Ticket> ticketList = new ArrayList<>();
        String sql = "SELECT * FROM tickets WHERE tipo = ?";
        try(PreparedStatement st = connection.prepareStatement(sql)) {
            st.setString(1, type);
            try(ResultSet rs = st.executeQuery()){
                while(rs.next()){
                    Ticket t = null;
                    switch(type){
                        case "Carrera" -> {
                            t = new TicketCarrera(
                                    rs.getString("Nombre"),
                                    rs.getString("DNI_Beneficiario"),
                                    rs.getInt("id"),
                                    rs.getInt("Dorsal")
                            );
                        }
                        case "Rifa" -> {
                            t = new TicketRifa(
                                    rs.getString("Nombre"),
                                    rs.getString("DNI_Beneficiario"),
                                    rs.getInt("id"),
                                    rs.getInt("id_Boleto")
                            );
                        }
                        case "Concierto" -> {
                            t = new TicketConcierto(
                                    rs.getString("Nombre"),
                                    rs.getString("DNI_Beneficiario"),
                                    rs.getInt("id"),
                                    rs.getInt("asiento")
                            );
                        }
                    }
                    if(t!=null) {
                        ticketList.add(t);
                    }
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
     * @param ticket the ticket we are registering in the database
     */
    @Override
    public boolean registerTicket(Ticket ticket) {
        String sql = "INSERT INTO tickets (nombre, DNIBeneficiario, id, tipo) VALUES (?, ?, ?, ?)";
        String type;
        if(ticket instanceof TicketCarrera){
            type = "carrera";
        }else if(ticket instanceof TicketConcierto){
            type = "concierto";
        }else if(ticket instanceof TicketRifa){
            type = "rifa";
        }else{
            throw new IllegalArgumentException("ERROR: Uknown ticket type");
        }
        try(PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, ticket.getNombre());
            stmt.setString(2, ticket.getDniBeneficiario());
            stmt.setInt(3, ticket.getId());
            stmt.setString(4, type);
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
        String sql = "DELETE FROM tickets WHERE id = ?";
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


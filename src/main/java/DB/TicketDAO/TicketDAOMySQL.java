package DB.TicketDAO;

import Datos.Ticket.*;
import Datos.Usuario.UsuarioBase;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class TicketDAOMySQL implements iTicketDAO {
    private Connection connection;
    public TicketDAOMySQL(Connection connection){
        this.connection = connection;
    }

    @Override
    public List<String> ticketNameAndType(int id) {
        String sql = "SELECT e.nombre, e.tipo FROM TICKET t JOIN ENTRADA en ON en.ID = t.id_Entrada JOIN EVENTO e ON e.ID = en.id_Evento where t.id = ?";
        List<String> s = new ArrayList<>();
        try(PreparedStatement st = connection.prepareStatement(sql)){
            st.setInt(1, id);
            try (ResultSet rs = st.executeQuery()) {
                if (rs.next()) {
                    s.add(rs.getString("nombre"));
                    s.add(rs.getString("tipo"));
                    return s;
                } else {
                    System.err.println("No type found for ticket id: " + id);
                }
            }
        }catch (SQLException e){
            System.err.println("ERROR: " + e.getMessage());
        }
        return null;
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
                    List<String> tuple = ticketNameAndType(rs.getInt("id"));
                    String nombre =tuple.getFirst();
                    String DNI = rs.getString("DNI_Beneficiario");
                    int id = rs.getInt("id");
                    int info = rs.getInt("informacion");
                    String type = tuple.get(1);
                    switch(type){
                        case "Carrera" -> {
                            ticketList.add(new TicketCarrera(nombre, DNI, id, info));
                        }
                        case "Rifa" -> {
                            ticketList.add(new TicketRifa(nombre, DNI, id, info));
                        }
                        case "Concierto" -> {
                            ticketList.add(new TicketConcierto(nombre, DNI, id, info));
                        }
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
     * Method to retrieve all tickets that are from the same type
     * @param type the type of the tickets we are retrieving
     */
    public List<Ticket>  searchByType(String type) {
        List<Ticket> ticketList = new ArrayList<>();
        String sql = "SELECT * FROM tickets";
        try(PreparedStatement st = connection.prepareStatement(sql)) {
            try(ResultSet rs = st.executeQuery()){
                while(rs.next()) {
                    List<String> tuple = ticketNameAndType(rs.getInt("id"));
                    if (tuple.get(1).equals(type)) {
                        String nombre = tuple.getFirst();
                        String DNI = rs.getString("DNI_Beneficiario");
                        int id = rs.getInt("id");
                        int info = rs.getInt("informacion");
                        switch (type) {
                                case "Carrera" -> {
                                    ticketList.add(new TicketCarrera(nombre, DNI, id, info));
                                }
                                case "Rifa" -> {
                                    ticketList.add(new TicketRifa(nombre, DNI, id, info));
                                }
                                case "Concierto" -> {
                                    ticketList.add(new TicketConcierto(nombre, DNI, id, info));
                                }
                        }



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
     */
    @Override
    public boolean registerTicket(String nombre, String DNI_Beneficiario, int id, String type) {
        String sql = "INSERT INTO tickets (nombre, DNIBeneficiario, id, informacion) VALUES (?, ?, ?, ?)";
        try(PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, nombre);
            stmt.setString(2, DNI_Beneficiario);
            stmt.setInt(3, id);
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


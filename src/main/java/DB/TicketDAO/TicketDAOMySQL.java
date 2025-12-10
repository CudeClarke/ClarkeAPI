package DB.TicketDAO;

import DB.MySQLConnection;
import Datos.Ticket.*;
import Datos.Usuario.UsuarioBase;
import Datos.Usuario.iUsuario;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

//MUST DELETE NOMBRE ONCE TICKET CLASS IS CHANGED
public class TicketDAOMySQL implements iTicketDAO {
    private static TicketDAOMySQL instance;
    private final Connection connection;

    private TicketDAOMySQL(){
        this.connection = MySQLConnection.getInstance().getConnection();
    }

    public static TicketDAOMySQL getInstance() {
        if (instance == null) instance = new TicketDAOMySQL();
        return instance;
    }

    /**
     * Constructor package-visible, needed for testing
     * @param connection
     */
    TicketDAOMySQL(Connection connection) {
        this.connection = connection;
    }

    /**
     * Method to the retrieve the type of a specific ticket, based on the event type
     * @param id the id of the ticket whose type you want to confirm
     */
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
    /**
     * Method to the create a ticket of a certain type
     * @param type the type of the ticket that we are trying to create
     */
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
     * Method to retrieve all tickets that are from the same type
     * @param type the type of the tickets we are retrieving
     */
    /*public List<iTicket>  searchByType(int type) {
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
                        iTicket ticket = factory.createTicket(user, dni, id, info);
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

*/



    /**
     * Method to retrieve all tickets purchased by a user with a given DNI
     * @param dni the dni of the user whose tickets are to be retrieved
     */
    @Override
    public List<iTicket> searchByUser(String dni) {
        List<iTicket> ticketList = new ArrayList<>();
        String sql = "SELECT * FROM ticket WHERE DNI_USUARIO = ?";
        try(PreparedStatement st = connection.prepareStatement(sql)) {
            st.setString(1, dni);
            try(ResultSet rs = st.executeQuery()){
                while(rs.next()){
                    int id = rs.getInt("ID_TICKET");
                    int type = ticketType(id);
                    iUsuario user = new UsuarioBase("a", "b", "c",dni);
                    String Dni_asistente = rs.getString("Dni_asistente");
                    String info = rs.getString("Informacion");
                    TicketFactory factory = getFactoryByType(type);
                    float pago_extra = rs.getFloat("Pago_extra");
                    iTicket ticket;
                    if(pago_extra == 0) {
                        ticket = factory.createTicket(user, Dni_asistente, info, id);
                    }else{
                        ticket = factory.createTicket(user, Dni_asistente, pago_extra, info, id);
                    }
                    ticketList.add((ticket));
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
     * Method to retrieve all tickets linked to the given entry
     * @param idEntrada The id of the entry whose tickets we want to list
     */
    @Override
    public List<iTicket> searchByEntrada(int idEntrada) {
        List<iTicket> ticketList = new ArrayList<>();
        String sql = "SELECT * FROM ticket WHERE ID_ENTRADA = ?";
        try(PreparedStatement st = connection.prepareStatement(sql)) {
            st.setInt(1, idEntrada);
            try(ResultSet rs = st.executeQuery()){
                while(rs.next()){
                    int id = rs.getInt("ID_TICKET");
                    int type = ticketType(id);
                    iUsuario user = new UsuarioBase("a","b","c",rs.getString("DNI_USUARIO"));
                    String Dni_asistente = rs.getString("Dni_asistente");
                    String info = rs.getString("Informacion");
                    TicketFactory factory = getFactoryByType(type);
                    float pago_extra = rs.getFloat("Pago_extra");
                    iTicket ticket;
                    if(pago_extra == 0) {
                        ticket = factory.createTicket(user, Dni_asistente, info, id);
                    }else{
                        ticket = factory.createTicket(user, Dni_asistente, pago_extra, info, id);
                    }
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
     * Metodo para buscar un ticket a partir de su id.
     * @param idTicket Id del ticket que se desea buscar.
     * @return Objeto iTicket si existe, o null en caso contrario.
     */
    @Override
    public iTicket searchById(int idTicket) {
        String sql = "SELECT * FROM ticket WHERE ID_TICKET = ?";
        try(PreparedStatement st = connection.prepareStatement(sql)) {
            st.setInt(1, idTicket);
            try(ResultSet rs = st.executeQuery()){
                if(rs.next()){
                    int type = ticketType(idTicket);
                    iUsuario user = new UsuarioBase("a", "b", "c",rs.getString("DNI_USUARIO")); //SHOULD BE IMPLEMENTED CORRECTLY
                    String Dni_asistente = rs.getString("Dni_asistente");
                    String info = rs.getString("Informacion");
                    TicketFactory factory = getFactoryByType(type);
                    float pago_extra = rs.getFloat("Pago_extra");
                    iTicket ticket;
                    if(pago_extra == 0) {
                        ticket = factory.createTicket(user, Dni_asistente, info, idTicket);
                    }else{
                        ticket = factory.createTicket(user, Dni_asistente, pago_extra, info, idTicket);
                    }
                    return ticket;
                }
            }catch(SQLException e){
                System.err.println("Error searching ticket/s for user: " + e.getMessage());
            }
        }catch (SQLException e){
            System.err.println("Error searching ticket/s for user:" + e.getMessage());
        }
        return null;
    }



    /**
     * Method for registering certain ticket in the database
     */
    @Override
    public boolean registerTicket(iTicket ticket, String dniComprador, int idEntrada, String informacion) {
        String sql = "INSERT INTO ticket (DNI_USUARIO, ID_ENTRADA, Dni_asistente, Pago_extra, Informacion) VALUES (?, ?, ?, ?, ?)";
        try(PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, dniComprador);
            stmt.setInt(2, idEntrada);
            stmt.setString(3, ticket.getDniAsistente());
            stmt.setFloat(4, ticket.getPagoExtra());
            stmt.setString(5, informacion);
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


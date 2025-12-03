package API;

import io.javalin.http.Handler;
import java.util.List;

import DB.TicketDAO.*;
import Datos.Ticket.*;
import Datos.Usuario.*;
import utils.json_generator;

public class TicketHandler {

    private final static TicketDAOFactory factory = new TicketDAOMySQLFactory();
    private final static iTicketDAO ticketDAO = factory.createTicketDAO();

    public static class DatosTicket {
        public String nombre;
        public String dni;
        public int id;
        public String info;

        public DatosTicket() {
        }
    }

    public static Handler getTickets = ctx -> {
        String res;
        String dni = ctx.queryParam("dni");
        String typeParam = ctx.queryParam("type");

        try {
            if (dni != null && !dni.isBlank()) {
                iUsuario user = new UsuarioBase("", "", "", dni);
                List<iTicket> lista = ticketDAO.searchByUser(user);

                if (lista != null && !lista.isEmpty()) {
                    res = json_generator.Java_to_json(lista);
                } else {
                    res = json_generator.status_response(1, "No tickets found for user with DNI: " + dni);
                }
            } else if (typeParam != null && !typeParam.isBlank()) {
                int type = Integer.parseInt(typeParam);
                List<iTicket> lista = ticketDAO.searchByType(type);

                if (lista != null && !lista.isEmpty()) {
                    res = json_generator.Java_to_json(lista);
                } else {
                    res = json_generator.status_response(1, "No tickets found for type: " + type);
                }
            } else {
                res = json_generator.status_response(1, "Please specify 'dni' or 'type' query parameter");
            }
        } catch (NumberFormatException e) {
            res = json_generator.status_response(1, "Invalid type parameter format");
        } catch (Exception e) {
            e.printStackTrace();
            res = json_generator.status_response(1, "Internal Server Error: " + e.getMessage());
        }

        ctx.json(res);
    };

    public static Handler getTicketById = ctx -> {
        String idParam = ctx.pathParam("id");
        String res = json_generator.status_response(1, "Invalid ticket ID format");

        if (idParam != null && !idParam.isBlank()) {
            try {
                int id = Integer.parseInt(idParam);
                int type = ticketDAO.ticketType(id);

                if (type > 0) {
                    res = String.format("{\"id\": %d, \"type\": %d, \"message\": \"Ticket found\"}", id, type);
                } else {
                    res = json_generator.status_response(1, "Could not find ticket in database");
                }
            } catch (NumberFormatException e) {
                res = json_generator.status_response(1, "Invalid ID format");
            } catch (Exception e) {
                e.printStackTrace();
                res = json_generator.status_response(1, "Database Error: " + e.getMessage());
            }
        }

        ctx.json(res);
    };

    public static Handler addTicket = ctx -> {
        String res = json_generator.status_response(1, "Unknown error");

        try {
            DatosTicket datos = ctx.bodyAsClass(DatosTicket.class);

            if (datos.nombre == null || datos.nombre.isBlank() ||
                    datos.dni == null || datos.dni.isBlank()) {
                res = json_generator.status_response(1, "Nombre y DNI son obligatorios.");
            } else {
                boolean exito = ticketDAO.registerTicket(
                        datos.nombre,
                        datos.dni,
                        datos.id,
                        datos.info);

                if (exito) {
                    res = json_generator.status_response(0, "Ticket registrado correctamente.");
                } else {
                    res = json_generator.status_response(1, "Fallo al registrar ticket (DB Error/Duplicado).");
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
            res = json_generator.status_response(1, "Error en los datos: " + e.getMessage());
        }

        ctx.json(res);
    };

    public static Handler deleteTicket = ctx -> {
        String idParam = ctx.pathParam("id");
        String res = json_generator.status_response(1, "Invalid ticket ID format");

        if (idParam != null && !idParam.isBlank()) {
            try {
                boolean exito = ticketDAO.deleteTicket(idParam);

                if (exito) {
                    res = json_generator.status_response(0, "Ticket eliminado correctamente.");
                } else {
                    res = json_generator.status_response(1, "No se pudo eliminar el ticket (no existe o DB error).");
                }
            } catch (Exception e) {
                e.printStackTrace();
                res = json_generator.status_response(1, "Database Error: " + e.getMessage());
            }
        }

        ctx.json(res);
    };
}

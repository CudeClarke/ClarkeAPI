package API;

import io.javalin.http.Handler;
import java.util.List;

import Datos.Ticket.*;
import Datos.Usuario.*;
import Managers.TicketManager;
import utils.json_utils;

public class TicketHandler {

    private final static TicketManager ticketManager = new TicketManager(DB.MySQLAccessFactory.getInstance());

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
                List<iTicket> lista = ticketManager.searchByUser(dni);

                if (lista != null && !lista.isEmpty()) {
                    res = json_utils.Java_to_json_string(lista);
                } else {
                    res = json_utils.status_response(1, "No tickets found for user with DNI: " + dni);
                }
            } else if (typeParam != null && !typeParam.isBlank()) {
                // Search by type not supported in TicketManager
                res = json_utils.status_response(1, "Search by type not implemented yet");
            } else {
                res = json_utils.status_response(1, "Please specify 'dni' or 'type' query parameter");
            }
        } catch (NumberFormatException e) {
            res = json_utils.status_response(1, "Invalid type parameter format");
        } catch (Exception e) {
            e.printStackTrace();
            res = json_utils.status_response(1, "Internal Server Error: " + e.getMessage());
        }

        ctx.json(res);
    };

    public static Handler getTicketById = ctx -> {
        // ticketType method not available in TicketManager
        String res = json_utils.status_response(1, "Get Ticket By ID not supported in this version");
        ctx.json(res);
    };

    public static Handler addTicket = ctx -> {
        String res = json_utils.status_response(1, "Unknown error");

        try {
            DatosTicket datos = ctx.bodyAsClass(DatosTicket.class);

            if (datos.nombre == null || datos.nombre.isBlank() ||
                    datos.dni == null || datos.dni.isBlank()) {
                res = json_utils.status_response(1, "Nombre y DNI son obligatorios.");
            } else {
                // Create ticket object for registration
                iUsuario user = new UsuarioBase(datos.nombre, "", "", datos.dni);
                iTicket ticket = new Ticket(user, datos.dni);

                boolean exito = ticketManager.registerTicket(
                        ticket,
                        datos.dni,
                        datos.id,
                        datos.info);

                if (exito) {
                    // Return the created ticket object instead of just status
                    res = json_utils.Java_to_json_string(ticket);
                } else {
                    res = json_utils.status_response(1, "Fallo al registrar ticket (DB Error/Duplicado).");
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
            res = json_utils.status_response(1, "Error en los datos: " + e.getMessage());
        }

        ctx.json(res);
    };

    public static Handler deleteTicket = ctx -> {
        String idParam = ctx.pathParam("id");
        String res = json_utils.status_response(1, "Invalid ticket ID format");

        if (idParam != null && !idParam.isBlank()) {
            try {
                boolean exito = ticketManager.deleteTicket(idParam);

                if (exito) {
                    res = json_utils.status_response(0, "Ticket eliminado correctamente.");
                } else {
                    res = json_utils.status_response(1, "No se pudo eliminar el ticket (no existe o DB error).");
                }
            } catch (Exception e) {
                e.printStackTrace();
                res = json_utils.status_response(1, "Database Error: " + e.getMessage());
            }
        }

        ctx.json(res);
    };
}

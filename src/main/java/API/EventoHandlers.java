package API;

import io.javalin.http.Handler;
import java.util.List;
import java.sql.Date; 

import DB.EventoDAO.*;
import Evento.*;        
import utils.json_generator; 

public class EventoHandlers {

    private final static EventoDAOFactory factory = new EventoDAOMySQLFactory();
    private final static iEventoDAO eventoDAO = factory.createEventoDAO();

    public static class DatosEvento {
        public int idevento;
        public int id_tipo_evento;
        public int aforo;
        public double recaudacion;
        public double objetivo;
        public String nombre;
        public String fecha;
        public String lugar;
        public String descripcion;
        
        public DatosEvento() {}
    }


    public static Handler getEvents = ctx -> {
        String res;
        try {
            List<iEvento> lista = eventoDAO.getAllEventos();
            if (lista != null && !lista.isEmpty()) {
                res = json_generator.Java_to_json(lista);
            } else {
                res = json_generator.status_response(1, "No events found in database");
            }
        } catch (Exception e) {
            e.printStackTrace();
            res = json_generator.status_response(1, "Internal Server Error: " + e.getMessage());
        }
        ctx.json(res);
    };

    public static Handler getEventByName = ctx -> {
        String nombre = ctx.pathParam("nombre");
        String res = json_generator.status_response(1, "Invalid event name format");

        if (nombre != null && !nombre.isBlank()) {
            try {
                iEvento evento = eventoDAO.searchByName(nombre);
                if (evento != null) {
                    res = json_generator.Java_to_json(evento);
                } else {
                    res = json_generator.status_response(1, "Could not find event in database");
                }
            } catch (Exception e) {
                e.printStackTrace();
                res = json_generator.status_response(1, "Database Error: " + e.getMessage());
            }
        }
        ctx.json(res);
    };


    public static Handler addEvent = ctx -> {
        String res = json_generator.status_response(1, "Unknown error");

        try {
            DatosEvento datos = ctx.bodyAsClass(DatosEvento.class);

            if (datos.nombre == null || datos.nombre.isBlank() || datos.fecha == null) {
                res = json_generator.status_response(1, "Nombre y Fecha son obligatorios.");
            } else {
                
                java.sql.Date fechaSQL = java.sql.Date.valueOf(datos.fecha);

                // TODO: Descomentar cuando el DAO tenga el método oficial
                /*
                ((DB.EventoDAO.EventoDAOMySQL)eventoDAO).AñadirEvento(
                    datos.idevento, datos.id_tipo_evento, datos.nombre, fechaSQL,
                    datos.aforo, datos.recaudacion, datos.objetivo, 
                    datos.lugar, datos.descripcion
                );
                */

                boolean exito = true; 

                if (exito) {
                    res = json_generator.status_response(0, "Evento recibido correctamente.");
                } else {
                    res = json_generator.status_response(1, "Fallo al añadir evento (DB Error/Duplicado).");
                }
            }

        } catch (Exception e) {
            e.printStackTrace(); 
            res = json_generator.status_response(1, "Error en los datos: " + e.getMessage());
        }
        
        ctx.json(res);
    };
}

package API;

import com.fasterxml.jackson.core.TreeNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import io.javalin.http.Handler;
import io.javalin.http.Context;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import Managers.EventoManager;
import DB.MySQLAccessFactory;
import Datos.Evento.*;
import utils.json_generator; 

public class EventoHandlers {
    private static final EventoManager eventoManager = new EventoManager(MySQLAccessFactory.getInstance());

    private static iEvento eventoFromRequest(Context ctx){
        iEvento evento = null;
        try {
            evento = ctx.bodyAsClass(EventoCarrera.class);
        }catch (Exception e){
            System.out.println("Not EventoCarrera");
        }
        if (evento == null){
            try {
                evento = ctx.bodyAsClass(EventoConcierto.class);
            }catch (Exception e){
                System.out.println("Not EventoConcierto");
            }
        }
        if (evento == null){
            try {
                evento = ctx.bodyAsClass(EventoRifa.class);
            }catch (Exception e){
                System.out.println("Not EventoRifa");
            }
        }

        return evento;
    }

    public static Handler getEvents = ctx -> {
        String res = "";
        List<iEvento> eventos = eventoManager.getAllEventos();

        if (eventos != null && !eventos.isEmpty()) {
            ObjectMapper mapper = new ObjectMapper();
            ArrayNode jsonArray = mapper.createArrayNode();
            for (int i = 0; i<eventos.size(); i++){
                ObjectNode current_node = mapper.createObjectNode();
                iEvento current_evento = eventos.get(i);
                current_node.put("idEvento", i+1);
                current_node.put("tipo", eventoManager.getEventType(current_evento));
                current_node.putPOJO("evento", current_evento);
                jsonArray.add(current_node);
            }

            res = jsonArray.toString();
        } else {
            res = json_generator.status_response(1, "No events found in database");
        }

        ctx.json(res);
    };

    public static Handler getEventByName = ctx -> {
        String nombre = ctx.pathParam("nombre");
        String res = json_generator.status_response(1, "Invalid event name format");

        if (nombre != null && !nombre.isBlank()) {
            iEvento evento = eventoManager.searchByName(nombre);
            if (evento != null) {
                res = json_generator.Java_to_json(evento);
            } else {
                res = json_generator.status_response(1, "Could not find event in database");
            }
        }

        ctx.json(res);
    };


    public static Handler addEvent = ctx -> {
        String res = "";
        iEvento evento = eventoFromRequest(ctx);
        
        if (evento == null){
            res = json_generator.status_response(1, "Request body does not hold Evento data");
        }
        else {
            if (evento.getNombre() == null || evento.getNombre().isBlank() || evento.getDate() == null) {
                res = json_generator.status_response(1, "Nombre y Fecha son obligatorios.");
            } else {
                if (eventoManager.registerEvento(evento)) {
                    res = json_generator.status_response(0, "Evento recibido correctamente.");
                } else {
                    res = json_generator.status_response(1, "Fallo al añadir evento (DB Error/Duplicado).");
                }
            }
        }
        
        ctx.json(res);
    };
}

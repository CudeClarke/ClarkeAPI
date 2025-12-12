package API;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import io.javalin.http.Handler;
import java.util.List;

import Managers.EventoManager;
import DB.MySQLAccessFactory;
import Datos.Evento.*;
import utils.json_utils;

public class EventoHandlers {
    private static EventoManager eventoManager = new EventoManager(MySQLAccessFactory.getInstance());

    public static Handler getEvents = ctx -> {
        String res = "";
        List<iEvento> eventos = eventoManager.getAllEventos();

        if (eventos != null && !eventos.isEmpty()) {
            ObjectMapper mapper = new ObjectMapper();
            ArrayNode jsonArray = mapper.createArrayNode();
            for (iEvento evento : eventos) {
                ObjectNode current_node = mapper.createObjectNode();
                current_node.put("tipo", eventoManager.getEventType(evento));
                current_node.putPOJO("evento", evento);
                jsonArray.add(current_node);
            }

            res = jsonArray.toString();
        } else {
            res = json_utils.status_response(1, "No events found in database");
        }

        ctx.json(res);
    };

    public static Handler getEventByID = ctx -> {
        String res = "";
        int idEvento;
        try {
            idEvento = Integer.parseInt(ctx.pathParam("idEvento"));
        } catch (NumberFormatException e){
            idEvento = -1;
        }
        if (idEvento > 0){
            iEvento evento = eventoManager.searchById(idEvento);
            if (evento == null){
                res = json_utils.status_response(1, "Could not find event");
            }else{
                ObjectMapper mapper = new ObjectMapper();
                ObjectNode jsonObject = mapper.createObjectNode();
                jsonObject.put("tipo", eventoManager.getEventType(evento));
                jsonObject.putPOJO("evento", evento);
                res = jsonObject.toString();
            }
        } else {
            res = json_utils.status_response(1, "Invalid evento ID");
        }

        ctx.json(res);
    };

    public static Handler getEventByName = ctx -> {
        String nombre = ctx.pathParam("nombre");
        String res = json_utils.status_response(1, "Invalid event name format");

        if (nombre != null && !nombre.isBlank()) {
            iEvento evento = eventoManager.searchByName(nombre);
            if (evento != null) {
                res = json_utils.Java_to_json_string(evento);
            } else {
                res = json_utils.status_response(1, "Could not find event in database");
            }
        }

        ctx.json(res);
    };


    public static Handler addEvent = ctx -> {
        String res = "";
        iEvento evento = json_utils.json_string_to_iEvento(ctx.body());
        
        if (evento == null){
            res = json_utils.status_response(1, "Request body does not hold Evento data");
        }
        else {
            if (evento.getNombre() == null || evento.getNombre().isBlank() || evento.getDate() == null) {
                res = json_utils.status_response(1, "Nombre y Fecha son obligatorios.");
            } else {
                if (eventoManager.registerEvento(evento)) {
                    res = json_utils.status_response(0, "Evento recibido correctamente.");
                } else {
                    res = json_utils.status_response(1, "Fallo al añadir evento (DB Error/Duplicado).");
                }
            }
        }
        
        ctx.json(res);
    };
}

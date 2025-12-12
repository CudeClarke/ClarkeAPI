package API;

import Datos.Entrada.EntradaCarrera;
import Datos.Entrada.EntradaConcierto;
import Datos.Entrada.EntradaRifa;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import io.javalin.http.Handler;
import java.util.List;

import DB.MySQLAccessFactory;
import Managers.EntradaManager;
import Datos.Entrada.iEntrada;
import utils.json_utils;

public class EntradaHandlers {

    private static EntradaManager manager = new EntradaManager(MySQLAccessFactory.getInstance());

    private static iEntrada entradaFromJson(JsonNode json){
        iEntrada entrada = null;
        ObjectMapper mapper = new ObjectMapper();
        try {
            entrada = mapper.treeToValue(json, EntradaCarrera.class);
        } catch (JsonProcessingException e) {
            System.out.println(e.getMessage());
            System.out.println("Not EntradaCarrera");
        }
        if (entrada == null){
            try {
                entrada = mapper.treeToValue(json, EntradaConcierto.class);
            } catch (JsonProcessingException e) {
                System.out.println(e.getMessage());
                System.out.println("Not EntradaConcierto");
            }
        }
        if (entrada == null){
            try {
                entrada = mapper.treeToValue(json, EntradaRifa.class);
            } catch (JsonProcessingException e) {
                System.out.println(e.getMessage());
                System.out.println("Not EntradaRifa");
            }
        }
        return entrada;
    }

    public static Handler getEntradasByEvento = ctx -> {
        String res = json_utils.status_response(1, "Tipo de ID incorrecto");

        try {
            String idParam = ctx.pathParam("id");
            if (idParam.matches("\\d+")) {
                int idEvento = Integer.parseInt(idParam);
                
                List<iEntrada> entradas = manager.getEntradasByEvento(idEvento);

                if (entradas != null && !entradas.isEmpty()) {
                    ObjectMapper mapper = new ObjectMapper();
                    ArrayNode jsonArray = mapper.createArrayNode();
                    for (iEntrada entrada : entradas) {
                        ObjectNode current_node = mapper.createObjectNode();
                        current_node.put("idEvento", idEvento);
                        current_node.putPOJO("entrada", entrada);
                        jsonArray.add(current_node);
                    }
                    res = jsonArray.toString();
                } else {
                    res = json_utils.status_response(1, "No hay entradas");
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            res = json_utils.status_response(1, "Server Error: " + e.getMessage());
        }

        ctx.json(res);
    };


    public static Handler addEntrada = ctx -> {
        String res = json_utils.status_response(1, "Error");

        ObjectMapper parser = new ObjectMapper();
        JsonNode req = parser.readTree(ctx.body());
        
        int id_evento = -1;
        iEntrada entrada = null;
        if (req.has("idEvento")) {id_evento = req.get("idEvento").asInt();}
        if (req.has("entrada")) {entrada = entradaFromJson(req.get("entrada"));}

        if (id_evento > 0 && entrada != null) {
            if (entrada.getNombre() == null || entrada.getNombre().isBlank()) {
                res = json_utils.status_response(1, "Se necesita nombre");
            } else {
                boolean exito = manager.addEntrada(entrada, id_evento);
                if (exito) {
                    res = json_utils.status_response(0, "Entrada creada correctamente");
                } else {
                    res = json_utils.status_response(1, "Ha ocurrido un error");
                }
            }
        } else {
            res = json_utils.status_response(1, "Cuerpo vacío");
        }
        ctx.json(res);
    };


    public static Handler deleteEntrada = ctx -> {
        String res = json_utils.status_response(1, "Formtao de ID incorrecto");

        try {
            String idParam = ctx.pathParam("id");
            // Validar que es un numero
            if (idParam.matches("\\d+")) {
                int idEntrada = Integer.parseInt(idParam);

                if (manager.deleteEntrada(idEntrada)) {
                    res = json_utils.status_response(0, "Entrada eliminada");
                } else {
                    res = json_utils.status_response(1, "No se pudo eliminar");
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            res = json_utils.status_response(1, "Server Error: " + e.getMessage());
        }

        ctx.json(res);
    };
}
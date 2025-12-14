package utils;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import Datos.Evento.*;
import Datos.Usuario.*;

public class json_utils {
    private final static String[] status= {"OK", "ERROR"};

    public static String status_response(int status_code, String message){
        return String.format("{\"Status\":\"%s\", \"Message\":\"%s\"}", status[status_code], message);
    }

    public static String Java_to_json_string(Object o){
        try {
            return new ObjectMapper().writeValueAsString(o);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }

    public static JsonNode Java_to_json_node(Object o){
        try {
            return new ObjectMapper().valueToTree(o);
        } catch (IllegalArgumentException e) {
            throw new RuntimeException(e);
        }
    }

    public static iEvento json_string_to_iEvento(String json){
        iEvento evento = null;
        ObjectMapper mapper = new ObjectMapper();
        try {
            evento = mapper.readValue(json, EventoCarrera.class);
        }catch (Exception e){
            System.out.println(e.getMessage());
            System.out.println("Not EventoCarrera");
        }
        if (evento == null){
            try {
                evento = mapper.readValue(json, EventoConcierto.class);
            }catch (Exception e){
                System.out.println(e.getMessage());
                System.out.println("Not EventoConcierto");
            }
        }
        if (evento == null){
            try {
                evento = mapper.readValue(json, EventoRifa.class);
            }catch (Exception e){
                System.out.println(e.getMessage());
                System.out.println("Not EventoRifa");
            }
        }
        if (evento == null){
            try {
                evento = mapper.readValue(json, Evento.class);
            }catch (Exception e){
                System.out.println(e.getMessage());
                System.out.println("Not Evento");
            }
        }
        return evento;
    }

    public static iUsuario json_string_to_iUsuario(String json){
        iUsuario usuario = null;
        ObjectMapper mapper = new ObjectMapper();
        try {
            usuario = mapper.readValue(json, UsuarioRegistrado.class);
        } catch (JsonProcessingException e){
            System.out.println("Not UsuarioRegistrado");
        }
        if (usuario == null){
            try {
                usuario = mapper.readValue(json, UsuarioBase.class);
            } catch (JsonProcessingException e) {
                System.out.println("Not UsuarioBase");
            }
        }
        return usuario;
    }

}

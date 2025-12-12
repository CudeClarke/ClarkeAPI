package utils;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

public class json_generator {
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
}

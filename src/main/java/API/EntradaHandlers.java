package API;

import io.javalin.http.Handler;
import java.util.List;
import DB.MySQLAccessFactory;
import Managers.EntradaManager;
import Datos.Entrada.iEntrada;
import utils.json_generator;

public class EntradaHandlers {

    private static final EntradaManager manager = new EntradaManager(MySQLAccessFactory.getInstance());

    // DTO
    // Está aquí porque el JSON recibe id_evento pero la clase Entrada
    // no tiene ese campo. Hay que modificar todos los tipos de entrada para solucionarlo
    public static class EntradaDTO {
        public int id_evento; 
        public int cantidad;    
        public float precio;
        public String nombre;
        public String descripcion;
        
        public EntradaDTO() {} 
    }


    public static Handler getEntradasByEvento = ctx -> {
        String res = json_generator.status_response(1, "Tipo de ID incorrecto");

        try {
            String idParam = ctx.pathParam("id");
            // Validación de que es un número
            if (idParam.matches("\\d+")) {
                int idEvento = Integer.parseInt(idParam);
                
                List<iEntrada> lista = manager.getEntradasByEvento(idEvento);

                if (lista != null && !lista.isEmpty()) {
                    res = json_generator.Java_to_json(lista);
                } else {
                    res = json_generator.status_response(1, "No hay entradas");
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            res = json_generator.status_response(1, "Server Error: " + e.getMessage());
        }

        ctx.json(res);
    };


    public static Handler addEntrada = ctx -> {
        String res = json_generator.status_response(1, "Error");

        try {
            EntradaDTO dto = ctx.bodyAsClass(EntradaDTO.class);

            if (dto != null) {
                if (dto.nombre == null || dto.nombre.isBlank()) {
                    res = json_generator.status_response(1, "Se necesita nombre");
                } else {
                    boolean exito = manager.addEntrada(
                        dto.id_evento, 
                        dto.cantidad, 
                        dto.precio, 
                        dto.nombre, 
                        dto.descripcion
                    );

                    if (exito) {
                        res = json_generator.status_response(0, "Entrada creada correctamente");
                    } else {
                        res = json_generator.status_response(1, "Ha ocurrido un error");
                    }
                }
            } else {
                res = json_generator.status_response(1, "Cuerpo vacío");
            }

        } catch (Exception e) {
            e.printStackTrace();
            res = json_generator.status_response(1, "Error parsing data: " + e.getMessage());
        }

        ctx.json(res);
    };


    public static Handler deleteEntrada = ctx -> {
        String res = json_generator.status_response(1, "Formtao de ID incorrecto");

        try {
            String idParam = ctx.pathParam("id");
            // Validar que es un numero
            if (idParam.matches("\\d+")) {
                int idEntrada = Integer.parseInt(idParam);

                if (manager.deleteEntrada(idEntrada)) {
                    res = json_generator.status_response(0, "Entrada eliminada");
                } else {
                    res = json_generator.status_response(1, "No se pudo eliminar");
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            res = json_generator.status_response(1, "Server Error: " + e.getMessage());
        }

        ctx.json(res);
    };
}
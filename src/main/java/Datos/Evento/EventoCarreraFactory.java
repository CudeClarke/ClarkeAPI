package Datos.Evento;

import java.sql.Date;

public class EventoCarreraFactory extends EventoFactory{
    @Override
    public iEvento createEvento(String nombre, String ubicacion, int objetivoRecaudacion, String descripcion, Date date, String url, String informacion) {
        // TODO: Convertir informacion_extra a los atributos necesarios de la clase concreta
        // El código se deja así ante el inminente refactor de las clases de datos
        return new EventoCarrera(nombre, ubicacion, objetivoRecaudacion, descripcion, date, url, 10);
    }
}

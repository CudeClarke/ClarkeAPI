package Datos.Evento;

import java.sql.Date;

public class EventoDefaultFactory extends EventoFactory{
    @Override
    public iEvento createEvento(String nombre, String ubicacion, int recaudacion, int objetivoRecaudacion, String descripcion, Date date, String url, String informacion) {
        return new Evento(nombre, ubicacion, recaudacion, objetivoRecaudacion, descripcion, date, url);
    }
}

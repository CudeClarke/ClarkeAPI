package Datos.Evento;

import java.sql.Date;

public class EventoCarreraFactory extends EventoFactory{
    @Override
    public iEvento createEvento(String nombre, String ubicacion, int recaudacion, int objetivoRecaudacion, String descripcion, Date date, String url, String informacion) {
        int recorrido;
        try {
            recorrido = Integer.parseInt(informacion);
        } catch (NumberFormatException e){
            recorrido = 0;
        }
        return new EventoCarrera(nombre, ubicacion, recaudacion, objetivoRecaudacion, descripcion, date, url, recorrido);
    }
}

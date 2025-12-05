package Datos.Evento;

import java.sql.Date;

public abstract class EventoFactory {
    public abstract iEvento createEvento(String nombre, String ubicacion, int recaudacion, int objetivoRecaudacion, String descripcion, Date date, String url, String informacion);
}

package Datos.Evento;

import java.sql.Date;

public class EventoConciertoFactory extends EventoFactory{
    @Override
    public iEvento createEvento(String nombre, String ubicacion, int objetivoRecaudacion, String descripcion, Date date, String url, String artista) {
        return new EventoConcierto(nombre, ubicacion, objetivoRecaudacion, descripcion, date, url, artista);
    }

    @Override
    public iEvento createEvento(String nombre, String ubicacion, int recaudacion, int objetivoRecaudacion, String descripcion, Date date, String url, String artista) {
        return new EventoConcierto(nombre, ubicacion, recaudacion, objetivoRecaudacion, descripcion, date, url, artista);
    }

    @Override
    public iEvento createEvento(String nombre, String ubicacion, int recaudacion, int objetivoRecaudacion, String descripcion, Date date, String url, int ID, String artista) {
        return new EventoConcierto(nombre, ubicacion, recaudacion, objetivoRecaudacion, descripcion, date, url, ID, artista);
    }
}

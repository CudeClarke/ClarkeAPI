package Datos.Evento;

import java.sql.Date;

public class EventoConcierto extends Evento {
    private String artista;

    public EventoConcierto(String nombre, String ubicacion, int recaudacion, int objetivoRecaudacion, String descripcion, Date date, String url, String artista) {
        super(nombre, ubicacion, recaudacion, objetivoRecaudacion, descripcion, date, url);
        this.artista = artista;
    }

    public String getArtista() {
        return artista;
    }

    public void setArtista(String artista) {
        this.artista = artista;
    }

    @Override
    public String getInformacion() { return artista; }

}

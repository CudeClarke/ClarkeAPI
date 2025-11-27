package Evento;

import java.util.Date;

public class EventoConcierto extends EventoAbstracto {
    private String artista;
    private String ubicacion;
    private Date fecha;

    public EventoConcierto(String nombre, int objetivoRecaudacion, int aforo, String artista, String ubicacion, Date fecha) {
        super(nombre, objetivoRecaudacion, aforo);
        this.artista = artista;
        this.ubicacion = ubicacion;
        this.fecha = fecha;
    }

    public String getArtista() {
        return artista;
    }

    public void setArtista(String artista) {
        this.artista = artista;
    }

    public String getUbicacion() {
        return ubicacion;
    }

    public void setUbicacion(String ubicacion) {
        this.ubicacion = ubicacion;
    }

    public Date getFecha() {
        return fecha;
    }

    public void setFecha(Date fecha) {
        this.fecha = fecha;
    }

}

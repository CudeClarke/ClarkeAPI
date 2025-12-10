package Datos.Evento;

import java.sql.Date;

public class EventoConcierto extends Evento {
    private String artista;

    public EventoConcierto(String nombre, String ubicacion, int objetivoRecaudacion, String descripcion, Date date, String url, String artista) {
        super(nombre, ubicacion, objetivoRecaudacion, descripcion, date, url);
        this.artista = artista;
    }

    public EventoConcierto(String nombre, String ubicacion, int recaudacion, int objetivoRecaudacion, String descripcion, Date date, String url, String artista) {
        super(nombre, ubicacion, recaudacion, objetivoRecaudacion, descripcion, date, url);
        this.artista = artista;
    }

    public EventoConcierto(String nombre, String ubicacion, int recaudacion, int objetivoRecaudacion, String descripcion, Date date, String url, int ID, String artista) {
        super(nombre, ubicacion, recaudacion, objetivoRecaudacion, descripcion, date, url, ID);
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

    public String toString() {
        return "EventoConcierto{" +
                "ID=" + getID() +
                ", nombre='" + getNombre() + '\'' +
                ", fecha=" + getDate() +
                ", ubicacion='" + getUbicacion() + '\'' +
                ", recaudado=" + getRecaudacion() +
                ", artista='" + artista + '\'' + //
                '}';
    }
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;
        EventoConcierto that = (EventoConcierto) o;
        return java.util.Objects.equals(artista, that.artista);
    }

    public int hashCode() {
        return java.util.Objects.hash(super.hashCode(), artista);
    }
}

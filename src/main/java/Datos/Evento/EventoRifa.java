package Datos.Evento;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class EventoRifa extends Evento {
    private List<Integer> premios;

    public EventoRifa(String nombre, String ubicacion, int objetivoRecaudacion, String descripcion, String date, String url) {
        super(nombre, ubicacion, objetivoRecaudacion, descripcion, date, url);
        premios = new ArrayList<>();
    }

    public EventoRifa(String nombre, String ubicacion, int recaudacion, int objetivoRecaudacion, String descripcion, String date, String url) {
        super(nombre, ubicacion, recaudacion, objetivoRecaudacion, descripcion, date, url);
        premios = new ArrayList<>();
    }

    public EventoRifa(String nombre, String ubicacion, int recaudacion, int objetivoRecaudacion, String descripcion, String date, String url,  int ID) {
        super(nombre, ubicacion, recaudacion, objetivoRecaudacion, descripcion, date, url, ID);
        premios = new ArrayList<>();
    }

    public List<Integer> getPremios() {
        return premios;
    }

    public void setPremios(List<Integer> premios) {
        this.premios = List.copyOf(premios);
    }

    public String getInformacion() { return premios.stream()
            .map(String::valueOf) // O .map(n -> String.valueOf(n))
            .collect(Collectors.joining(", "));}

    public String toString() {
        return "EventoRifa{" +
                "ID=" + getID() +
                ", nombre='" + getNombre() + '\'' +
                ", fecha=" + getDate() +
                ", ubicacion='" + getUbicacion() + '\'' +
                ", recaudado=" + getRecaudacion() +
                ", premios=" + premios +
                '}';
    }

    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;
        EventoRifa that = (EventoRifa) o;
        return java.util.Objects.equals(premios, that.premios);
    }

    public int hashCode() {
        return java.util.Objects.hash(super.hashCode(), premios);
    }
}

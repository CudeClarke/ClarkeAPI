package Datos.Evento;

import java.sql.Date;

public class EventoCarrera extends Evento {
    private int recorrido;

    public EventoCarrera(String nombre, String ubicacion, int recaudacion, int objetivoRecaudacion, String descripcion, Date date, String url, int recorrido) {
        super(nombre, ubicacion, recaudacion, objetivoRecaudacion, descripcion, date, url);
        this.recorrido = recorrido;
    }

    public int getRecorrido() {
        return recorrido;
    }

    public void setRecorrido(int recorrido) {
        this.recorrido = recorrido;
    }

    @Override
    public String getInformacion() { return String.valueOf(recorrido);}

    public String toString() {
        return "EventoCarrera{" +
                super.toString() +
                ", recorrido=" + recorrido + "km" +
                '}';
    }

    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;
        EventoCarrera that = (EventoCarrera) o;
        return recorrido == that.recorrido;
    }

    public int hashCode() {
        return java.util.Objects.hash(super.hashCode(), recorrido);
    }
}

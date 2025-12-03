package Datos.Evento;

import java.sql.Date;

public class EventoCarrera extends Evento {
    private int recorrido;

    public EventoCarrera(String nombre, String ubicacion, int objetivoRecaudacion, String descripcion, Date date, String url, int recorrido) {
        super(nombre, ubicacion, objetivoRecaudacion, descripcion, date, url);
        this.recorrido = recorrido;
    }

    public int getRecorrido() {
        return recorrido;
    }

    public void setRecorrido(int recorrido) {
        this.recorrido = recorrido;
    }
}

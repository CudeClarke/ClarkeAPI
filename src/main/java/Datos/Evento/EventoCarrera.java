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

}

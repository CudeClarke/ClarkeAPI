package Evento;

public class EventoCarrera extends EventoAbstracto {
    private String ubicacion;
    private int recorrido;

    public EventoCarrera(String nombre, int objetivoRecaudacion, int aforo) {
        super(nombre, objetivoRecaudacion, aforo);
        this.ubicacion = ubicacion;
        this.recorrido = recorrido;
    }

    public String getUbicacion() {
        return ubicacion;
    }

    public void setUbicacion(String ubicacion) {
        this.ubicacion = ubicacion;
    }

    public int getRecorrido() {
        return recorrido;
    }

    public void setRecorrido(int recorrido) {
        this.recorrido = recorrido;
    }
}

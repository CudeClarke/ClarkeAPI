package Datos.Evento;

public class EventoCarrera extends Evento {
    private String ubicacion;
    private int recorrido;

    public EventoCarrera(String nombre, int objetivoRecaudacion, int aforo, String ubicacion, int recorrido) {
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

    @Override
    public String toString(){
        return "Carrera [Ubicacion=" + ubicacion + ", recorrido=" + recorrido + "]";
    }
}

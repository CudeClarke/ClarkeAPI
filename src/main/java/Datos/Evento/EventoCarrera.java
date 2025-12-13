package Datos.Evento;


public class EventoCarrera extends Evento {
    private int recorrido;

    // Empty builder, should not be used. Used for json deserialization purposes exclusively by Jackson
    public EventoCarrera(){}

    public EventoCarrera(String nombre, String ubicacion, float objetivoRecaudacion, String descripcion, String date, String url, int recorrido) {
        super(nombre, ubicacion, objetivoRecaudacion, descripcion, date, url);
        this.recorrido = recorrido;
    }

    public EventoCarrera(String nombre, String ubicacion, float recaudacion, float objetivoRecaudacion, String descripcion, String date, String url, int recorrido) {
        super(nombre, ubicacion, recaudacion, objetivoRecaudacion, descripcion, date, url);
        this.recorrido = recorrido;
    }

    public EventoCarrera(String nombre, String ubicacion, float recaudacion, float objetivoRecaudacion, String descripcion, String date, String url, int ID, int recorrido) {
        super(nombre, ubicacion, recaudacion, objetivoRecaudacion, descripcion, date, url, ID);
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
                "ID=" + getID() +
                ", nombre='" + getNombre() + '\'' +
                ", fecha=" + getDate() +
                ", ubicacion='" + getUbicacion() + '\'' +
                ", recaudado=" + getRecaudacion() +
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

package Evento;

public class EntradaConcierto extends EntradaAbstracta {
    private int seccion;

    public EntradaConcierto(int subAforo, float precio,  int seccion) {
        super(subAforo, precio);
        this.seccion = seccion;
    }

    public int getSeccion() {
        return seccion;
    }

    public void setSeccion(int seccion) {
        this.seccion = seccion;
    }

    public String toString(){
        return "EntradaConcierto [seccion=" + seccion + "precio=" + super.getPrecio() + "]";
    }
}

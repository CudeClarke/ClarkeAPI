package Datos.Entrada;

public class EntradaConcierto extends Entrada {

    public EntradaConcierto(int subAforo, float precio, String nombre, String descripcion,  int seccion) {
        super(subAforo, precio, nombre, descripcion);
    }

    public String toString(){
        return "EntradaConcierto [seccion=" + super.getPrecio() + "]";
    }
}

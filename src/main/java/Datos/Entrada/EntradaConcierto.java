package Datos.Entrada;

public class EntradaConcierto extends Entrada {

    // Empty builder, should not be used. Used for json deserialization purposes exclusively by Jackson
    public EntradaConcierto(){}

    public EntradaConcierto(int subAforo, float precio, String nombre, String descripcion) {
        super(subAforo, precio, nombre, descripcion);
    }

    public String toString(){
        return super.toString();
    }
}

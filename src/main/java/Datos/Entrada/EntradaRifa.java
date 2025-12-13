package Datos.Entrada;

public class EntradaRifa extends Entrada {

    // Empty builder, should not be used. Used for json deserialization purposes exclusively by Jackson
    public EntradaRifa(){}

    public EntradaRifa(int subAforo, float precio, String nombre, String descripcion) {
        super(subAforo, precio, nombre, descripcion);
    }

    public EntradaRifa(int id, int subAforo, float precio, String nombre, String descripcion) {
        super(id, subAforo, precio, nombre, descripcion);
    }

    public String toString(){
        return super.toString();
    }
}

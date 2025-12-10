package Datos.Entrada;

public class EntradaCarrera extends Entrada {

    // Empty builder, should not be used. Used for json deserialization purposes exclusively by Jackson
    public EntradaCarrera(){}

    public EntradaCarrera(int subAforo, float precio, String nombre, String descripcion) {
        super(subAforo, precio, nombre, descripcion);
    }

    public EntradaCarrera(int id, int subAforo, float precio, String nombre, String descripcion){
        super(id, subAforo, precio, nombre, descripcion);
    }

    public String toString(){
        return super.toString();
    }
}

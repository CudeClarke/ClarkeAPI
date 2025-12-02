package Evento;

import java.util.List;
import java.util.Objects;

public class Tag {
    private int id; // Identificador unico para la base de datos
    private String nombre;

    public Tag(String nombre) {
        this.nombre = nombre;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        Tag tag = (Tag) o;
        return Objects.equals(nombre, tag.nombre);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(nombre);
    }

    @Override
    public String toString() {
        return this.nombre;
    }
}

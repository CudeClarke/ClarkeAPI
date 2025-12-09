package Datos.Evento;

import java.util.Objects;

public class Patrocinador {
    private String nombre;
    private String logo;

    public Patrocinador(String nombre, String logo) {
        this.nombre = nombre;
        this.logo = logo;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getLogo() {
        return logo;
    }

    public void setLogo(String logo) {
        this.logo = logo;
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        Patrocinador that = (Patrocinador) o;
        return Objects.equals(nombre, that.nombre) && Objects.equals(logo, that.logo);
    }

    @Override
    public int hashCode() {
        return Objects.hash(nombre, logo);
    }

    @Override
    public String toString() {
        return "Patrocinador [nombre=" + nombre + ", logo=" + logo + "]";
    }
}

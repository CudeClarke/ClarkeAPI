package Evento;

import java.util.Objects;

public class Patrocinador {
    private String nombre;
    private String logo;
    private String web;

    public Patrocinador(String nombre, String logo, String web) {
        this.nombre = nombre;
        this.logo = logo;
        this.web = web;
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

    public String getWeb() {
        return web;
    }

    public void setWeb(String web) {
        this.web = web;
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        Patrocinador that = (Patrocinador) o;
        return Objects.equals(nombre, that.nombre) && Objects.equals(logo, that.logo) && Objects.equals(web, that.web);
    }

    @Override
    public int hashCode() {
        return Objects.hash(nombre, logo, web);
    }

    @Override
    public String toString() {
        return "Patrocinador [nombre=" + nombre + ", logo=" + logo + ", web=" + web + "]";
    }
}

package Datos.Evento;

import Datos.Entrada.Entrada;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class Evento implements iEvento {
    private String nombre;
    private String ubicacion;
    private int recaudacion;
    private int objetivoRecaudacion;

    private Set<String> tags;
    private Set<Patrocinador> patrocinadores;
    private List<Entrada> entradas;

    public Evento(String nombre, String ubicacion, int objetivoRecaudacion) {
        this.nombre = nombre;
        this.ubicacion = ubicacion;
        this.objetivoRecaudacion = objetivoRecaudacion;
        recaudacion = 0;

        tags = new HashSet<>();
        patrocinadores = new HashSet<>();
        entradas = new ArrayList<>();
    }

    /**
     * Metodo para agregar un tag a un evento
     * @param tag no nulo a agregar a la lista
     */
    public void addTag(String tag) {
        if (tag != null) {
            tags.add(tag);
        }
    }

    /**
     * Metodo para borrar un tag de un evento
     * @param tag a eliminar de la lista
     */
    public void removeTag(String tag) {
        tags.remove(tag);
    }

    /**
     * Metodo para agregar un patrocinador a un evento
     * @param p patrocinador no nulo a agregar
     */
    public void addPatrocinador(Patrocinador p) {
        if (p != null) {
            patrocinadores.add(p);
        }
    }

    /**
     * Metodo para eliminar un patrociandor de un evento
     * @param p patrocinador a eliminar de la lista
     */
    public void removePatrocinador(Patrocinador p) {
        patrocinadores.remove(p);
    }

    public void addEntrada(Entrada e) {
        if (e != null) {
            entradas.add(e);
        }
    }

    public void deleteEntrada(Entrada e) {
        entradas.remove(e);
    }

    public List<Entrada> getEntradas() {
        return entradas;
    }

    public String getUbicacion() {
        return ubicacion;
    }

    public void setUbicacion(String ubicacion) {
        this.ubicacion = ubicacion;
    }

    public Set<String> getTags() {
        return tags;
    }

    public Set<Patrocinador> getPatrocinadores() {
        return patrocinadores;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public int getRecaudacion() {
        return recaudacion;
    }

    public void setRecaudacion(int recaudacion) {
        this.recaudacion = recaudacion;
    }

    public int getObjetivoRecaudacion() {
        return objetivoRecaudacion;
    }

    public void setObjetivoRecaudacion(int objetivoRecaudacion) {
        this.objetivoRecaudacion = objetivoRecaudacion;
    }

    @Override
    public String toString(){
        return "Evento Abstracto [NOMBRE=" + nombre + "RECAUDACION=" + recaudacion + "OBJRECAUDACION" + objetivoRecaudacion + "]";
    }
}

package Datos.Evento;

import java.util.HashSet;
import java.util.Set;

public class Evento implements iEvento {
    private String nombre;
    private int recaudacion;
    private int objetivoRecaudacion;
    private int aforo;

    private Set<String> tags;
    private Set<Patrocinador> patrocinadores;

    public Evento(String nombre, int objetivoRecaudacion, int aforo) {
        this.nombre = nombre;
        this.objetivoRecaudacion = objetivoRecaudacion;
        this.aforo = aforo;
        recaudacion = 0;

        tags = new HashSet<>();
        patrocinadores = new HashSet<>();
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

    public int getAforo() {
        return aforo;
    }

    public void setAforo(int aforo) {
        this.aforo = aforo;
    }

    @Override
    public String toString(){
        return "Evento Abstracto [NOMBRE=" + nombre + "RECAUDACION=" + recaudacion + "OBJRECAUDACION" + objetivoRecaudacion +"AFORO=" + aforo + "]";
    }
}

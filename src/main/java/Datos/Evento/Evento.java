package Datos.Evento;

import Datos.Entrada.iEntrada;

import java.util.*;

public class Evento implements iEvento {
    private String nombre;
    private String ubicacion;
    private float recaudacion;
    private float objetivoRecaudacion;
    private String descripcion;
    private String date;
    private String url;
    private int ID;

    private Set<String> tags;
    private Set<Patrocinador> patrocinadores;
    private List<iEntrada> entradas;

    // Empty builder, should not be used. Used for json deserialization purposes exclusively by Jackson
    public Evento(){}

    public Evento(String nombre, String ubicacion, float objetivoRecaudacion, String descripcion, String date, String url) {
        this.nombre = nombre;
        this.ubicacion = ubicacion;
        this.objetivoRecaudacion = objetivoRecaudacion;
        this.descripcion = descripcion;
        recaudacion = 0;
        this.date = date;
        this.url = url;
        this.ID = -1;

        tags = new HashSet<>();
        patrocinadores = new HashSet<>();
        entradas = new ArrayList<>();
    }

    public Evento(String nombre, String ubicacion, float recaudacion, float objetivoRecaudacion, String descripcion, String date, String url) {
        this.nombre = nombre;
        this.ubicacion = ubicacion;
        this.objetivoRecaudacion = objetivoRecaudacion;
        this.descripcion = descripcion;
        this.recaudacion = recaudacion;
        this.date = date;
        this.url = url;
        this.ID = -1;

        tags = new HashSet<>();
        patrocinadores = new HashSet<>();
        entradas = new ArrayList<>();
    }

    public Evento(String nombre, String ubicacion, float recaudacion, float objetivoRecaudacion, String descripcion, String date, String url, int ID) {
        this.nombre = nombre;
        this.ubicacion = ubicacion;
        this.objetivoRecaudacion = objetivoRecaudacion;
        this.descripcion = descripcion;
        this.recaudacion = recaudacion;
        this.date = date;
        this.url = url;
        this.ID = ID;

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

    public void addEntrada(iEntrada e) {
        if (e != null) {
            entradas.add(e);
        }
    }

    public void deleteEntrada(iEntrada e) {
        entradas.remove(e);
    }

    public List<iEntrada> getEntradas() {
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

    public void setTags(Set<String> tags) {
        this.tags = tags;
    }

    public Set<Patrocinador> getPatrocinadores() {
        return patrocinadores;
    }

    public void setPatrocinadores(Set<Patrocinador> patrocinadores) {
        this.patrocinadores = patrocinadores;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public float getRecaudacion() {
        return recaudacion;
    }

    public void setRecaudacion(float recaudacion) {
        this.recaudacion = recaudacion;
    }

    public float getObjetivoRecaudacion() {
        return objetivoRecaudacion;
    }

    public void setObjetivoRecaudacion(float objetivoRecaudacion) {
        this.objetivoRecaudacion = objetivoRecaudacion;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getInformacion() { return ""; }

    public int getID() {
        return ID;
    }

    public void setID(int ID) {
        this.ID = ID;
    }

    public String toString() {
        return "Evento{" +
                "ID=" + ID +
                ", nombre='" + nombre + '\'' +
                ", fecha=" + date +
                ", ubicacion='" + ubicacion + '\'' +
                ", recaudado=" + recaudacion +
                ", tags=" + (tags != null ? tags.size() : 0) +
                '}';
    }

    public boolean equals(Object o) {
        if (this == o) return true;
        // Used getClass() as Concierto is not the same as Carrera
        if (o == null || getClass() != o.getClass()) return false;
        Evento evento = (Evento) o;
        return java.util.Objects.equals(nombre, evento.nombre) &&
                java.util.Objects.equals(date, evento.date) &&
                java.util.Objects.equals(ubicacion, evento.ubicacion) &&
                java.util.Objects.equals(url, evento.url);
    }

    public int hashCode() {
        return java.util.Objects.hash(nombre, ubicacion, date, url);
    }
}

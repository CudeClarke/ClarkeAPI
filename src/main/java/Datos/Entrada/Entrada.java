package Datos.Entrada;

import Datos.Ticket.iTicket;

import java.util.ArrayList;
import java.util.List;

public class Entrada implements iEntrada {
    private int subAforo;
    private float precio;
    private List<iTicket> tickets;
    private String nombre;
    private String descripcion;
    private int id;

    // Empty builder, should not be used. Used for json deserialization purposes exclusively by Jackson
    public Entrada(){}

    public Entrada(int subAforo, float precio, String nombre, String descripcion) {
        this.subAforo = subAforo;
        this.precio = precio;
        this.nombre = nombre;
        this.descripcion = descripcion;
        this.id = -1; // Inalid id

        this.tickets = new ArrayList<iTicket>();
    }

    public Entrada(int id, int subAforo, float precio, String nombre, String descripcion) {
        this.subAforo = subAforo;
        this.precio = precio;
        this.nombre = nombre;
        this.descripcion = descripcion;
        this.id = id;

        this.tickets = new ArrayList<iTicket>();
    }

    public List<iTicket> getTickets() {
        return tickets;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public void addTicket(iTicket ticket) {
        tickets.add(ticket);
    }

    public void removeTicket(iTicket ticket) {
        tickets.remove(ticket);
    }

    public int getSubAforo() {
        return subAforo;
    }

    public void setSubAforo(int subAforo) {
        this.subAforo = subAforo;
    }

    public float getPrecio() {
        return precio;
    }

    public void setPrecio(float precio) {
        this.precio = precio;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String toString() {
        return "Entrada{" +
                "id='" + id + '\'' +
                ", nombre='" + nombre + '\'' +
                ", descripcion='" + descripcion + '\'' +
                ", precio=" + precio +
                ", subAforo=" + subAforo +
                ", ticketsVendidos=" + (tickets != null ? tickets.size() : 0) +
                '}';
    }

    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Entrada entrada = (Entrada) o;
        return subAforo == entrada.subAforo &&
                Float.compare(entrada.precio, precio) == 0 &&
                java.util.Objects.equals(nombre, entrada.nombre) &&
                java.util.Objects.equals(descripcion, entrada.descripcion);
    }

    public int hashCode() {
        return java.util.Objects.hash(subAforo, precio, nombre, descripcion);
    }
}

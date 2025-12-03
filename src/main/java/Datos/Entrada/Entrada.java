package Datos.Entrada;

import Datos.Ticket.iTicket;

import java.util.ArrayList;
import java.util.List;

public class Entrada implements iEntrada {
    private int subAforo;
    private float precio;
    private List<iTicket> tickets;
    private String informacion;

    public Entrada(int subAforo, float precio, String informacion) {
        this.subAforo = subAforo;
        this.precio = precio;
        this.informacion = informacion;

        this.tickets = new ArrayList<iTicket>();
    }

    public List<iTicket> getTickets() {
        return tickets;
    }

    public String getInformacion() {
        return informacion;
    }

    public void setInformacion(String informacion) {
        this.informacion = informacion;
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

    public String toString(){
        return "Datos.Entrada Abstracta [subAforo=" + subAforo + ", precio=" + precio + "]";
    }
}

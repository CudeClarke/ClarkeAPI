package Datos.Entrada;

import Datos.Ticket.iTicket;

import java.util.ArrayList;
import java.util.List;

public class Entrada implements iEntrada {
    private int subAforo;
    private float precio;
    private List<iTicket> tickets;

    public Entrada(int subAforo, float precio) {
        this.subAforo = subAforo;
        this.precio = precio;

        this.tickets = new ArrayList<iTicket>();
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

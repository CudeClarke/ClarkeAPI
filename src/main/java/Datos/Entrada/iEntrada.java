package Datos.Entrada;

import Ticket.iTicket;

public interface iEntrada {
    public int getSubAforo();
    public void setSubAforo(int subAforo);
    public float getPrecio();
    public void setPrecio(float precio);

    public void addTicket(iTicket ticket);
    public void removeTicket(iTicket ticket);
}

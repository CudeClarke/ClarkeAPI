package Datos.Entrada;

import Datos.Ticket.iTicket;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;

import java.util.List;

@JsonDeserialize(as = Entrada.class)
public interface iEntrada {
    public int getSubAforo();
    public void setSubAforo(int subAforo);
    public float getPrecio();
    public void setPrecio(float precio);
    public List<iTicket> getTickets();
    public void setNombre(String nombre);
    public String getNombre();
    public String getDescripcion();
    public void setDescripcion(String descripcion);
    public int getId();
    public void setId(int id);

    public void addTicket(iTicket ticket);
    public void removeTicket(iTicket ticket);
}

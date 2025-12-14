package Managers;

import java.util.*;

import Datos.Usuario.iUsuario;
import Datos.Evento.iEvento;
import Datos.Entrada.iEntrada;
import utils.IDGenerator;

public class Transaction {
    int ID;
    iUsuario comprador;
    List<iEvento> eventos;
    Set<Integer> ticket_ids;

    public Transaction(iUsuario comprador){
        ID = IDGenerator.getCounter();
        this.comprador = comprador;
        eventos = new ArrayList<>();
        ticket_ids = new HashSet<>();
    }

    public void addEvento(iEvento evento){
        eventos.add(evento);
    }

    public iEvento eventoIncluded(int idEvento){
        iEvento res = null;
        int i = 0;
        boolean included = false;
        while (!included && i<eventos.size()){
            if (eventos.get(i).getID() == idEvento){
                res = eventos.get(i);
                included = true;
            }
            i++;
        }
        return res;
    }

    public iEntrada entradaIncluded(iEvento evento, int idEntrada){
        iEntrada res = null;
        int i = 0;
        boolean included = false;
        List<iEntrada> entradas = evento.getEntradas();
        while (!included && i < entradas.size()){
            if (entradas.get(i).getId() == idEntrada){
                res = entradas.get(i);
                included = true;
            }
            i++;
        }
        return  res;
    }

    public Set<Integer> getTicket_ids(){
        return ticket_ids;
    }

    public void addTicketIDs(int id){
        ticket_ids.add(id);
    }

    public int getID() {
        return ID;
    }

    public iUsuario getComprador() {
        return comprador;
    }

    public List<iEvento> getEventos() {
        return eventos;
    }

    @Override
    public int hashCode() {
        return Objects.hash(comprador, eventos);
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        Transaction that = (Transaction) o;
        return Objects.equals(comprador, that.comprador) && Objects.equals(eventos, that.eventos);
    }

    @Override
    public String toString() {
        return "Transaction{" +
                "ID=" + ID +
                ", comprador=" + comprador +
                ", eventos=" + eventos +
                '}';
    }
}

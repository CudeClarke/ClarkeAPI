package Datos.Ticket;

import Datos.Usuario.iUsuario;

public class TicketRifaFactory extends TicketFactory{
    @Override
    public iTicket createTicket(iUsuario usuario, String dniAsistente, String idBoleto) {
        return new TicketRifa(usuario, dniAsistente, Integer.parseInt(idBoleto));
    }

    @Override
    public iTicket createTicket(iUsuario usuario, String dniAsistente, float pagoExtra, String idBoleto) {
        return new TicketRifa(usuario, dniAsistente, pagoExtra, Integer.parseInt(idBoleto));
    }

    @Override
    public iTicket createTicket(iUsuario usuario, String dniAsistente, float pagoExtra, String idBoleto, int id) {
        return new TicketRifa(usuario, dniAsistente, pagoExtra, Integer.parseInt(idBoleto), id);
    }
}

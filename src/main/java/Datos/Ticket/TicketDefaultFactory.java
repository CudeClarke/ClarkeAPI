package Datos.Ticket;

import Datos.Usuario.iUsuario;

public class TicketDefaultFactory extends TicketFactory{
    @Override
    public iTicket createTicket(iUsuario usuario, String dniAsistente, String info) {
        return new Ticket(usuario, dniAsistente);
    }

    @Override
    public iTicket createTicket(iUsuario usuario, String dniAsistente, float pagoExtra, String info) {
        return new Ticket(usuario, dniAsistente, pagoExtra);
    }

    @Override
    public iTicket createTicket(iUsuario usuario, String dniAsistente, float pagoExtra, String info, int id) {
        return new Ticket(usuario, dniAsistente, pagoExtra, id);
    }

    @Override
    public iTicket createTicket(iUsuario usuario, String dniAsistente, String info, int id) {
        return new Ticket(usuario, dniAsistente, id);
    }
}

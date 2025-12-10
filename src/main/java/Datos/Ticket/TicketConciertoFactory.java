package Datos.Ticket;

import Datos.Usuario.iUsuario;

public class TicketConciertoFactory extends TicketFactory{
    @Override
    public iTicket createTicket(iUsuario usuario, String dniAsistente, String asiento) {
        return new TicketConcierto(usuario, dniAsistente, asiento);
    }

    @Override
    public iTicket createTicket(iUsuario usuario, String dniAsistente, float pagoExtra, String asiento) {
        return new TicketConcierto(usuario, dniAsistente, pagoExtra, asiento);
    }

    public iTicket createTicket(iUsuario usuario, String dniAsistente, float pagoExtra, String asiento, int id) {
        return new TicketConcierto(usuario, dniAsistente, pagoExtra, asiento, id);
    }

    public iTicket createTicket(iUsuario usuario, String dniAsistente, String asiento, int id) {
        return new TicketConcierto(usuario, dniAsistente, asiento, id);
    }
}

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
}

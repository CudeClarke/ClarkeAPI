package Datos.Ticket;

import Datos.Usuario.iUsuario;

public class TicketCarreraFactory extends TicketFactory{
    @Override
    public iTicket createTicket(iUsuario usuario, String dniAsistente, String dorsal) {
        return new TicketCarrera(usuario, dniAsistente, Integer.parseInt(dorsal));
    }

    @Override
    public iTicket createTicket(iUsuario usuario, String dniAsistente, float pagoExtra, String dorsal) {
        return new TicketCarrera(usuario, dniAsistente, pagoExtra, Integer.parseInt(dorsal));
    }

    @Override
    public iTicket createTicket(iUsuario usuario, String dniAsistente, float pagoExtra, String dorsal, int id) {
        return new TicketCarrera(usuario, dniAsistente, pagoExtra, Integer.parseInt(dorsal),id);
    }

    @Override
    public iTicket createTicket(iUsuario usuario, String dniAsistente, String dorsal, int id) {
        return new TicketCarrera(usuario, dniAsistente, Integer.parseInt(dorsal),id);
    }
}

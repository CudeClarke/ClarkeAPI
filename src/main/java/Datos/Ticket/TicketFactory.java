package Datos.Ticket;

import Datos.Usuario.iUsuario;

public abstract class TicketFactory {
    public abstract iTicket createTicket(iUsuario usuario, String dniAsistente, String info);
    public abstract iTicket createTicket(iUsuario usuario, String dniAsistente, float pagoExtra, String info);
    public abstract iTicket createTicket(iUsuario usuario, String dniAsistente, float pagoExtra, String info,int id);
}

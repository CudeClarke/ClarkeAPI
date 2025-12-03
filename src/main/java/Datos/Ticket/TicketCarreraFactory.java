package Datos.Ticket;

import Datos.Usuario.iUsuario;

public class TicketCarreraFactory extends TicketFactory{
    @Override
    public iTicket createTicket(iUsuario usuario, String dniAsistente, int id, int dorsal) {
        return new TicketCarrera(usuario, dniAsistente, id, dorsal);
    }
}

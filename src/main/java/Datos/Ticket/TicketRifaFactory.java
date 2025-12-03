package Datos.Ticket;

import Datos.Usuario.iUsuario;

public class TicketRifaFactory extends TicketFactory{
    @Override
    public iTicket createTicket(iUsuario usuario, String dniAsistente, int id, int idBoleto) {
        return new TicketRifa(usuario, dniAsistente, id, idBoleto);
    }
}

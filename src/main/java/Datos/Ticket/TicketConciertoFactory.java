package Datos.Ticket;

import Datos.Usuario.iUsuario;

public class TicketConciertoFactory extends TicketFactory{
    @Override
    public iTicket createTicket(iUsuario usuario, String dniBeneficiario, int id, int asiento) {
        return new TicketConcierto(usuario, dniBeneficiario, id, asiento);
    }
}

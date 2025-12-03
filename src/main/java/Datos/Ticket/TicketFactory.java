package Datos.Ticket;

import Datos.Usuario.iUsuario;

public abstract class TicketFactory {
    public abstract iTicket createTicket(iUsuario usuario, String dniBeneficiario, int id, int info);
}

package Datos.Ticket;

import Datos.Usuario.iUsuario;

public class TicketCarreraFactory extends TicketFactory{
    @Override
    public iTicket createTicket(iUsuario nombre, String dniBeneficiario, int id, int dorsal) {
        return new TicketCarrera(nombre, dniBeneficiario, id, dorsal);
    }
}

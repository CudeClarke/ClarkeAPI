package Ticket;

public class TicketCarreraFactory extends TicketFactory{
    @Override
    public iTicket createTicket(String nombre, String dniBeneficiario, int id, int dorsal) {
        return new TicketCarrera(nombre, dniBeneficiario, id, dorsal);
    }
}

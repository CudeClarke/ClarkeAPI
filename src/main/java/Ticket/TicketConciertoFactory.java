package Ticket;

public class TicketConciertoFactory extends TicketFactory{
    @Override
    public iTicket createTicket(String nombre, String dniBeneficiario, int id, int asiento) {
        return new TicketConcierto(nombre, dniBeneficiario, id, asiento);
    }
}

package Ticket;

public class TicketRifaFactory extends TicketFactory{
    @Override
    public iTicket createTicket(String nombre, String dniBeneficiario, int id, int idBoleto) {
        return new TicketRifa(nombre, dniBeneficiario, id, idBoleto);
    }
}

package Datos.Ticket;

public abstract class TicketFactory {
    public abstract iTicket createTicket(String nombre, String dniBeneficiario, int id, int info);
}

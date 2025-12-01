package Ticket;

public class TicketRifa extends Ticket {
    private int idBoleto;

    public TicketRifa(String nombre, String dniBeneficiario, int id, int idBoleto) {
        super(nombre, dniBeneficiario, id);
        this.idBoleto = idBoleto;
    }

    public int getIdBoleto() {
        return idBoleto;
    }

    public void setIdBoleto(int idBoleto) {
        this.idBoleto = idBoleto;
    }

    public String toString(){
        return "Rifa [Nombre=" + getNombre() + ", DNI=" + getDniBeneficiario() + ", ID=" + getId() + "ID boleto=" + idBoleto + "]";
    }
}

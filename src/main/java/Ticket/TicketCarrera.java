package Ticket;

public class TicketCarrera extends Ticket {
    private int dorsal;

    public TicketCarrera(String nombre, String dniBeneficiario, int id, int dorsal) {
        super(nombre, dniBeneficiario, id);
        this.dorsal = dorsal;
    }

    public int getDorsal() {
        return dorsal;
    }

    public void setDorsal(int dorsal) {
        this.dorsal = dorsal;
    }

    public String toString(){
        return "Carrera [Nombre=" + super.getNombre() + ", DNI=" + super.getDniBeneficiario() + ", ID=" + super.getId() + "Dorsal=" + dorsal + "]";
    }
}

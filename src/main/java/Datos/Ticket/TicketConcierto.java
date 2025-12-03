package Datos.Ticket;

public class TicketConcierto extends Ticket{
    private int asiento;

    public TicketConcierto(String nombre, String dniBeneficiario, int id, int asiento) {
        super(nombre, dniBeneficiario, id);
        this.asiento = asiento;
    }

    public int getAsiento() {
        return asiento;
    }

    public void setDorsal(int asiento) {
        this.asiento = asiento;
    }

    public String toString(){
        return "Concierto [Nombre=" + super.getNombre() + ", DNI=" + super.getDniAsistente() + ", ID=" + super.getId() + "Asiento=" + asiento + "]";
    }
}

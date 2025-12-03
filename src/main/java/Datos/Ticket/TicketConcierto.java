package Datos.Ticket;

import Datos.Usuario.iUsuario;

public class TicketConcierto extends Ticket{
    private int asiento;

    public TicketConcierto(iUsuario usuario, String dniAsistente, int id, int asiento) {
        super(usuario, dniAsistente, id);
        this.asiento = asiento;
    }

    public int getAsiento() {
        return asiento;
    }

    public void setDorsal(int asiento) {
        this.asiento = asiento;
    }

    public String toString(){
        return "Concierto [Usuario=" + super.getUsuario().toString() + ", DNI=" + super.getDniAsistente() + ", ID=" + super.getId() + "Asiento=" + asiento + "]";
    }
}

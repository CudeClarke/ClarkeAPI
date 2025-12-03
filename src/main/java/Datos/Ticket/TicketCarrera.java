package Datos.Ticket;

import Datos.Usuario.iUsuario;

public class TicketCarrera extends Ticket {
    private int dorsal;

    public TicketCarrera(iUsuario usuario, String dniAsistente, int id, int dorsal) {
        super(usuario, dniAsistente, id);
        this.dorsal = dorsal;
    }

    public int getDorsal() {
        return dorsal;
    }

    public void setDorsal(int dorsal) {
        this.dorsal = dorsal;
    }

    public String toString(){
        return "Carrera [Usuario=" + super.getUsuario().toString() + ", DNI=" + super.getDniAsistente() + ", ID=" + super.getId() + "Dorsal=" + dorsal + "]";
    }
}

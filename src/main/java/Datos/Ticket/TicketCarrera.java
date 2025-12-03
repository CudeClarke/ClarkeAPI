package Datos.Ticket;

import Datos.Usuario.iUsuario;

public class TicketCarrera extends Ticket {
    private int dorsal;

    public TicketCarrera(iUsuario usuario, String dniAsistente, int dorsal) {
        super(usuario, dniAsistente);
        this.dorsal = dorsal;
    }

    public TicketCarrera(iUsuario usuario, String dniAsistente, float pagoExtra, int dorsal) {
        super(usuario, dniAsistente, pagoExtra);
        this.dorsal = dorsal;
    }

    public int getDorsal() {
        return dorsal;
    }

    public void setDorsal(int dorsal) {
        this.dorsal = dorsal;
    }

    public String toString(){
        return "Carrera [Usuario=" + super.getUsuario().toString() + ", DNI=" + super.getDniAsistente() + "Dorsal=" + dorsal + "]";
    }
}

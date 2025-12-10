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

    public TicketCarrera(iUsuario usuario, String dniAsistente, float pagoExtra, int dorsal, int id) {
        super(usuario, dniAsistente, pagoExtra, id);
        this.dorsal = dorsal;
    }

    public int getDorsal() {
        return dorsal;
    }

    public void setDorsal(int dorsal) {
        this.dorsal = dorsal;
    }

    public String toString() {
        String dniUser = (getUsuario() != null) ? getUsuario().getDni() : "null";
        return "TicketCarrera{" +
                "comprador=" + dniUser +
                ", asistente='" + getDniAsistente() + '\'' +
                ", dorsal=" + dorsal +
                '}';
    }

    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;
        TicketCarrera that = (TicketCarrera) o;
        return dorsal == that.dorsal;
    }

    public int hashCode() {
        return java.util.Objects.hash(super.hashCode(), dorsal);
    }
}

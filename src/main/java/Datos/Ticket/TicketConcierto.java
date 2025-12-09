package Datos.Ticket;

import Datos.Usuario.iUsuario;

public class TicketConcierto extends Ticket{
    private String asiento;

    public TicketConcierto(iUsuario usuario, String dniAsistente, String asiento) {
        super(usuario, dniAsistente);
        this.asiento = asiento;
    }

    public TicketConcierto(iUsuario usuario, String dniAsistente, float pagoExtra, String asiento) {
        super(usuario, dniAsistente, pagoExtra);
        this.asiento = asiento;
    }

    public String getAsiento() {
        return asiento;
    }

    public void setAsiento(String asiento) {
        this.asiento = asiento;
    }

    public String toString() {
        return "TicketConcierto{" +
                super.toString() +
                ", asiento='" + asiento + '\'' +
                '}';
    }

    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;
        TicketConcierto that = (TicketConcierto) o;
        return java.util.Objects.equals(asiento, that.asiento);
    }

    public int hashCode() {
        return java.util.Objects.hash(super.hashCode(), asiento);
    }
}

package Datos.Ticket;

import Datos.Usuario.iUsuario;

public class Ticket implements iTicket {
    private iUsuario usuario;
    private String dniAsistente;
    private float pagoExtra;

    public Ticket (iUsuario usuario, String dniBeneficiario) {
        this.usuario = usuario;
        this.dniAsistente = dniBeneficiario;
        this.pagoExtra = 0;
    }

    public Ticket (iUsuario usuario, String dniBeneficiario, float pagoExtra) {
        this.usuario = usuario;
        this.dniAsistente = dniBeneficiario;
        this.pagoExtra = pagoExtra;
    }

    public iUsuario getUsuario() {
        return usuario;
    }

    public void setUsuario(iUsuario usuario) {
        this.usuario = usuario;
    }

    public String getDniAsistente() {
        return dniAsistente;
    }

    public void setDniAsistente(String dniAsistente) {
        this.dniAsistente = dniAsistente;
    }

    public float getPagoExtra() {
        return pagoExtra;
    }

    public void setPagoExtra(float pagoExtra) {
        this.pagoExtra = pagoExtra;
    }

    public String toString() {
        String dniUser = (usuario != null) ? usuario.getDni() : "null";
        return "Ticket{" +
                "comprador=" + dniUser +
                ", asistente='" + dniAsistente + '\'' +
                ", pagoExtra=" + pagoExtra +
                '}';
    }

    public boolean equals(Object o) {
        if (this == o) return true;
        // Used getClass() so a TicketCarrera can never be the same as a TicketConcierto
        if (o == null || getClass() != o.getClass()) return false;
        Ticket ticket = (Ticket) o;
        return Float.compare(ticket.pagoExtra, pagoExtra) == 0 &&
                java.util.Objects.equals(dniAsistente, ticket.dniAsistente) &&
                java.util.Objects.equals(usuario, ticket.usuario);
    }

    public int hashCode() {
        return java.util.Objects.hash(usuario, dniAsistente, pagoExtra);
    }
}

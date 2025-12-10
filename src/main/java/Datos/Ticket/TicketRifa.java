package Datos.Ticket;

import Datos.Usuario.iUsuario;

public class TicketRifa extends Ticket {
    private int idBoleto;

    public TicketRifa(iUsuario usuario, String dniBeneficiario, int idBoleto) {
        super(usuario, dniBeneficiario);
        this.idBoleto = idBoleto;
    }

    public TicketRifa(iUsuario usuario, String dniBeneficiario, float pagoExtra, int idBoleto) {
        super(usuario, dniBeneficiario, pagoExtra);
        this.idBoleto = idBoleto;
    }

    public TicketRifa(iUsuario usuario, String dniBeneficiario, float pagoExtra, int idBoleto, int id) {
        super(usuario, dniBeneficiario, pagoExtra, id);
        this.idBoleto = idBoleto;
    }

    public TicketRifa(iUsuario usuario, String dniBeneficiario, int idBoleto, int id) {
        super(usuario, dniBeneficiario, id);
        this.idBoleto = idBoleto;
    }

    public int getIdBoleto() {
        return idBoleto;
    }

    public void setIdBoleto(int idBoleto) {
        this.idBoleto = idBoleto;
    }

    public String toString() {
        String dniUser = (getUsuario() != null) ? getUsuario().getDni() : "null";
        return "TicketRifa{" + "id" + getId()  +
                "comprador=" + dniUser +
                ", asistente='" + getDniAsistente() + '\'' +
                ", idBoleto=" + idBoleto +
                '}';
    }

    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;
        TicketRifa that = (TicketRifa) o;
        return idBoleto == that.idBoleto;
    }

    public int hashCode() {
        return java.util.Objects.hash(super.hashCode(), idBoleto);
    }
}

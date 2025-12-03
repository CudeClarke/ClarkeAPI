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

    public int getIdBoleto() {
        return idBoleto;
    }

    public void setIdBoleto(int idBoleto) {
        this.idBoleto = idBoleto;
    }

    public String toString(){
        return "Rifa [Usuario=" + super.getUsuario().toString() + ", DNI=" + super.getDniAsistente() + "ID boleto=" + idBoleto + "]";
    }
}

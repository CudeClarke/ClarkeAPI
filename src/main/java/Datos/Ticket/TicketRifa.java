package Datos.Ticket;

import Datos.Usuario.iUsuario;

public class TicketRifa extends Ticket {
    private int idBoleto;

    public TicketRifa(iUsuario usuario, String dniBeneficiario, int id, int idBoleto) {
        super(usuario, dniBeneficiario, id);
        this.idBoleto = idBoleto;
    }

    public int getIdBoleto() {
        return idBoleto;
    }

    public void setIdBoleto(int idBoleto) {
        this.idBoleto = idBoleto;
    }

    public String toString(){
        return "Rifa [Usuario=" + super.getUsuario().toString() + ", DNI=" + super.getDniAsistente() + ", ID=" + super.getId() + "ID boleto=" + idBoleto + "]";
    }
}

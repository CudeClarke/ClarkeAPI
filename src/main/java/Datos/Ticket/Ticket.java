package Datos.Ticket;

import Datos.Usuario.iUsuario;

public class Ticket implements iTicket {
    private iUsuario usuario;
    private String dniAsistente;

    public Ticket (iUsuario usuario, String dniBeneficiario) {
        this.usuario = usuario;
        this.dniAsistente = dniBeneficiario;
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

    public String toString(){
        return "Ticket [Nombre=" + usuario.toString() + ", DNI=" + dniAsistente + "]";
    }
}

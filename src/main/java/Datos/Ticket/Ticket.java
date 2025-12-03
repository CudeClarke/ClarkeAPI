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

    public String toString(){
        return "Ticket [Nombre=" + usuario.toString() + ", DNI=" + dniAsistente + "]";
    }
}

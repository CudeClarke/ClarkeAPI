package Datos.Ticket;

import Datos.Usuario.iUsuario;

public class Ticket implements iTicket {
    private iUsuario usuario;
    private String dniAsistente;
    private int id;

    public Ticket (iUsuario usuario, String dniBeneficiario, int id) {
        this.usuario = usuario;
        this.dniAsistente = dniBeneficiario;
        this.id = id;
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

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String toString(){
        return "Ticket [Nombre=" + usuario.toString() + ", DNI=" + dniAsistente + ", ID=" + id + "]";
    }
}

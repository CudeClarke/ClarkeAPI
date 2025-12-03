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

    public String toString(){
        return "Concierto [Usuario=" + super.getUsuario().toString() + ", DNI=" + super.getDniAsistente() + "Asiento=" + asiento + "]";
    }
}

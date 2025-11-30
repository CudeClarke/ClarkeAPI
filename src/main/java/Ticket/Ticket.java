package Ticket;

public class Ticket implements iTicket {
    private String nombre;
    private String dniBeneficiario;
    private int id;

    public Ticket (String nombre, String dniBeneficiario, int id) {
        this.nombre = nombre;
        this.dniBeneficiario = dniBeneficiario;
        this.id = id;
    }

    public String getNombre() {
        return nombre;
    }

    public String getDniBeneficiario() {
        return dniBeneficiario;
    }

    public int getId() {
        return id;
    }
}

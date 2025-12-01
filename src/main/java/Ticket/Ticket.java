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

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getDniBeneficiario() {
        return dniBeneficiario;
    }

    public void setDniBeneficiario(String dniBeneficiario) {
        this.dniBeneficiario = dniBeneficiario;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String toString(){
        return "Ticket [NOMBRE=" + nombre + ", DNI=" + dniBeneficiario + ", ID=" + id + "]";
    }
}

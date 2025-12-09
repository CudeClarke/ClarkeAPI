package Datos.Usuario;

public class UsuarioRegistrado extends UsuarioBase {
    private String direccion;
    private String tlf;

    public UsuarioRegistrado(){}

    public UsuarioRegistrado(String nombre, String apellido, String email, String dni, boolean spam, String direccion, String tlf) {
        super(nombre, apellido, email, dni, spam);
        this.direccion = direccion;
        this.tlf = tlf;
    }

    public UsuarioRegistrado(String nombre, String apellido, String email, String dni, String direccion, String tlf) {
        super(nombre, apellido, email, dni);
        this.direccion = direccion;
        this.tlf = tlf;
    }

    public String getDireccion() {
        return direccion;
    }

    public void setDireccion(String direccion) {
        this.direccion = direccion;
    }

    public String getTlf() {
        return tlf;
    }

    public void setTlf(String tlf) {
        this.tlf = tlf;
    }

    public String toString() {
        String dniUser = (getUsuario() != null) ? getUsuario().getDni() : "null";
        return "TicketRifa{" +
                "comprador=" + dniUser +
                ", asistente='" + getDniAsistente() + '\'' +
                ", idBoleto=" + idBoleto + // Campo propio
                '}';
    }

    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false; 
        UsuarioRegistrado that = (UsuarioRegistrado) o;
        return java.util.Objects.equals(direccion, that.direccion) &&
                java.util.Objects.equals(tlf, that.tlf);
    }

    public int hashCode() {
        return java.util.Objects.hash(super.hashCode(), direccion, tlf);
    }
}

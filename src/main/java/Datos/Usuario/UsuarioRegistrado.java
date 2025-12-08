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

    public String toString(){
        return "UsuarioRegistrado [DNI=" + super.getDni() + ", Nombre=" + super.getNombre() + ", Apellido=" + super.getApellidos() +
                ", TLF=" + tlf + ", Direccion=" + direccion + ", Spam=" + super.isSpam() + "]";
    }
}

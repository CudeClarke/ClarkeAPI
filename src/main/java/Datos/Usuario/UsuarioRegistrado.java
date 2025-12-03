package Datos.Usuario;

public class UsuarioRegistrado extends UsuarioBase {
    private String direccion;
    private int tlf;

    public UsuarioRegistrado(String nombre, String apellido, String email, String dni, boolean spam, String direccion, int tlf) {
        super(nombre, apellido, email, dni, spam);
        this.direccion = direccion;
        this.tlf = tlf;
    }

    public UsuarioRegistrado(String nombre, String apellido, String email, String dni, String direccion, int tlf) {
        super(nombre, apellido, email, dni);
        this.direccion = direccion;
        this.tlf = tlf;
    }

    public String getDireccion() {
        return direccion;
    }

    public int getTlf() {
        return tlf;
    }

    public void setDireccion(String direccion) {
        this.direccion = direccion;
    }

    public void setTlf(int tlf) {
        this.tlf = tlf;
    }

    public String toString(){
        return "UsuarioRegistrado [DNI=" + super.getDni() + ", Nombre=" + super.getNombre() + ", Apellido=" + super.getApellidos() +
                ", TLF=" + tlf + ", Direccion=" + direccion + ", Spam=" + super.isSpam() + "]";
    }
}

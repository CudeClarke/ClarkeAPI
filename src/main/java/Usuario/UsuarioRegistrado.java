package Usuario;

public class UsuarioRegistrado extends UsuarioBase {
    private String direccion;
    private boolean socio;
    private int tlf;

    public UsuarioRegistrado(String nombre, String apellido, String email, String dni, boolean spam, String direccion, int tlf) {
        super(nombre, apellido, email, dni, spam);
        this.direccion = direccion;
        this.tlf = tlf;
        this.socio = false;
    }

    public UsuarioRegistrado(String nombre, String apellido, String email, String dni, String direccion, int tlf) {
        super(nombre, apellido, email, dni);
        this.direccion = direccion;
        this.tlf = tlf;
        this.socio = true;
    }

    public String getDireccion() {
        return direccion;
    }

    public boolean isSocio() {
        return socio;
    }

    public int getTlf() {
        return tlf;
    }

    public void setDireccion(String direccion) {
        this.direccion = direccion;
    }

    public void setSocio(boolean socio) {
        this.socio = socio;
    }

    public void setTlf(int tlf) {
        this.tlf = tlf;
    }

    public String toString(){
        return "UsuarioRegistrado [DNI=" + getDni() + ", Nombre=" + getNombre() + ", Apellido=" + getApellido() +
                ", TLF=" + tlf + ", Direccion=" + ", Socio=" + socio + direccion + ", Spam=" + isSpam() + "]";
    }
}

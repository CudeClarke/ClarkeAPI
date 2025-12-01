package Usuario;

public class UsuarioBase implements iUsuario {
    private String nombre;
    private String apellido;
    private String email;
    private String dni;
    private boolean spam;

    public UsuarioBase(String nombre, String apellido, String email, String dni) {
        this.nombre = nombre;
        this.apellido = apellido;
        this.email = email;
        this.dni = dni;
        this.spam = false;
    }

    public UsuarioBase(String nombre, String apellido, String email, String dni, boolean spam) {
        this.nombre = nombre;
        this.email = email;
        this.dni = dni;
        this.spam = spam;
    }

    @Override
    public String getNombre() {
        return nombre;
    }

    @Override
    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    @Override
    public String getApellido() {
        return apellido;
    }

    @Override
    public void setApellido(String apellido) {
        this.apellido = apellido;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getDni() {
        return dni;
    }

    public void setDni(String dni) {
        this.dni = dni;
    }

    public boolean isSpam() {
        return spam;
    }

    public void setSpam(boolean spam) {
        this.spam = spam;
    }

    @Override
    public String toString() {
        return "User [DNI=" + dni + ", Nombre=" + nombre + ", Apellido=" + apellido + ", Spam=" + spam + "]";
    }
}

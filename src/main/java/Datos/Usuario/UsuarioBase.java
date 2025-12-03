package Datos.Usuario;

public class UsuarioBase implements iUsuario {
    private String nombre;
    private String apellidos;
    private String email;
    private String dni;
    private boolean spam;

    public UsuarioBase(){}

    public UsuarioBase(String nombre, String apellidos, String email, String dni) {
        this.nombre = nombre;
        this.apellidos = apellidos;
        this.email = email;
        this.dni = dni;
        this.spam = false;
    }

    public UsuarioBase(String nombre, String apellidos, String email, String dni, boolean spam) {
        this.nombre = nombre;
        this.apellidos = apellidos;
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
    public String getApellidos() {
        return apellidos;
    }

    @Override
    public void setApellidos(String apellidos) {
        this.apellidos = apellidos;
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
        return "User [DNI=" + dni + ", Nombre=" + nombre + ", Apellido=" + apellidos + ", Spam=" + spam + "]";
    }
}

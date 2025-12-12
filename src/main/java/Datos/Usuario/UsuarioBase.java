package Datos.Usuario;

public class UsuarioBase implements iUsuario {
    private String nombre;
    private String apellidos;
    private String email;
    private String dni;
    private boolean spam;

    // Empty builder, should not be used. Used for json deserialization purposes exclusively by Jackson
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

    public String toString() {
        return "UsuarioBase{" +
                "dni='" + dni + '\'' +
                ", nombre='" + nombre + '\'' +
                ", apellidos='" + apellidos + '\'' +
                ", email='" + email + '\'' +
                ", spam=" + spam +
                '}';
    }

    public boolean equals(Object o) {
        if (this == o) return true;
        // Used getClass() to distinguish between UsuarioBase and UsuarioRegistrado
        if (o == null || getClass() != o.getClass()) return false;
        UsuarioBase that = (UsuarioBase) o;
        return spam == that.spam &&
                java.util.Objects.equals(nombre, that.nombre) &&
                java.util.Objects.equals(apellidos, that.apellidos) &&
                java.util.Objects.equals(email, that.email) &&
                java.util.Objects.equals(dni, that.dni);
    }

    public int hashCode() {
        return java.util.Objects.hash(nombre, apellidos, email, dni, spam);
    }
}

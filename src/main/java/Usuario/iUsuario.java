package Usuario;

public interface iUsuario {
    public String getNombre();
    public void setNombre(String nombre);
    public String getApellidos();
    public void setApellidos(String apellido);
    public String getEmail();
    public void setEmail(String email);
    public String getDni();
    public void setDni(String dni);
    public boolean isSpam();
    public void setSpam(boolean Spam);
}

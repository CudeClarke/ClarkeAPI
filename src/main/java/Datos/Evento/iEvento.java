package Datos.Evento;

public interface iEvento {
    public String getNombre();
    public void setNombre(String nombre);
    public int getRecaudacion();
    public void setRecaudacion(int recaudacion);
    public int getObjetivoRecaudacion();
    public void setObjetivoRecaudacion(int objetivoRecaudacion);
    public int getAforo();
    public void setAforo(int aforo);
    
    public void addTag(String tag);
    public void removeTag(String tag);
    public void addPatrocinador(Patrocinador p);
    public void removePatrocinador(Patrocinador p);
}

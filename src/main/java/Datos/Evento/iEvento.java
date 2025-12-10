package Datos.Evento;

import Datos.Entrada.Entrada;

import java.util.List;
import java.util.Set;

public interface iEvento {
    public String getNombre();
    public void setNombre(String nombre);
    public int getRecaudacion();
    public void setRecaudacion(int recaudacion);
    public int getObjetivoRecaudacion();
    public void setObjetivoRecaudacion(int objetivoRecaudacion);
    public String getUbicacion();
    public void setUbicacion(String ubicacion);
    public String getDescripcion();
    public void setDescripcion(String descripcion);
    public String getDate();
    public void setDate(String date);
    public String getUrl();
    public void setUrl(String url);
    public String getInformacion();
    public Set<String> getTags();
    public void setTags(Set<String> tags);
    public Set<Patrocinador> getPatrocinadores();
    public void setPatrocinadores(Set<Patrocinador> patrocinadores);
    public List<Entrada> getEntradas();
    public int getID();
    public void setID(int ID);
    
    public void addTag(String tag);
    public void removeTag(String tag);
    public void addPatrocinador(Patrocinador p);
    public void removePatrocinador(Patrocinador p);
    public void addEntrada(Entrada e);
    public void deleteEntrada(Entrada e);
}

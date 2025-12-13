package Datos.Evento;

import Datos.Entrada.iEntrada;

import java.util.List;
import java.util.Set;

public interface iEvento {
    public String getNombre();
    public void setNombre(String nombre);
    public float getRecaudacion();
    public void setRecaudacion(float recaudacion);
    public float getObjetivoRecaudacion();
    public void setObjetivoRecaudacion(float objetivoRecaudacion);
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
    public List<iEntrada> getEntradas();
    public int getID();
    public void setID(int ID);
    
    public void addTag(String tag);
    public void removeTag(String tag);
    public void addPatrocinador(Patrocinador p);
    public void removePatrocinador(Patrocinador p);
    public void addEntrada(iEntrada e);
    public void deleteEntrada(iEntrada e);
}

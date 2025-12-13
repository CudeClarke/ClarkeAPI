package Datos.Evento;



public abstract class EventoFactory {
    public abstract iEvento createEvento(String nombre, String ubicacion, float objetivoRecaudacion, String descripcion, String date, String url, String informacion);
    public abstract iEvento createEvento(String nombre, String ubicacion, float recaudacion, float objetivoRecaudacion, String descripcion, String date, String url, String informacion);
    public abstract iEvento createEvento(String nombre, String ubicacion, float recaudacion, float objetivoRecaudacion, String descripcion, String date, String url, int ID, String informacion);
}

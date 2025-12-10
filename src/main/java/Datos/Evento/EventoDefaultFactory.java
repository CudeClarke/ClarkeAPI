package Datos.Evento;


public class EventoDefaultFactory extends EventoFactory{
    @Override
    public iEvento createEvento(String nombre, String ubicacion, int objetivoRecaudacion, String descripcion, String date, String url, String informacion) {
        return new Evento(nombre, ubicacion, objetivoRecaudacion, descripcion, date, url);
    }

    @Override
    public iEvento createEvento(String nombre, String ubicacion, int recaudacion, int objetivoRecaudacion, String descripcion, String date, String url, String informacion) {
        return new Evento(nombre, ubicacion, recaudacion, objetivoRecaudacion, descripcion, date, url);
    }

    @Override
    public iEvento createEvento(String nombre, String ubicacion, int recaudacion, int objetivoRecaudacion, String descripcion, String date, String url, int ID, String informacion) {
        return new Evento(nombre, ubicacion, recaudacion, objetivoRecaudacion, descripcion, date, url, ID);
    }
}

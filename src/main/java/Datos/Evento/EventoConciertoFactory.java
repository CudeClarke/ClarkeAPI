package Datos.Evento;


public class EventoConciertoFactory extends EventoFactory{
    @Override
    public iEvento createEvento(String nombre, String ubicacion, float objetivoRecaudacion, String descripcion, String date, String url, String artista) {
        return new EventoConcierto(nombre, ubicacion, objetivoRecaudacion, descripcion, date, url, artista);
    }

    @Override
    public iEvento createEvento(String nombre, String ubicacion, float recaudacion, float objetivoRecaudacion, String descripcion, String date, String url, String artista) {
        return new EventoConcierto(nombre, ubicacion, recaudacion, objetivoRecaudacion, descripcion, date, url, artista);
    }

    @Override
    public iEvento createEvento(String nombre, String ubicacion, float recaudacion, float objetivoRecaudacion, String descripcion, String date, String url, int ID, String artista) {
        return new EventoConcierto(nombre, ubicacion, recaudacion, objetivoRecaudacion, descripcion, date, url, ID, artista);
    }
}

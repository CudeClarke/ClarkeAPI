package Datos.Evento;


public class EventoCarreraFactory extends EventoFactory{
    @Override
    public iEvento createEvento(String nombre, String ubicacion, int objetivoRecaudacion, String descripcion, String date, String url, String informacion) {
        int recorrido;
        try {
            recorrido = Integer.parseInt(informacion);
        } catch (NumberFormatException e){
            recorrido = 0;
        }
        return new EventoCarrera(nombre, ubicacion, objetivoRecaudacion, descripcion, date, url, recorrido);
    }

    @Override
    public iEvento createEvento(String nombre, String ubicacion, int recaudacion, int objetivoRecaudacion, String descripcion, String date, String url, String informacion) {
        int recorrido;
        try {
            recorrido = Integer.parseInt(informacion);
        } catch (NumberFormatException e){
            recorrido = 0;
        }
        return new EventoCarrera(nombre, ubicacion, recaudacion, objetivoRecaudacion, descripcion, date, url, recorrido);
    }

    @Override
    public iEvento createEvento(String nombre, String ubicacion, int recaudacion, int objetivoRecaudacion, String descripcion, String date, String url, int ID, String informacion) {
        int recorrido;
        try {
            recorrido = Integer.parseInt(informacion);
        } catch (NumberFormatException e){
            recorrido = 0;
        }
        return new EventoCarrera(nombre, ubicacion, recaudacion, objetivoRecaudacion, descripcion, date, url, ID, recorrido);
    }
}

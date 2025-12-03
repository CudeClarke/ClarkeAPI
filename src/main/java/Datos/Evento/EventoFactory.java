package Datos.Evento;

public abstract class EventoFactory {
    public abstract iEvento createEvento(String nombre, int objetivoRecaudacion, int aforo, String informacion_extra);
}

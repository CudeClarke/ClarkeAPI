package Evento;

public class EventoAbstracto implements iEvento {
    private String nombre;
    private int recaudacion;
    private int objetivoRecaudacion;
    private int aforo;

    public EventoAbstracto(String nombre, int objetivoRecaudacion, int aforo) {
        this.nombre = nombre;
        this.objetivoRecaudacion = objetivoRecaudacion;
        this.aforo = aforo;
        recaudacion = 0;
    }
}

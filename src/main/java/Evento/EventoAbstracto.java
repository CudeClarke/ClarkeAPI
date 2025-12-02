package Evento;

public class EventoAbstracto implements iEvento {
    private String nombre;
    private int recaudacion;
    private int objetivoRecaudacion;
    private int aforo;

    enum Patrocinadores{
        // Agrega aqui todos los patrocinadores que necesites
    }

    enum Tags {
        CENA,
        CARRERA,
        RIFA
        // Agrega aquí todos los tags que necesites
    }

    public EventoAbstracto(String nombre, int objetivoRecaudacion, int aforo) {
        this.nombre = nombre;
        this.objetivoRecaudacion = objetivoRecaudacion;
        this.aforo = aforo;
        recaudacion = 0;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public int getRecaudacion() {
        return recaudacion;
    }

    public void setRecaudacion(int recaudacion) {
        this.recaudacion = recaudacion;
    }

    public int getObjetivoRecaudacion() {
        return objetivoRecaudacion;
    }

    public void setObjetivoRecaudacion(int objetivoRecaudacion) {
        this.objetivoRecaudacion = objetivoRecaudacion;
    }

    public int getAforo() {
        return aforo;
    }

    public void setAforo(int aforo) {
        this.aforo = aforo;
    }

    @Override
    public String toString(){
        return "Evento Abstracto [NOMBRE=" + nombre + "RECAUDACION=" + recaudacion + "OBJRECAUDACION" + objetivoRecaudacion +"AFORO=" + aforo + "]";
    }
}

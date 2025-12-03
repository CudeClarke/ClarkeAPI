package Datos.Evento;

import java.util.List;

public class EventoRifa extends Evento {
    private List<Integer> premios;

    public EventoRifa(String nombre, int objetivoRecaudacion, int aforo, List<Integer> premios) {
        super(nombre, objetivoRecaudacion, aforo);
        if (premios == null) {
            this.premios = List.of();
        } else {
            this.premios = List.copyOf(premios);
        }
    }

    public List<Integer> getPremios() {
        return premios;
    }

    public void setPremios(List<Integer> premios) {
        this.premios = List.copyOf(premios);
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("Rifa [");
        for(int i = 0; i < premios.size(); i++){
            sb.append(premios.get(i));
        }
        sb.append("]");
        return sb.toString();
    }

}

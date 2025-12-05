package Datos.Evento;

import java.sql.Date;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class EventoRifa extends Evento {
    private List<Integer> premios;

    public EventoRifa(String nombre, String ubicacion, int recaudacion, int objetivoRecaudacion, String descripcion, Date date, String url) {
        super(nombre, ubicacion, recaudacion, objetivoRecaudacion, descripcion, date, url);
        premios = new ArrayList<>();
    }

    public List<Integer> getPremios() {
        return premios;
    }

    public void setPremios(List<Integer> premios) {
        this.premios = List.copyOf(premios);
    }

    public String getInformacion() { return premios.stream()
            .map(String::valueOf) // O .map(n -> String.valueOf(n))
            .collect(Collectors.joining(", "));}

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

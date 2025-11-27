package Evento;

import java.util.ArrayList;
import java.util.List;

public class EventoRifa extends EventoAbstracto {
    private List<Integer> premios;

    public EventoRifa(String nombre, int objetivoRecaudacion, int aforo) {
        super(nombre, objetivoRecaudacion, aforo);
        this.premios = new ArrayList<>();
    }

}

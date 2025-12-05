package Datos.Evento;

import java.sql.Date;
import java.util.ArrayList;

public class EventoRifaFactory extends EventoFactory{

    @Override
    public iEvento createEvento(String nombre, String ubicacion, int recaudacion, int objetivoRecaudacion, String descripcion, Date date, String url, String informacion) {
        return new EventoRifa(nombre, ubicacion, recaudacion, objetivoRecaudacion, descripcion, date, url);
    }
}

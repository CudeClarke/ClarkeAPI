package Datos.Evento;

import java.util.ArrayList;

public class EventoRifaFactory extends EventoFactory{
    @Override
    public iEvento createEvento(String nombre, String ubicacion, float objetivoRecaudacion, String descripcion, String date, String url, String informacion) {
        return new EventoRifa(nombre, ubicacion, objetivoRecaudacion, descripcion, date, url);
    }

    @Override
    public iEvento createEvento(String nombre, String ubicacion, float recaudacion, float objetivoRecaudacion, String descripcion, String date, String url, String informacion) {
        return new EventoRifa(nombre, ubicacion, recaudacion, objetivoRecaudacion, descripcion, date, url);
    }

    @Override
    public iEvento createEvento(String nombre, String ubicacion, float recaudacion, float objetivoRecaudacion, String descripcion, String date, String url, int ID, String informacion) {
        return new EventoRifa(nombre, ubicacion, recaudacion, objetivoRecaudacion, descripcion, date, url, ID);
    }
}

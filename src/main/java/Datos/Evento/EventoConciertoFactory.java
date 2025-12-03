package Datos.Evento;

import java.util.Date;

public class EventoConciertoFactory extends EventoFactory{
    @Override
    public iEvento createEvento(String nombre, int objetivoRecaudacion, int aforo, String informacion_extra) {
        // TODO: Convertir informacion_extra a los atributos necesarios de la clase concreta
        // El código se deja así ante el inminente refactor de las clases de datos
        return new EventoConcierto(nombre, objetivoRecaudacion, aforo, "placeholder", "placerholder", new Date());
    }
}

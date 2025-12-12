package Datos.Ticket;

import Datos.Usuario.iUsuario;

public class TicketRifaFactory extends TicketFactory{
    @Override
    public iTicket createTicket(iUsuario usuario, String dniAsistente, String idBoleto) {
        int idBoleto_int;
        try {
            idBoleto_int = Integer.parseInt(idBoleto);
        }catch (NumberFormatException e){
            idBoleto_int = -1;
        }
        return new TicketRifa(usuario, dniAsistente, idBoleto_int);
    }

    @Override
    public iTicket createTicket(iUsuario usuario, String dniAsistente, float pagoExtra, String idBoleto) {
        int idBoleto_int;
        try {
            idBoleto_int = Integer.parseInt(idBoleto);
        }catch (NumberFormatException e){
            idBoleto_int = -1;
        }
        return new TicketRifa(usuario, dniAsistente, pagoExtra, idBoleto_int);
    }

    @Override
    public iTicket createTicket(iUsuario usuario, String dniAsistente, float pagoExtra, String idBoleto, int id) {
        int idBoleto_int;
        try {
            idBoleto_int = Integer.parseInt(idBoleto);
        }catch (NumberFormatException e){
            idBoleto_int = -1;
        }
        return new TicketRifa(usuario, dniAsistente, pagoExtra, idBoleto_int, id);
    }

    public iTicket createTicket(iUsuario usuario, String dniAsistente, String idBoleto, int id) {
        int idBoleto_int;
        try {
            idBoleto_int = Integer.parseInt(idBoleto);
        }catch (NumberFormatException e){
            idBoleto_int = -1;
        }
        return new TicketRifa(usuario, dniAsistente, idBoleto_int, id);
    }
}

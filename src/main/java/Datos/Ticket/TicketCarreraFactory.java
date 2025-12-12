package Datos.Ticket;

import Datos.Usuario.iUsuario;

public class TicketCarreraFactory extends TicketFactory{
    @Override
    public iTicket createTicket(iUsuario usuario, String dniAsistente, String dorsal) {
        int dorsal_int;
        try {
            dorsal_int = Integer.parseInt(dorsal);
        }catch (NumberFormatException e){
            dorsal_int = -1;
        }
        return new TicketCarrera(usuario, dniAsistente, dorsal_int);
    }

    @Override
    public iTicket createTicket(iUsuario usuario, String dniAsistente, float pagoExtra, String dorsal) {
        int dorsal_int;
        try {
            dorsal_int = Integer.parseInt(dorsal);
        }catch (NumberFormatException e){
            dorsal_int = -1;
        }
        return new TicketCarrera(usuario, dniAsistente, pagoExtra, dorsal_int);
    }

    @Override
    public iTicket createTicket(iUsuario usuario, String dniAsistente, float pagoExtra, String dorsal, int id) {
        int dorsal_int;
        try {
            dorsal_int = Integer.parseInt(dorsal);
        }catch (NumberFormatException e){
            dorsal_int = -1;
        }
        return new TicketCarrera(usuario, dniAsistente, pagoExtra, dorsal_int, id);
    }

    @Override
    public iTicket createTicket(iUsuario usuario, String dniAsistente, String dorsal, int id) {
        int dorsal_int;
        try {
            dorsal_int = Integer.parseInt(dorsal);
        }catch (NumberFormatException e){
            dorsal_int = -1;
        }
        return new TicketCarrera(usuario, dniAsistente, dorsal_int, id);
    }
}

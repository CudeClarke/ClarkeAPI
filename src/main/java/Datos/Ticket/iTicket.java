package Datos.Ticket;

import Datos.Usuario.iUsuario;

public interface iTicket {
    public iUsuario getUsuario();
    public void setUsuario(iUsuario usuario);
    public String getDniAsistente();
    public void setDniAsistente(String dniAsistente);
    public float getPagoExtra();
    public void setPagoExtra(float pagoExtra);
    public int getId();
    public void setId(int id);
    public String getInformacion();
}

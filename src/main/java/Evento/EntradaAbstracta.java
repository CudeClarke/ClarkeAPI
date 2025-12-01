package Evento;

public class EntradaAbstracta implements iEntrada {
    private int subAforo;
    private float precio;

    public EntradaAbstracta(int subAforo, float precio) {
        this.subAforo = subAforo;
        this.precio = precio;
    }

    public int getSubAforo() {
        return subAforo;
    }

    public void setSubAforo(int subAforo) {
        this.subAforo = subAforo;
    }

    public float getPrecio() {
        return precio;
    }

    public void setPrecio(float precio) {
        this.precio = precio;
    }

    public String toString(){
        return "Entrada Abstracta [subAforo=" + subAforo + ", precio=" + precio + "]";
    }
}

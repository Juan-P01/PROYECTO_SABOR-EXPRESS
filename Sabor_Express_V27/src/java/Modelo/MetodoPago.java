package Modelo;

public class MetodoPago {
    private int idMetodoPago;
    private String nombre;
    private int estado;

    public MetodoPago() {
    }

    public MetodoPago(int idMetodoPago, String nombre, int estado) {
        this.idMetodoPago = idMetodoPago;
        this.nombre = nombre;
        this.estado = estado;
    }

    public int getIdMetodoPago() {
        return idMetodoPago;
    }

    public void setIdMetodoPago(int idMetodoPago) {
        this.idMetodoPago = idMetodoPago;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public int getEstado() {
        return estado;
    }

    public void setEstado(int estado) {
        this.estado = estado;
    }
}
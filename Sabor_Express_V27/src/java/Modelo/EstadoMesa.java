package Modelo;

public class EstadoMesa {
    private int idEstadoMesa;
    private String nombre;
    private int estado;

    public EstadoMesa() {
    }

    public EstadoMesa(int idEstadoMesa, String nombre, int estado) {
        this.idEstadoMesa = idEstadoMesa;
        this.nombre = nombre;
        this.estado = estado;
    }

    public int getIdEstadoMesa() {
        return idEstadoMesa;
    }

    public void setIdEstadoMesa(int idEstadoMesa) {
        this.idEstadoMesa = idEstadoMesa;
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
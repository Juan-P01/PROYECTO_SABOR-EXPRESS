package Modelo;

public class EstadoPedido {
    private int idEstadoPedido;
    private String detalleEstadoPedido;
    private int estado;

    public int getIdEstadoPedido() {
        return idEstadoPedido;
    }

    public void setIdEstadoPedido(int idEstadoPedido) {
        this.idEstadoPedido = idEstadoPedido;
    }

    public String getDetalleEstadoPedido() {
        return detalleEstadoPedido;
    }

    public void setDetalleEstadoPedido(String detalleEstadoPedido) {
        this.detalleEstadoPedido = detalleEstadoPedido;
    }

    public int getEstado() {
        return estado;
    }

    public void setEstado(int estado) {
        this.estado = estado;
    }
}
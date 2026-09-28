package Modelo;

import java.sql.Date;

public class Factura {
    private int idFactura;
    private String numeroFactura;
    private double totalFactura;
    private Date fechaFactura;
    private int pedidosIdPedido;
    private int clientesIdCliente;
    private int estado;

    public int getIdFactura() {
        return idFactura;
    }

    public void setIdFactura(int idFactura) {
        this.idFactura = idFactura;
    }

    public String getNumeroFactura() {
        return numeroFactura;
    }

    public void setNumeroFactura(String numeroFactura) {
        this.numeroFactura = numeroFactura;
    }

    public double getTotalFactura() {
        return totalFactura;
    }

    public void setTotalFactura(double totalFactura) {
        this.totalFactura = totalFactura;
    }

    public Date getFechaFactura() {
        return fechaFactura;
    }

    public void setFechaFactura(Date fechaFactura) {
        this.fechaFactura = fechaFactura;
    }

    public int getPedidosIdPedido() {
        return pedidosIdPedido;
    }

    public void setPedidosIdPedido(int pedidosIdPedido) {
        this.pedidosIdPedido = pedidosIdPedido;
    }

    public int getClientesIdCliente() {
        return clientesIdCliente;
    }

    public void setClientesIdCliente(int clientesIdCliente) {
        this.clientesIdCliente = clientesIdCliente;
    }

    public int getEstado() {
        return estado;
    }

    public void setEstado(int estado) {
        this.estado = estado;
    }
}
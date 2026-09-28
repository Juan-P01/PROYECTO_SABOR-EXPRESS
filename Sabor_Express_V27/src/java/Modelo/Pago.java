package Modelo;

public class Pago {
    private int idPago;
    private int idFactura;
    private int idMetodoPago;
    private double monto;
    private String fechaPago;
    private int estado;

    public Pago() {
    }

    public Pago(int idPago, int idFactura, int idMetodoPago, double monto, String fechaPago, int estado) {
        this.idPago = idPago;
        this.idFactura = idFactura;
        this.idMetodoPago = idMetodoPago;
        this.monto = monto;
        this.fechaPago = fechaPago;
        this.estado = estado;
    }

    public int getIdPago() {
        return idPago;
    }

    public void setIdPago(int idPago) {
        this.idPago = idPago;
    }

    public int getIdFactura() {
        return idFactura;
    }

    public void setIdFactura(int idFactura) {
        this.idFactura = idFactura;
    }

    public int getIdMetodoPago() {
        return idMetodoPago;
    }

    public void setIdMetodoPago(int idMetodoPago) {
        this.idMetodoPago = idMetodoPago;
    }

    public double getMonto() {
        return monto;
    }

    public void setMonto(double monto) {
        this.monto = monto;
    }

    public String getFechaPago() {
        return fechaPago;
    }

    public void setFechaPago(String fechaPago) {
        this.fechaPago = fechaPago;
    }

    public int getEstado() {
        return estado;
    }

    public void setEstado(int estado) {
        this.estado = estado;
    }
}
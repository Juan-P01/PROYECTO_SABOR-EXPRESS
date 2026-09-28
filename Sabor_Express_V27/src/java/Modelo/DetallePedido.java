package Modelo;

import java.math.BigDecimal;

public class DetallePedido {

    private int id_detalle_pedido;
    private int cantidad;
    private BigDecimal subtotal;
    private BigDecimal precio_unitario;
    private int menus_id_menu;
    private int pedidos_id_pedido;
    private int estados_pedidos_id_estado_pedido;
    private int estado;

    public int getId_detalle_pedido() {
        return id_detalle_pedido;
    }

    public void setId_detalle_pedido(int id_detalle_pedido) {
        this.id_detalle_pedido = id_detalle_pedido;
    }

    public int getCantidad() {
        return cantidad;
    }

    public void setCantidad(int cantidad) {
        this.cantidad = cantidad;
    }

    public BigDecimal getSubtotal() {
        return subtotal;
    }

    public void setSubtotal(BigDecimal subtotal) {
        this.subtotal = subtotal;
    }

    public BigDecimal getPrecio_unitario() {
        return precio_unitario;
    }

    public void setPrecio_unitario(BigDecimal precio_unitario) {
        this.precio_unitario = precio_unitario;
    }

    public int getMenus_id_menu() {
        return menus_id_menu;
    }

    public void setMenus_id_menu(int menus_id_menu) {
        this.menus_id_menu = menus_id_menu;
    }

    public int getPedidos_id_pedido() {
        return pedidos_id_pedido;
    }

    public void setPedidos_id_pedido(int pedidos_id_pedido) {
        this.pedidos_id_pedido = pedidos_id_pedido;
    }

    public int getEstados_pedidos_id_estado_pedido() {
        return estados_pedidos_id_estado_pedido;
    }

    public void setEstados_pedidos_id_estado_pedido(int estados_pedidos_id_estado_pedido) {
        this.estados_pedidos_id_estado_pedido = estados_pedidos_id_estado_pedido;
    }

    public int getEstado() {
        return estado;
    }

    public void setEstado(int estado) {
        this.estado = estado;
    }
}
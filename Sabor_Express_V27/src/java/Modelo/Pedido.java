  
                                                                                                     
                                                                                          
   
package Modelo;

import java.time.Instant;
   
  
                   
   
public class Pedido {

    public int getIdPedido() {
        return idPedido;
    }

    public void setIdPedido(int idPedido) {
        this.idPedido = idPedido;
    }

    public double getTotal() {
        return total;
    }

    public void setTotal(double total) {
        this.total = total;
    }

    public Instant getFechaPedido() {
        return fechaPedido;
    }

    public void setFechaPedido(Instant fechaPedido) {
        this.fechaPedido = fechaPedido;
    }

    public int getMesasIdMesa() {
        return mesasIdMesa;
    }

    public void setMesasIdMesa(int mesasIdMesa) {
        this.mesasIdMesa = mesasIdMesa;
    }

    public int getClientesIdCliente() {
        return clientesIdCliente;
    }

    public void setClientesIdCliente(int clientesIdCliente) {
        this.clientesIdCliente = clientesIdCliente;
    }

    public int getUsuariosIdUsuario() {
        return usuariosIdUsuario;
    }

    public void setUsuariosIdUsuario(int usuariosIdUsuario) {
        this.usuariosIdUsuario = usuariosIdUsuario;
    }

    public int getEstadosPedidosIdEstadoPedido() {
        return estadosPedidosIdEstadoPedido;
    }

    public void setEstadosPedidosIdEstadoPedido(int estadosPedidosIdEstadoPedido) {
        this.estadosPedidosIdEstadoPedido = estadosPedidosIdEstadoPedido;
    }

    public boolean isEstado() {
        return estado;
    }

    public void setEstado(boolean estado) {
        this.estado = estado;
    }


    private int idPedido;
    private double total;
    private Instant fechaPedido;
    private int mesasIdMesa;
    private int clientesIdCliente;
    private int usuariosIdUsuario;
    private int estadosPedidosIdEstadoPedido;
    private boolean estado;
    
    
}

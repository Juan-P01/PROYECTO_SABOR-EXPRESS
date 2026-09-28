  
                                                                                                     
                                                                                          
   
package Controlador;

import Modelo.Pedido;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;

public class PedidoDAO {

    private Conexion conect = new Conexion();

    public Pedido consultarPedido(int idPedido) {
        Pedido miPedido = null;
        Connection conn = conect.getConn();

        try {
            String querySql = "SELECT id_pedido, total, fecha_pedido, mesas_id_mesa, "
                    + "clientes_id_cliente, usuarios_id_usuario, estados_pedidos_id_estado_pedido, estado "
                    + "FROM pedidos WHERE id_pedido = ?";

            PreparedStatement ps = conn.prepareStatement(querySql);
            ps.setInt(1, idPedido);

            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                miPedido = new Pedido();
                miPedido.setIdPedido(rs.getInt("id_pedido"));
                miPedido.setTotal(rs.getDouble("total"));
                miPedido.setFechaPedido(rs.getTimestamp("fecha_pedido").toInstant());
                miPedido.setMesasIdMesa(rs.getInt("mesas_id_mesa"));
                miPedido.setClientesIdCliente(rs.getInt("clientes_id_cliente"));
                miPedido.setUsuariosIdUsuario(rs.getInt("usuarios_id_usuario"));
                miPedido.setEstadosPedidosIdEstadoPedido(rs.getInt("estados_pedidos_id_estado_pedido"));
                miPedido.setEstado(rs.getBoolean("estado"));
            }
            return miPedido;
        } catch (Exception e) {
            System.out.println("Error al consultar pedido: " + e.getMessage());
            return miPedido;
        }
    }

    public boolean insertarPedido(Pedido miPedido) {
        boolean insertar = false;
        Connection conn = conect.getConn();

        try {
            String querySql = "INSERT INTO pedidos (total, fecha_pedido, mesas_id_mesa, "
                    + "clientes_id_cliente, usuarios_id_usuario, estados_pedidos_id_estado_pedido, estado) "
                    + "VALUES (?, ?, ?, ?, ?, ?, ?)";

            PreparedStatement ps = conn.prepareStatement(querySql);
            ps.setDouble(1, miPedido.getTotal());
            ps.setTimestamp(2, miPedido.getFechaPedido() != null ? Timestamp.from(miPedido.getFechaPedido()) : Timestamp.from(java.time.Instant.now()));
            ps.setInt(3, miPedido.getMesasIdMesa());
            ps.setInt(4, miPedido.getClientesIdCliente());
            ps.setInt(5, miPedido.getUsuariosIdUsuario());
            ps.setInt(6, miPedido.getEstadosPedidosIdEstadoPedido());
            ps.setBoolean(7, miPedido.isEstado());

            ps.executeUpdate();
            insertar = true;
            System.out.println("Pedido Insertado correctamente");

        } catch (Exception e) {
            System.out.println("Error al Insertar Pedido: " + e.getMessage());
        }
        return insertar;
    }

    public boolean actualizarPedido(Pedido miPedido) {
        boolean actualizar = false;
        Connection conn = conect.getConn();

        try {
            String querySql = "UPDATE pedidos SET total = ?, mesas_id_mesa = ?, "
                    + "clientes_id_cliente = ?, usuarios_id_usuario = ?, "
                    + "estados_pedidos_id_estado_pedido = ?, estado = ? "
                    + "WHERE id_pedido = ?";

            PreparedStatement ps = conn.prepareStatement(querySql);
            ps.setDouble(1, miPedido.getTotal());
            ps.setInt(2, miPedido.getMesasIdMesa());
            ps.setInt(3, miPedido.getClientesIdCliente());
            ps.setInt(4, miPedido.getUsuariosIdUsuario());
            ps.setInt(5, miPedido.getEstadosPedidosIdEstadoPedido());
            ps.setBoolean(6, miPedido.isEstado());
            ps.setInt(7, miPedido.getIdPedido());

            if (ps.executeUpdate() > 0) {
                actualizar = true;
                System.out.println("Pedido actualizado con éxito");
            }
        } catch (Exception e) {
            System.out.println("Error al actualizar el Pedido: " + e.getMessage());
        }
        return actualizar;
    }

    public boolean inactivarPedido(int id) {
        boolean inactivar = false;
        String querySql = "UPDATE pedidos SET estado = 0 WHERE id_pedido = ?";
        Connection conn = conect.getConn();

        try {
            PreparedStatement ps = conn.prepareStatement(querySql);
            ps.setInt(1, id);
            if (ps.executeUpdate() > 0) {
                inactivar = true;
            }
        } catch (Exception e) {
            System.out.println("Error al inactivar pedido: " + e.getMessage());
        }

        return inactivar;
    }

    public boolean activarPedido(int id) {
        boolean activar = false;
        String querySql = "UPDATE pedidos SET estado = 1 WHERE id_pedido = ?";
        Connection conn = conect.getConn();

        try {
            PreparedStatement ps = conn.prepareStatement(querySql);
            ps.setInt(1, id);
            if (ps.executeUpdate() > 0) {
                activar = true;
            }
        } catch (Exception e) {
            System.out.println("Error al activar pedido: " + e.getMessage());
        }

        return activar;
    }

    public boolean eliminarPedido(int id) {
        boolean eliminar = false;
        String querySql = "DELETE FROM pedidos WHERE id_pedido = ?";
        Connection conn = conect.getConn();

        try {
            PreparedStatement ps = conn.prepareStatement(querySql);
            ps.setInt(1, id);
            if (ps.executeUpdate() > 0) {
                eliminar = true;
            }
        } catch (SQLException e) {
            System.out.println("Error al eliminar pedido: " + e.getMessage());
        }
        return eliminar;
    }
    public boolean actualizarEstadoPedido(int idPedido, int idEstadoPedido) {
        String sql = "UPDATE pedidos SET estados_pedidos_id_estado_pedido = ? WHERE id_pedido = ? AND estado = 1";
        Connection conn = conect.getConn();
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, idEstadoPedido);
            ps.setInt(2, idPedido);
            return ps.executeUpdate() > 0;
        } catch (Exception e) {
            System.out.println("Error al actualizar estado del pedido: " + e.getMessage());
            return false;
        }
    }

}
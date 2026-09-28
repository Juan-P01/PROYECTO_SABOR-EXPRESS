package Controlador;

import Controlador.Conexion;
import Modelo.DetallePedido;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

public class DetallePedidoDAO {

    private Conexion conect = new Conexion();

    public DetallePedido consultarDetallePedido(int id) {
        DetallePedido detalle = null;
        Connection conn = conect.getConn();

        try {
            String querySql = "SELECT id_detalle_pedido, cantidad, subtotal, precio_unitario, "
                    + "menus_id_menu, pedidos_id_pedido, estados_pedidos_id_estado_pedido, estado "
                    + "FROM detalles_pedidos WHERE id_detalle_pedido = ?";

            PreparedStatement ps = conn.prepareStatement(querySql);
            ps.setInt(1, id);

            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                detalle = new DetallePedido();
                detalle.setId_detalle_pedido(rs.getInt("id_detalle_pedido"));
                detalle.setCantidad(rs.getInt("cantidad"));
                detalle.setSubtotal(rs.getBigDecimal("subtotal"));
                detalle.setPrecio_unitario(rs.getBigDecimal("precio_unitario"));
                detalle.setMenus_id_menu(rs.getInt("menus_id_menu"));
                detalle.setPedidos_id_pedido(rs.getInt("pedidos_id_pedido"));
                detalle.setEstados_pedidos_id_estado_pedido(rs.getInt("estados_pedidos_id_estado_pedido"));
                detalle.setEstado(rs.getInt("estado"));
            }
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
        return detalle;
    }

    public boolean insertarDetallePedido(DetallePedido detalle) {
        boolean insertar = false;
        Connection conn = conect.getConn();

        try {
            String querySql = "INSERT INTO detalles_pedidos (cantidad, subtotal, precio_unitario, "
                    + "menus_id_menu, pedidos_id_pedido, estados_pedidos_id_estado_pedido, estado) "
                    + "VALUES (?, ?, ?, ?, ?, ?, ?)";

            PreparedStatement ps = conn.prepareStatement(querySql);
            ps.setInt(1, detalle.getCantidad());
            ps.setBigDecimal(2, detalle.getSubtotal());
            ps.setBigDecimal(3, detalle.getPrecio_unitario());
            ps.setInt(4, detalle.getMenus_id_menu());
            ps.setInt(5, detalle.getPedidos_id_pedido());
            ps.setInt(6, detalle.getEstados_pedidos_id_estado_pedido());
            ps.setInt(7, detalle.getEstado());

            ps.executeUpdate();
            insertar = true;
            System.out.println("Detalle insertado");
        } catch (Exception e) {
            System.out.println("Error al insertar detalle: " + e.getMessage());
        }
        return insertar;
    }

    public boolean actualizarDetallePedido(DetallePedido detalle) {
        boolean actualizar = false;
        Connection conn = conect.getConn();

        try {
            String querySql = "UPDATE detalles_pedidos SET cantidad = ?, subtotal = ?, precio_unitario = ?, "
                    + "menus_id_menu = ?, pedidos_id_pedido = ?, estados_pedidos_id_estado_pedido = ? "
                    + "WHERE id_detalle_pedido = ?";

            PreparedStatement ps = conn.prepareStatement(querySql);
            ps.setInt(1, detalle.getCantidad());
            ps.setBigDecimal(2, detalle.getSubtotal());
            ps.setBigDecimal(3, detalle.getPrecio_unitario());
            ps.setInt(4, detalle.getMenus_id_menu());
            ps.setInt(5, detalle.getPedidos_id_pedido());
            ps.setInt(6, detalle.getEstados_pedidos_id_estado_pedido());
            ps.setInt(7, detalle.getId_detalle_pedido());

            if (ps.executeUpdate() > 0) {
                actualizar = true;
                System.out.println("Detalle actualizado");
            }
        } catch (Exception e) {
            System.out.println("Error al actualizar detalle: " + e.getMessage());
        }
        return actualizar;
    }

    public boolean inactivarDetallePedido(int id) {
        boolean inactivar = false;
        String querySql = "UPDATE detalles_pedidos SET estado = 0 WHERE id_detalle_pedido = ?";
        Connection conn = conect.getConn();

        try {
            PreparedStatement ps = conn.prepareStatement(querySql);
            ps.setInt(1, id);
            if (ps.executeUpdate() > 0) {
                inactivar = true;
            }
        } catch (Exception e) {
            System.out.println("Error al inactivar detalle: " + e.getMessage());
        }
        return inactivar;
    }

    public boolean activarDetallePedido(int id) {
        boolean activar = false;
        String querySql = "UPDATE detalles_pedidos SET estado = 1 WHERE id_detalle_pedido = ?";
        Connection conn = conect.getConn();

        try {
            PreparedStatement ps = conn.prepareStatement(querySql);
            ps.setInt(1, id);
            if (ps.executeUpdate() > 0) {
                activar = true;
            }
        } catch (Exception e) {
            System.out.println("Error al activar detalle: " + e.getMessage());
        }
        return activar;
    }
}
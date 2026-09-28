package Controlador;

import Modelo.EstadoPedido;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class EstadoPedidoDAO {

    private Conexion conect = new Conexion();

    public EstadoPedido consultarEstadoPedido(int idEstadoPedido) {
        EstadoPedido miEstadoPedido = null;
        Connection conn = conect.getConn();

        try {
            String querySql = "SELECT id_estado_pedido, detalle_estado_pedido, estado FROM estados_pedidos WHERE id_estado_pedido = ?";
            PreparedStatement ps = conn.prepareStatement(querySql);
            ps.setInt(1, idEstadoPedido);

            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                miEstadoPedido = new EstadoPedido();
                miEstadoPedido.setIdEstadoPedido(rs.getInt("id_estado_pedido"));
                miEstadoPedido.setDetalleEstadoPedido(rs.getString("detalle_estado_pedido"));
                miEstadoPedido.setEstado(rs.getInt("estado"));
            }
            return miEstadoPedido;
        } catch (Exception e) {
            System.out.println(e.getMessage());
            return miEstadoPedido;
        }
    }

    public boolean InsertarEstadoPedido(EstadoPedido miEstadoPedido) {
        boolean insertar = false;
        Connection conn = conect.getConn();

        try {
            String querySql = "INSERT INTO estados_pedidos (id_estado_pedido, detalle_estado_pedido) VALUES (?, ?)";
            PreparedStatement ps = conn.prepareStatement(querySql);
            ps.setInt(1, miEstadoPedido.getIdEstadoPedido());
            ps.setString(2, miEstadoPedido.getDetalleEstadoPedido());

            ps.executeUpdate();
            insertar = true;
            System.out.println("Dato Insertado");

        } catch (Exception e) {
            System.out.println("Error al Insertar Estado Pedido: " + e.getMessage());
        }
        return insertar;
    }

    public boolean actualizarEstadoPedido(EstadoPedido miEstadoPedido) {
        boolean actualizar = false;
        Connection conn = conect.getConn();

        try {
            String querySql = "UPDATE estados_pedidos SET detalle_estado_pedido = ? WHERE id_estado_pedido = ?";
            PreparedStatement ps = conn.prepareStatement(querySql);
            ps.setString(1, miEstadoPedido.getDetalleEstadoPedido());
            ps.setInt(2, miEstadoPedido.getIdEstadoPedido());

            if (ps.executeUpdate() > 0) {
                actualizar = true;
                System.out.println("datos corregidos");
            }
        } catch (Exception e) {
            System.out.println("Error al actualizar el Estado Pedido: " + e.getMessage());
        }
        return actualizar;
    }

    public boolean inactivarEstadoPedido(int id) {
        boolean inactivar = false;
        String querySql = "UPDATE estados_pedidos SET estado = 0 WHERE id_estado_pedido = ?";
        Connection conn = conect.getConn();

        try {
            PreparedStatement ps = conn.prepareStatement(querySql);
            ps.setInt(1, id);
            if (ps.executeUpdate() > 0) {
                inactivar = true;
            }
        } catch (Exception e) {
            System.out.println("Error al inactivar estado pedido: " + e.getMessage());
        }

        return inactivar;
    }

    public boolean ActivarEstadoPedido(int id) {
        boolean Activar = false;
        String querySql = "UPDATE estados_pedidos SET estado = 1 WHERE id_estado_pedido = ?";
        Connection conn = conect.getConn();

        try {
            PreparedStatement ps = conn.prepareStatement(querySql);
            ps.setInt(1, id);
            if (ps.executeUpdate() > 0) {
                Activar = true;
            }
        } catch (Exception e) {
            System.out.println("Error al Activar estado pedido: " + e.getMessage());
        }

        return Activar;
    }

    public boolean eliminarEstadoPedido(int id) {
        boolean eliminar = false;
        String querySql = "DELETE FROM estados_pedidos WHERE id_estado_pedido = ?";
        Connection conn = conect.getConn();

        try {
            PreparedStatement ps = conn.prepareStatement(querySql);
            ps.setInt(1, id);
            if (ps.executeUpdate() > 0) {
                eliminar = true;
            }
        } catch (SQLException e) {
            System.out.println("Error al eliminar estado pedido: " + e.getMessage());
        }
        return eliminar;
    }
}
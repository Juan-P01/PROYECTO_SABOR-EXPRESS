package Controlador;

import Modelo.Factura;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class FacturaDAO {

    private Conexion conect = new Conexion();

    public Factura consultarFactura(String numeroFactura) {
        Factura miFactura = null;
        Connection conn = conect.getConn();

        try {
            String querySql = "SELECT id_factura, numero_factura, total_factura, fecha_factura, pedidos_id_pedido, clientes_id_cliente, estado FROM facturas WHERE numero_factura = ?";
            PreparedStatement ps = conn.prepareStatement(querySql);
            ps.setString(1, numeroFactura);

            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                miFactura = new Factura();
                miFactura.setIdFactura(rs.getInt("id_factura"));
                miFactura.setNumeroFactura(rs.getString("numero_factura"));
                miFactura.setTotalFactura(rs.getDouble("total_factura"));
                miFactura.setFechaFactura(rs.getDate("fecha_factura"));
                miFactura.setPedidosIdPedido(rs.getInt("pedidos_id_pedido"));
                miFactura.setClientesIdCliente(rs.getInt("clientes_id_cliente"));
                miFactura.setEstado(rs.getInt("estado"));
            }
            return miFactura;
        } catch (Exception e) {
            System.out.println(e.getMessage());
            return miFactura;
        }
    }

    public boolean InsertarFactura(Factura miFactura) {
        boolean insertar = false;
        Connection conn = conect.getConn();

        try {
            String querySql = "INSERT INTO facturas (numero_factura, total_factura, fecha_factura, pedidos_id_pedido, clientes_id_cliente) VALUES (?, ?, ?, ?, ?)";
            PreparedStatement ps = conn.prepareStatement(querySql);
            ps.setString(1, miFactura.getNumeroFactura());
            ps.setDouble(2, miFactura.getTotalFactura());
            ps.setDate(3, miFactura.getFechaFactura());
            ps.setInt(4, miFactura.getPedidosIdPedido());
            ps.setInt(5, miFactura.getClientesIdCliente());

            ps.executeUpdate();
            insertar = true;
            System.out.println("Dato Insertado");

        } catch (Exception e) {
            System.out.println("Error al Insertar Factura: " + e.getMessage());
        }
        return insertar;
    }

    public boolean actualizarFactura(Factura miFactura) {
        boolean actualizar = false;
        Connection conn = conect.getConn();

        try {
            String querySql = "UPDATE facturas SET numero_factura = ?, total_factura = ?, fecha_factura = ? WHERE id_factura = ?";
            PreparedStatement ps = conn.prepareStatement(querySql);
            ps.setString(1, miFactura.getNumeroFactura());
            ps.setDouble(2, miFactura.getTotalFactura());
            ps.setDate(3, miFactura.getFechaFactura());
            ps.setInt(4, miFactura.getIdFactura());

            if (ps.executeUpdate() > 0) {
                actualizar = true;
                System.out.println("datos corregidos");
            }
        } catch (Exception e) {
            System.out.println("Error al actualizar la Factura: " + e.getMessage());
        }
        return actualizar;
    }

    public boolean inactivarFactura(int id) {
        boolean inactivar = false;
        String querySql = "UPDATE facturas SET estado = 0 WHERE id_factura = ?";
        Connection conn = conect.getConn();

        try {
            PreparedStatement ps = conn.prepareStatement(querySql);
            ps.setInt(1, id);
            if (ps.executeUpdate() > 0) {
                inactivar = true;
            }
        } catch (Exception e) {
            System.out.println("Error al inactivar factura: " + e.getMessage());
        }

        return inactivar;
    }

    public boolean ActivarFactura(int id) {
        boolean Activar = false;
        String querySql = "UPDATE facturas SET estado = 1 WHERE id_factura = ?";
        Connection conn = conect.getConn();

        try {
            PreparedStatement ps = conn.prepareStatement(querySql);
            ps.setInt(1, id);
            if (ps.executeUpdate() > 0) {
                Activar = true;
            }
        } catch (Exception e) {
            System.out.println("Error al Activar factura: " + e.getMessage());
        }

        return Activar;
    }

    public boolean eliminarFactura(int id) {
        boolean eliminar = false;
        String querySql = "DELETE FROM facturas WHERE id_factura = ?";
        Connection conn = conect.getConn();

        try {
            PreparedStatement ps = conn.prepareStatement(querySql);
            ps.setInt(1, id);
            if (ps.executeUpdate() > 0) {
                eliminar = true;
            }
        } catch (SQLException e) {
            System.out.println("Error al eliminar factura: " + e.getMessage());
        }
        return eliminar;
    }
}
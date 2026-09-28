package Controlador;

import Controlador.Conexion;
import Modelo.Pago;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class PagoDAO {
    Connection con;
    PreparedStatement ps;
    ResultSet rs;
    Conexion cn = new Conexion();

    public boolean insertar(Pago p) {
        String sql = "INSERT INTO pagos (facturas_id_factura, metodos_pago_id_metodo_pago, monto, fecha_pago, estado) VALUES (?, ?, ?, ?, ?)";
        try {
            con = cn.getConn();
            ps = con.prepareStatement(sql);
            ps.setInt(1, p.getIdFactura());
            ps.setInt(2, p.getIdMetodoPago());
            ps.setDouble(3, p.getMonto());
            ps.setString(4, p.getFechaPago());
            ps.setInt(5, p.getEstado());
            ps.execute();
            return true;
        } catch (SQLException e) {
            System.out.println("Error al insertar: " + e.getMessage());
            return false;
        }
    }

    public boolean actualizar(Pago p) {
        String sql = "UPDATE pagos SET facturas_id_factura=?, metodos_pago_id_metodo_pago=?, monto=?, fecha_pago=?, estado=? WHERE id_pago=?";
        try {
            con = cn.getConn();
            ps = con.prepareStatement(sql);
            ps.setInt(1, p.getIdFactura());
            ps.setInt(2, p.getIdMetodoPago());
            ps.setDouble(3, p.getMonto());
            ps.setString(4, p.getFechaPago());
            ps.setInt(5, p.getEstado());
            ps.setInt(6, p.getIdPago());
            ps.execute();
            return true;
        } catch (SQLException e) {
            System.out.println("Error al actualizar: " + e.getMessage());
            return false;
        }
    }

    public Pago consultar(int id) {
        String sql = "SELECT * FROM pagos WHERE id_pago = ?";
        Pago p = new Pago();
        try {
            con = cn.getConn();
            ps = con.prepareStatement(sql);
            ps.setInt(1, id);
            rs = ps.executeQuery();
            if (rs.next()) {
                p.setIdPago(rs.getInt("id_pago"));
                p.setIdFactura(rs.getInt("facturas_id_factura"));
                p.setIdMetodoPago(rs.getInt("metodos_pago_id_metodo_pago"));
                p.setMonto(rs.getDouble("monto"));
                p.setFechaPago(rs.getString("fecha_pago"));
                p.setEstado(rs.getInt("estado"));
            }
        } catch (SQLException e) {
            System.out.println("Error al consultar: " + e.getMessage());
        }
        return p;
    }

    public boolean inactivar(int id) {
        String sql = "UPDATE pagos SET estado = 0 WHERE id_pago = ?";
        try {
            con = cn.getConn();
            ps = con.prepareStatement(sql);
            ps.setInt(1, id);
            ps.execute();
            return true;
        } catch (SQLException e) {
            System.out.println("Error al inactivar: " + e.getMessage());
            return false;
        }
    }

    public boolean activar(int id) {
        String sql = "UPDATE pagos SET estado = 1 WHERE id_pago = ?";
        try {
            con = cn.getConn();
            ps = con.prepareStatement(sql);
            ps.setInt(1, id);
            ps.execute();
            return true;
        } catch (SQLException e) {
            System.out.println("Error al activar: " + e.getMessage());
            return false;
        }
    }
}
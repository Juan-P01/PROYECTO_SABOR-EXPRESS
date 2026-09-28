package Controlador;

import Controlador.Conexion;
import Modelo.MetodoPago;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class MetodoPagoDAO {
    Connection con;
    PreparedStatement ps;
    ResultSet rs;
    Conexion cn = new Conexion();

    public boolean insertar(MetodoPago mp) {
        String sql = "INSERT INTO metodos_pago (tipo_pago, estado) VALUES (?, ?)";
        try {
            con = cn.getConn();
            ps = con.prepareStatement(sql);
            ps.setString(1, mp.getNombre());
            ps.setInt(2, mp.getEstado());
            ps.execute();
            return true;
        } catch (SQLException e) {
            System.out.println("Error al insertar: " + e.getMessage());
            return false;
        }
    }

    public boolean actualizar(MetodoPago mp) {
        String sql = "UPDATE metodos_pago SET tipo_pago=?, estado=? WHERE id_metodo_pago=?";
        try {
            con = cn.getConn();
            ps = con.prepareStatement(sql);
            ps.setString(1, mp.getNombre());
            ps.setInt(2, mp.getEstado());
            ps.setInt(3, mp.getIdMetodoPago());
            ps.execute();
            return true;
        } catch (SQLException e) {
            System.out.println("Error al actualizar: " + e.getMessage());
            return false;
        }
    }

    public MetodoPago consultar(int idMetodoPago) {
        String sql = "SELECT * FROM metodos_pago WHERE id_metodo_pago = ?";
        MetodoPago mp = new MetodoPago();
        try {
            con = cn.getConn();
            ps = con.prepareStatement(sql);
            ps.setInt(1, idMetodoPago);
            rs = ps.executeQuery();
            if (rs.next()) {
                mp.setIdMetodoPago(rs.getInt("id_metodo_pago"));
                mp.setNombre(rs.getString("tipo_pago"));
                mp.setEstado(rs.getInt("estado"));
            }
        } catch (SQLException e) {
            System.out.println("Error al consultar: " + e.getMessage());
        }
        return mp;
    }

    public boolean inactivar(int idMetodoPago) {
        String sql = "UPDATE metodos_pago SET estado = 0 WHERE id_metodo_pago = ?";
        try {
            con = cn.getConn();
            ps = con.prepareStatement(sql);
            ps.setInt(1, idMetodoPago);
            ps.execute();
            return true;
        } catch (SQLException e) {
            System.out.println("Error al inactivar: " + e.getMessage());
            return false;
        }
    }

    public boolean activar(int idMetodoPago) {
        String sql = "UPDATE metodos_pago SET estado = 1 WHERE id_metodo_pago = ?";
        try {
            con = cn.getConn();
            ps = con.prepareStatement(sql);
            ps.setInt(1, idMetodoPago);
            ps.execute();
            return true;
        } catch (SQLException e) {
            System.out.println("Error al activar: " + e.getMessage());
            return false;
        }
    }
}
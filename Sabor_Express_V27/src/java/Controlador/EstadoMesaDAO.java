package Controlador;

import Controlador.Conexion;
import Modelo.EstadoMesa;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class EstadoMesaDAO {
    Connection con;
    PreparedStatement ps;
    ResultSet rs;
    Conexion cn = new Conexion();

    public boolean insertar(EstadoMesa em) {
        String sql = "INSERT INTO estados_mesas (descripcion_estado, estado) VALUES (?, ?)";
        try {
            con = cn.getConn();
            ps = con.prepareStatement(sql);
            ps.setString(1, em.getNombre());
            ps.setInt(2, em.getEstado());
            ps.execute();
            return true;
        } catch (SQLException e) {
            System.out.println("Error al insertar: " + e.getMessage());
            return false;
        }
    }

    public boolean actualizar(EstadoMesa em) {
        String sql = "UPDATE estados_mesas SET descripcion_estado=?, estado=? WHERE id_estado_mesa=?";
        try {
            con = cn.getConn();
            ps = con.prepareStatement(sql);
            ps.setString(1, em.getNombre());
            ps.setInt(2, em.getEstado());
            ps.setInt(3, em.getIdEstadoMesa());
            ps.execute();
            return true;
        } catch (SQLException e) {
            System.out.println("Error al actualizar: " + e.getMessage());
            return false;
        }
    }

    public EstadoMesa consultar(int idEstadoMesa) {
        String sql = "SELECT * FROM estados_mesas WHERE id_estado_mesa = ?";
        EstadoMesa em = new EstadoMesa();
        try {
            con = cn.getConn();
            ps = con.prepareStatement(sql);
            ps.setInt(1, idEstadoMesa);
            rs = ps.executeQuery();
            if (rs.next()) {
                em.setIdEstadoMesa(rs.getInt("id_estado_mesa"));
                em.setNombre(rs.getString("descripcion_estado"));
                em.setEstado(rs.getInt("estado"));
            }
        } catch (SQLException e) {
            System.out.println("Error al consultar: " + e.getMessage());
        }
        return em;
    }

    public boolean inactivar(int idEstadoMesa) {
        String sql = "UPDATE estados_mesas SET estado = 0 WHERE id_estado_mesa = ?";
        try {
            con = cn.getConn();
            ps = con.prepareStatement(sql);
            ps.setInt(1, idEstadoMesa);
            ps.execute();
            return true;
        } catch (SQLException e) {
            System.out.println("Error al inactivar: " + e.getMessage());
            return false;
        }
    }

    public boolean activar(int idEstadoMesa) {
        String sql = "UPDATE estados_mesas SET estado = 1 WHERE id_estado_mesa = ?";
        try {
            con = cn.getConn();
            ps = con.prepareStatement(sql);
            ps.setInt(1, idEstadoMesa);
            ps.execute();
            return true;
        } catch (SQLException e) {
            System.out.println("Error al activar: " + e.getMessage());
            return false;
        }
    }
}
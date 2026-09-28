package Controlador;

import Controlador.Conexion;
import Modelo.RolHasPermiso;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class RolHasPermisoDAO {
    Connection con;
    PreparedStatement ps;
    ResultSet rs;
    Conexion cn = new Conexion();

    public boolean insertar(RolHasPermiso rp) {
        String sql = "INSERT INTO roles_has_permisos (roles_id_rol, permisos_id_permisos, estado) VALUES (?, ?, ?)";
        try {
            con = cn.getConn();
            ps = con.prepareStatement(sql);
            ps.setInt(1, rp.getIdRol());
            ps.setInt(2, rp.getIdPermiso());
            ps.setInt(3, rp.getEstado());
            ps.execute();
            return true;
        } catch (SQLException e) {
            System.out.println("Error al insertar: " + e.getMessage());
            return false;
        }
    }

    public boolean actualizar(RolHasPermiso rp, int idRolAntiguo, int idPermisoAntiguo) {
        String sql = "UPDATE roles_has_permisos SET roles_id_rol=?, permisos_id_permisos=?, estado=? WHERE roles_id_rol=? AND permisos_id_permisos=?";
        try {
            con = cn.getConn();
            ps = con.prepareStatement(sql);
            ps.setInt(1, rp.getIdRol());
            ps.setInt(2, rp.getIdPermiso());
            ps.setInt(3, rp.getEstado());
            ps.setInt(4, idRolAntiguo);
            ps.setInt(5, idPermisoAntiguo);
            ps.execute();
            return true;
        } catch (SQLException e) {
            System.out.println("Error al actualizar: " + e.getMessage());
            return false;
        }
    }

    public RolHasPermiso consultar(int idRol, int idPermiso) {
        String sql = "SELECT * FROM roles_has_permisos WHERE roles_id_rol = ? AND permisos_id_permisos = ?";
        RolHasPermiso rp = new RolHasPermiso();
        try {
            con = cn.getConn();
            ps = con.prepareStatement(sql);
            ps.setInt(1, idRol);
            ps.setInt(2, idPermiso);
            rs = ps.executeQuery();
            if (rs.next()) {
                rp.setIdRol(rs.getInt("roles_id_rol"));
                rp.setIdPermiso(rs.getInt("permisos_id_permisos"));
                rp.setEstado(rs.getInt("estado"));
            }
        } catch (SQLException e) {
            System.out.println("Error al consultar: " + e.getMessage());
        }
        return rp;
    }

    public boolean inactivar(int idRol, int idPermiso) {
        String sql = "UPDATE roles_has_permisos SET estado = 0 WHERE roles_id_rol = ? AND permisos_id_permisos = ?";
        try {
            con = cn.getConn();
            ps = con.prepareStatement(sql);
            ps.setInt(1, idRol);
            ps.setInt(2, idPermiso);
            ps.execute();
            return true;
        } catch (SQLException e) {
            System.out.println("Error al inactivar: " + e.getMessage());
            return false;
        }
    }

    public boolean activar(int idRol, int idPermiso) {
        String sql = "UPDATE roles_has_permisos SET estado = 1 WHERE roles_id_rol = ? AND permisos_id_permisos = ?";
        try {
            con = cn.getConn();
            ps = con.prepareStatement(sql);
            ps.setInt(1, idRol);
            ps.setInt(2, idPermiso);
            ps.execute();
            return true;
        } catch (SQLException e) {
            System.out.println("Error al activar: " + e.getMessage());
            return false;
        }
    }
}
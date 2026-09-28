package Controlador;

import Modelo.Permiso;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class PermisoDAO {

    private Conexion conect = new Conexion();

    public Permiso consultarPermiso(int idPermiso) {
        Permiso miPermiso = null;
        Connection conn = conect.getConn();

        try {
            String querySql = "SELECT id_permisos, descripcion, estado FROM permisos WHERE id_permisos = ?";
            PreparedStatement ps = conn.prepareStatement(querySql);
            ps.setInt(1, idPermiso);

            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                miPermiso = new Permiso();
                miPermiso.setIdPermisos(rs.getInt("id_permisos"));
                miPermiso.setDescripcion(rs.getString("descripcion"));
                miPermiso.setEstado(rs.getInt("estado"));
            }
            return miPermiso;
        } catch (Exception e) {
            System.out.println(e.getMessage());
            return miPermiso;
        }
    }

    public boolean InsertarPermiso(Permiso miPermiso) {
        boolean insertar = false;
        Connection conn = conect.getConn();

        try {
            String querySql = "INSERT INTO permisos (descripcion) VALUES (?)";
            PreparedStatement ps = conn.prepareStatement(querySql);
            ps.setString(1, miPermiso.getDescripcion());

            ps.executeUpdate();
            insertar = true;
            System.out.println("Dato Insertado");

        } catch (Exception e) {
            System.out.println("Error al Insertar Permiso: " + e.getMessage());
        }
        return insertar;
    }

    public boolean actualizarPermiso(Permiso miPermiso) {
        boolean actualizar = false;
        Connection conn = conect.getConn();

        try {
            String querySql = "UPDATE permisos SET descripcion = ? WHERE id_permisos = ?";
            PreparedStatement ps = conn.prepareStatement(querySql);
            ps.setString(1, miPermiso.getDescripcion());
            ps.setInt(2, miPermiso.getIdPermisos());

            if (ps.executeUpdate() > 0) {
                actualizar = true;
                System.out.println("datos corregidos");
            }
        } catch (Exception e) {
            System.out.println("Error al actualizar el Permiso: " + e.getMessage());
        }
        return actualizar;
    }

    public boolean inactivarPermiso(int id) {
        boolean inactivar = false;
        String querySql = "UPDATE permisos SET estado = 0 WHERE id_permisos = ?";
        Connection conn = conect.getConn();

        try {
            PreparedStatement ps = conn.prepareStatement(querySql);
            ps.setInt(1, id);
            if (ps.executeUpdate() > 0) {
                inactivar = true;
            }
        } catch (Exception e) {
            System.out.println("Error al inactivar permiso: " + e.getMessage());
        }

        return inactivar;
    }

    public boolean ActivarPermiso(int id) {
        boolean Activar = false;
        String querySql = "UPDATE permisos SET estado = 1 WHERE id_permisos = ?";
        Connection conn = conect.getConn();

        try {
            PreparedStatement ps = conn.prepareStatement(querySql);
            ps.setInt(1, id);
            if (ps.executeUpdate() > 0) {
                Activar = true;
            }
        } catch (Exception e) {
            System.out.println("Error al Activar permiso: " + e.getMessage());
        }

        return Activar;
    }

    public boolean eliminarPermiso(int id) {
        boolean eliminar = false;
        String querySql = "DELETE FROM permisos WHERE id_permisos = ?";
        Connection conn = conect.getConn();

        try {
            PreparedStatement ps = conn.prepareStatement(querySql);
            ps.setInt(1, id);
            if (ps.executeUpdate() > 0) {
                eliminar = true;
            }
        } catch (SQLException e) {
            System.out.println("Error al eliminar permiso: " + e.getMessage());
        }
        return eliminar;
    }
}
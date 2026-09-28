package Controlador;

import Modelo.Rol;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class RolDAO {

    private Conexion conect = new Conexion();

    
    public Rol consultarRol(int idRol) {
        Rol miRol = null;
        Connection conn = conect.getConn();

        try {
            String querySql = "SELECT id_rol, tipo_de_rol, estado FROM roles WHERE id_rol = ?";

            PreparedStatement ps = conn.prepareStatement(querySql);
            ps.setInt(1, idRol);

            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                miRol = new Rol();

                miRol.setIdRol(rs.getInt("id_rol"));
                miRol.setTipoDeRol(rs.getString("tipo_de_rol"));
                miRol.setEstado(rs.getInt("estado"));
            }

            return miRol;

        } catch (Exception e) {
            System.out.println("Error al consultar Rol: " + e.getMessage());
            return miRol;
        }
    }

    
    public boolean InsertarRol(Rol miRol) {
        boolean insertar = false;
        Connection conn = conect.getConn();

        try {
            String querySql = "INSERT INTO roles (tipo_de_rol, estado) VALUES (?, ?)";

            PreparedStatement ps = conn.prepareStatement(querySql);

            ps.setString(1, miRol.getTipoDeRol());
            ps.setInt(2, miRol.getEstado());

            ps.executeUpdate();

            insertar = true;
            System.out.println("Dato Insertado");

        } catch (Exception e) {
            System.out.println("Error al Insertar Rol: " + e.getMessage());
        }

        return insertar;
    }

    
    public boolean actualizarRol(Rol miRol) {
        boolean actualizar = false;
        Connection conn = conect.getConn();

        try {
            String querySql = "UPDATE roles SET tipo_de_rol = ?, estado = ? WHERE id_rol = ?";

            PreparedStatement ps = conn.prepareStatement(querySql);

            ps.setString(1, miRol.getTipoDeRol());
            ps.setInt(2, miRol.getEstado());
            ps.setInt(3, miRol.getIdRol());

            if (ps.executeUpdate() > 0) {
                actualizar = true;
                System.out.println("Datos corregidos");
            }

        } catch (Exception e) {
            System.out.println("Error al actualizar el Rol: " + e.getMessage());
        }

        return actualizar;
    }

    
    public boolean inactivarRol(int id) {
        boolean inactivar = false;

        String querySql = "UPDATE roles SET estado = 0 WHERE id_rol = ?";
        Connection conn = conect.getConn();

        try {
            PreparedStatement ps = conn.prepareStatement(querySql);

            ps.setInt(1, id);

            if (ps.executeUpdate() > 0) {
                inactivar = true;
            }

        } catch (Exception e) {
            System.out.println("Error al inactivar rol: " + e.getMessage());
        }

        return inactivar;
    }

    
    public boolean ActivarRol(int id) {
        boolean activar = false;

        String querySql = "UPDATE roles SET estado = 1 WHERE id_rol = ?";
        Connection conn = conect.getConn();

        try {
            PreparedStatement ps = conn.prepareStatement(querySql);

            ps.setInt(1, id);

            if (ps.executeUpdate() > 0) {
                activar = true;
            }

        } catch (Exception e) {
            System.out.println("Error al Activar rol: " + e.getMessage());
        }

        return activar;
    }

    
    public boolean eliminarRol(int id) {
        boolean eliminar = false;

        String querySql = "DELETE FROM roles WHERE id_rol = ?";
        Connection conn = conect.getConn();

        try {
            PreparedStatement ps = conn.prepareStatement(querySql);

            ps.setInt(1, id);

            if (ps.executeUpdate() > 0) {
                eliminar = true;
            }

        } catch (SQLException e) {
            System.out.println("Error al eliminar rol: " + e.getMessage());
        }

        return eliminar;
    }
}
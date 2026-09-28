  
                                                                                                     
                                                                                          
   
package Controlador;

import Modelo.TipoDocumento;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class TipoDocumentoDAO {

    private Conexion conect = new Conexion();

    public TipoDocumento consultarTipoDocumento(int idTipoDocumento) {
        TipoDocumento miTipoDoc = null;
        Connection conn = conect.getConn();

        try {
            String querySql = "SELECT id_tipo_documento, descripcion_tipo_documento, estado "
                    + "FROM tipos_documento WHERE id_tipo_documento = ?";

            PreparedStatement ps = conn.prepareStatement(querySql);
            ps.setInt(1, idTipoDocumento);

            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                miTipoDoc = new TipoDocumento();
                miTipoDoc.setIdTipoDocumento(rs.getInt("id_tipo_documento"));
                miTipoDoc.setDescripcionTipoDocumento(rs.getString("descripcion_tipo_documento"));
                miTipoDoc.setEstado(rs.getBoolean("estado"));
            }
            return miTipoDoc;
        } catch (Exception e) {
            System.out.println("Error al consultar tipo de documento: " + e.getMessage());
            return miTipoDoc;
        }
    }

    public boolean insertarTipoDocumento(TipoDocumento miTipoDoc) {
        boolean insertar = false;
        Connection conn = conect.getConn();

        try {
            String querySql = "INSERT INTO tipos_documento (descripcion_tipo_documento, estado) VALUES (?, ?)";

            PreparedStatement ps = conn.prepareStatement(querySql);
            ps.setString(1, miTipoDoc.getDescripcionTipoDocumento());
            ps.setBoolean(2, miTipoDoc.isEstado());

            ps.executeUpdate();
            insertar = true;
            System.out.println("Tipo de documento insertado correctamente");

        } catch (Exception e) {
            System.out.println("Error al insertar tipo de documento: " + e.getMessage());
        }
        return insertar;
    }

    public boolean actualizarTipoDocumento(TipoDocumento miTipoDoc) {
        boolean actualizar = false;
        Connection conn = conect.getConn();

        try {
            String querySql = "UPDATE tipos_documento SET descripcion_tipo_documento = ?, estado = ? "
                    + "WHERE id_tipo_documento = ?";

            PreparedStatement ps = conn.prepareStatement(querySql);
            ps.setString(1, miTipoDoc.getDescripcionTipoDocumento());
            ps.setBoolean(2, miTipoDoc.isEstado());
            ps.setInt(3, miTipoDoc.getIdTipoDocumento());

            if (ps.executeUpdate() > 0) {
                actualizar = true;
                System.out.println("Tipo de documento actualizado con éxito");
            }
        } catch (Exception e) {
            System.out.println("Error al actualizar tipo de documento: " + e.getMessage());
        }
        return actualizar;
    }

    public boolean inactivarTipoDocumento(int id) {
        boolean inactivar = false;
        String querySql = "UPDATE tipos_documento SET estado = 0 WHERE id_tipo_documento = ?";
        Connection conn = conect.getConn();

        try {
            PreparedStatement ps = conn.prepareStatement(querySql);
            ps.setInt(1, id);
            if (ps.executeUpdate() > 0) {
                inactivar = true;
            }
        } catch (Exception e) {
            System.out.println("Error al inactivar tipo de documento: " + e.getMessage());
        }

        return inactivar;
    }

    public boolean activarTipoDocumento(int id) {
        boolean activar = false;
        String querySql = "UPDATE tipos_documento SET estado = 1 WHERE id_tipo_documento = ?";
        Connection conn = conect.getConn();

        try {
            PreparedStatement ps = conn.prepareStatement(querySql);
            ps.setInt(1, id);
            if (ps.executeUpdate() > 0) {
                activar = true;
            }
        } catch (Exception e) {
            System.out.println("Error al activar tipo de documento: " + e.getMessage());
        }

        return activar;
    }

    public boolean eliminarTipoDocumento(int id) {
        boolean eliminar = false;
        String querySql = "DELETE FROM tipos_documento WHERE id_tipo_documento = ?";
        Connection conn = conect.getConn();

        try {
            PreparedStatement ps = conn.prepareStatement(querySql);
            ps.setInt(1, id);
            if (ps.executeUpdate() > 0) {
                eliminar = true;
            }
        } catch (SQLException e) {
            System.out.println("Error al eliminar tipo de documento: " + e.getMessage());
        }
        return eliminar;
    }
}
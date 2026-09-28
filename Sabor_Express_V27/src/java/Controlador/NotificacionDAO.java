package Controlador;

import Modelo.Notificacion;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class NotificacionDAO {

    private Conexion conect = new Conexion();

    public Notificacion consultarNotificacion(int idNotificacion) {
        Notificacion miNotificacion = null;
        Connection conn = conect.getConn();

        try {
            String querySql = "SELECT id_notificacion, titulo, mensaje, fecha_envio, leido, usuarios_id_usuario, estado FROM notificaciones WHERE id_notificacion = ?";
            PreparedStatement ps = conn.prepareStatement(querySql);
            ps.setInt(1, idNotificacion);

            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                miNotificacion = new Notificacion();
                miNotificacion.setIdNotificacion(rs.getInt("id_notificacion"));
                miNotificacion.setTitulo(rs.getString("titulo"));
                miNotificacion.setMensaje(rs.getString("mensaje"));
                miNotificacion.setFechaEnvio(rs.getTimestamp("fecha_envio").toInstant());
                miNotificacion.setLeido(rs.getBoolean("leido"));
                miNotificacion.setUsuariosIdUsuario(rs.getInt("usuarios_id_usuario"));
                miNotificacion.setEstado(rs.getInt("estado"));
            }
            return miNotificacion;
        } catch (Exception e) {
            System.out.println(e.getMessage());
            return miNotificacion;
        }
    }

    public boolean InsertarNotificacion(Notificacion miNotificacion) {
        boolean insertar = false;
        Connection conn = conect.getConn();

        try {
            String querySql = "INSERT INTO notificaciones (titulo, mensaje, fecha_envio, leido, usuarios_id_usuario) VALUES (?, ?, ?, ?, ?)";
            PreparedStatement ps = conn.prepareStatement(querySql);
            ps.setString(1, miNotificacion.getTitulo());
            ps.setString(2, miNotificacion.getMensaje());
            ps.setTimestamp(3, miNotificacion.getFechaEnvio() != null ? java.sql.Timestamp.from(miNotificacion.getFechaEnvio()) : null);
            ps.setBoolean(4, miNotificacion.isLeido());
            ps.setInt(5, miNotificacion.getUsuariosIdUsuario());

            ps.executeUpdate();
            insertar = true;
            System.out.println("Dato Insertado");

        } catch (Exception e) {
            System.out.println("Error al Insertar Notificacion: " + e.getMessage());
        }
        return insertar;
    }

    public boolean actualizarNotificacion(Notificacion miNotificacion) {
        boolean actualizar = false;
        Connection conn = conect.getConn();

        try {
            String querySql = "UPDATE notificaciones SET titulo = ?, mensaje = ?, leido = ? WHERE id_notificacion = ?";
            PreparedStatement ps = conn.prepareStatement(querySql);
            ps.setString(1, miNotificacion.getTitulo());
            ps.setString(2, miNotificacion.getMensaje());
            ps.setBoolean(3, miNotificacion.isLeido());
            ps.setInt(4, miNotificacion.getIdNotificacion());

            if (ps.executeUpdate() > 0) {
                actualizar = true;
                System.out.println("datos corregidos");
            }
        } catch (Exception e) {
            System.out.println("Error al actualizar la Notificacion: " + e.getMessage());
        }
        return actualizar;
    }

    public boolean inactivarNotificacion(int id) {
        boolean inactivar = false;
        String querySql = "UPDATE notificaciones SET estado = 0 WHERE id_notificacion = ?";
        Connection conn = conect.getConn();

        try {
            PreparedStatement ps = conn.prepareStatement(querySql);
            ps.setInt(1, id);
            if (ps.executeUpdate() > 0) {
                inactivar = true;
            }
        } catch (Exception e) {
            System.out.println("Error al inactivar notificacion: " + e.getMessage());
        }

        return inactivar;
    }

    public boolean ActivarNotificacion(int id) {
        boolean Activar = false;
        String querySql = "UPDATE notificaciones SET estado = 1 WHERE id_notificacion = ?";
        Connection conn = conect.getConn();

        try {
            PreparedStatement ps = conn.prepareStatement(querySql);
            ps.setInt(1, id);
            if (ps.executeUpdate() > 0) {
                Activar = true;
            }
        } catch (Exception e) {
            System.out.println("Error al Activar notificacion: " + e.getMessage());
        }

        return Activar;
    }

    public boolean eliminarNotificacion(int id) {
        boolean eliminar = false;
        String querySql = "DELETE FROM notificaciones WHERE id_notificacion = ?";
        Connection conn = conect.getConn();

        try {
            PreparedStatement ps = conn.prepareStatement(querySql);
            ps.setInt(1, id);
            if (ps.executeUpdate() > 0) {
                eliminar = true;
            }
        } catch (SQLException e) {
            System.out.println("Error al eliminar notificacion: " + e.getMessage());
        }
        return eliminar;
    }
    public java.util.List<java.util.Map<String,Object>> listarNoLeidas(int usuarioId) {
        java.util.List<java.util.Map<String,Object>> salida = new java.util.ArrayList<>();
        String sql = "SELECT id_notificacion,titulo,mensaje,fecha_envio,leido FROM notificaciones WHERE usuarios_id_usuario=? AND estado=1 AND leido=0 ORDER BY fecha_envio DESC";
        try (Connection conn=conect.getConn(); PreparedStatement ps=conn.prepareStatement(sql)) {
            ps.setInt(1, usuarioId);
            try (ResultSet rs=ps.executeQuery()) {
                while(rs.next()){
                    java.util.Map<String,Object> n=new java.util.LinkedHashMap<>();
                    n.put("id",rs.getInt("id_notificacion")); n.put("titulo",rs.getString("titulo"));
                    n.put("mensaje",rs.getString("mensaje")); n.put("fecha",rs.getTimestamp("fecha_envio").toString());
                    salida.add(n);
                }
            }
        } catch(SQLException e) { throw new RuntimeException("No se pudieron consultar las notificaciones.",e); }
        return salida;
    }

    public boolean eliminarParaUsuario(int idNotificacion, int usuarioId) {
        String sql="DELETE FROM notificaciones WHERE id_notificacion=? AND usuarios_id_usuario=?";
        try(Connection conn=conect.getConn(); PreparedStatement ps=conn.prepareStatement(sql)){
            ps.setInt(1,idNotificacion); ps.setInt(2,usuarioId); return ps.executeUpdate()>0;
        } catch(SQLException e){ return false; }
    }

    public boolean eliminarTodasParaUsuario(int usuarioId) {
        String sql="DELETE FROM notificaciones WHERE usuarios_id_usuario=?";
        try(Connection conn=conect.getConn(); PreparedStatement ps=conn.prepareStatement(sql)){
            ps.setInt(1,usuarioId); ps.executeUpdate(); return true;
        } catch(SQLException e){ return false; }
    }

    public boolean marcarLeidas(int usuarioId) {
        String sql="UPDATE notificaciones SET leido=1 WHERE usuarios_id_usuario=? AND estado=1 AND leido=0";
        try(Connection conn=conect.getConn(); PreparedStatement ps=conn.prepareStatement(sql)){ps.setInt(1,usuarioId);return ps.executeUpdate()>=0;}
        catch(SQLException e){return false;}
    }

}

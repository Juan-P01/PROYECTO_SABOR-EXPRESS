  
                                                                                                     
                                                                                          
   
package Controlador;

import Modelo.HistorialPuntos;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;

public class HistorialPuntosDAO {

    private Conexion conect = new Conexion();

    public HistorialPuntos consultarHistorialPuntos(int idHistorialPuntos) {
        HistorialPuntos miHistorial = null;
        Connection conn = conect.getConn();

        try {
            String querySql = "SELECT id_historial_puntos, puntos, fecha_movimiento, tipo_de_movimiento, "
                    + "puntos_restantes, clientes_id_cliente, estado "
                    + "FROM historiales_puntos WHERE id_historial_puntos = ?";

            PreparedStatement ps = conn.prepareStatement(querySql);
            ps.setInt(1, idHistorialPuntos);

            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                miHistorial = new HistorialPuntos();
                miHistorial.setIdHistorialPuntos(rs.getInt("id_historial_puntos"));
                miHistorial.setPuntos(rs.getInt("puntos"));
                miHistorial.setFechaMovimiento(rs.getTimestamp("fecha_movimiento").toInstant());
                miHistorial.setTipoDeMovimiento(rs.getString("tipo_de_movimiento"));
                miHistorial.setPuntosRestantes(rs.getInt("puntos_restantes"));
                miHistorial.setClientesIdCliente(rs.getInt("clientes_id_cliente"));
                miHistorial.setEstado(rs.getBoolean("estado"));
            }
            return miHistorial;
        } catch (Exception e) {
            System.out.println("Error al consultar historial de puntos: " + e.getMessage());
            return miHistorial;
        }
    }

    public boolean insertarHistorialPuntos(HistorialPuntos miHistorial) {
        boolean insertar = false;
        Connection conn = conect.getConn();

        try {
            String querySql = "INSERT INTO historiales_puntos (puntos, fecha_movimiento, tipo_de_movimiento, "
                    + "puntos_restantes, clientes_id_cliente, estado) VALUES (?, ?, ?, ?, ?, ?)";

            PreparedStatement ps = conn.prepareStatement(querySql);
            ps.setInt(1, miHistorial.getPuntos());
            ps.setTimestamp(2, miHistorial.getFechaMovimiento() != null ? Timestamp.from(miHistorial.getFechaMovimiento()) : Timestamp.from(java.time.Instant.now()));
            ps.setString(3, miHistorial.getTipoDeMovimiento());
            ps.setInt(4, miHistorial.getPuntosRestantes());
            ps.setInt(5, miHistorial.getClientesIdCliente());
            ps.setBoolean(6, miHistorial.isEstado());

            ps.executeUpdate();
            insertar = true;
            System.out.println("Historial de puntos insertado correctamente");

        } catch (Exception e) {
            System.out.println("Error al insertar historial de puntos: " + e.getMessage());
        }
        return insertar;
    }

    public boolean actualizarHistorialPuntos(HistorialPuntos miHistorial) {
        boolean actualizar = false;
        Connection conn = conect.getConn();

        try {
            String querySql = "UPDATE historiales_puntos SET puntos = ?, tipo_de_movimiento = ?, "
                    + "puntos_restantes = ?, clientes_id_cliente = ?, estado = ? "
                    + "WHERE id_historial_puntos = ?";

            PreparedStatement ps = conn.prepareStatement(querySql);
            ps.setInt(1, miHistorial.getPuntos());
            ps.setString(2, miHistorial.getTipoDeMovimiento());
            ps.setInt(3, miHistorial.getPuntosRestantes());
            ps.setInt(4, miHistorial.getClientesIdCliente());
            ps.setBoolean(5, miHistorial.isEstado());
            ps.setInt(6, miHistorial.getIdHistorialPuntos());

            if (ps.executeUpdate() > 0) {
                actualizar = true;
                System.out.println("Historial de puntos actualizado con éxito");
            }
        } catch (Exception e) {
            System.out.println("Error al actualizar historial de puntos: " + e.getMessage());
        }
        return actualizar;
    }

    public boolean inactivarHistorialPuntos(int id) {
        boolean inactivar = false;
        String querySql = "UPDATE historiales_puntos SET estado = 0 WHERE id_historial_puntos = ?";
        Connection conn = conect.getConn();

        try {
            PreparedStatement ps = conn.prepareStatement(querySql);
            ps.setInt(1, id);
            if (ps.executeUpdate() > 0) {
                inactivar = true;
            }
        } catch (Exception e) {
            System.out.println("Error al inactivar historial de puntos: " + e.getMessage());
        }

        return inactivar;
    }

    public boolean activarHistorialPuntos(int id) {
        boolean activar = false;
        String querySql = "UPDATE historiales_puntos SET estado = 1 WHERE id_historial_puntos = ?";
        Connection conn = conect.getConn();

        try {
            PreparedStatement ps = conn.prepareStatement(querySql);
            ps.setInt(1, id);
            if (ps.executeUpdate() > 0) {
                activar = true;
            }
        } catch (Exception e) {
            System.out.println("Error al activar historial de puntos: " + e.getMessage());
        }

        return activar;
    }

    public boolean eliminarHistorialPuntos(int id) {
        boolean eliminar = false;
        String querySql = "DELETE FROM historiales_puntos WHERE id_historial_puntos = ?";
        Connection conn = conect.getConn();

        try {
            PreparedStatement ps = conn.prepareStatement(querySql);
            ps.setInt(1, id);
            if (ps.executeUpdate() > 0) {
                eliminar = true;
            }
        } catch (SQLException e) {
            System.out.println("Error al eliminar historial de puntos: " + e.getMessage());
        }
        return eliminar;
    }
}

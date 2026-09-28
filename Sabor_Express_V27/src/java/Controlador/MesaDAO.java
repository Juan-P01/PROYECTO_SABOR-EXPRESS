package Controlador;

import Modelo.Mesa;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class MesaDAO {
    private final Conexion conect = new Conexion();

    public Mesa consultarMesa(int idMesa) {
        String sql = "SELECT m.id_mesa, m.numero, m.estados_mesas_id_estado_mesa, em.descripcion_estado, m.estado "
                + "FROM mesas m INNER JOIN estados_mesas em ON em.id_estado_mesa = m.estados_mesas_id_estado_mesa "
                + "WHERE m.id_mesa = ?";
        try (Connection conn = conect.getConn(); PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, idMesa);
            try (ResultSet rs = ps.executeQuery()) { return rs.next() ? convertir(rs) : null; }
        } catch (SQLException | NullPointerException e) {
            System.out.println("Error al consultar mesa: " + e.getMessage()); return null;
        }
    }

    public List<Mesa> listarMesas() {
        List<Mesa> lista = new ArrayList<>();
        String sql = "SELECT m.id_mesa, m.numero, m.estados_mesas_id_estado_mesa, em.descripcion_estado, m.estado "
                + "FROM mesas m INNER JOIN estados_mesas em ON em.id_estado_mesa = m.estados_mesas_id_estado_mesa "
                + "ORDER BY m.numero";
        try (Connection conn = conect.getConn(); PreparedStatement ps = conn.prepareStatement(sql); ResultSet rs = ps.executeQuery()) {
            while (rs.next()) lista.add(convertir(rs));
        } catch (SQLException | NullPointerException e) {
            System.out.println("Error al listar mesas: " + e.getMessage());
        }
        return lista;
    }

    public boolean actualizarEstadoMesa(int idMesa, int idEstadoMesa) {
        String sql = "UPDATE mesas SET estados_mesas_id_estado_mesa = ? WHERE id_mesa = ? AND estado = 1";
        try (Connection conn = conect.getConn(); PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, idEstadoMesa); ps.setInt(2, idMesa); return ps.executeUpdate() > 0;
        } catch (SQLException | NullPointerException e) {
            System.out.println("Error al actualizar estado de mesa: " + e.getMessage()); return false;
        }
    }

    
    public boolean insertarMesa(Mesa mesa) {
        String sql = "INSERT INTO mesas (numero, estados_mesas_id_estado_mesa, estado) VALUES (?, ?, ?)";
        try (Connection conn = conect.getConn(); PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, mesa.getNumeroMesa());
            ps.setInt(2, mesa.getEstadosMesasIdEstadoMesa());
            ps.setBoolean(3, mesa.isEstado());
            return ps.executeUpdate() > 0;
        } catch (SQLException | NullPointerException e) { return false; }
    }

    public boolean actualizarMesa(Mesa mesa) {
        String sql = "UPDATE mesas SET numero = ?, estados_mesas_id_estado_mesa = ?, estado = ? WHERE id_mesa = ?";
        try (Connection conn = conect.getConn(); PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, mesa.getNumeroMesa()); ps.setInt(2, mesa.getEstadosMesasIdEstadoMesa());
            ps.setBoolean(3, mesa.isEstado()); ps.setInt(4, mesa.getIdMesa()); return ps.executeUpdate() > 0;
        } catch (SQLException | NullPointerException e) { return false; }
    }

    public boolean inactivarMesa(int id) { return cambiarEstadoRegistro(id, 0); }
    public boolean activarMesa(int id) { return cambiarEstadoRegistro(id, 1); }
    public boolean eliminarMesa(int id) {
        String sql = "DELETE FROM mesas WHERE id_mesa = ?";
        try (Connection conn = conect.getConn(); PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, id); return ps.executeUpdate() > 0;
        } catch (SQLException | NullPointerException e) { return false; }
    }

    private boolean cambiarEstadoRegistro(int id, int estado) {
        String sql = "UPDATE mesas SET estado = ? WHERE id_mesa = ?";
        try (Connection conn = conect.getConn(); PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, estado); ps.setInt(2, id); return ps.executeUpdate() > 0;
        } catch (SQLException | NullPointerException e) { return false; }
    }

    private Mesa convertir(ResultSet rs) throws SQLException {
        Mesa m = new Mesa();
        m.setIdMesa(rs.getInt("id_mesa"));
        m.setNumeroMesa(rs.getInt("numero"));
        m.setEstadosMesasIdEstadoMesa(rs.getInt("estados_mesas_id_estado_mesa"));
        m.setEstadoMesa(rs.getString("descripcion_estado"));
        m.setEstado(rs.getBoolean("estado"));
        return m;
    }
}

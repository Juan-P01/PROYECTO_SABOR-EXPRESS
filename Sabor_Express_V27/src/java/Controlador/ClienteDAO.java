package Controlador;

import Modelo.Cliente;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class ClienteDAO {

    private Conexion conect = new Conexion();

    public Cliente consultarCliente(int idCliente) {
        Cliente miCliente = null;
        Connection conn = conect.getConn();

        try {
            String querySql = "SELECT id_cliente, puntos_fidelizacion, usuarios_id_usuario, estado FROM clientes WHERE id_cliente = ?";
            PreparedStatement ps = conn.prepareStatement(querySql);
            ps.setInt(1, idCliente);

            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                miCliente = new Cliente();
                miCliente.setIdCliente(rs.getInt("id_cliente"));
                miCliente.setPuntosFidelizacion(rs.getInt("puntos_fidelizacion"));
                miCliente.setUsuariosIdUsuario(rs.getInt("usuarios_id_usuario"));
                miCliente.setEstado(rs.getInt("estado"));
            }
            return miCliente;
        } catch (Exception e) {
            System.out.println(e.getMessage());
            return miCliente;
        }
    }

    public boolean InsertarCliente(Cliente miCliente) {
        boolean insertar = false;
        Connection conn = conect.getConn();

        try {
            String querySql = "INSERT INTO clientes (puntos_fidelizacion, usuarios_id_usuario) VALUES (?, ?)";
            PreparedStatement ps = conn.prepareStatement(querySql);
            ps.setInt(1, miCliente.getPuntosFidelizacion());
            ps.setInt(2, miCliente.getUsuariosIdUsuario());

            ps.executeUpdate();
            insertar = true;
            System.out.println("Dato Insertado");

        } catch (Exception e) {
            System.out.println("Error al Insertar Cliente: " + e.getMessage());
        }
        return insertar;
    }

    public boolean actualizarCliente(Cliente miCliente) {
        boolean actualizar = false;
        Connection conn = conect.getConn();

        try {
            String querySql = "UPDATE clientes SET puntos_fidelizacion = ?, usuarios_id_usuario = ? WHERE id_cliente = ?";
            PreparedStatement ps = conn.prepareStatement(querySql);
            ps.setInt(1, miCliente.getPuntosFidelizacion());
            ps.setInt(2, miCliente.getUsuariosIdUsuario());
            ps.setInt(3, miCliente.getIdCliente());

            if (ps.executeUpdate() > 0) {
                actualizar = true;
                System.out.println("datos corregidos");
            }
        } catch (Exception e) {
            System.out.println("Error al actualizar el Cliente: " + e.getMessage());
        }
        return actualizar;
    }

    public boolean inactivarCliente(int id) {
        boolean inactivar = false;
        String querySql = "UPDATE clientes SET estado = 0 WHERE id_cliente = ?";
        Connection conn = conect.getConn();

        try {
            PreparedStatement ps = conn.prepareStatement(querySql);
            ps.setInt(1, id);
            if (ps.executeUpdate() > 0) {
                inactivar = true;
            }
        } catch (Exception e) {
            System.out.println("Error al inactivar cliente: " + e.getMessage());
        }

        return inactivar;
    }

    public boolean ActivarCliente(int id) {
        boolean Activar = false;
        String querySql = "UPDATE clientes SET estado = 1 WHERE id_cliente = ?";
        Connection conn = conect.getConn();

        try {
            PreparedStatement ps = conn.prepareStatement(querySql);
            ps.setInt(1, id);
            if (ps.executeUpdate() > 0) {
                Activar = true;
            }
        } catch (Exception e) {
            System.out.println("Error al Activar cliente: " + e.getMessage());
        }

        return Activar;
    }

    public boolean eliminarCliente(int id) {
        boolean eliminar = false;
        String querySql = "DELETE FROM clientes WHERE id_cliente = ?";
        Connection conn = conect.getConn();

        try {
            PreparedStatement ps = conn.prepareStatement(querySql);
            ps.setInt(1, id);
            if (ps.executeUpdate() > 0) {
                eliminar = true;
            }
        } catch (SQLException e) {
            System.out.println("Error al eliminar cliente: " + e.getMessage());
        }
        return eliminar;
    }
}
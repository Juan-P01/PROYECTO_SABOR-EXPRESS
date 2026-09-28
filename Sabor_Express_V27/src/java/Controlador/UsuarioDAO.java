package Controlador;

import Modelo.Usuario;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import org.mindrot.jbcrypt.BCrypt;

                                                    
public class UsuarioDAO {
    private final Conexion conect = new Conexion();

    public Usuario consultarUsuarioPorId(int idUsuario) {
        String sql = "SELECT id_usuario, nombre, apellido, direccion, telefono, correo, contrasena, "
                + "nro_documento, ultimo_acceso, fecha_de_creacion, fecha_nacimiento, fecha_vencimiento_clave, "
                + "autorizacion_datos, roles_id_rol, tipos_documento_id_tipo_documento, estado "
                + "FROM usuarios WHERE id_usuario = ? AND estado = 1";
        try (Connection conn = conect.getConn(); PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, idUsuario);
            try (ResultSet rs = ps.executeQuery()) {
                return rs.next() ? convertirUsuario(rs) : null;
            }
        } catch (SQLException | NullPointerException e) {
            return null;
        }
    }

    public String obtenerNombreRol(int idRol) {
        String sql = "SELECT tipo_de_rol FROM roles WHERE id_rol = ?";
        try (Connection conn = conect.getConn(); PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, idRol);
            try (ResultSet rs = ps.executeQuery()) {
                return rs.next() ? rs.getString("tipo_de_rol") : "Usuario";
            }
        } catch (SQLException | NullPointerException e) {
            return "Usuario";
        }
    }

    public String obtenerTipoDocumento(int idTipoDocumento) {
        String sql = "SELECT descripcion_tipo_documento FROM tipos_documento WHERE id_tipo_documento = ?";
        try (Connection conn = conect.getConn(); PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, idTipoDocumento);
            try (ResultSet rs = ps.executeQuery()) {
                return rs.next() ? rs.getString("descripcion_tipo_documento") : "No especificado";
            }
        } catch (SQLException | NullPointerException e) {
            return "No especificado";
        }
    }

    public int obtenerPuntosCliente(int idUsuario) {
        String sql = "SELECT COALESCE(c.puntos_fidelizacion, 0) AS puntos "
                + "FROM clientes c WHERE c.usuarios_id_usuario = ? AND c.estado = 1";
        try (Connection conn = conect.getConn(); PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, idUsuario);
            try (ResultSet rs = ps.executeQuery()) {
                return rs.next() ? rs.getInt("puntos") : 0;
            }
        } catch (SQLException | NullPointerException e) {
            return 0;
        }
    }

    public Usuario consultarUsuario(String correo) {
        String sql = "SELECT id_usuario, nombre, apellido, direccion, telefono, correo, contrasena, "
                + "nro_documento, ultimo_acceso, fecha_de_creacion, fecha_nacimiento, fecha_vencimiento_clave, "
                + "autorizacion_datos, roles_id_rol, tipos_documento_id_tipo_documento, estado "
                + "FROM usuarios WHERE estado = 1 AND (LOWER(correo)=LOWER(?) OR LOWER(SUBSTRING_INDEX(correo,'@',1))=LOWER(?))";
        try (Connection conn = conect.getConn(); PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, correo); ps.setString(2, correo);
            try (ResultSet rs = ps.executeQuery()) { return rs.next() ? convertirUsuario(rs) : null; }
        } catch (SQLException | NullPointerException e) { return null; }
    }


                                                                                                            
    public Usuario consultarUsuarioPorCorreo(String correo) {
        String sql = "SELECT id_usuario, nombre, apellido, direccion, telefono, correo, contrasena, "
                + "nro_documento, ultimo_acceso, fecha_de_creacion, fecha_nacimiento, fecha_vencimiento_clave, "
                + "autorizacion_datos, roles_id_rol, tipos_documento_id_tipo_documento, estado "
                + "FROM usuarios WHERE LOWER(correo)=LOWER(?) OR LOWER(SUBSTRING_INDEX(correo,'@',1))=LOWER(?) LIMIT 1";
        try (Connection conn = conect.getConn(); PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, correo); ps.setString(2, correo);
            try (ResultSet rs = ps.executeQuery()) { return rs.next() ? convertirUsuario(rs) : null; }
        } catch (SQLException | NullPointerException e) { return null; }
    }
    public Usuario validarCredenciales(String correo, String contrasena) {
        Usuario usuario = consultarUsuario(correo);
        if (usuario == null || !coincideContrasena(contrasena, usuario.getContrasena())) return null;
        actualizarUltimoAcceso(usuario.getIdUsuario());
        return usuario;
    }

    public List<Usuario> listarUsuarios(String busqueda) {
        List<Usuario> lista = new ArrayList<>();
        String sql = "SELECT id_usuario, nombre, apellido, direccion, telefono, correo, contrasena, nro_documento, "
                + "ultimo_acceso, fecha_de_creacion, fecha_nacimiento, fecha_vencimiento_clave, autorizacion_datos, "
                + "roles_id_rol, tipos_documento_id_tipo_documento, estado FROM usuarios "
                + "WHERE (? = '' OR nombre LIKE ? OR apellido LIKE ? OR correo LIKE ?) ORDER BY id_usuario";
        String q = busqueda == null ? "" : busqueda.trim();
        try (Connection conn = conect.getConn(); PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, q); ps.setString(2, "%" + q + "%"); ps.setString(3, "%" + q + "%"); ps.setString(4, "%" + q + "%");
            try (ResultSet rs = ps.executeQuery()) { while (rs.next()) lista.add(convertirUsuario(rs)); }
        } catch (SQLException | NullPointerException e) { System.out.println("Error al listar usuarios: " + e.getMessage()); }
        return lista;
    }

    public boolean actualizarDesdeAdmin(Usuario u) {
        if(u==null || u.getTelefono()==null || !u.getTelefono().trim().matches("\\d{7,15}") || u.getNroDocumento()==null || !u.getNroDocumento().trim().matches("\\d{6,15}")) return false;
        String sql = "UPDATE usuarios SET nombre=?, apellido=?, direccion=?, telefono=?, correo=?, nro_documento=?, "
                + "fecha_nacimiento=?, autorizacion_datos=?, roles_id_rol=?, tipos_documento_id_tipo_documento=?, estado=? WHERE id_usuario=?";
        try (Connection conn = conect.getConn(); PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, u.getNombre().trim()); ps.setString(2, u.getApellido().trim()); ps.setString(3, u.getDireccion().trim());
            ps.setString(4, u.getTelefono().trim()); ps.setString(5, u.getCorreo().trim().toLowerCase()); ps.setString(6, u.getNroDocumento());
            ps.setTimestamp(7, Timestamp.from(u.getFechaNacimiento() == null ? Instant.now() : u.getFechaNacimiento()));
            ps.setInt(8, u.getAutorizacionDatos()); ps.setInt(9, u.getRolesIdRol()); ps.setInt(10, u.getTiposDocumentoIdTipoDocumento());
            ps.setBoolean(11, u.isEstado()); ps.setInt(12, u.getIdUsuario());
            return ps.executeUpdate() > 0;
        } catch (SQLException | NullPointerException e) { System.out.println("Error al actualizar usuario: " + e.getMessage()); return false; }
    }

    public boolean inactivarUsuario(int idUsuario) {
        String sql = "UPDATE usuarios SET estado = 0 WHERE id_usuario = ?";
        try (Connection conn = conect.getConn(); PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, idUsuario); return ps.executeUpdate() > 0;
        } catch (SQLException | NullPointerException e) { return false; }
    }

    public boolean activarUsuario(int idUsuario) {
        String sql = "UPDATE usuarios SET estado = 1 WHERE id_usuario = ?";
        try (Connection conn = conect.getConn(); PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, idUsuario); return ps.executeUpdate() > 0;
        } catch (SQLException | NullPointerException e) { return false; }
    }

                                                                                     
    public int crearDesdeAdmin(Usuario usuario) throws SQLException {
        if (usuario == null) throw new SQLException("Datos de usuario inválidos.");
        if (usuario.getTelefono()==null || !usuario.getTelefono().matches("\\d{10}")) throw new SQLException("El teléfono debe tener exactamente 10 dígitos.");
        if (usuario.getNroDocumento()==null || !usuario.getNroDocumento().matches("\\d{10}")) throw new SQLException("El documento debe tener exactamente 10 dígitos.");
        if (usuario.getContrasena()==null || usuario.getContrasena().length()<6) throw new SQLException("La contraseña debe tener mínimo 6 caracteres.");
        try(Connection conn=conect.getConn()){ if(conn==null) throw new SQLException("No hay conexión con la base de datos."); return insertarUsuario(conn,usuario); }
    }

    public boolean actualizarPerfilCliente(Usuario u) throws SQLException {
        if (u == null) throw new SQLException("Datos de usuario inválidos.");
        String sql = "UPDATE usuarios SET nombre=?, apellido=?, direccion=?, telefono=?, correo=?, nro_documento=?, tipos_documento_id_tipo_documento=? WHERE id_usuario=? AND estado=1 AND roles_id_rol=4";
        try (Connection conn = conect.getConn()) {
            if (conn == null) throw new SQLException("No hay conexión con la base de datos.");
            try (PreparedStatement ps = conn.prepareStatement(sql)) {
                ps.setString(1, u.getNombre().trim()); ps.setString(2, u.getApellido().trim());
                ps.setString(3, u.getDireccion().trim()); ps.setString(4, u.getTelefono().trim());
                ps.setString(5, u.getCorreo().trim().toLowerCase()); ps.setString(6, u.getNroDocumento().trim());
                ps.setInt(7, u.getTiposDocumentoIdTipoDocumento()); ps.setInt(8, u.getIdUsuario());
                return ps.executeUpdate() == 1;
            }
        }
    }

    public boolean existeCorreoODocumentoEnOtroUsuario(String correo, String documento, int idUsuario) throws SQLException {
        String sql = "SELECT 1 FROM usuarios WHERE estado=1 AND id_usuario<>? AND (LOWER(correo)=LOWER(?) OR nro_documento=?) LIMIT 1";
        try (Connection conn=conect.getConn()) {
            if(conn==null) throw new SQLException("No hay conexión con la base de datos.");
            try(PreparedStatement ps=conn.prepareStatement(sql)){ps.setInt(1,idUsuario);ps.setString(2,correo.trim());ps.setString(3,documento.trim());try(ResultSet rs=ps.executeQuery()){return rs.next();}}
        }
    }

    
    public boolean actualizarUsuario(Usuario usuario) { return actualizarDesdeAdmin(usuario); }
    public boolean ActivarUsuario(int idUsuario) { return activarUsuario(idUsuario); }
    public boolean InsertarUsuario(Usuario usuario) {
        Connection conn = conect.getConn(); if (conn == null) return false;
        try { return insertarUsuario(conn, usuario) > 0; } catch (SQLException e) { return false; }
    }

    public boolean registrarCliente(Usuario usuario) {
        Connection conn = conect.getConn(); if (conn == null) return false;
        try {
            boolean original = conn.getAutoCommit(); conn.setAutoCommit(false);
            try {
                int idUsuario = insertarUsuario(conn, usuario);
                try (PreparedStatement ps = conn.prepareStatement("INSERT INTO clientes (puntos_fidelizacion, usuarios_id_usuario, estado) VALUES (?, ?, 1)")) {
                    ps.setInt(1, 0); ps.setInt(2, idUsuario); ps.executeUpdate();
                }
                conn.commit(); usuario.setIdUsuario(idUsuario); return true;
            } catch (SQLException e) { conn.rollback(); return false; }
            finally { conn.setAutoCommit(original); }
        } catch (SQLException e) { return false; }
    }

    private int insertarUsuario(Connection conn, Usuario usuario) throws SQLException {
        String sql = "INSERT INTO usuarios (nombre, apellido, direccion, telefono, correo, contrasena, nro_documento, ultimo_acceso, fecha_de_creacion, fecha_nacimiento, fecha_vencimiento_clave, autorizacion_datos, roles_id_rol, tipos_documento_id_tipo_documento, estado) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, 1)";
        Instant ahora = Instant.now();
        try (PreparedStatement ps = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            ps.setString(1, usuario.getNombre().trim()); ps.setString(2, usuario.getApellido().trim()); ps.setString(3, usuario.getDireccion().trim());
            ps.setString(4, usuario.getTelefono().trim()); ps.setString(5, usuario.getCorreo().trim().toLowerCase());
            ps.setString(6, BCrypt.hashpw(usuario.getContrasena(), BCrypt.gensalt(12))); ps.setString(7, usuario.getNroDocumento());
            ps.setTimestamp(8, Timestamp.from(ahora)); ps.setTimestamp(9, Timestamp.from(ahora));
            ps.setTimestamp(10, Timestamp.from(usuario.getFechaNacimiento() != null ? usuario.getFechaNacimiento() : ahora));
            ps.setTimestamp(11, Timestamp.from(ahora.plusSeconds(365L * 24 * 60 * 60))); ps.setInt(12, usuario.getAutorizacionDatos());
            ps.setInt(13, usuario.getRolesIdRol()); ps.setInt(14, usuario.getTiposDocumentoIdTipoDocumento()); ps.executeUpdate();
            try (ResultSet claves = ps.getGeneratedKeys()) { if (claves.next()) return claves.getInt(1); }
        }
        throw new SQLException("No se generó el identificador del usuario.");
    }

    private void actualizarUltimoAcceso(int idUsuario) {
        try (Connection conn = conect.getConn(); PreparedStatement ps = conn.prepareStatement("UPDATE usuarios SET ultimo_acceso = CURRENT_TIMESTAMP WHERE id_usuario = ?")) {
            ps.setInt(1, idUsuario); ps.executeUpdate();
        } catch (SQLException | NullPointerException ignored) {}
    }

    private boolean coincideContrasena(String textoPlano, String hash) {
        try { return hash != null && BCrypt.checkpw(textoPlano, hash); } catch (IllegalArgumentException e) { return false; }
    }

    private Usuario convertirUsuario(ResultSet rs) throws SQLException {
        Usuario u = new Usuario(); u.setIdUsuario(rs.getInt("id_usuario")); u.setNombre(rs.getString("nombre")); u.setApellido(rs.getString("apellido"));
        u.setDireccion(rs.getString("direccion")); u.setTelefono(rs.getString("telefono")); u.setCorreo(rs.getString("correo")); u.setContrasena(rs.getString("contrasena"));
        u.setNroDocumento(rs.getString("nro_documento")); Timestamp ultimo = rs.getTimestamp("ultimo_acceso");
        Timestamp creacion = rs.getTimestamp("fecha_de_creacion");
        Timestamp nacimiento = rs.getTimestamp("fecha_nacimiento");
        Timestamp vencimiento = rs.getTimestamp("fecha_vencimiento_clave");
        u.setUltimoAcceso(ultimo == null ? null : ultimo.toInstant());
        u.setFechaDeCreacion(creacion == null ? null : creacion.toInstant());
        u.setFechaNacimiento(nacimiento == null ? null : nacimiento.toInstant());
        u.setFechaVencimientoClave(vencimiento == null ? null : vencimiento.toInstant());
        u.setAutorizacionDatos(rs.getInt("autorizacion_datos")); u.setRolesIdRol(rs.getInt("roles_id_rol")); u.setTiposDocumentoIdTipoDocumento(rs.getInt("tipos_documento_id_tipo_documento")); u.setEstado(rs.getBoolean("estado"));
        return u;
    }
    public boolean restablecerContrasena(String correo, String hash) {
        String sql="UPDATE usuarios SET contrasena=?, fecha_vencimiento_clave=DATE_ADD(CURRENT_TIMESTAMP,INTERVAL 1 YEAR) WHERE correo=? AND estado=1";
        try(Connection conn=conect.getConn();PreparedStatement ps=conn.prepareStatement(sql)){ps.setString(1,hash);ps.setString(2,correo);return ps.executeUpdate()==1;}catch(SQLException|NullPointerException e){return false;}
    }

}

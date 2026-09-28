package Controlador;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

                                                                              
public class AdministradorDAO {
    private final Conexion conexion = new Conexion();

    public List<Map<String, Object>> listarUsuarios(String busqueda) {
        List<Map<String, Object>> out = new ArrayList<>();
        String sql = "SELECT u.id_usuario, u.nombre, u.apellido, u.correo, u.estado, u.ultimo_acceso, u.direccion, u.telefono, u.nro_documento, u.fecha_nacimiento, u.autorizacion_datos, u.roles_id_rol, u.tipos_documento_id_tipo_documento, r.tipo_de_rol, td.descripcion_tipo_documento "
                + "FROM usuarios u LEFT JOIN roles r ON r.id_rol = u.roles_id_rol LEFT JOIN tipos_documento td ON td.id_tipo_documento = u.tipos_documento_id_tipo_documento "
                + "WHERE (? = '' OR u.nombre LIKE ? OR u.apellido LIKE ? OR u.correo LIKE ?) ORDER BY u.id_usuario";
        String q = busqueda == null ? "" : busqueda.trim();
        try (Connection c = conexion.getConn()) {
            if(c==null) throw new SQLException("No hay conexión con la base de datos.");
            try (PreparedStatement p=c.prepareStatement(sql)) {
            p.setString(1, q); p.setString(2, "%"+q+"%"); p.setString(3, "%"+q+"%"); p.setString(4, "%"+q+"%");
            try (ResultSet r = p.executeQuery()) { while (r.next()) { Map<String,Object> m = new LinkedHashMap<>();
                m.put("idUsuario", r.getInt("id_usuario")); m.put("nombre", r.getString("nombre")); m.put("apellido", r.getString("apellido"));
                m.put("correo", r.getString("correo")); m.put("estado", r.getBoolean("estado")); m.put("ultimoAcceso", r.getTimestamp("ultimo_acceso"));
                m.put("direccion", r.getString("direccion")); m.put("telefono", r.getString("telefono")); m.put("nroDocumento", r.getString("nro_documento"));
                m.put("fechaNacimiento", r.getTimestamp("fecha_nacimiento")); m.put("autorizacionDatos", r.getInt("autorizacion_datos"));
                m.put("rolesIdRol", r.getInt("roles_id_rol")); m.put("rol", r.getString("tipo_de_rol") == null ? "Sin rol" : r.getString("tipo_de_rol")); m.put("tipoDocumento", r.getString("descripcion_tipo_documento") == null ? "Sin tipo" : r.getString("descripcion_tipo_documento")); m.put("tipoDocumentoId", r.getInt("tipos_documento_id_tipo_documento")); out.add(m);
            }}
            }
        } catch (SQLException | NullPointerException e) { throw new RuntimeException("No se pudieron cargar los usuarios.", e); }
        return out;
    }

    public List<Map<String,Object>> listarRoles() { return simpleCatalogo("SELECT id_rol AS id, tipo_de_rol AS nombre FROM roles WHERE estado=1 ORDER BY id_rol"); }
    public List<Map<String,Object>> listarEstadosMesa() { return simpleCatalogo("SELECT id_estado_mesa AS id, descripcion_estado AS nombre FROM estados_mesas WHERE estado=1 ORDER BY id_estado_mesa"); }
    public List<Map<String,Object>> listarEstadosPedido() { return simpleCatalogo("SELECT id_estado_pedido AS id, detalle_estado_pedido AS nombre FROM estados_pedidos WHERE estado=1 ORDER BY id_estado_pedido"); }

    private List<Map<String,Object>> simpleCatalogo(String sql) {
        List<Map<String,Object>> out = new ArrayList<>();
        try (Connection c=conexion.getConn(); PreparedStatement p=c.prepareStatement(sql); ResultSet r=p.executeQuery()) {
            while(r.next()){ Map<String,Object> m=new LinkedHashMap<>(); m.put("id",r.getInt("id")); m.put("nombre",r.getString("nombre")); out.add(m); }
        } catch(SQLException|NullPointerException e){ throw new RuntimeException("No se pudo cargar el catálogo.",e); }
        return out;
    }

    public List<Map<String,Object>> listarPedidos(Integer estado) {
        List<Map<String,Object>> out = new ArrayList<>();
        String sql = "SELECT p.id_pedido, p.total, p.fecha_pedido, p.mesas_id_mesa, me.numero, p.clientes_id_cliente, CONCAT(u.nombre,' ',u.apellido) AS cliente, p.estados_pedidos_id_estado_pedido, ep.detalle_estado_pedido, "
                + "COALESCE(GROUP_CONCAT(CONCAT(m.nombre,' x',d.cantidad) ORDER BY d.id_detalle_pedido SEPARATOR ', '),'Sin productos') AS productos, MAX(inc.motivo) AS motivo, MAX(inc.detalle) AS detalle "
                + "FROM pedidos p INNER JOIN mesas me ON me.id_mesa=p.mesas_id_mesa INNER JOIN clientes c ON c.id_cliente=p.clientes_id_cliente "
                + "INNER JOIN usuarios u ON u.id_usuario=c.usuarios_id_usuario INNER JOIN estados_pedidos ep ON ep.id_estado_pedido=p.estados_pedidos_id_estado_pedido "
                + "LEFT JOIN detalles_pedidos d ON d.pedidos_id_pedido=p.id_pedido AND d.estado=1 LEFT JOIN menus m ON m.id_menu=d.menus_id_menu "
                + "LEFT JOIN incidencias_domicilio inc ON inc.id_incidencia=(SELECT MAX(i2.id_incidencia) FROM incidencias_domicilio i2 WHERE i2.pedido_id=p.id_pedido AND i2.estado=1) "
                + "WHERE p.estado=1 AND (? IS NULL OR p.estados_pedidos_id_estado_pedido=?) GROUP BY p.id_pedido ORDER BY p.fecha_pedido DESC";
        try (Connection c=conexion.getConn(); PreparedStatement p=c == null ? null : c.prepareStatement(sql)) {
            if(estado==null){p.setNull(1, java.sql.Types.INTEGER);p.setNull(2, java.sql.Types.INTEGER);} else {p.setInt(1,estado);p.setInt(2,estado);}
            try(ResultSet r=p.executeQuery()){while(r.next()){Map<String,Object> m=new LinkedHashMap<>();m.put("idPedido",r.getInt("id_pedido"));m.put("total",r.getBigDecimal("total"));m.put("fechaPedido",r.getTimestamp("fecha_pedido"));m.put("idMesa",r.getInt("mesas_id_mesa"));m.put("numeroMesa",r.getInt("numero"));m.put("idCliente",r.getInt("clientes_id_cliente"));m.put("cliente",r.getString("cliente"));m.put("idEstado",r.getInt("estados_pedidos_id_estado_pedido"));m.put("estado",r.getString("detalle_estado_pedido"));m.put("productos",r.getString("productos"));m.put("incidenciaMotivo",r.getString("motivo"));m.put("incidenciaDetalle",r.getString("detalle"));out.add(m);}}
        } catch(SQLException|NullPointerException e){throw new RuntimeException("No se pudieron cargar los pedidos.",e);} return out;
    }

    public Map<String,Object> dashboard() {
        Map<String,Object> out=new LinkedHashMap<>();
        out.put("ingresosMes", scalarDecimal("SELECT COALESCE(SUM(pg.monto),0) FROM pagos pg WHERE pg.confirmado=1 AND pg.estado=1 AND MONTH(pg.fecha_pago)=MONTH(CURDATE()) AND YEAR(pg.fecha_pago)=YEAR(CURDATE())"));
        out.put("pedidosHoy", scalarLong("SELECT COUNT(*) FROM pedidos WHERE estado=1 AND DATE(fecha_pedido)=CURDATE()"));
        out.put("mesasActivas", scalarLong("SELECT COUNT(*) FROM mesas WHERE estado=1"));
        out.put("clientesHoy", scalarLong("SELECT COUNT(*) FROM clientes c INNER JOIN usuarios u ON u.id_usuario=c.usuarios_id_usuario WHERE c.estado=1 AND DATE(u.fecha_de_creacion)=CURDATE()"));
        return out;
    }

    public List<Map<String,Object>> ingresosUltimosDias(int dias) { return serieDiaria("SELECT DATE(pg.fecha_pago) fecha, COALESCE(SUM(pg.monto),0) valor FROM pagos pg WHERE pg.confirmado=1 AND pg.estado=1 AND pg.fecha_pago >= DATE_SUB(CURDATE(), INTERVAL ? DAY) GROUP BY DATE(pg.fecha_pago) ORDER BY fecha", dias, "valor"); }
    public List<Map<String,Object>> pedidosPorDia(int dias) { return serieDiaria("SELECT DATE(fecha_pedido) fecha, COUNT(*) valor FROM pedidos WHERE estado=1 AND fecha_pedido >= DATE_SUB(CURDATE(), INTERVAL ? DAY) GROUP BY DATE(fecha_pedido) ORDER BY fecha", dias, "valor"); }
    public List<Map<String,Object>> clientesPorDia(int dias) { return serieDiaria("SELECT DATE(u.fecha_de_creacion) fecha, COUNT(*) valor FROM clientes c INNER JOIN usuarios u ON u.id_usuario=c.usuarios_id_usuario WHERE c.estado=1 AND u.fecha_de_creacion >= DATE_SUB(CURDATE(), INTERVAL ? DAY) GROUP BY DATE(u.fecha_de_creacion) ORDER BY fecha", dias, "valor"); }

    private List<Map<String,Object>> serieDiaria(String sql,int dias,String valueColumn){List<Map<String,Object>>out=new ArrayList<>();try(Connection c=conexion.getConn();PreparedStatement p=c.prepareStatement(sql)){p.setInt(1,dias);try(ResultSet r=p.executeQuery()){while(r.next()){Map<String,Object>m=new LinkedHashMap<>();m.put("fecha",r.getDate("fecha").toString());m.put("valor",r.getBigDecimal(valueColumn));out.add(m);}}}catch(SQLException|NullPointerException e){throw new RuntimeException("No se pudo generar la serie estadística.",e);}return out;}

    public List<Map<String,Object>> rankingPlatos(){List<Map<String,Object>>out=new ArrayList<>();String sql="SELECT m.id_menu,m.nombre,c.nombre AS categoria,SUM(d.cantidad) cantidad_vendida,COALESCE(SUM(d.subtotal),0) ingresos FROM detalles_pedidos d INNER JOIN menus m ON m.id_menu=d.menus_id_menu INNER JOIN categorias c ON c.id_categoria=m.id_categoria INNER JOIN pedidos p ON p.id_pedido=d.pedidos_id_pedido WHERE d.estado=1 AND p.estado=1 AND p.estados_pedidos_id_estado_pedido=4 GROUP BY m.id_menu,m.nombre,c.nombre ORDER BY ingresos DESC";try(Connection c=conexion.getConn();PreparedStatement p=c.prepareStatement(sql);ResultSet r=p.executeQuery()){while(r.next()){Map<String,Object>m=new LinkedHashMap<>();m.put("idMenu",r.getInt("id_menu"));m.put("nombre",r.getString("nombre"));m.put("categoria",r.getString("categoria"));m.put("cantidadVendida",r.getLong("cantidad_vendida"));m.put("ingresos",r.getBigDecimal("ingresos"));out.add(m);}}catch(SQLException|NullPointerException e){throw new RuntimeException("No se pudo generar el ranking.",e);}return out;}

    private java.math.BigDecimal scalarDecimal(String sql){try(Connection c=conexion.getConn();PreparedStatement p=c.prepareStatement(sql);ResultSet r=p.executeQuery()){if(r.next())return r.getBigDecimal(1);}catch(SQLException|NullPointerException e){throw new RuntimeException("No se pudo calcular una estadística.",e);}return java.math.BigDecimal.ZERO;}
    private long scalarLong(String sql){try(Connection c=conexion.getConn();PreparedStatement p=c.prepareStatement(sql);ResultSet r=p.executeQuery()){if(r.next())return r.getLong(1);}catch(SQLException|NullPointerException e){throw new RuntimeException("No se pudo calcular una estadística.",e);}return 0;}
    public List<Map<String,Object>> listarReservas() {
        List<Map<String,Object>> out=new ArrayList<>();
        String sql="SELECT r.id_reserva,r.fecha_reserva,r.hora_reserva,r.cantidad_personas,r.estado_reserva,r.observaciones,CONCAT(u.nombre,' ',u.apellido) cliente,u.telefono,m.numero mesa FROM reservas r INNER JOIN clientes c ON c.id_cliente=r.clientes_id_cliente INNER JOIN usuarios u ON u.id_usuario=c.usuarios_id_usuario INNER JOIN mesas m ON m.id_mesa=r.mesas_id_mesa WHERE r.estado=1 ORDER BY r.fecha_reserva,r.hora_reserva";
        try(Connection c=conexion.getConn();PreparedStatement p=c.prepareStatement(sql);ResultSet r=p.executeQuery()){
            while(r.next()){Map<String,Object>x=new LinkedHashMap<>();x.put("id",r.getInt("id_reserva"));x.put("fecha",r.getDate("fecha_reserva"));x.put("hora",r.getTime("hora_reserva"));x.put("personas",r.getInt("cantidad_personas"));x.put("estado",r.getString("estado_reserva"));x.put("observaciones",r.getString("observaciones"));x.put("cliente",r.getString("cliente"));x.put("telefono",r.getString("telefono"));x.put("mesa",r.getInt("mesa"));out.add(x);}
        }catch(SQLException|NullPointerException e){throw new RuntimeException("No se pudieron cargar las reservas.",e);}return out;
    }

    public List<Map<String,Object>> listarPagos() {
        List<Map<String,Object>> out=new ArrayList<>();
        String sql="SELECT pg.id_pago,pg.monto,pg.confirmado,pg.fecha_pago,mp.tipo_pago,f.numero_factura,p.id_pedido,CONCAT(u.nombre,' ',u.apellido) cliente FROM pagos pg INNER JOIN metodos_pago mp ON mp.id_metodo_pago=pg.metodos_pago_id_metodo_pago INNER JOIN facturas f ON f.id_factura=pg.facturas_id_factura INNER JOIN pedidos p ON p.id_pedido=f.pedidos_id_pedido INNER JOIN clientes c ON c.id_cliente=f.clientes_id_cliente INNER JOIN usuarios u ON u.id_usuario=c.usuarios_id_usuario WHERE pg.estado=1 ORDER BY pg.fecha_pago DESC";
        try(Connection c=conexion.getConn();PreparedStatement p=c.prepareStatement(sql);ResultSet r=p.executeQuery()){
            while(r.next()){Map<String,Object>x=new LinkedHashMap<>();x.put("id",r.getInt("id_pago"));x.put("monto",r.getBigDecimal("monto"));x.put("confirmado",r.getBoolean("confirmado"));x.put("fecha",r.getTimestamp("fecha_pago"));x.put("metodo",r.getString("tipo_pago"));x.put("factura",r.getString("numero_factura"));x.put("pedido",r.getInt("id_pedido"));x.put("cliente",r.getString("cliente"));out.add(x);}
        }catch(SQLException|NullPointerException e){throw new RuntimeException("No se pudieron cargar los pagos.",e);}return out;
    }

    public List<Map<String,Object>> listarFacturas() {
        List<Map<String,Object>> out=new ArrayList<>();
        String sql="SELECT f.id_factura,f.numero_factura,f.total_factura,f.fecha_factura,f.pedidos_id_pedido,CONCAT(u.nombre,' ',u.apellido) cliente FROM facturas f INNER JOIN clientes c ON c.id_cliente=f.clientes_id_cliente INNER JOIN usuarios u ON u.id_usuario=c.usuarios_id_usuario WHERE f.estado=1 ORDER BY f.fecha_factura DESC,f.id_factura DESC";
        try(Connection c=conexion.getConn();PreparedStatement p=c.prepareStatement(sql);ResultSet r=p.executeQuery()){
            while(r.next()){Map<String,Object>x=new LinkedHashMap<>();x.put("id",r.getInt("id_factura"));x.put("numero",r.getString("numero_factura"));x.put("total",r.getBigDecimal("total_factura"));x.put("fecha",r.getDate("fecha_factura"));x.put("pedido",r.getInt("pedidos_id_pedido"));x.put("cliente",r.getString("cliente"));out.add(x);}
        }catch(SQLException|NullPointerException e){throw new RuntimeException("No se pudieron cargar las facturas.",e);}return out;
    }

    public List<Map<String,Object>> listarFidelizacion() {
        List<Map<String,Object>> out=new ArrayList<>();
        String sql="SELECT c.id_cliente,CONCAT(u.nombre,' ',u.apellido) cliente,u.correo,c.puntos_fidelizacion,COALESCE((SELECT COUNT(*) FROM canjes cg WHERE cg.clientes_id_cliente=c.id_cliente AND cg.estado=1),0) canjes FROM clientes c INNER JOIN usuarios u ON u.id_usuario=c.usuarios_id_usuario WHERE c.estado=1 ORDER BY c.puntos_fidelizacion DESC";
        try(Connection c=conexion.getConn();PreparedStatement p=c.prepareStatement(sql);ResultSet r=p.executeQuery()){
            while(r.next()){Map<String,Object>x=new LinkedHashMap<>();x.put("idCliente",r.getInt("id_cliente"));x.put("cliente",r.getString("cliente"));x.put("correo",r.getString("correo"));x.put("puntos",r.getInt("puntos_fidelizacion"));x.put("canjes",r.getInt("canjes"));out.add(x);}
        }catch(SQLException|NullPointerException e){throw new RuntimeException("No se pudo cargar fidelización.",e);}return out;
    }

    public boolean actualizarEstadoReserva(int id,String estado) {
        String sql="UPDATE reservas SET estado_reserva=? WHERE id_reserva=? AND estado=1";
        try(Connection c=conexion.getConn();PreparedStatement p=c.prepareStatement(sql)){p.setString(1,estado);p.setInt(2,id);return p.executeUpdate()==1;}catch(SQLException|NullPointerException e){return false;}
    }

}

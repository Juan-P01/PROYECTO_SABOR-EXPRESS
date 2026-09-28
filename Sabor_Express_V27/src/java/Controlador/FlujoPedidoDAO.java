package Controlador;

import Modelo.Menu;
import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

                                                                                 
public class FlujoPedidoDAO {
    private static final int ESTADO_PENDIENTE = 1;
    private static final int ESTADO_PREPARACION = 2;
    private static final int ESTADO_SERVIDO = 3;
    private static final int ESTADO_LISTO = 5;
    private static final int ESTADO_CAMINO = 6;
    private static final int ESTADO_ENTREGADO = 7;
    private static final int ESTADO_PAGADO = 4;
    private static final int ESTADO_CANCELADO = 8;
    private static final int MESA_DOMICILIO = 999;
    private static final int MAX_PRODUCTOS_CLIENTE = 20;
    public static final String DIRECCION_BASE_REPARTIDOR = "Cra. 3 #3-34, Guaduas, Cundinamarca";
    private final Conexion conexion = new Conexion();

    public List<Map<String, Object>> mesasDisponibles() {
        String sql = "SELECT m.id_mesa, m.numero, em.descripcion_estado FROM mesas m "
                + "JOIN estados_mesas em ON em.id_estado_mesa=m.estados_mesas_id_estado_mesa "
                + "WHERE m.estado=1 AND m.estados_mesas_id_estado_mesa=1 AND m.numero<>? ORDER BY m.numero";
        List<Map<String, Object>> salida = new ArrayList<>();
        try (Connection c = conexion.getConn(); PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setInt(1, MESA_DOMICILIO);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    Map<String, Object> fila = new LinkedHashMap<>();
                    fila.put("id", rs.getInt("id_mesa"));
                    fila.put("numero", rs.getInt("numero"));
                    fila.put("estado", rs.getString("descripcion_estado"));
                    salida.add(fila);
                }
            }
        } catch (SQLException | NullPointerException e) { }
        return salida;
    }

    public List<Map<String, Object>> menuDisponible() {
        List<Map<String, Object>> salida = new ArrayList<>();
        for (Menu m : new MenuDAO().listar()) {
            if (m.getEstado() == 1 && m.getDisponible() == 1) {
                Map<String, Object> fila = new LinkedHashMap<>();
                fila.put("id", m.getIdMenu()); fila.put("nombre", m.getNombre());
                fila.put("descripcion", m.getDescripcion()); fila.put("categoria", m.getCategoria());
                fila.put("imagen", m.getImagen()); fila.put("precio", m.getPrecio());
                salida.add(fila);
            }
        }
        return salida;
    }

    public int crearPedido(int idUsuario, int idMesa, String tipoEntrega, String direccion,
            String metodoPago, String observaciones, Map<Integer, Integer> items) throws SQLException {
        if (items == null || items.isEmpty()) throw new SQLException("Selecciona por lo menos un producto.");
        boolean domicilio = "DOMICILIO".equals(tipoEntrega);
        if (domicilio) {
            idMesa = mesaDomicilio();
            if (direccion == null || direccion.trim().length() < 5) throw new SQLException("Ingresa una dirección de entrega válida.");
            validarCoberturaGuaduas(direccion);
        } else if (!mesaActiva(idMesa)) {
            throw new SQLException("Selecciona una mesa disponible.");
        }
        if (!"EFECTIVO".equals(metodoPago) && !"TARJETA".equals(metodoPago) && !"TRANSFERENCIA".equals(metodoPago) && !"DEMO".equals(metodoPago)) {
            throw new SQLException("Selecciona un método de pago válido.");
        }
        try (Connection c = conexion.getConn()) {
            if (c == null) throw new SQLException("No hay conexión con la base de datos.");
            boolean auto = c.getAutoCommit(); c.setAutoCommit(false);
            try {
                if (!domicilio && !bloquearMesaDisponible(c, idMesa)) {
                    throw new SQLException("La mesa seleccionada ya no está disponible. Elige otra mesa.");
                }
                int cliente = clienteDeUsuario(c, idUsuario);
                if (cliente == 0) throw new SQLException("La cuenta no está habilitada como cliente.");
                Map<Integer, Menu> productos = productosValidos(c, items.keySet());
                if (productos.size() != items.size()) throw new SQLException("Uno de los productos no está disponible.");
                int cantidadTotal = 0;
                BigDecimal total = BigDecimal.ZERO;
                for (Map.Entry<Integer, Integer> entrada : items.entrySet()) {
                    if (entrada.getValue() == null || entrada.getValue() < 1) throw new SQLException("Cantidad inválida.");
                    cantidadTotal += entrada.getValue();
                    if (cantidadTotal > MAX_PRODUCTOS_CLIENTE) throw new SQLException("El máximo por cliente es de 20 productos por pedido.");
                    total = total.add(BigDecimal.valueOf(productos.get(entrada.getKey()).getPrecio()).multiply(BigDecimal.valueOf(entrada.getValue())));
                }
                String insert = "INSERT INTO pedidos (total, fecha_pedido, mesas_id_mesa, clientes_id_cliente, usuarios_id_usuario, estados_pedidos_id_estado_pedido, tipo_entrega, direccion_entrega, metodo_pago, codigo_entrega, observaciones_entrega, estado) VALUES (?, CURRENT_TIMESTAMP, ?, ?, ?, ?, ?, ?, ?, ?, ?, 1)";
                int pedido; String pinGenerado = domicilio ? generarPin() : null;
                try (PreparedStatement ps = c.prepareStatement(insert, Statement.RETURN_GENERATED_KEYS)) {
                    ps.setBigDecimal(1, total); ps.setInt(2, idMesa); ps.setInt(3, cliente); ps.setInt(4, idUsuario);
                    ps.setInt(5, ESTADO_PENDIENTE); ps.setString(6, domicilio ? "DOMICILIO" : "MESA");
                    ps.setString(7, domicilio ? direccion.trim() : null); ps.setString(8, metodoPago); ps.setString(9, pinGenerado); ps.setString(10, domicilio ? limpiarObservacion(observaciones) : null);
                    ps.executeUpdate();
                    try (ResultSet keys = ps.getGeneratedKeys()) { if (!keys.next()) throw new SQLException("No se generó el pedido."); pedido = keys.getInt(1); }
                }
                String detalle = "INSERT INTO detalles_pedidos (cantidad, subtotal, precio_unitario, menus_id_menu, pedidos_id_pedido, estados_pedidos_id_estado_pedido, estado) VALUES (?, ?, ?, ?, ?, ?, 1)";
                try (PreparedStatement ps = c.prepareStatement(detalle)) {
                    for (Map.Entry<Integer, Integer> entrada : items.entrySet()) {
                        Menu menu = productos.get(entrada.getKey()); BigDecimal precio = BigDecimal.valueOf(menu.getPrecio());
                        ps.setInt(1, entrada.getValue()); ps.setBigDecimal(2, precio.multiply(BigDecimal.valueOf(entrada.getValue())));
                        ps.setBigDecimal(3, precio); ps.setInt(4, menu.getIdMenu()); ps.setInt(5, pedido); ps.setInt(6, ESTADO_PENDIENTE); ps.addBatch();
                    }
                    ps.executeBatch();
                }
                if (domicilio && pinGenerado != null) {
                    try (PreparedStatement np = c.prepareStatement("INSERT INTO notificaciones(titulo,mensaje,fecha_envio,leido,usuarios_id_usuario,estado) VALUES(?,?,CURRENT_TIMESTAMP,0,?,1)")) {
                        np.setString(1, "PIN de entrega");
                        np.setString(2, "Tu PIN para recibir el Pedido #" + pedido + " es: " + pinGenerado);
                        np.setInt(3, idUsuario);
                        np.executeUpdate();
                    }
                }
                c.commit(); return pedido;
            } catch (SQLException e) { c.rollback(); throw e; }
            finally { c.setAutoCommit(auto); }
        }
    }

    public int crearPedidoMesero(int idMesero, int idMesa, String metodoPago, Map<Integer, Integer> items) throws SQLException {
        if (!"EFECTIVO".equals(metodoPago) && !"TARJETA".equals(metodoPago) && !"TRANSFERENCIA".equals(metodoPago) && !"DEMO".equals(metodoPago)) throw new SQLException("Selecciona un método de pago válido.");
        int clienteServicio = clienteServicio();
        return crearPedidoConUsuario(idMesero, clienteServicio, idMesa, metodoPago, items);
    }

    private int crearPedidoConUsuario(int idMesero, int cliente, int idMesa, String metodoPago, Map<Integer, Integer> items) throws SQLException {
        if (!mesaActiva(idMesa) || items == null || items.isEmpty()) throw new SQLException("Selecciona una mesa y productos válidos.");
        try (Connection c = conexion.getConn()) {
            if (c == null) throw new SQLException("No hay conexión con la base de datos.");
            boolean auto = c.getAutoCommit(); c.setAutoCommit(false);
            try {
                if (!bloquearMesaDisponible(c, idMesa)) {
                    throw new SQLException("La mesa seleccionada ya no está disponible. Elige otra mesa.");
                }
                Map<Integer, Menu> productos = productosValidos(c, items.keySet());
                if (productos.size() != items.size()) throw new SQLException("Uno de los productos no está disponible.");
                BigDecimal total = BigDecimal.ZERO;
                for (Map.Entry<Integer,Integer> e : items.entrySet()) { if (e.getValue() < 1 || e.getValue() > 50) throw new SQLException("Cantidad inválida."); total=total.add(BigDecimal.valueOf(productos.get(e.getKey()).getPrecio()).multiply(BigDecimal.valueOf(e.getValue()))); }
                int pedido;
                try (PreparedStatement ps=c.prepareStatement("INSERT INTO pedidos (total, fecha_pedido, mesas_id_mesa, clientes_id_cliente, usuarios_id_usuario, estados_pedidos_id_estado_pedido, tipo_entrega, metodo_pago, estado) VALUES (?,CURRENT_TIMESTAMP,?,?,?,?,'MESA',?,1)",Statement.RETURN_GENERATED_KEYS)) {
                    ps.setBigDecimal(1,total); ps.setInt(2,idMesa); ps.setInt(3,cliente); ps.setInt(4,idMesero); ps.setInt(5,ESTADO_PENDIENTE); ps.setString(6,metodoPago); ps.executeUpdate();
                    try(ResultSet rs=ps.getGeneratedKeys()){if(!rs.next())throw new SQLException("No se generó el pedido.");pedido=rs.getInt(1);}
                }
                try(PreparedStatement ps=c.prepareStatement("INSERT INTO detalles_pedidos (cantidad, subtotal, precio_unitario, menus_id_menu, pedidos_id_pedido, estados_pedidos_id_estado_pedido, estado) VALUES (?,?,?,?,?,?,1)")){
                    for(Map.Entry<Integer,Integer> e:items.entrySet()){Menu m=productos.get(e.getKey());BigDecimal precio=BigDecimal.valueOf(m.getPrecio());ps.setInt(1,e.getValue());ps.setBigDecimal(2,precio.multiply(BigDecimal.valueOf(e.getValue())));ps.setBigDecimal(3,precio);ps.setInt(4,m.getIdMenu());ps.setInt(5,pedido);ps.setInt(6,ESTADO_PENDIENTE);ps.addBatch();}ps.executeBatch();
                }
                c.commit(); return pedido;
            } catch(SQLException e){c.rollback();throw e;} finally {c.setAutoCommit(auto);}
        }
    }

    public List<Map<String,Object>> pedidos(String vista, int idUsuario) {
        String filtro = "";
        if ("COCINA".equals(vista)) filtro=" AND p.estados_pedidos_id_estado_pedido IN (1,2)";
        
        if ("REPARTIDOR".equals(vista)) filtro=" AND p.tipo_entrega='DOMICILIO' AND ((p.estados_pedidos_id_estado_pedido=5 AND EXISTS (SELECT 1 FROM usuarios ur WHERE ur.id_usuario="+idUsuario+" AND ur.roles_id_rol=5 AND ur.estado=1 AND LOWER(p.direccion_entrega) LIKE CONCAT('%',LOWER(ur.zona_ruta),'%'))) OR (p.estados_pedidos_id_estado_pedido=6 AND p.repartidor_id_usuario="+idUsuario+"))";
        
        if ("MESERO".equals(vista)) filtro=" AND p.tipo_entrega='MESA' AND p.estados_pedidos_id_estado_pedido=5";
        if ("MESERO_HISTORIAL".equals(vista)) filtro=" AND p.tipo_entrega='MESA' AND p.usuarios_id_usuario="+idUsuario+" AND p.estados_pedidos_id_estado_pedido="+ESTADO_SERVIDO;
        if ("CLIENTE".equals(vista)) filtro=" AND p.usuarios_id_usuario="+idUsuario;
        if ("CAJERO".equals(vista)) filtro=" AND p.tipo_entrega='MESA' AND p.estados_pedidos_id_estado_pedido=3";
        String sql="SELECT p.id_pedido,p.total,p.fecha_pedido,p.tipo_entrega,p.direccion_entrega,p.metodo_pago,p.codigo_entrega,p.observaciones_entrega,p.repartidor_id_usuario,p.estados_pedidos_id_estado_pedido,ep.detalle_estado_pedido,m.numero,u.nombre,u.apellido,u.telefono FROM pedidos p JOIN estados_pedidos ep ON ep.id_estado_pedido=p.estados_pedidos_id_estado_pedido JOIN mesas m ON m.id_mesa=p.mesas_id_mesa JOIN usuarios u ON u.id_usuario=p.usuarios_id_usuario WHERE p.estado=1"+filtro+" ORDER BY p.fecha_pedido DESC";
        List<Map<String,Object>> salida=new ArrayList<>();
        try(Connection c=conexion.getConn();PreparedStatement ps=c.prepareStatement(sql);ResultSet rs=ps.executeQuery()){
            while(rs.next()){Map<String,Object> f=new LinkedHashMap<>();f.put("id",rs.getInt("id_pedido"));f.put("total",rs.getBigDecimal("total"));f.put("fecha",rs.getTimestamp("fecha_pedido").toString());f.put("tipo",rs.getString("tipo_entrega"));f.put("direccion",rs.getString("direccion_entrega"));f.put("metodoPago",rs.getString("metodo_pago")); if("CLIENTE".equals(vista)) f.put("codigoEntrega",rs.getString("codigo_entrega")); f.put("observacionesEntrega",rs.getString("observaciones_entrega"));f.put("mesa",rs.getInt("numero"));f.put("estadoId",rs.getInt("estados_pedidos_id_estado_pedido"));f.put("estado",rs.getString("detalle_estado_pedido"));f.put("repartidor",rs.getObject("repartidor_id_usuario"));f.put("creador",rs.getString("nombre")+" "+rs.getString("apellido"));f.put("telefono",rs.getString("telefono"));f.put("items",detalles(c,rs.getInt("id_pedido")));salida.add(f);}
        }catch(SQLException|NullPointerException e){}return salida;
    }

    public boolean actualizarPorRol(int idPedido,int idUsuario,String rol,String accion) throws SQLException {
        int destino=0; String condicion="";
        if("COCINERO".equals(rol)&&"iniciar".equals(accion)){destino=ESTADO_PREPARACION;condicion="estados_pedidos_id_estado_pedido="+ESTADO_PENDIENTE;}
        if("COCINERO".equals(rol)&&"listo".equals(accion)){destino=ESTADO_LISTO;condicion="estados_pedidos_id_estado_pedido="+ESTADO_PREPARACION;}
        if("MESERO".equals(rol)&&"servido".equals(accion)){destino=ESTADO_SERVIDO;condicion="tipo_entrega='MESA' AND estados_pedidos_id_estado_pedido="+ESTADO_LISTO;}
        if("REPARTIDOR".equals(rol)&&"tomar".equals(accion)){
            try(Connection c=conexion.getConn();PreparedStatement ps=c.prepareStatement("UPDATE pedidos SET estados_pedidos_id_estado_pedido=?, repartidor_id_usuario=? WHERE id_pedido=? AND estado=1 AND tipo_entrega='DOMICILIO' AND estados_pedidos_id_estado_pedido=? AND repartidor_id_usuario IS NULL AND EXISTS (SELECT 1 FROM usuarios ur WHERE ur.id_usuario=? AND ur.roles_id_rol=5 AND ur.estado=1 AND ur.en_linea=1)")){ps.setInt(1,ESTADO_CAMINO);ps.setInt(2,idUsuario);ps.setInt(3,idPedido);ps.setInt(4,ESTADO_LISTO);ps.setInt(5,idUsuario);return ps.executeUpdate()==1;}
        }
        if("REPARTIDOR".equals(rol)&&"entregado".equals(accion)){
            try(Connection c=conexion.getConn();PreparedStatement ps=c.prepareStatement("UPDATE pedidos SET estados_pedidos_id_estado_pedido=? WHERE id_pedido=? AND estado=1 AND repartidor_id_usuario=? AND estados_pedidos_id_estado_pedido=?")){ps.setInt(1,ESTADO_ENTREGADO);ps.setInt(2,idPedido);ps.setInt(3,idUsuario);ps.setInt(4,ESTADO_CAMINO);return ps.executeUpdate()==1;}
        }
        if(destino==0)return false;
        try(Connection c=conexion.getConn();PreparedStatement ps=c.prepareStatement("UPDATE pedidos SET estados_pedidos_id_estado_pedido=? WHERE id_pedido=? AND estado=1 AND "+condicion)){
            ps.setInt(1,destino); ps.setInt(2,idPedido); boolean ok=ps.executeUpdate()==1;
            if(ok && "COCINERO".equals(rol) && "listo".equals(accion)) crearNotificacionPedidoListo(c,idPedido);
            return ok;
        }
    }

    public String numeroFactura(int idPedido) throws SQLException {
        String sql="SELECT numero_factura FROM facturas WHERE pedidos_id_pedido=? AND estado=1 ORDER BY id_factura DESC LIMIT 1";
        try(Connection c=conexion.getConn();PreparedStatement ps=c.prepareStatement(sql)){ps.setInt(1,idPedido);try(ResultSet rs=ps.executeQuery()){return rs.next()?rs.getString(1):null;}}
    }

                                                                                                                   
    public int finalizarDomicilio(int idPedido, int idRepartidor, String pin) throws SQLException {
        try (Connection c = conexion.getConn()) {
            if (c == null) throw new SQLException("No hay conexión con la base de datos.");
            boolean auto=c.getAutoCommit(); c.setAutoCommit(false);
            try {
                int cliente=0;
                try(PreparedStatement ps=c.prepareStatement("SELECT clientes_id_cliente,codigo_entrega FROM pedidos WHERE id_pedido=? AND estado=1 AND tipo_entrega='DOMICILIO' AND repartidor_id_usuario=? AND estados_pedidos_id_estado_pedido=6 FOR UPDATE")){
                    ps.setInt(1,idPedido); ps.setInt(2,idRepartidor);
                    try(ResultSet rs=ps.executeQuery()){if(!rs.next()) throw new SQLException("El domicilio debe estar en camino antes de finalizarlo."); cliente=rs.getInt(1); String codigo=rs.getString(2); if(codigo==null || pin==null || !codigo.equals(pin.trim())) throw new SQLException("PIN de entrega incorrecto.");}
                }
                int puntos=0;
                try(PreparedStatement ps=c.prepareStatement("SELECT COALESCE(SUM(d.cantidad*m.puntos_fidelizacion),0) FROM detalles_pedidos d JOIN menus m ON m.id_menu=d.menus_id_menu WHERE d.pedidos_id_pedido=? AND d.estado=1")){
                    ps.setInt(1,idPedido);try(ResultSet rs=ps.executeQuery()){rs.next();puntos=rs.getInt(1);}
                }
                int factura=0;
                try(PreparedStatement ps=c.prepareStatement("SELECT id_factura FROM facturas WHERE pedidos_id_pedido=? AND estado=1 LIMIT 1")){
                    ps.setInt(1,idPedido);try(ResultSet rs=ps.executeQuery()){if(rs.next())factura=rs.getInt(1);}
                }
                if(factura==0){
                    try(PreparedStatement ps=c.prepareStatement("INSERT INTO facturas(numero_factura,total_factura,fecha_factura,pedidos_id_pedido,clientes_id_cliente,estado) VALUES(?,?,?,?,?,1)",Statement.RETURN_GENERATED_KEYS)){
                        ps.setString(1,"FACT-DEMO-"+idPedido);ps.setBigDecimal(2,obtenerTotal(c,idPedido));ps.setDate(3,new java.sql.Date(System.currentTimeMillis()));ps.setInt(4,idPedido);ps.setInt(5,cliente);ps.executeUpdate();
                        try(ResultSet rs=ps.getGeneratedKeys()){if(!rs.next())throw new SQLException("No se generó la factura demo.");factura=rs.getInt(1);}
                    }
                }
                try(PreparedStatement ps=c.prepareStatement("INSERT INTO pagos(monto,confirmado,fecha_pago,metodos_pago_id_metodo_pago,facturas_id_factura,estado) VALUES(?,1,CURRENT_TIMESTAMP,(SELECT id_metodo_pago FROM metodos_pago WHERE tipo_pago='Pago demo' LIMIT 1),?,1)")){
                    ps.setBigDecimal(1,obtenerTotal(c,idPedido));ps.setInt(2,factura);if(ps.executeUpdate()!=1)throw new SQLException("No fue posible registrar el pago demo.");
                }
                try(PreparedStatement ps=c.prepareStatement("UPDATE pedidos SET estados_pedidos_id_estado_pedido=?,fecha_entrega=CURRENT_TIMESTAMP WHERE id_pedido=? AND estado=1 AND repartidor_id_usuario=? AND estados_pedidos_id_estado_pedido=6")){
                    ps.setInt(1,ESTADO_ENTREGADO);ps.setInt(2,idPedido);ps.setInt(3,idRepartidor);if(ps.executeUpdate()!=1)throw new SQLException("No fue posible finalizar el domicilio.");
                }
                int nuevoSaldo=0;
                try(PreparedStatement ps=c.prepareStatement("SELECT puntos_fidelizacion FROM clientes WHERE id_cliente=? AND estado=1 FOR UPDATE")){
                    ps.setInt(1,cliente);try(ResultSet rs=ps.executeQuery()){if(!rs.next())throw new SQLException("El cliente del pedido no está activo.");nuevoSaldo=rs.getInt(1)+puntos;}
                }
                try(PreparedStatement ps=c.prepareStatement("UPDATE clientes SET puntos_fidelizacion=? WHERE id_cliente=? AND estado=1")){ps.setInt(1,nuevoSaldo);ps.setInt(2,cliente);ps.executeUpdate();}
                if(puntos>0)try(PreparedStatement ps=c.prepareStatement("INSERT INTO historiales_puntos (puntos,fecha_movimiento,tipo_de_movimiento,puntos_restantes,clientes_id_cliente,estado) VALUES (?,CURRENT_TIMESTAMP,?,?,?,1)")){
                    ps.setInt(1,puntos);ps.setString(2,"Puntos por pedido pagado #"+idPedido);ps.setInt(3,nuevoSaldo);ps.setInt(4,cliente);ps.executeUpdate();
                }
                c.commit(); return puntos;
            }catch(SQLException e){c.rollback();throw e;}finally{c.setAutoCommit(auto);}
        }
    }

                                                                                     
    public boolean cancelarDomicilio(int idPedido,int idRepartidor,String motivo,String detalle) throws SQLException {
        String sql="UPDATE pedidos SET estados_pedidos_id_estado_pedido=? WHERE id_pedido=? AND tipo_entrega='DOMICILIO' AND repartidor_id_usuario=? AND estados_pedidos_id_estado_pedido=6 AND estado=1";
        try(Connection c=conexion.getConn()){
            boolean auto=c.getAutoCommit(); c.setAutoCommit(false);
            try {
                try(PreparedStatement ps=c.prepareStatement(sql)){ ps.setInt(1,ESTADO_CANCELADO); ps.setInt(2,idPedido); ps.setInt(3,idRepartidor); if(ps.executeUpdate()!=1){ c.rollback(); return false; } }
                try(PreparedStatement pi=c.prepareStatement("INSERT INTO incidencias_domicilio(pedido_id,repartidor_id_usuario,motivo,detalle) VALUES(?,?,?,?)")){pi.setInt(1,idPedido);pi.setInt(2,idRepartidor);pi.setString(3,motivo);pi.setString(4,detalle);pi.executeUpdate();}
                String texto="Pedido #"+idPedido+" cancelado. Motivo: "+(motivo==null?"":motivo)+(detalle==null||detalle.trim().isEmpty()?"":". Detalle: "+detalle.trim());
                try(PreparedStatement pn=c.prepareStatement("INSERT INTO notificaciones(titulo,mensaje,fecha_envio,leido,usuarios_id_usuario,estado) SELECT ?,?,CURRENT_TIMESTAMP,0,usuarios_id_usuario,1 FROM pedidos WHERE id_pedido=? UNION ALL SELECT ?,?,CURRENT_TIMESTAMP,0,id_usuario,1 FROM usuarios WHERE roles_id_rol=1 AND estado=1")){
                    pn.setString(1,"Pedido cancelado"); pn.setString(2,texto); pn.setInt(3,idPedido); pn.setString(4,"Incidencia de domicilio"); pn.setString(5,texto); pn.executeUpdate();
                }
                c.commit(); return true;
            } catch(SQLException e){c.rollback();throw e;} finally {c.setAutoCommit(auto);}
        }
    }

    public boolean cancelarPedidoCliente(int idPedido,int idUsuario,String detalle) throws SQLException {
        try(Connection c=conexion.getConn()){
            boolean auto=c.getAutoCommit(); c.setAutoCommit(false);
            try{
                int updated;
                try(PreparedStatement ps=c.prepareStatement("UPDATE pedidos SET estados_pedidos_id_estado_pedido=? WHERE id_pedido=? AND usuarios_id_usuario=? AND estado=1 AND estados_pedidos_id_estado_pedido=?")){ps.setInt(1,ESTADO_CANCELADO);ps.setInt(2,idPedido);ps.setInt(3,idUsuario);ps.setInt(4,ESTADO_PENDIENTE);updated=ps.executeUpdate();}
                if(updated!=1){c.rollback();return false;}
                String texto="El cliente solicitó cancelar el pedido #"+idPedido+(detalle==null||detalle.trim().isEmpty()?"":". Detalle: "+detalle.trim());
                try(PreparedStatement ps=c.prepareStatement("INSERT INTO notificaciones(titulo,mensaje,fecha_envio,leido,usuarios_id_usuario,estado) SELECT 'Solicitud de cancelación',?,CURRENT_TIMESTAMP,0,id_usuario,1 FROM usuarios WHERE roles_id_rol IN (1,2) AND estado=1")){ps.setString(1,texto);ps.executeUpdate();}
                c.commit();return true;
            }catch(SQLException e){c.rollback();throw e;}finally{c.setAutoCommit(auto);}
        }
    }

    public List<Map<String,Object>> historialRepartidor(int idUsuario) {
        List<Map<String,Object>> out=new ArrayList<>();
        String sql="SELECT p.id_pedido,p.fecha_pedido,p.total,p.direccion_entrega,CASE WHEN p.estados_pedidos_id_estado_pedido=8 THEN 'Cancelado' ELSE ep.detalle_estado_pedido END AS estado_actual FROM pedidos p JOIN estados_pedidos ep ON ep.id_estado_pedido=p.estados_pedidos_id_estado_pedido WHERE p.repartidor_id_usuario=? AND p.tipo_entrega='DOMICILIO' ORDER BY p.fecha_pedido DESC";
        try(Connection c=conexion.getConn();PreparedStatement ps=c.prepareStatement(sql)){ps.setInt(1,idUsuario);try(ResultSet rs=ps.executeQuery()){while(rs.next()){Map<String,Object>x=new LinkedHashMap<>();x.put("id",rs.getInt(1));x.put("fecha",rs.getTimestamp(2).toString());x.put("total",rs.getBigDecimal(3));x.put("direccion",rs.getString(4));x.put("estado",rs.getString(5));out.add(x);}}}catch(SQLException|NullPointerException e){} return out;
    }

                                                                                                                                                  
    public int confirmarPago(int idPedido, int idCajero) throws SQLException {
        try (Connection c = conexion.getConn()) {
            if (c == null) throw new SQLException("No hay conexión con la base de datos.");
            boolean auto=c.getAutoCommit(); c.setAutoCommit(false);
            try {
                int cliente=0, mesa=0;
                try(PreparedStatement ps=c.prepareStatement(
                        "SELECT clientes_id_cliente,mesas_id_mesa FROM pedidos WHERE id_pedido=? AND estado=1 AND tipo_entrega='MESA' AND estados_pedidos_id_estado_pedido=3 FOR UPDATE")){
                    ps.setInt(1,idPedido);
                    try(ResultSet rs=ps.executeQuery()){
                        if(!rs.next()) throw new SQLException("El pedido debe estar servido y pendiente de pago.");
                        cliente=rs.getInt(1); mesa=rs.getInt(2);
                    }
                }
                int unidades=0;
                try(PreparedStatement ps=c.prepareStatement("SELECT COALESCE(SUM(cantidad),0) FROM detalles_pedidos WHERE pedidos_id_pedido=? AND estado=1")){
                    ps.setInt(1,idPedido); try(ResultSet rs=ps.executeQuery()){rs.next();unidades=rs.getInt(1);}
                }
                int puntos=0;
                try(PreparedStatement ps=c.prepareStatement("SELECT COALESCE(SUM(d.cantidad*m.puntos_fidelizacion),0) FROM detalles_pedidos d JOIN menus m ON m.id_menu=d.menus_id_menu WHERE d.pedidos_id_pedido=? AND d.estado=1")){ps.setInt(1,idPedido);try(ResultSet rs=ps.executeQuery()){rs.next();puntos=rs.getInt(1);}}
                int factura=0;
                try(PreparedStatement ps=c.prepareStatement("SELECT id_factura FROM facturas WHERE pedidos_id_pedido=? AND estado=1 LIMIT 1")){ps.setInt(1,idPedido);try(ResultSet rs=ps.executeQuery()){if(rs.next())factura=rs.getInt(1);}}
                if(factura==0){
                    try(PreparedStatement ps=c.prepareStatement("INSERT INTO facturas(numero_factura,total_factura,fecha_factura,pedidos_id_pedido,clientes_id_cliente,estado) VALUES(?,?,?,?,?,1)",Statement.RETURN_GENERATED_KEYS)){
                        ps.setString(1,"FACT-DEMO-"+idPedido);ps.setBigDecimal(2,obtenerTotal(c,idPedido));ps.setDate(3,new java.sql.Date(System.currentTimeMillis()));ps.setInt(4,idPedido);ps.setInt(5,cliente);ps.executeUpdate();try(ResultSet rs=ps.getGeneratedKeys()){if(!rs.next())throw new SQLException("No se generó la factura demo.");factura=rs.getInt(1);}}
                }
                try(PreparedStatement ps=c.prepareStatement("INSERT INTO pagos(monto,confirmado,fecha_pago,metodos_pago_id_metodo_pago,facturas_id_factura,estado) VALUES(?,1,CURRENT_TIMESTAMP,COALESCE((SELECT CASE p.metodo_pago WHEN 'EFECTIVO' THEN 1 WHEN 'TARJETA' THEN 2 WHEN 'TRANSFERENCIA' THEN 3 ELSE 4 END FROM pedidos p WHERE p.id_pedido=?),4),?,1)")){ps.setBigDecimal(1,obtenerTotal(c,idPedido));ps.setInt(2,idPedido);ps.setInt(3,factura);if(ps.executeUpdate()!=1)throw new SQLException("No fue posible registrar el pago.");}
                try(PreparedStatement ps=c.prepareStatement("UPDATE pedidos SET estados_pedidos_id_estado_pedido=? WHERE id_pedido=? AND estado=1 AND estados_pedidos_id_estado_pedido=3")){
                    ps.setInt(1,ESTADO_PAGADO);ps.setInt(2,idPedido);if(ps.executeUpdate()!=1)throw new SQLException("No fue posible confirmar el pago demo.");
                }
                try(PreparedStatement ps=c.prepareStatement("UPDATE mesas SET estados_mesas_id_estado_mesa=1 WHERE id_mesa=? AND estado=1")){
                    ps.setInt(1,mesa); ps.executeUpdate();
                }
                int nuevoSaldo=0;
                if(cliente>0){
                    int saldo=0;
                    try(PreparedStatement ps=c.prepareStatement("SELECT puntos_fidelizacion FROM clientes WHERE id_cliente=? AND estado=1 FOR UPDATE")){
                        ps.setInt(1,cliente); try(ResultSet rs=ps.executeQuery()){if(!rs.next()) throw new SQLException("El cliente del pedido no está activo."); saldo=rs.getInt(1);}
                    }
                    nuevoSaldo=saldo+puntos;
                    try(PreparedStatement ps=c.prepareStatement("UPDATE clientes SET puntos_fidelizacion=? WHERE id_cliente=? AND estado=1")){
                        ps.setInt(1,nuevoSaldo); ps.setInt(2,cliente); ps.executeUpdate();
                    }
                    if(puntos>0) try(PreparedStatement ps=c.prepareStatement(
                            "INSERT INTO historiales_puntos (puntos,fecha_movimiento,tipo_de_movimiento,puntos_restantes,clientes_id_cliente,estado) VALUES (?,CURRENT_TIMESTAMP,?,?,?,1)")){
                        ps.setInt(1,puntos); ps.setString(2,"Puntos por pedido pagado #"+idPedido); ps.setInt(3,nuevoSaldo); ps.setInt(4,cliente); ps.executeUpdate();
                    }
                }
                c.commit(); return puntos;
            } catch(SQLException e){c.rollback();throw e;} finally{c.setAutoCommit(auto);}
        }
    }


    public Map<String,Object> disponibilidadRepartidor(int idUsuario) { Map<String,Object> out=new LinkedHashMap<>(); out.put("enLinea",false); out.put("zona", "Guaduas, Cundinamarca"); out.put("direccionBase", DIRECCION_BASE_REPARTIDOR); try(Connection c=conexion.getConn();PreparedStatement ps=c.prepareStatement("SELECT en_linea,zona_ruta FROM usuarios WHERE id_usuario=? AND roles_id_rol=5 AND estado=1")){ps.setInt(1,idUsuario);try(ResultSet rs=ps.executeQuery()){if(rs.next()){out.put("enLinea",rs.getBoolean(1));out.put("zona",rs.getString(2));}}}catch(SQLException|NullPointerException e){} return out; }
    public boolean validarDisponibilidad(int idUsuario) throws SQLException {
        try(Connection c=conexion.getConn();PreparedStatement ps=c.prepareStatement("SELECT en_linea FROM usuarios WHERE id_usuario=? AND roles_id_rol=5 AND estado=1")){ps.setInt(1,idUsuario);try(ResultSet rs=ps.executeQuery()){return rs.next() && rs.getBoolean(1);}}
    }
    public boolean cambiarDisponibilidad(int idUsuario, boolean enLinea) throws SQLException {
        try(Connection c=conexion.getConn();PreparedStatement ps=c.prepareStatement("UPDATE usuarios SET en_linea=? WHERE id_usuario=? AND roles_id_rol=5 AND estado=1")){ps.setBoolean(1,enLinea);ps.setInt(2,idUsuario);return ps.executeUpdate()==1;}
    }
    public Map<String,Object> resumenTurno(int idUsuario) {
        Map<String,Object> out=new LinkedHashMap<>(); out.put("entregas",0);out.put("efectivo",BigDecimal.ZERO);out.put("liquidar",BigDecimal.ZERO);
        String sql="SELECT COUNT(*) entregas,COALESCE(SUM(total),0) efectivo FROM pedidos WHERE repartidor_id_usuario=? AND tipo_entrega='DOMICILIO' AND estados_pedidos_id_estado_pedido=7 AND estado=1 AND DATE(fecha_entrega)=CURRENT_DATE";
        try(Connection c=conexion.getConn();PreparedStatement ps=c.prepareStatement(sql)){ps.setInt(1,idUsuario);try(ResultSet rs=ps.executeQuery()){if(rs.next()){out.put("entregas",rs.getInt("entregas"));out.put("efectivo",rs.getBigDecimal("efectivo"));out.put("liquidar",rs.getBigDecimal("efectivo"));}}}catch(SQLException|NullPointerException e){} return out;
    }
    private String zonaRuta(int idUsuario) { try(Connection c=conexion.getConn();PreparedStatement ps=c.prepareStatement("SELECT COALESCE(NULLIF(zona_ruta,''),'Guaduas, Cundinamarca') FROM usuarios WHERE id_usuario=?")){ps.setInt(1,idUsuario);try(ResultSet rs=ps.executeQuery()){return rs.next()?rs.getString(1):"Guaduas";}}catch(Exception e){return "Guaduas";} }
    private void validarCoberturaGuaduas(String direccion) throws SQLException { String v=direccion.toLowerCase(java.util.Locale.ROOT); if(!v.contains("guaduas")) throw new SQLException("La cobertura de domicilios es exclusiva para Guaduas, Cundinamarca."); }
    private String generarPin(){ return String.format("%04d", new java.util.Random().nextInt(10000)); }
    private String limpiarObservacion(String v){ return v==null?null:v.trim().substring(0,Math.min(v.trim().length(),500)); }

    public String configuracion(String clave){try(Connection c=conexion.getConn();PreparedStatement ps=c.prepareStatement("SELECT valor FROM configuracion_negocio WHERE clave=? AND estado=1")){ps.setString(1,clave);try(ResultSet rs=ps.executeQuery()){return rs.next()?rs.getString(1):"";}}catch(SQLException|NullPointerException e){return "";}}
    public boolean guardarConfiguracion(String clave,String valor){try(Connection c=conexion.getConn();PreparedStatement ps=c.prepareStatement("INSERT INTO configuracion_negocio(clave,valor,estado) VALUES(?,?,1) ON DUPLICATE KEY UPDATE valor=VALUES(valor),estado=1")){ps.setString(1,clave);ps.setString(2,valor);return ps.executeUpdate()>0;}catch(SQLException|NullPointerException e){return false;}}

    public Map<String,Object> puntosCliente(int idUsuario) {
        Map<String,Object> resultado=new LinkedHashMap<>(); resultado.put("saldo",0); resultado.put("historial",new ArrayList<Map<String,Object>>());
        String sql="SELECT c.id_cliente,c.puntos_fidelizacion FROM clientes c WHERE c.usuarios_id_usuario=? AND c.estado=1";
        try(Connection c=conexion.getConn();PreparedStatement ps=c.prepareStatement(sql)){
            ps.setInt(1,idUsuario);try(ResultSet rs=ps.executeQuery()){if(!rs.next())return resultado;int cliente=rs.getInt("id_cliente");resultado.put("saldo",rs.getInt("puntos_fidelizacion"));
                List<Map<String,Object>> historial=new ArrayList<>();try(PreparedStatement hp=c.prepareStatement("SELECT puntos,fecha_movimiento,tipo_de_movimiento,puntos_restantes FROM historiales_puntos WHERE clientes_id_cliente=? AND estado=1 ORDER BY fecha_movimiento DESC LIMIT 10")){hp.setInt(1,cliente);try(ResultSet h=hp.executeQuery()){while(h.next()){Map<String,Object> fila=new LinkedHashMap<>();fila.put("puntos",h.getInt("puntos"));fila.put("fecha",h.getTimestamp("fecha_movimiento").toString());fila.put("tipo",h.getString("tipo_de_movimiento"));fila.put("restantes",h.getInt("puntos_restantes"));historial.add(fila);}}}resultado.put("historial",historial);}
        }catch(SQLException|NullPointerException e){}return resultado;
    }

    private List<Map<String,Object>> detalles(Connection c,int pedido)throws SQLException{List<Map<String,Object>> d=new ArrayList<>();try(PreparedStatement ps=c.prepareStatement("SELECT d.cantidad,m.nombre FROM detalles_pedidos d JOIN menus m ON m.id_menu=d.menus_id_menu WHERE d.pedidos_id_pedido=? AND d.estado=1")){ps.setInt(1,pedido);try(ResultSet rs=ps.executeQuery()){while(rs.next()){Map<String,Object> x=new LinkedHashMap<>();x.put("cantidad",rs.getInt(1));x.put("nombre",rs.getString(2));d.add(x);}}}return d;}
    private boolean mesaActiva(int mesa){try(Connection c=conexion.getConn();PreparedStatement ps=c.prepareStatement("SELECT 1 FROM mesas WHERE id_mesa=? AND estado=1 AND estados_mesas_id_estado_mesa=1")){ps.setInt(1,mesa);try(ResultSet rs=ps.executeQuery()){return rs.next();}}catch(SQLException|NullPointerException e){return false;}}

                                                                                                                    
    private boolean bloquearMesaDisponible(Connection c,int mesa)throws SQLException{
        try(PreparedStatement ps=c.prepareStatement("SELECT id_mesa FROM mesas WHERE id_mesa=? AND estado=1 AND estados_mesas_id_estado_mesa=1 FOR UPDATE")){
            ps.setInt(1,mesa);
            try(ResultSet rs=ps.executeQuery()){
                if(!rs.next()) return false;
            }
        }
        try(PreparedStatement ps=c.prepareStatement("UPDATE mesas SET estados_mesas_id_estado_mesa=2 WHERE id_mesa=? AND estado=1 AND estados_mesas_id_estado_mesa=1")){
            ps.setInt(1,mesa);
            return ps.executeUpdate()==1;
        }
    }
    private int mesaDomicilio()throws SQLException{try(Connection c=conexion.getConn();PreparedStatement ps=c.prepareStatement("SELECT id_mesa FROM mesas WHERE numero=? AND estado=1")){ps.setInt(1,MESA_DOMICILIO);try(ResultSet rs=ps.executeQuery()){if(rs.next())return rs.getInt(1);}}throw new SQLException("Falta crear la mesa virtual de domicilios.");}
    private int clienteDeUsuario(Connection c,int usuario)throws SQLException{try(PreparedStatement ps=c.prepareStatement("SELECT id_cliente FROM clientes WHERE usuarios_id_usuario=? AND estado=1")){ps.setInt(1,usuario);try(ResultSet rs=ps.executeQuery()){return rs.next()?rs.getInt(1):0;}}}
    private int clienteServicio()throws SQLException{try(Connection c=conexion.getConn();PreparedStatement ps=c.prepareStatement("SELECT id_cliente FROM clientes WHERE usuarios_id_usuario=(SELECT id_usuario FROM usuarios WHERE correo='cliente.salon@saborexpress.local' LIMIT 1) AND estado=1")){try(ResultSet rs=ps.executeQuery()){if(rs.next())return rs.getInt(1);}}throw new SQLException("Ejecuta el script de actualización para crear el cliente de servicio.");}
    private Map<Integer,Menu> productosValidos(Connection c,java.util.Set<Integer> ids)throws SQLException{Map<Integer,Menu> r=new LinkedHashMap<>();if(ids.isEmpty())return r;StringBuilder q=new StringBuilder("SELECT m.id_menu,m.nombre,m.descripcion,c.nombre AS categoria,m.imagen,m.precio,m.disponible,m.puntos_fidelizacion,m.estado FROM menus m JOIN categorias c ON c.id_categoria=m.id_categoria WHERE m.estado=1 AND m.disponible=1 AND m.id_menu IN (");for(int i=0;i<ids.size();i++){if(i>0)q.append(',');q.append('?');}q.append(')');try(PreparedStatement ps=c.prepareStatement(q.toString())){int n=1;for(Integer id:ids)ps.setInt(n++,id);try(ResultSet rs=ps.executeQuery()){while(rs.next()){Menu m=new Menu();m.setIdMenu(rs.getInt(1));m.setNombre(rs.getString(2));m.setDescripcion(rs.getString(3));m.setCategoria(rs.getString(4));m.setImagen(rs.getString(5));m.setPrecio(rs.getDouble(6));m.setDisponible(rs.getInt(7));m.setPuntosFidelizacion(rs.getInt(8));m.setEstado(rs.getInt(9));r.put(m.getIdMenu(),m);}}}return r;}
    private BigDecimal obtenerTotal(Connection c,int pedido)throws SQLException{try(PreparedStatement ps=c.prepareStatement("SELECT total FROM pedidos WHERE id_pedido=?")){ps.setInt(1,pedido);try(ResultSet rs=ps.executeQuery()){if(!rs.next())throw new SQLException("Pedido no encontrado.");return rs.getBigDecimal(1);}}}
    private void actualizarMesa(Connection c,int mesa,int estado)throws SQLException{try(PreparedStatement ps=c.prepareStatement("UPDATE mesas SET estados_mesas_id_estado_mesa=? WHERE id_mesa=?")){ps.setInt(1,estado);ps.setInt(2,mesa);ps.executeUpdate();}}
    private void crearNotificacionPedidoListo(Connection c,int idPedido) throws SQLException {
        String sql="INSERT INTO notificaciones(titulo,mensaje,fecha_envio,leido,usuarios_id_usuario,estado) " +
                "SELECT 'Pedido listo',CASE WHEN p.tipo_entrega='DOMICILIO' THEN CONCAT('Tu pedido #',p.id_pedido,' ya salió de cocina y va en camino. Tu PIN de entrega es: ',COALESCE(p.codigo_entrega,'—')) ELSE CONCAT('Tu pedido #',p.id_pedido,' está listo para servir.') END,CURRENT_TIMESTAMP,0,u.id_usuario,1 " +
                "FROM pedidos p INNER JOIN clientes cl ON cl.id_cliente=p.clientes_id_cliente " +
                "INNER JOIN usuarios u ON u.id_usuario=cl.usuarios_id_usuario " +
                "WHERE p.id_pedido=? AND u.estado=1 " +
                "AND NOT EXISTS (SELECT 1 FROM notificaciones n WHERE n.usuarios_id_usuario=u.id_usuario AND n.titulo='Pedido listo' AND n.mensaje=CASE WHEN p.tipo_entrega='DOMICILIO' THEN CONCAT('Tu pedido #',p.id_pedido,' ya salió de cocina y va en camino. Tu PIN de entrega es: ',COALESCE(p.codigo_entrega,'—')) ELSE CONCAT('Tu pedido #',p.id_pedido,' está listo para servir.') END AND n.estado=1)";
        try(PreparedStatement ps=c.prepareStatement(sql)){ps.setInt(1,idPedido);ps.executeUpdate();}
    }

}

package Controlador;

import java.sql.*;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

                                              
public class ReservaDAO {
    private final Conexion conexion = new Conexion();

    public List<Map<String,Object>> mesasDisponiblesParaReserva(Date fecha, Time hora) {
        List<Map<String,Object>> out = new ArrayList<>();
        String sql = "SELECT m.id_mesa,m.numero FROM mesas m " +
                "WHERE m.estado=1 AND m.estados_mesas_id_estado_mesa=1 AND m.numero<>999 " +
                "AND NOT EXISTS (SELECT 1 FROM reservas r WHERE r.mesas_id_mesa=m.id_mesa " +
                "AND r.fecha_reserva=? AND r.estado=1 AND r.estado_reserva='CONFIRMADA' " +
                "AND ABS(TIME_TO_SEC(TIMEDIFF(r.hora_reserva,?))) < 7200) ORDER BY m.numero";
        try(Connection c=conexion.getConn(); PreparedStatement ps=c.prepareStatement(sql)){
            ps.setDate(1,fecha); ps.setTime(2,hora);
            try(ResultSet rs=ps.executeQuery()){
                while(rs.next()){Map<String,Object> m=new LinkedHashMap<>();m.put("id",rs.getInt(1));m.put("numero",rs.getInt(2));out.add(m);}
            }
        }catch(SQLException|NullPointerException e){ throw new RuntimeException("No se pudieron cargar las mesas disponibles.",e); }
        return out;
    }

    public List<Map<String,Object>> reservasTodas(){
        List<Map<String,Object>> out=new ArrayList<>();
        String sql="SELECT r.id_reserva,r.fecha_reserva,r.hora_reserva,r.cantidad_personas,r.estado_reserva,r.observaciones,m.numero,CONCAT(u.nombre,' ',u.apellido) cliente FROM reservas r JOIN clientes c ON c.id_cliente=r.clientes_id_cliente JOIN usuarios u ON u.id_usuario=c.usuarios_id_usuario JOIN mesas m ON m.id_mesa=r.mesas_id_mesa WHERE r.estado=1 ORDER BY r.fecha_reserva,r.hora_reserva";
        try(Connection c=conexion.getConn();PreparedStatement ps=c.prepareStatement(sql);ResultSet rs=ps.executeQuery()){while(rs.next()){Map<String,Object>x=new LinkedHashMap<>();x.put("id",rs.getInt(1));x.put("fecha",rs.getDate(2).toString());x.put("hora",rs.getTime(3).toString());x.put("personas",rs.getInt(4));x.put("estado",rs.getString(5));x.put("observaciones",rs.getString(6));x.put("mesa",rs.getInt(7));x.put("cliente",rs.getString(8));out.add(x);}}catch(SQLException e){throw new RuntimeException("No se pudieron cargar las reservas.",e);}return out;
    }

                                                                                                      
    public List<Map<String,Object>> clientesParaReserva(){
        List<Map<String,Object>> out=new ArrayList<>();
        String sql="SELECT c.id_cliente,CONCAT(u.nombre,' ',u.apellido) nombre,u.correo FROM clientes c JOIN usuarios u ON u.id_usuario=c.usuarios_id_usuario WHERE c.estado=1 AND u.estado=1 ORDER BY u.nombre,u.apellido";
        try(Connection c=conexion.getConn();PreparedStatement ps=c.prepareStatement(sql);ResultSet rs=ps.executeQuery()){
            while(rs.next()){Map<String,Object> x=new LinkedHashMap<>();x.put("id",rs.getInt(1));x.put("nombre",rs.getString(2));x.put("correo",rs.getString(3));out.add(x);}
        }catch(SQLException|NullPointerException e){throw new RuntimeException("No se pudieron cargar los clientes para reservas.",e);}
        return out;
    }

                                                                                                              
    public boolean confirmarLlegada(int idReserva) throws SQLException {
        if(idReserva<=0) throw new SQLException("La reserva seleccionada no es válida.");
        String sql="UPDATE reservas SET estado_reserva='LLEGO' WHERE id_reserva=? AND estado=1 AND estado_reserva='CONFIRMADA'";
        try(Connection c=conexion.getConn();PreparedStatement ps=c.prepareStatement(sql)){
            ps.setInt(1,idReserva);
            return ps.executeUpdate()==1;
        }
    }

    public List<Map<String,Object>> reservasCliente(int idUsuario){
        List<Map<String,Object>> out=new ArrayList<>();
        String sql="SELECT r.id_reserva,r.fecha_reserva,r.hora_reserva,r.cantidad_personas,r.estado_reserva,r.observaciones,m.numero " +
                "FROM reservas r JOIN clientes c ON c.id_cliente=r.clientes_id_cliente JOIN mesas m ON m.id_mesa=r.mesas_id_mesa " +
                "WHERE c.usuarios_id_usuario=? AND r.estado=1 ORDER BY r.fecha_reserva,r.hora_reserva";
        try(Connection c=conexion.getConn();PreparedStatement ps=c.prepareStatement(sql)){ps.setInt(1,idUsuario);try(ResultSet rs=ps.executeQuery()){
            while(rs.next()){Map<String,Object> x=new LinkedHashMap<>();x.put("id",rs.getInt(1));x.put("fecha",rs.getDate(2).toString());x.put("hora",rs.getTime(3).toString());x.put("personas",rs.getInt(4));x.put("estado",rs.getString(5));x.put("observaciones",rs.getString(6));x.put("mesa",rs.getInt(7));out.add(x);}
        }}catch(SQLException|NullPointerException e){throw new RuntimeException("No se pudieron cargar tus reservas.",e);}return out;
    }

    public void crearReserva(int idUsuario, Date fecha, Time hora, int personas, int mesa, String observaciones) throws SQLException {
        int cliente=clienteDeUsuario(idUsuario);
        if(cliente==0) throw new SQLException("La cuenta no está habilitada como cliente.");
        crearReservaInterna(cliente,fecha,hora,personas,mesa,observaciones);
    }

    public void crearReservaCliente(int idCliente, Date fecha, Time hora, int personas, int mesa, String observaciones) throws SQLException {
        if(idCliente<=0) throw new SQLException("Selecciona un cliente válido.");
        crearReservaInterna(idCliente,fecha,hora,personas,mesa,observaciones);
    }

    private int clienteDeUsuario(int idUsuario)throws SQLException{try(Connection c=conexion.getConn();PreparedStatement ps=c.prepareStatement("SELECT id_cliente FROM clientes WHERE usuarios_id_usuario=? AND estado=1")){ps.setInt(1,idUsuario);try(ResultSet rs=ps.executeQuery()){return rs.next()?rs.getInt(1):0;}}}

    private void crearReservaInterna(int cliente, Date fecha, Time hora, int personas, int mesa, String observaciones) throws SQLException {
        if(fecha==null||hora==null) throw new SQLException("Fecha y hora son obligatorias.");
        java.time.LocalDate hoy=java.time.LocalDate.now(); java.time.LocalDate limite=hoy.plusMonths(3); java.time.LocalDate solicitada=fecha.toLocalDate();
        if(solicitada.isBefore(hoy)) throw new SQLException("La fecha de reserva no puede estar en el pasado.");
        if(solicitada.isAfter(limite)) throw new SQLException("La reserva solo puede realizarse con máximo 3 meses de anticipación.");
        if(solicitada.equals(hoy) && !hora.toLocalTime().isAfter(java.time.LocalTime.now())) throw new SQLException("La hora de reserva debe ser futura.");
        if(personas<1||personas>20) throw new SQLException("La cantidad de personas debe estar entre 1 y 20.");
        if(mesa<=0) throw new SQLException("Selecciona una mesa.");
        try(Connection c=conexion.getConn()){
            if(c==null)throw new SQLException("No hay conexión con la base de datos.");
            boolean auto=c.getAutoCommit();c.setAutoCommit(false);
            try{
                try(PreparedStatement ps=c.prepareStatement("SELECT id_mesa FROM mesas WHERE id_mesa=? AND estado=1 AND estados_mesas_id_estado_mesa=1 FOR UPDATE")){
                    ps.setInt(1,mesa);try(ResultSet rs=ps.executeQuery()){if(!rs.next())throw new SQLException("La mesa seleccionada no está disponible.");}
                }
                try(PreparedStatement ps=c.prepareStatement("SELECT 1 FROM reservas WHERE mesas_id_mesa=? AND fecha_reserva=? AND estado=1 AND estado_reserva='CONFIRMADA' AND ABS(TIME_TO_SEC(TIMEDIFF(hora_reserva,?))) < 7200 FOR UPDATE")){
                    ps.setInt(1,mesa);ps.setDate(2,fecha);ps.setTime(3,hora);try(ResultSet rs=ps.executeQuery()){if(rs.next())throw new SQLException("La mesa ya tiene una reserva cercana a esa hora.");}
                }
                try(PreparedStatement ps=c.prepareStatement("INSERT INTO reservas(fecha_reserva,hora_reserva,cantidad_personas,estado_reserva,clientes_id_cliente,mesas_id_mesa,observaciones) VALUES(?,?,?,'CONFIRMADA',?,?,?)")){
                    ps.setDate(1,fecha);ps.setTime(2,hora);ps.setInt(3,personas);ps.setInt(4,cliente);ps.setInt(5,mesa);ps.setString(6,observaciones==null?null:observaciones.trim());ps.executeUpdate();
                }
                c.commit();
            }catch(SQLException e){c.rollback();throw e;}finally{c.setAutoCommit(auto);}
        }
    }
}

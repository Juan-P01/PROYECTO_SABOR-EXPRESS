package Controlador;

import Modelo.Menu;
import java.sql.*;
import java.util.*;

public class MenuDAO {
    private final Conexion cn = new Conexion();

    public boolean insertar(Menu m) {
        String sql = "INSERT INTO menus (nombre, descripcion, id_categoria, imagen, precio, disponible, puntos_fidelizacion, estado) VALUES (?, ?, ?, ?, ?, ?, ?, ?)";
        try (Connection con=cn.getConn(); PreparedStatement ps=con.prepareStatement(sql)) {
            ps.setString(1,m.getNombre()); ps.setString(2,m.getDescripcion()); ps.setInt(3,m.getIdCategoria());
            ps.setString(4,normalizarImagen(m.getImagen())); ps.setDouble(5,m.getPrecio()); ps.setInt(6,m.getDisponible());
            ps.setInt(7,m.getPuntosFidelizacion()); ps.setInt(8,m.getEstado()); return ps.executeUpdate()>0;
        } catch(SQLException|NullPointerException e){ System.out.println("Error al insertar menú: "+e.getMessage()); return false; }
    }
    public boolean actualizar(Menu m) {
        String sql="UPDATE menus SET nombre=?,descripcion=?,id_categoria=?,imagen=?,precio=?,disponible=?,puntos_fidelizacion=?,estado=? WHERE id_menu=?";
        try(Connection con=cn.getConn();PreparedStatement ps=con.prepareStatement(sql)){
            ps.setString(1,m.getNombre());ps.setString(2,m.getDescripcion());ps.setInt(3,m.getIdCategoria());ps.setString(4,normalizarImagen(m.getImagen()));
            ps.setDouble(5,m.getPrecio());ps.setInt(6,m.getDisponible());ps.setInt(7,m.getPuntosFidelizacion());ps.setInt(8,m.getEstado());ps.setInt(9,m.getIdMenu());return ps.executeUpdate()>0;
        }catch(SQLException|NullPointerException e){System.out.println("Error al actualizar menú: "+e.getMessage());return false;}
    }
    public List<Menu> listar(){
        List<Menu> lista=new ArrayList<>();
        String sql="SELECT m.id_menu,m.nombre,m.descripcion,m.id_categoria,c.nombre AS categoria,m.imagen,m.precio,m.disponible,m.puntos_fidelizacion,m.estado FROM menus m JOIN categorias c ON c.id_categoria=m.id_categoria ORDER BY m.id_menu";
        try(Connection con=cn.getConn();PreparedStatement ps=con.prepareStatement(sql);ResultSet rs=ps.executeQuery()){while(rs.next())lista.add(convertir(rs));}
        catch(SQLException|NullPointerException e){System.out.println("Error al listar menús: "+e.getMessage());} return lista;
    }
    public Menu consultar(int id){String sql="SELECT m.id_menu,m.nombre,m.descripcion,m.id_categoria,c.nombre AS categoria,m.imagen,m.precio,m.disponible,m.puntos_fidelizacion,m.estado FROM menus m JOIN categorias c ON c.id_categoria=m.id_categoria WHERE m.id_menu=?";
        try(Connection con=cn.getConn();PreparedStatement ps=con.prepareStatement(sql)){ps.setInt(1,id);try(ResultSet rs=ps.executeQuery()){return rs.next()?convertir(rs):null;}}catch(SQLException|NullPointerException e){return null;}}
    public boolean inactivar(int id){return cambiarEstado(id,0);} public boolean activar(int id){return cambiarEstado(id,1);}
    private boolean cambiarEstado(int id,int estado){try(Connection con=cn.getConn();PreparedStatement ps=con.prepareStatement("UPDATE menus SET estado=? WHERE id_menu=?")){ps.setInt(1,estado);ps.setInt(2,id);return ps.executeUpdate()>0;}catch(SQLException|NullPointerException e){return false;}}
    public List<Menu> topVentas(int limite) {
        List<Menu> out = new ArrayList<>();
        String sql = "SELECT m.id_menu,m.nombre,m.descripcion,m.id_categoria,c.nombre AS categoria,m.imagen,m.precio,m.disponible,m.puntos_fidelizacion,m.estado,SUM(d.cantidad) AS vendidos FROM detalles_pedidos d INNER JOIN menus m ON m.id_menu=d.menus_id_menu INNER JOIN categorias c ON c.id_categoria=m.id_categoria INNER JOIN pedidos p ON p.id_pedido=d.pedidos_id_pedido WHERE d.estado=1 AND p.estado=1 AND p.estados_pedidos_id_estado_pedido=4 AND m.estado=1 AND m.disponible=1 GROUP BY m.id_menu,m.nombre,m.descripcion,m.id_categoria,c.nombre,m.imagen,m.precio,m.disponible,m.puntos_fidelizacion,m.estado ORDER BY vendidos DESC, m.id_menu ASC LIMIT ?";
        try (Connection con=cn.getConn(); PreparedStatement ps=con.prepareStatement(sql)) {
            ps.setInt(1, limite);
            try (ResultSet rs=ps.executeQuery()) { while(rs.next()) { out.add(convertir(rs)); } }
        } catch(SQLException|NullPointerException e) { System.out.println("Error al listar top ventas: "+e.getMessage()); }
        return out;
    }

    public List<Map<String,Object>> listarCategorias(){List<Map<String,Object>> out=new ArrayList<>();String sql="SELECT id_categoria,nombre,icono FROM categorias WHERE estado=1 ORDER BY id_categoria";try(Connection con=cn.getConn();PreparedStatement ps=con.prepareStatement(sql);ResultSet rs=ps.executeQuery()){while(rs.next()){Map<String,Object>x=new LinkedHashMap<>();x.put("id",rs.getInt(1));x.put("nombre",rs.getString(2));x.put("icono",rs.getString(3));out.add(x);}}catch(SQLException|NullPointerException e){System.out.println("Error al listar categorías: "+e.getMessage());}return out;}
    private Menu convertir(ResultSet rs)throws SQLException{Menu m=new Menu();m.setIdMenu(rs.getInt("id_menu"));m.setNombre(rs.getString("nombre"));m.setDescripcion(rs.getString("descripcion"));m.setIdCategoria(rs.getInt("id_categoria"));m.setCategoria(rs.getString("categoria"));m.setImagen(rs.getString("imagen"));m.setPrecio(rs.getDouble("precio"));m.setDisponible(rs.getInt("disponible"));m.setPuntosFidelizacion(rs.getInt("puntos_fidelizacion"));m.setEstado(rs.getInt("estado"));return m;}
    private String normalizarImagen(String imagen){return imagen==null||imagen.trim().isEmpty()?null:imagen.trim();}
}

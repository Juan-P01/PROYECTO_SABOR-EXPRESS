package Servlet;

import Controlador.FlujoPedidoDAO;
import Controlador.NotificacionDAO;
import Modelo.Usuario;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import java.io.IOException;
import java.sql.SQLException;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

@WebServlet("/operaciones")
public class OperacionesServlet extends HttpServlet {
    private final FlujoPedidoDAO dao = new FlujoPedidoDAO();
    private final NotificacionDAO notificacionDAO = new NotificacionDAO();

    @Override protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        String accion = req.getParameter("accion");
        Usuario usuario = usuario(req);
        if ("menu".equals(accion)) { json(resp, lista(dao.menuDisponible())); return; }
        if ("mesas".equals(accion)) { if (usuario == null) { error(resp,401,"Inicia sesión."); } else json(resp,lista(dao.mesasDisponibles())); return; }
        if ("puntos".equals(accion)) { if (usuario == null || usuario.getRolesIdRol()!=4) { error(resp,403,"Acceso restringido."); } else json(resp,obj(dao.puntosCliente(usuario.getIdUsuario()))); return; }
        if ("historialRepartidor".equals(accion)) { if(usuario==null||usuario.getRolesIdRol()!=5){error(resp,403,"Acceso restringido.");return;} json(resp,lista(dao.historialRepartidor(usuario.getIdUsuario()))); return; }
        if ("direccionBaseRepartidor".equals(accion)) { if(usuario==null||usuario.getRolesIdRol()!=5){error(resp,403,"Acceso restringido.");return;} json(resp,obj(java.util.Map.of("direccionBase",Controlador.FlujoPedidoDAO.DIRECCION_BASE_REPARTIDOR))); return; }
        if ("disponibilidadRepartidor".equals(accion)) { if(usuario==null||usuario.getRolesIdRol()!=5){error(resp,403,"Acceso restringido.");return;} json(resp,obj(dao.disponibilidadRepartidor(usuario.getIdUsuario()))); return; }
        if ("resumenRepartidor".equals(accion)) { if(usuario==null||usuario.getRolesIdRol()!=5){error(resp,403,"Acceso restringido.");return;} json(resp,obj(dao.resumenTurno(usuario.getIdUsuario()))); return; }
        if ("notificaciones".equals(accion)) {
            if (usuario == null || usuario.getRolesIdRol()!=4) { error(resp,403,"Acceso restringido."); return; }
            json(resp,lista(notificacionDAO.listarNoLeidas(usuario.getIdUsuario()))); return;
        }
        if ("pedidos".equals(accion)) {
            if (usuario == null) { error(resp,401,"Inicia sesión."); return; }
            String vista = vistaPorRol(usuario.getRolesIdRol());
            if (vista == null) { error(resp,403,"No tienes acceso a pedidos."); return; }
            if (usuario.getRolesIdRol()==2 && "1".equals(req.getParameter("historial"))) vista="MESERO_HISTORIAL";
            json(resp,lista(dao.pedidos(vista, usuario.getIdUsuario()))); return;
        }
        error(resp,400,"Acción no válida.");
    }

    @Override protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        req.setCharacterEncoding("UTF-8");
        Usuario usuario = usuario(req);
        if (usuario == null) { volver(req,resp,"Debes iniciar sesión para continuar.","/vista/login.jsp"); return; }
        String accion=req.getParameter("accion");
        try {
            if ("eliminarNotificacion".equals(accion) && usuario.getRolesIdRol()==4) {
                int id=numero(req,"idNotificacion");
                boolean ok=notificacionDAO.eliminarParaUsuario(id,usuario.getIdUsuario());
                if(!ok) throw new SQLException("No fue posible eliminar la notificación.");
                json(resp,"{\"ok\":true}"); return;
            }
            if ("eliminarTodasNotificaciones".equals(accion) && usuario.getRolesIdRol()==4) {
                boolean ok=notificacionDAO.eliminarTodasParaUsuario(usuario.getIdUsuario());
                if(!ok) throw new SQLException("No fue posible eliminar las notificaciones.");
                json(resp,"{\"ok\":true}"); return;
            }
            if ("marcarNotificacionesLeidas".equals(accion) && usuario.getRolesIdRol()==4) {
                boolean ok=notificacionDAO.marcarLeidas(usuario.getIdUsuario());
                if(!ok) throw new SQLException("No fue posible marcar las notificaciones como leídas.");
                json(resp,"{\"ok\":true}");
                return;
            }
            if ("crearCliente".equals(accion) && usuario.getRolesIdRol()==4) {
                int pedido=dao.crearPedido(usuario.getIdUsuario(),numeroOpcional(req,"mesa"),req.getParameter("tipoEntrega"),req.getParameter("direccion"),req.getParameter("metodoPago"),req.getParameter("observacionesEntrega"),items(req.getParameter("items")));
                volver(req,resp,"Pedido #"+pedido+" enviado a cocina correctamente.","/index.jsp?pedidoEnviado=1"); return;
            }
            if ("crearMesero".equals(accion) && usuario.getRolesIdRol()==2) {
                int pedido=dao.crearPedidoMesero(usuario.getIdUsuario(),numero(req,"mesa"),req.getParameter("metodoPago"),items(req.getParameter("items")));
                volver(req,resp,"Comanda #"+pedido+" enviada a cocina.","/panel/mesero"); return;
            }
            if ("confirmarPago".equals(accion) && usuario.getRolesIdRol()==6) {
                int pedido=numero(req,"pedido");
                int puntos=dao.confirmarPago(pedido, usuario.getIdUsuario());
                String factura=dao.numeroFactura(pedido);
                volver(req,resp,puntos >= 0 ? "Pago confirmado. Factura demo " + (factura == null ? "generada" : factura) + " creada correctamente. Se asignaron " + puntos + " puntos al cliente." : "No fue posible confirmar el pago.","/panel/cajero");
                return;
            }
            if ("disponibilidadRepartidor".equals(accion)) {
                if (usuario.getRolesIdRol()!=5) { volver(req,resp,"Acceso restringido.","/vista/login.jsp"); return; }
                String valor=req.getParameter("enLinea");
                if (!"1".equals(valor) && !"0".equals(valor)) throw new SQLException("La disponibilidad seleccionada no es válida.");
                if (!dao.cambiarDisponibilidad(usuario.getIdUsuario(),"1".equals(valor))) throw new SQLException("No fue posible actualizar tu disponibilidad.");
                volver(req,resp,"1".equals(valor)?"Ahora estás En línea para recibir domicilios.":"Ahora estás Fuera de línea.","/panel/repartidor"); return;
            }
            if ("cancelarCliente".equals(accion)) {
                if (usuario.getRolesIdRol()!=4) { volver(req,resp,"Acceso restringido.","/vista/login.jsp"); return; }
                int pedidoId=numero(req,"pedido");
                if(!dao.cancelarPedidoCliente(pedidoId,usuario.getIdUsuario(),req.getParameter("detalleIncidencia"))) throw new SQLException("El pedido ya no puede cancelarse porque entró en preparación o fue finalizado.");
                volver(req,resp,"Solicitud de cancelación registrada.","/MisPedidos"); return;
            }
            if ("cancelarDomicilio".equals(accion)) {
                if (usuario.getRolesIdRol()!=5) { volver(req,resp,"Solo el repartidor puede cancelar un domicilio asignado.","/panel/repartidor"); return; }
                int pedidoId=numero(req,"pedido");
                if(req.getParameter("motivoCancelacion")==null || req.getParameter("motivoCancelacion").trim().isEmpty()) throw new SQLException("Debes seleccionar un motivo de cancelación."); if(!dao.cancelarDomicilio(pedidoId,usuario.getIdUsuario(),req.getParameter("motivoCancelacion"),req.getParameter("detalleIncidencia"))) throw new SQLException("El pedido no puede cancelarse en su estado actual.");
                volver(req,resp,"Pedido cancelado","/panel/repartidor");
                return;
            }
            if ("estado".equals(accion)) {
                String rol=nombreRol(usuario.getRolesIdRol());
                int pedidoId=numero(req,"pedido");
                boolean ok;
                if ("REPARTIDOR".equals(rol) && "entregado".equals(req.getParameter("cambio"))) {
                    int puntos=dao.finalizarDomicilio(pedidoId, usuario.getIdUsuario(), req.getParameter("pinEntrega"));
                    ok=puntos>=0;
                    volver(req,resp,ok?"Entrega finalizada y pago demo registrado. Se asignaron "+puntos+" puntos al cliente.":"No fue posible finalizar la entrega.","/panel/repartidor");
                    return;
                }
                ok=dao.actualizarPorRol(pedidoId,usuario.getIdUsuario(),rol,req.getParameter("cambio"));
                volver(req,resp,ok?"Estado actualizado correctamente.":"No fue posible actualizar el pedido. Verifica su estado actual.","/panel/"+vistaPorRol(usuario.getRolesIdRol()).toLowerCase()); return;
            }
            volver(req,resp,"No tienes permiso para realizar esta acción.","/panel/cliente");
        } catch (SQLException | IllegalArgumentException e) { volver(req,resp,e.getMessage(),"/panel/"+vistaPorRol(usuario.getRolesIdRol()).toLowerCase()); }
    }

    private Usuario usuario(HttpServletRequest req) { HttpSession s=req.getSession(false); return s == null ? null : (Usuario)s.getAttribute("usuarioLogueado"); }
    private int numero(HttpServletRequest r,String clave) { try { return Integer.parseInt(r.getParameter(clave)); } catch(Exception e) { throw new IllegalArgumentException("Datos incompletos o inválidos."); } }
    private int numeroOpcional(HttpServletRequest r,String clave) { String valor=r.getParameter(clave); if(valor==null||valor.trim().isEmpty()) return 0; return numero(r,clave); }
    private Map<Integer,Integer> items(String valor) {
        Map<Integer,Integer> salida=new LinkedHashMap<>(); if(valor==null||valor.trim().isEmpty()) return salida;
        for(String item:valor.split(",")){String[] p=item.split(":");if(p.length!=2)throw new IllegalArgumentException("Los productos no son válidos.");try{salida.put(Integer.valueOf(p[0]),Integer.valueOf(p[1]));}catch(NumberFormatException e){throw new IllegalArgumentException("Los productos no son válidos.");}}
        return salida;
    }
    private String vistaPorRol(int rol) { switch(rol){case 2:return "MESERO";case 3:return "COCINA";case 4:return "CLIENTE";case 5:return "REPARTIDOR";case 6:return "CAJERO";default:return null;} }
    private String nombreRol(int rol) { switch(rol){case 2:return "MESERO";case 3:return "COCINERO";case 5:return "REPARTIDOR";case 6:return "CAJERO";default:return "";} }
                                                                                  
    private void volver(HttpServletRequest req,HttpServletResponse resp,String mensaje,String destino)throws IOException {
        req.getSession().setAttribute("mensajeFlash", mensaje);
        resp.sendRedirect(req.getContextPath() + destino);
    }
    private void error(HttpServletResponse r,int status,String mensaje)throws IOException {r.setStatus(status);json(r,"{\"ok\":false,\"mensaje\":"+q(mensaje)+"}");}
    private void json(HttpServletResponse r,String contenido)throws IOException {r.setContentType("application/json;charset=UTF-8");r.getWriter().write(contenido);}
    private String lista(List<Map<String,Object>> l){StringBuilder s=new StringBuilder("[");for(int i=0;i<l.size();i++){if(i>0)s.append(',');s.append(obj(l.get(i)));}return s.append(']').toString();}
    private String obj(Map<String,Object> m){StringBuilder s=new StringBuilder("{");int i=0;for(Map.Entry<String,Object> e:m.entrySet()){if(i++>0)s.append(',');s.append(q(e.getKey())).append(':').append(valor(e.getValue()));}return s.append('}').toString();}
    @SuppressWarnings("unchecked") private String valor(Object v){if(v==null)return "null";if(v instanceof Number||v instanceof Boolean)return String.valueOf(v);if(v instanceof List)return lista((List<Map<String,Object>>)v);return q(String.valueOf(v));}
    private String q(String s){return "\""+String.valueOf(s).replace("\\","\\\\").replace("\"","\\\"").replace("\r","\\r").replace("\n","\\n")+"\"";}
}

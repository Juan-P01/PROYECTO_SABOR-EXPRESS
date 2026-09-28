package Servlet;

import Controlador.AdministradorDAO;
import Controlador.MenuDAO;
import Controlador.MesaDAO;
import Controlador.PedidoDAO;
import Controlador.UsuarioDAO;
import Controlador.Conexion;
import Controlador.NotificacionDAO;
import Modelo.Menu;
import Modelo.Pedido;
import Modelo.Usuario;
import java.io.IOException;
import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneOffset;
import java.util.List;
import java.util.Map;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.annotation.MultipartConfig;
import jakarta.servlet.http.Part;
import java.io.File;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;
import java.util.UUID;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

@WebServlet("/admin-api")
@MultipartConfig(maxFileSize=5*1024*1024,maxRequestSize=7*1024*1024)
public class AdminApiServlet extends HttpServlet {
    private final AdministradorDAO adminDAO = new AdministradorDAO();
    private final UsuarioDAO usuarioDAO = new UsuarioDAO();
    private final MenuDAO menuDAO = new MenuDAO();
    private final MesaDAO mesaDAO = new MesaDAO();
    private final PedidoDAO pedidoDAO = new PedidoDAO();
    private final NotificacionDAO notificacionDAO = new NotificacionDAO();

    @Override protected void doGet(HttpServletRequest req,HttpServletResponse resp)throws ServletException,IOException{process(req,resp,false);}
    @Override protected void doPost(HttpServletRequest req,HttpServletResponse resp)throws ServletException,IOException{req.setCharacterEncoding("UTF-8");process(req,resp,true);}

    private void process(HttpServletRequest req,HttpServletResponse resp,boolean post)throws ServletException,IOException{
        if(!esAdministrador(req.getSession(false))){json(resp,post?403:401,"{\"ok\":false,\"mensaje\":\"Acceso restringido al administrador.\"}");return;}
        String action=req.getParameter("action");
        if(action==null || action.trim().isEmpty()) action=req.getParameter("accion");
        if(action!=null) action=action.trim();
        try{
            switch(action==null?"":action){
                case "auth": json(resp,200,"{\"ok\":true,\"rol\":1}"); break;
                case "dashboard": json(resp,200,"{\"ok\":true,\"data\":"+map(adminDAO.dashboard())+"}"); break;
                case "usuarios":
                case "listar_usuarios": json(resp,200,array(adminDAO.listarUsuarios(req.getParameter("q")))); break;
                case "categorias":
                case "listar_categorias":
                case "listarCategorias": json(resp,200,array(menuDAO.listarCategorias())); break;
                case "canjeables":
                case "listar_canjeables": json(resp,200,array(canjeables())); break;
                case "configuracion": json(resp,200,array(configuracion())); break;
                case "roles": json(resp,200,array(adminDAO.listarRoles())); break;
                case "estadosMesas": json(resp,200,array(adminDAO.listarEstadosMesa())); break;
                case "estadosPedidos": json(resp,200,array(adminDAO.listarEstadosPedido())); break;
                case "menus":
                case "listar_menus": json(resp,200,arrayMenus(menuDAO.listar())); break;
                case "mesas":
                case "listar_mesas": json(resp,200,arrayMesas(mesaDAO.listarMesas())); break;
                case "pedidos":
                case "listar_pedidos": Integer ep=intParam(req.getParameter("estado")); json(resp,200,array(adminDAO.listarPedidos(ep))); break;
                case "reservas":
                case "listar_reservas": json(resp,200,array(adminDAO.listarReservas())); break;
                case "pagos":
                case "listar_pagos": json(resp,200,array(adminDAO.listarPagos())); break;
                case "facturas":
                case "listar_facturas": json(resp,200,array(adminDAO.listarFacturas())); break;
                case "fidelizacion":
                case "listar_fidelizacion": json(resp,200,array(adminDAO.listarFidelizacion())); break;
                case "incidencias":
                case "listar_incidencias": json(resp,200,array(adminDAO.listarPedidos(8))); break;
                case "notificaciones": json(resp,200,array(notificacionDAO.listarNoLeidas(((Usuario)req.getSession(false).getAttribute("usuarioLogueado")).getIdUsuario()))); break;
                case "marcarNotificacionesLeidas": requirePost(post); ok(resp,notificacionDAO.marcarLeidas(((Usuario)req.getSession(false).getAttribute("usuarioLogueado")).getIdUsuario()),"Notificaciones marcadas como leídas."); break;
                case "eliminarNotificacion": requirePost(post); ok(resp,notificacionDAO.eliminarParaUsuario(intParam(req.getParameter("idNotificacion")),((Usuario)req.getSession(false).getAttribute("usuarioLogueado")).getIdUsuario()),"Notificación eliminada."); break;
                case "eliminarTodasNotificaciones": requirePost(post); ok(resp,notificacionDAO.eliminarTodasParaUsuario(((Usuario)req.getSession(false).getAttribute("usuarioLogueado")).getIdUsuario()),"Notificaciones eliminadas."); break;
                case "reportes":
                case "listar_reportes": json(resp,200,reportes()); break;
                case "actualizarUsuario":
                case "editar_usuario":
                case "guardar_usuario": requirePost(post); actualizarUsuario(req,resp); break;
                case "crearUsuario":
                case "crear_usuario": requirePost(post); crearUsuario(req,resp); break;
                case "guardarCanjeable":
                case "crear_canjeable": requirePost(post); guardarCanjeable(req,resp); break;
                case "crearCategoria":
                case "crear_categoria": requirePost(post); guardarCategoria(req,resp,false); break;
                case "actualizarCategoria":
                case "editar_categoria": requirePost(post); guardarCategoria(req,resp,true); break;
                case "eliminarCategoria": requirePost(post); eliminarCategoria(req,resp); break;
                case "eliminarCanjeable": requirePost(post); eliminarCanjeable(req,resp); break;
                case "guardarConfiguracion": requirePost(post); guardarConfiguracion(req,resp); break;
                case "eliminarUsuario": requirePost(post); ok(resp,usuarioDAO.inactivarUsuario(intParam(req.getParameter("idUsuario"))),"Usuario eliminado correctamente."); break;
                case "activarUsuario": requirePost(post); ok(resp,usuarioDAO.activarUsuario(intParam(req.getParameter("idUsuario"))),"Usuario activado correctamente."); break;
                case "crearMenu":
                case "crear_plato": requirePost(post); crearMenu(req,resp); break;
                case "actualizarMenu":
                case "editar_plato": requirePost(post); actualizarMenu(req,resp); break;
                case "eliminarMenu": requirePost(post); ok(resp,menuDAO.inactivar(intParam(req.getParameter("idMenu"))),"Plato eliminado correctamente."); break;
                case "activarMenu": requirePost(post); ok(resp,menuDAO.activar(intParam(req.getParameter("idMenu"))),"Plato activado correctamente."); break;
                case "estadoMesa": requirePost(post); ok(resp,mesaDAO.actualizarEstadoMesa(intParam(req.getParameter("idMesa")),intParam(req.getParameter("idEstadoMesa"))),"Estado de mesa actualizado."); break;
                case "crearMesa":
                case "crear_mesa": requirePost(post); crearMesa(req,resp); break;
                case "actualizarMesa":
                case "editar_mesa": requirePost(post); actualizarMesa(req,resp); break;
                case "eliminarMesa": requirePost(post); ok(resp,mesaDAO.inactivarMesa(intParam(req.getParameter("idMesa"))),"Mesa eliminada correctamente."); break;
                case "activarMesa": requirePost(post); ok(resp,mesaDAO.activarMesa(intParam(req.getParameter("idMesa"))),"Mesa activada correctamente."); break;
                case "estadoPedido": requirePost(post); ok(resp,pedidoDAO.actualizarEstadoPedido(intParam(req.getParameter("idPedido")),intParam(req.getParameter("idEstadoPedido"))),"Estado del pedido actualizado."); break;
                case "estadoReserva": requirePost(post); ok(resp,adminDAO.actualizarEstadoReserva(intParam(req.getParameter("idReserva")),req.getParameter("estado")),"Estado de reserva actualizado."); break;
                default: json(resp,400,"{\"ok\":false,\"mensaje\":\"Acción no válida.\"}");
            }
        }catch(IllegalArgumentException e){json(resp,400,"{\"ok\":false,\"mensaje\":"+q(e.getMessage()==null?"Datos inválidos.":e.getMessage())+"}");}catch(Exception e){e.printStackTrace();json(resp,500,"{\"ok\":false,\"mensaje\":\"Ocurrió un error al procesar la operación.\"}");}
    }

    private void crearUsuario(HttpServletRequest req,HttpServletResponse resp)throws IOException {
        try {
            Usuario u=new Usuario();
            u.setNombre(req.getParameter("nombre"));u.setApellido(req.getParameter("apellido"));u.setDireccion(req.getParameter("direccion"));u.setTelefono(req.getParameter("telefono"));u.setCorreo(req.getParameter("correo"));u.setContrasena(req.getParameter("contrasena"));u.setNroDocumento(req.getParameter("nroDocumento"));u.setRolesIdRol(intParam(req.getParameter("rolesIdRol")));u.setTiposDocumentoIdTipoDocumento(intParam(req.getParameter("tipoDocumento")));u.setAutorizacionDatos(intParam(req.getParameter("autorizacionDatos")));
            if(u.getNombre()==null||u.getNombre().trim().isEmpty())throw new IllegalArgumentException("El nombre es obligatorio.");
            if(u.getApellido()==null||u.getApellido().trim().isEmpty())throw new IllegalArgumentException("El apellido es obligatorio.");
            if(u.getDireccion()==null||u.getDireccion().trim().isEmpty())throw new IllegalArgumentException("La dirección es obligatoria.");
            if(u.getCorreo()==null||u.getCorreo().trim().isEmpty())throw new IllegalArgumentException("El correo es obligatorio.");
            if(u.getRolesIdRol()<=0)throw new IllegalArgumentException("Selecciona un rol válido.");
            if(u.getTiposDocumentoIdTipoDocumento()<=0)throw new IllegalArgumentException("Selecciona un tipo de documento válido.");
            String fn=req.getParameter("fechaNacimiento"); if(fn!=null&&!fn.isEmpty())u.setFechaNacimiento(LocalDate.parse(fn).atStartOfDay().toInstant(ZoneOffset.UTC));
            if(usuarioDAO.existeCorreoODocumentoEnOtroUsuario(u.getCorreo(),u.getNroDocumento(),0)) throw new IllegalArgumentException("El correo o documento ya está registrado.");
            if(usuarioDAO.crearDesdeAdmin(u) <= 0) throw new IllegalArgumentException("No fue posible crear el usuario."); ok(resp,true,"Usuario creado correctamente.");
        } catch(Exception e){ ok(resp,false,e.getMessage()); }
    }

    private List<Map<String,Object>> canjeables() {
        List<Map<String,Object>> out=new ArrayList<>(); String sql="SELECT c.id_canjeable,c.nombre,c.descripcion,c.menus_id_menu,COALESCE(m.nombre,'Sin producto') producto,c.puntos_requeridos,c.activo FROM canjeables c LEFT JOIN menus m ON m.id_menu=c.menus_id_menu ORDER BY c.id_canjeable";
        try(java.sql.Connection c=new Conexion().getConn();java.sql.PreparedStatement p=c.prepareStatement(sql);java.sql.ResultSet r=p.executeQuery()){while(r.next()){Map<String,Object>x=new LinkedHashMap<>();x.put("id",r.getInt(1));x.put("nombre",r.getString(2));x.put("descripcion",r.getString(3));x.put("menuId",r.getObject(4));x.put("producto",r.getString(5));x.put("puntos",r.getInt(6));x.put("activo",r.getBoolean(7));out.add(x);}}catch(Exception e){throw new RuntimeException("No se pudieron cargar los canjeables.",e);}return out;
    }
    private List<Map<String,Object>> configuracion(){
        List<Map<String,Object>> out=new ArrayList<>(); String sql="SELECT clave,valor,estado FROM configuracion_negocio ORDER BY clave";
        try(java.sql.Connection c=new Conexion().getConn();java.sql.PreparedStatement p=c.prepareStatement(sql);java.sql.ResultSet r=p.executeQuery()){while(r.next()){Map<String,Object>x=new LinkedHashMap<>();x.put("clave",r.getString(1));x.put("valor",r.getString(2));x.put("estado",r.getBoolean(3));out.add(x);}}catch(Exception e){throw new RuntimeException("No se pudo cargar la configuración.",e);}return out;
    }
    private void guardarCanjeable(HttpServletRequest req,HttpServletResponse resp)throws IOException {
        Integer id=intParam(req.getParameter("id")); String nombre=req.getParameter("nombre"),desc=req.getParameter("descripcion"); Integer menu=intParam(req.getParameter("menuId")),puntos=intParam(req.getParameter("puntos"));
        if(nombre==null||nombre.trim().isEmpty()||puntos==null||puntos<1) {ok(resp,false,"Nombre y puntos válidos son obligatorios.");return;}
        String sql=id==null?"INSERT INTO canjeables(nombre,descripcion,menus_id_menu,puntos_requeridos,activo) VALUES(?,?,?,?,1)":"UPDATE canjeables SET nombre=?,descripcion=?,menus_id_menu=?,puntos_requeridos=?,activo=? WHERE id_canjeable=?";
        try(java.sql.Connection c=new Conexion().getConn();java.sql.PreparedStatement p=c.prepareStatement(sql)){p.setString(1,nombre.trim());p.setString(2,desc);if(menu==null)p.setNull(3,java.sql.Types.INTEGER);else p.setInt(3,menu);p.setInt(4,puntos);if(id!=null){p.setInt(5,"1".equals(req.getParameter("activo"))?1:0);p.setInt(6,id);}p.executeUpdate();ok(resp,true,"Canjeable guardado correctamente.");}catch(Exception e){ok(resp,false,"No fue posible guardar el canjeable: "+e.getMessage());}
    }
    private void eliminarCanjeable(HttpServletRequest req,HttpServletResponse resp)throws IOException {try(java.sql.Connection c=new Conexion().getConn();java.sql.PreparedStatement p=c.prepareStatement("UPDATE canjeables SET activo=0 WHERE id_canjeable=?")){p.setInt(1,intParam(req.getParameter("id")));ok(resp,p.executeUpdate()>0,"Canjeable desactivado.");}catch(Exception e){ok(resp,false,e.getMessage());}}
    private void guardarConfiguracion(HttpServletRequest req,HttpServletResponse resp)throws IOException {String clave=req.getParameter("clave"),valor=req.getParameter("valor");if(clave==null||valor==null){ok(resp,false,"Datos incompletos.");return;}try(java.sql.Connection c=new Conexion().getConn();java.sql.PreparedStatement p=c.prepareStatement("INSERT INTO configuracion_negocio(clave,valor,estado) VALUES(?,?,1) ON DUPLICATE KEY UPDATE valor=VALUES(valor),estado=1")){p.setString(1,clave);p.setString(2,valor);p.executeUpdate();ok(resp,true,"Configuración actualizada.");}catch(Exception e){ok(resp,false,e.getMessage());}}

    private void actualizarUsuario(HttpServletRequest req,HttpServletResponse resp)throws IOException{
        Integer idUsuario=intParam(first(req,"idUsuario","id_usuario"));
        Integer rol=intParam(first(req,"rolesIdRol","roles_id_rol"));
        Integer tipoDoc=intParam(first(req,"tipoDocumento","tipos_documento_id_tipo_documento"));
        Integer autorizacion=intParam(req.getParameter("autorizacionDatos"));
        if(idUsuario==null || idUsuario<1) throw new IllegalArgumentException("El usuario seleccionado no es válido.");
        if(rol==null || rol<1) throw new IllegalArgumentException("Selecciona un rol válido.");
        if(tipoDoc==null || tipoDoc<1) throw new IllegalArgumentException("Selecciona un tipo de documento válido.");
        if(autorizacion==null) autorizacion=1;
        Usuario u=new Usuario();
        u.setIdUsuario(idUsuario);
        u.setNombre(req.getParameter("nombre"));
        u.setApellido(req.getParameter("apellido"));
        u.setDireccion(req.getParameter("direccion"));
        u.setTelefono(req.getParameter("telefono"));
        u.setCorreo(req.getParameter("correo"));
        u.setNroDocumento(req.getParameter("nroDocumento"));
        u.setRolesIdRol(rol);
        u.setTiposDocumentoIdTipoDocumento(tipoDoc);
        u.setAutorizacionDatos(autorizacion);
        u.setEstado("1".equals(req.getParameter("estado")));
        String fn=first(req,"fechaNacimiento","fecha_nacimiento");
        if(fn!=null&&!fn.isEmpty()) u.setFechaNacimiento(LocalDate.parse(fn).atStartOfDay().toInstant(ZoneOffset.UTC));
        if(u.getNombre()==null||u.getNombre().trim().isEmpty()||u.getApellido()==null||u.getApellido().trim().isEmpty()) throw new IllegalArgumentException("Nombre y apellido son obligatorios.");
        if(u.getCorreo()==null||u.getCorreo().trim().isEmpty()) throw new IllegalArgumentException("El correo es obligatorio.");
        try {
            if(usuarioDAO.existeCorreoODocumentoEnOtroUsuario(u.getCorreo(),u.getNroDocumento(),u.getIdUsuario())) throw new IllegalArgumentException("El correo o documento ya está registrado en otro usuario.");
        } catch(java.sql.SQLException e) {
            throw new IllegalArgumentException("No fue posible validar el correo y documento del usuario.", e);
        }
        if(!usuarioDAO.actualizarDesdeAdmin(u)) throw new IllegalArgumentException("No fue posible actualizar el usuario. Verifica teléfono, documento y datos obligatorios.");
        ok(resp,true,"Usuario actualizado correctamente.");
    }
    private void guardarCategoria(HttpServletRequest req,HttpServletResponse resp,boolean editar)throws IOException {
        String nombre=req.getParameter("nombre"), icono=req.getParameter("icono"); Integer id=intParam(req.getParameter("idCategoria"));
        if(nombre==null||nombre.trim().isEmpty()) throw new IllegalArgumentException("El nombre de la categoría es obligatorio.");
        if(icono==null||icono.trim().isEmpty()) icono="🍓";
        icono=icono.trim();
        java.util.Set<String> iconosPermitidos=new java.util.HashSet<>(java.util.Arrays.asList("🍓","🍦","🥤","🥪","🍰","🍔","🍕","🍫","🧇","🍉","🍋","☕","🥭","🍪"));
        if(!iconosPermitidos.contains(icono)) throw new IllegalArgumentException("Selecciona un icono válido de la lista.");
        String sql=editar?"UPDATE categorias SET nombre=?,icono=?,estado=? WHERE id_categoria=?":"INSERT INTO categorias(nombre,icono,estado) VALUES(?,?,?)";
        try(java.sql.Connection c=new Conexion().getConn();java.sql.PreparedStatement p=c.prepareStatement(sql)){p.setString(1,nombre.trim());p.setString(2,icono.trim());p.setInt(3,"1".equals(req.getParameter("estado"))?1:0);if(editar)p.setInt(4,id);p.executeUpdate();ok(resp,true,editar?"Categoría actualizada correctamente.":"Categoría creada correctamente.");}
        catch(java.sql.SQLIntegrityConstraintViolationException e){ok(resp,false,"Ya existe una categoría con ese nombre.");}
        catch(Exception e){ok(resp,false,"No fue posible guardar la categoría: "+e.getMessage());}
    }
    private void eliminarCategoria(HttpServletRequest req,HttpServletResponse resp)throws IOException { Integer id=intParam(req.getParameter("idCategoria")); if(id==null||id<1)throw new IllegalArgumentException("La categoría seleccionada no es válida."); try(java.sql.Connection c=new Conexion().getConn();java.sql.PreparedStatement p=c.prepareStatement("UPDATE categorias SET estado=0 WHERE id_categoria=?")){p.setInt(1,id);ok(resp,p.executeUpdate()>0,"Categoría desactivada correctamente.");}catch(Exception e){ok(resp,false,"No fue posible eliminar la categoría.");}}

    private void crearMenu(HttpServletRequest req,HttpServletResponse resp)throws ServletException,IOException{if(intParam(req.getParameter("idCategoria"))==null)throw new IllegalArgumentException("Selecciona una categoría.");Menu m=menuFrom(req,false);m.setImagen(guardarImagen(req,null));ok(resp,menuDAO.insertar(m),"Plato creado correctamente.");}

    private String guardarImagen(HttpServletRequest req,String actual)throws ServletException,IOException{
        Part part=req.getPart("imagenArchivo");
        if(part==null || part.getSize()==0) return actual;
        String tipo=part.getContentType();
        if(tipo==null || !(tipo.equals("image/png")||tipo.equals("image/jpeg")||tipo.equals("image/webp"))) throw new IOException("La imagen debe ser PNG, JPG o WEBP.");
        if(part.getSize()>5*1024*1024) throw new IOException("La imagen no puede superar 5 MB.");
        String ext=tipo.equals("image/png")?".png":tipo.equals("image/webp")?".webp":".jpg";
        File dir=new File(getServletContext().getRealPath("/vista/assets/menu"));
        if(!dir.exists()&&!dir.mkdirs()) throw new IOException("No fue posible preparar el directorio de imágenes.");
        String nombre="menu-"+UUID.randomUUID()+ext; File destino=new File(dir,nombre);
        try(java.io.InputStream in=part.getInputStream()){Files.copy(in,destino.toPath(),StandardCopyOption.REPLACE_EXISTING);}
        return "vista/assets/menu/"+nombre;
    }
    private void crearMesa(HttpServletRequest req,HttpServletResponse resp)throws IOException{Modelo.Mesa m=mesaFrom(req,false);ok(resp,mesaDAO.insertarMesa(m),"Mesa creada correctamente.");}
    private void actualizarMesa(HttpServletRequest req,HttpServletResponse resp)throws IOException{Modelo.Mesa m=mesaFrom(req,true);ok(resp,mesaDAO.actualizarMesa(m),"Mesa actualizada correctamente.");}
    private Modelo.Mesa mesaFrom(HttpServletRequest req,boolean id){Modelo.Mesa m=new Modelo.Mesa();if(id)m.setIdMesa(intParam(req.getParameter("idMesa")));m.setNumeroMesa(intParam(req.getParameter("numero")));m.setEstadosMesasIdEstadoMesa(intParam(req.getParameter("idEstadoMesa")));m.setEstado("1".equals(req.getParameter("estado")));return m;}
    private void actualizarMenu(HttpServletRequest req,HttpServletResponse resp)throws ServletException,IOException{if(intParam(req.getParameter("idCategoria"))==null)throw new IllegalArgumentException("Selecciona una categoría.");Menu m=menuFrom(req,true);m.setImagen(guardarImagen(req,req.getParameter("imagenActual")));ok(resp,menuDAO.actualizar(m),"Plato actualizado correctamente");}
    private Menu menuFrom(HttpServletRequest req,boolean id){
        Menu m=new Menu();
        if(id){Integer idMenu=intParam(req.getParameter("idMenu"));if(idMenu==null||idMenu<1)throw new IllegalArgumentException("El plato seleccionado no es válido.");m.setIdMenu(idMenu);}
        String nombre=req.getParameter("nombre"),descripcion=req.getParameter("descripcion");
        Integer categoria=intParam(req.getParameter("idCategoria")), puntos=intParam(req.getParameter("puntosFidelizacion"));
        String precioTexto=req.getParameter("precio");
        if(nombre==null||nombre.trim().isEmpty())throw new IllegalArgumentException("El nombre del plato es obligatorio.");
        if(descripcion==null||descripcion.trim().isEmpty())throw new IllegalArgumentException("La descripción del plato es obligatoria.");
        if(categoria==null||categoria<1)throw new IllegalArgumentException("Selecciona una categoría.");
        if(puntos==null||puntos<0)throw new IllegalArgumentException("Los puntos de fidelización deben ser 0 o mayores.");
        if(precioTexto==null||precioTexto.trim().isEmpty())throw new IllegalArgumentException("El precio del plato es obligatorio.");
        final double precio; try{precio=Double.parseDouble(precioTexto);}catch(NumberFormatException e){throw new IllegalArgumentException("El precio del plato no es válido.");}
        if(precio<0)throw new IllegalArgumentException("El precio no puede ser negativo.");
        m.setNombre(nombre.trim());m.setDescripcion(descripcion.trim());m.setIdCategoria(categoria);m.setImagen(req.getParameter("imagenActual"));m.setPuntosFidelizacion(puntos);m.setPrecio(precio);m.setDisponible("1".equals(req.getParameter("disponible")) ? 1 : 0);m.setEstado("1".equals(req.getParameter("estado")) ? 1 : 0);return m;
    }
    private String reportes(){StringBuilder s=new StringBuilder("{\"ok\":true,\"dashboard\":").append(obj(adminDAO.dashboard())).append(",\"semanal\":{\"ingresos\":").append(array(adminDAO.ingresosUltimosDias(7))).append(",\"pedidos\":").append(array(adminDAO.pedidosPorDia(7))).append(",\"clientes\":").append(array(adminDAO.clientesPorDia(7))).append("},\"mensual\":{\"ingresos\":").append(array(adminDAO.ingresosUltimosDias(30))).append(",\"pedidos\":").append(array(adminDAO.pedidosPorDia(30))).append(",\"clientes\":").append(array(adminDAO.clientesPorDia(30))).append("},\"ranking\":").append(array(adminDAO.rankingPlatos())).append("}");return s.toString();}

    private boolean esAdministrador(HttpSession s){
        if(s==null) return false;
        Object r=s.getAttribute("rolUsuario");
        if(r instanceof Number && ((Number)r).intValue()==1) return true;
        if(r instanceof String){ try{ if(Integer.parseInt((String)r)==1) return true; }catch(NumberFormatException ignored){} }
        Object u=s.getAttribute("usuarioLogueado");
        if(u instanceof Usuario) return ((Usuario)u).getRolesIdRol()==1;
        return false;
    }
    private String first(HttpServletRequest req,String... names){for(String n:names){String v=req.getParameter(n);if(v!=null&&!v.trim().isEmpty())return v;}return null;}
    private void requirePost(boolean post)throws IOException{if(!post)throw new IllegalArgumentException("La operación requiere POST.");}
    private Integer intParam(String v){return v==null||v.isEmpty()?null:Integer.valueOf(v);}
    private void ok(HttpServletResponse r,boolean ok,String msg)throws IOException{json(r,ok?200:400,"{\"ok\":"+ok+",\"mensaje\":"+q(msg)+"}");}
    private void json(HttpServletResponse r,int status,String body)throws IOException{r.setStatus(status);r.setContentType("application/json;charset=UTF-8");r.setCharacterEncoding("UTF-8");r.setHeader("Cache-Control","no-store, no-cache, must-revalidate, max-age=0");r.getWriter().write(body);}
    private String obj(Map<String,Object> m){return "{\"ok\":true,\"data\":"+array(List.of(m))+"}".replace("[{","{").replace("}]","}");}
    private String array(List<Map<String,Object>> list){StringBuilder s=new StringBuilder("[");for(int i=0;i<list.size();i++){if(i>0)s.append(',');s.append(map(list.get(i)));}return s.append(']').toString();}
    private String map(Map<String,Object> m){StringBuilder s=new StringBuilder("{");int i=0;for(Map.Entry<String,Object>e:m.entrySet()){if(i++>0)s.append(',');s.append(q(e.getKey())).append(':').append(value(e.getValue()));}return s.append('}').toString();}
    private String value(Object v){if(v==null)return"null";if(v instanceof Number||v instanceof Boolean)return String.valueOf(v);if(v instanceof java.util.Date)return q(new java.text.SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss").format((java.util.Date)v));return q(String.valueOf(v));}
    private String q(String v){if(v==null)return"null";return "\""+v.replace("\\","\\\\").replace("\"","\\\"").replace("\r","\\r").replace("\n","\\n").replace("\t","\\t")+"\"";}
    private String arrayMenus(List<Menu> l){StringBuilder s=new StringBuilder("[");for(int i=0;i<l.size();i++){Menu m=l.get(i);if(i>0)s.append(',');s.append("{\"idMenu\":").append(m.getIdMenu()).append(",\"nombre\":").append(q(m.getNombre())).append(",\"descripcion\":").append(q(m.getDescripcion())).append(",\"idCategoria\":").append(m.getIdCategoria()).append(",\"categoria\":").append(q(m.getCategoria())).append(",\"imagen\":").append(q(m.getImagen())).append(",\"precio\":").append(m.getPrecio()).append(",\"puntosFidelizacion\":").append(m.getPuntosFidelizacion()).append(",\"disponible\":").append(m.getDisponible()==1).append(",\"estado\":").append(m.getEstado()==1).append('}');}return s.append(']').toString();}
    private String arrayMesas(List<Modelo.Mesa> l){StringBuilder s=new StringBuilder("[");for(int i=0;i<l.size();i++){Modelo.Mesa m=l.get(i);if(i>0)s.append(',');s.append("{\"idMesa\":").append(m.getIdMesa()).append(",\"numero\":").append(m.getNumeroMesa()).append(",\"idEstado\":").append(m.getEstadosMesasIdEstadoMesa()).append(",\"estado\":").append(q(m.getEstadoMesa())).append(",\"activo\":").append(m.isEstado()).append("}");}return s.append(']').toString();}
}

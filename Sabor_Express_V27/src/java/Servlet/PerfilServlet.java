package Servlet;

import Controlador.UsuarioDAO;
import Modelo.Usuario;
import java.io.IOException;
import java.time.Instant;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

   
                                                                                        
   
@WebServlet("/Perfil")
public class PerfilServlet extends HttpServlet {

    private final UsuarioDAO dao = new UsuarioDAO();

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        HttpSession session = request.getSession(false);
        Usuario actual = session == null ? null : (Usuario) session.getAttribute("usuarioLogueado");
        if (actual == null) { response.sendRedirect(request.getContextPath()+"/vista/login.jsp"); return; }
        String nombre=trim(request.getParameter("nombre")), apellido=trim(request.getParameter("apellido"));
        String direccion=trim(request.getParameter("direccion")), telefono=trim(request.getParameter("telefono"));
        String correo=trim(request.getParameter("correo")).toLowerCase(), documento=trim(request.getParameter("nroDocumento"));
        int tipo;
        try { tipo=Integer.parseInt(request.getParameter("tipoDocumento")); } catch(Exception e){ tipo=0; }
        String error=null;
        if(nombre.length()<2 || nombre.length()>50 || !nombre.matches("[\\p{L} .'-]+")) error="El nombre no es válido.";
        else if(apellido.length()<2 || apellido.length()>50 || !apellido.matches("[\\p{L} .'-]+")) error="El apellido no es válido.";
        else if(direccion.length()<5 || direccion.length()>150) error="La dirección debe tener entre 5 y 150 caracteres.";
        else if(!telefono.matches("\\d{10}")) error="El teléfono debe contener exactamente 10 dígitos.";
        else if(!documento.matches("\\d{10}")) error="El documento debe contener exactamente 10 dígitos.";
        else if(!correo.matches("^[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}$")) error="Ingresa un correo electrónico válido.";
        else if(tipo<1) error="Selecciona un tipo de documento válido.";
        try {
            if(error==null && dao.existeCorreoODocumentoEnOtroUsuario(correo,documento,actual.getIdUsuario())) error="El correo o documento ya está registrado en otra cuenta.";
            if(error==null){ Usuario u=new Usuario(); u.setIdUsuario(actual.getIdUsuario()); u.setNombre(nombre); u.setApellido(apellido); u.setDireccion(direccion); u.setTelefono(telefono); u.setCorreo(correo); u.setNroDocumento(documento); u.setTiposDocumentoIdTipoDocumento(tipo);
                if(!dao.actualizarPerfilCliente(u)) error="No fue posible actualizar tu perfil.";
                else { session.setAttribute("mensajeFlash","Perfil actualizado correctamente."); response.sendRedirect(request.getContextPath()+"/Perfil"); return; } }
        } catch(Exception e){ error="No fue posible actualizar tu perfil. Verifica la conexión con la base de datos."; }
        request.setAttribute("errorPerfil", error); doGet(request,response);
    }

    private String trim(String value){ return value==null?"":value.trim(); }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        HttpSession session = request.getSession(false);
        Usuario sesionUsuario = session == null
                ? null
                : (Usuario) session.getAttribute("usuarioLogueado");

        if (sesionUsuario == null) {
            response.sendRedirect(request.getContextPath() + "/vista/login.jsp");
            return;
        }

        
        Usuario usuario = dao.consultarUsuarioPorId(sesionUsuario.getIdUsuario());

        if (usuario == null) {
            if (session != null) {
                session.invalidate();
            }
            response.sendRedirect(request.getContextPath() + "/vista/login.jsp");
            return;
        }

        session.setAttribute("usuarioLogueado", usuario);
        session.setAttribute("rolUsuario", usuario.getRolesIdRol());

        request.setAttribute("usuario", usuario);
        request.setAttribute("nombreRol", dao.obtenerNombreRol(usuario.getRolesIdRol()));
        request.setAttribute("tipoDocumento", dao.obtenerTipoDocumento(usuario.getTiposDocumentoIdTipoDocumento()));
        request.setAttribute("puntosCliente", dao.obtenerPuntosCliente(usuario.getIdUsuario()));

        request.getRequestDispatcher("/vista/perfil.jsp").forward(request, response);
    }
}

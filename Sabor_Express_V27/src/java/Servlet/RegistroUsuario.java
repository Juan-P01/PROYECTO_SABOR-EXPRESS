package Servlet;

import Controlador.UsuarioDAO;
import Modelo.Usuario;
import java.io.IOException;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

@WebServlet("/RegistrarUsuario")
public class RegistroUsuario extends HttpServlet {

    private static final String VISTA_REGISTRO = "/vista/registro.jsp";

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        request.getRequestDispatcher(VISTA_REGISTRO).forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        request.setCharacterEncoding("UTF-8");

        
        String nombre = request.getParameter("nombre");
        String apellido = request.getParameter("apellido");
        String tipoDocumentoStr = request.getParameter("tipoDocumento");
        String nroDocumentoStr = request.getParameter("nroDocumento");
        String correo = request.getParameter("correo");
        String telefono = request.getParameter("telefono");
        String direccion = request.getParameter("direccion");
        String pass = request.getParameter("pass");
        String confirmar = request.getParameter("confirmar");
        String autorizacion = request.getParameter("autorizacionDatos");

        
        request.setAttribute("paramNombre", nombre);
        request.setAttribute("paramApellido", apellido);
        request.setAttribute("paramTipoDocumento", tipoDocumentoStr);
        request.setAttribute("paramNroDocumento", nroDocumentoStr);
        request.setAttribute("paramCorreo", correo);
        request.setAttribute("paramTelefono", telefono);
        request.setAttribute("paramDireccion", direccion);
        request.setAttribute("paramAutorizacionDatos", autorizacion);

        

        if (esVacio(nombre)) {
            request.setAttribute("error", "El campo Nombre es obligatorio.");
            request.getRequestDispatcher(VISTA_REGISTRO).forward(request, response);
            return;
        }

        if (esVacio(apellido)) {
            request.setAttribute("error", "El campo Apellido es obligatorio.");
            request.getRequestDispatcher(VISTA_REGISTRO).forward(request, response);
            return;
        }

        if (esVacio(tipoDocumentoStr)) {
            request.setAttribute("error", "Debes seleccionar un Tipo de Documento.");
            request.getRequestDispatcher(VISTA_REGISTRO).forward(request, response);
            return;
        }

        int tipoDoc;
        try {
            tipoDoc = Integer.parseInt(tipoDocumentoStr.trim());
        } catch (NumberFormatException e) {
            request.setAttribute("error", "El Tipo de Documento seleccionado no es válido.");
            request.getRequestDispatcher(VISTA_REGISTRO).forward(request, response);
            return;
        }

        if (esVacio(nroDocumentoStr)) {
            request.setAttribute("error", "El campo Número de Documento es obligatorio.");
            request.getRequestDispatcher(VISTA_REGISTRO).forward(request, response);
            return;
        }

        nroDocumentoStr = nroDocumentoStr.trim();
        if (!nroDocumentoStr.matches("\\d{10}")) {
            request.setAttribute("error", "El Número de Documento debe contener exactamente 10 dígitos.");
            request.getRequestDispatcher(VISTA_REGISTRO).forward(request, response);
            return;
        }

        if (esVacio(correo)) {
            request.setAttribute("error", "El campo Correo electrónico es obligatorio.");
            request.getRequestDispatcher(VISTA_REGISTRO).forward(request, response);
            return;
        }

        if (esVacio(telefono)) {
            request.setAttribute("error", "El campo Teléfono es obligatorio.");
            request.getRequestDispatcher(VISTA_REGISTRO).forward(request, response);
            return;
        }
        telefono = telefono.trim();
        if (!telefono.matches("\\d{10}")) {
            request.setAttribute("error", "El Número de Teléfono debe contener exactamente 10 dígitos.");
            request.getRequestDispatcher(VISTA_REGISTRO).forward(request, response);
            return;
        }

        if (esVacio(direccion)) {
            request.setAttribute("error", "El campo Dirección es obligatorio.");
            request.getRequestDispatcher(VISTA_REGISTRO).forward(request, response);
            return;
        }

        if (esVacio(pass)) {
            request.setAttribute("error", "El campo Contraseña es obligatorio.");
            request.getRequestDispatcher(VISTA_REGISTRO).forward(request, response);
            return;
        }

        if (pass.trim().length() < 6) {
            request.setAttribute("error", "La contraseña debe tener al menos 6 caracteres.");
            request.getRequestDispatcher(VISTA_REGISTRO).forward(request, response);
            return;
        }

        if (esVacio(confirmar)) {
            request.setAttribute("error", "Debes confirmar la contraseña.");
            request.getRequestDispatcher(VISTA_REGISTRO).forward(request, response);
            return;
        }

        if (!pass.equals(confirmar)) {
            request.setAttribute("error", "La contraseña y su confirmación no coinciden.");
            request.getRequestDispatcher(VISTA_REGISTRO).forward(request, response);
            return;
        }

        if (!"1".equals(autorizacion)) {
            request.setAttribute("error", "Debes autorizar el tratamiento de datos personales para continuar.");
            request.getRequestDispatcher(VISTA_REGISTRO).forward(request, response);
            return;
        }

        
        Usuario nuevoUsuario = new Usuario();
        nuevoUsuario.setNombre(nombre.trim());
        nuevoUsuario.setApellido(apellido.trim());
        nuevoUsuario.setCorreo(correo.trim());
        nuevoUsuario.setContrasena(pass);
        nuevoUsuario.setDireccion(direccion.trim());
        nuevoUsuario.setTelefono(telefono.trim());
        nuevoUsuario.setNroDocumento(nroDocumentoStr);
        nuevoUsuario.setTiposDocumentoIdTipoDocumento(tipoDoc);
        nuevoUsuario.setRolesIdRol(4); 
        nuevoUsuario.setAutorizacionDatos(1);

        
        UsuarioDAO midao = new UsuarioDAO();
        boolean guardado = midao.registrarCliente(nuevoUsuario);

        
        if (guardado) {
            
            HttpSession anterior = request.getSession(false);
            if (anterior != null) { anterior.invalidate(); }
            response.sendRedirect(request.getContextPath() + "/vista/login.jsp?registro=exitoso");
        } else {
            request.setAttribute("error", "No se pudo registrar la cuenta. Intenta nuevamente.");
            request.getRequestDispatcher(VISTA_REGISTRO).forward(request, response);
        }
    }

    private boolean esVacio(String valor) {
        return valor == null || valor.trim().isEmpty();
    }
}

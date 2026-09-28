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

@WebServlet("/IniciarSesion")
public class InicioSesion extends HttpServlet {

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        request.setCharacterEncoding("UTF-8");

        String correo = request.getParameter("correo");
        String pass = request.getParameter("pass");

        if (correo == null || correo.trim().isEmpty() || pass == null || pass.trim().isEmpty()) {
            request.setAttribute("mensaje", "Por favor ingresa tu correo/nombre de usuario y contraseña.");
            request.getRequestDispatcher("/vista/login.jsp").forward(request, response);
            return;
        }

        UsuarioDAO dao = new UsuarioDAO();
        Usuario registrado = dao.consultarUsuarioPorCorreo(correo.trim());
        if (registrado != null && !registrado.isEstado()) {
            request.setAttribute("mensaje", "Esta cuenta está inactiva. Comunícate con el administrador para solicitar su activación.");
            request.getRequestDispatcher("/vista/login.jsp").forward(request, response);
            return;
        }
        Usuario usuario = dao.validarCredenciales(correo.trim(), pass);

        if (usuario == null) {
            request.setAttribute("mensaje", "Correo/nombre de usuario o contraseña incorrectos.");
            request.getRequestDispatcher("/vista/login.jsp").forward(request, response);
            return;
        }

        
        HttpSession anterior = request.getSession(false);
        if (anterior != null) {
            anterior.invalidate();
        }

        HttpSession session = request.getSession(true);
        session.setAttribute("usuarioLogueado", usuario);
        session.setAttribute("rolUsuario", usuario.getRolesIdRol());
        session.setMaxInactiveInterval(30 * 60);

        response.sendRedirect(request.getContextPath() + destinoPorRol(usuario.getRolesIdRol()));
    }

    private String destinoPorRol(int rol) {
        switch (rol) {
            case 1: return "/vista/admin.jsp";
            case 2: return "/panel/mesero";
            case 3: return "/panel/cocina";
            case 4: return "/index.jsp";
            case 5: return "/panel/repartidor";
            case 6: return "/panel/cajero";
            default: return "/vista/login.jsp";
        }
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.sendRedirect(request.getContextPath() + "/vista/login.jsp");
    }
}

package Servlet;

import java.io.IOException;
import jakarta.servlet.Filter;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebFilter;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

                                                                                                     
@WebFilter(urlPatterns = "/vista/admin.jsp")
public class AdminPageFilter implements Filter {
    @Override
    public void doFilter(jakarta.servlet.ServletRequest request, jakarta.servlet.ServletResponse response,
            FilterChain chain) throws IOException, ServletException {
        HttpServletRequest req = (HttpServletRequest) request;
        HttpServletResponse resp = (HttpServletResponse) response;
        HttpSession session = req.getSession(false);
        Object role = session == null ? null : session.getAttribute("rolUsuario");
        boolean admin = role instanceof Number && ((Number) role).intValue() == 1;
        if (!admin && role instanceof String) {
            try { admin = Integer.parseInt((String) role) == 1; } catch (NumberFormatException ignored) {}
        }
        if (!admin && session != null && session.getAttribute("usuarioLogueado") instanceof Modelo.Usuario) {
            admin = ((Modelo.Usuario) session.getAttribute("usuarioLogueado")).getRolesIdRol() == 1;
        }
        if (!admin) {
            resp.sendRedirect(req.getContextPath() + "/vista/login.jsp");
            return;
        }
        chain.doFilter(request, response);
    }
}

package Servlet;

import Controlador.FlujoPedidoDAO;
import Modelo.Usuario;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import java.io.IOException;

@WebServlet("/MisPedidos")
public class MisPedidosServlet extends HttpServlet {
    private final FlujoPedidoDAO dao = new FlujoPedidoDAO();
    @Override protected void doGet(HttpServletRequest req,HttpServletResponse resp)throws ServletException,IOException{
        HttpSession s=req.getSession(false); Usuario u=s==null?null:(Usuario)s.getAttribute("usuarioLogueado");
        if(u==null){resp.sendRedirect(req.getContextPath()+"/vista/login.jsp");return;}
        if(u.getRolesIdRol()!=4){resp.sendRedirect(req.getContextPath()+"/Perfil");return;}
        req.setAttribute("pedidos",dao.pedidos("CLIENTE",u.getIdUsuario()));
        req.getRequestDispatcher("/vista/misPedidos.jsp").forward(req,resp);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {
        response.setStatus(HttpServletResponse.SC_METHOD_NOT_ALLOWED);
        response.setContentType("application/json;charset=UTF-8");
        response.getWriter().write("{\"ok\":false,\"mensaje\":\"Esta operación solo admite GET.\"}");
    }
}

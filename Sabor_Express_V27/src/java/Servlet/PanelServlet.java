package Servlet;

import Modelo.Usuario;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import java.io.IOException;

@WebServlet("/panel/*")
public class PanelServlet extends HttpServlet {
    @Override protected void doGet(HttpServletRequest req,HttpServletResponse resp)throws ServletException,IOException {
        HttpSession sesion=req.getSession(false); Usuario usuario=sesion==null?null:(Usuario)sesion.getAttribute("usuarioLogueado");
        if(usuario==null){req.setAttribute("mensaje","Inicia sesión para acceder a tu panel.");req.getRequestDispatcher("/vista/login.jsp").forward(req,resp);return;}
        Object mensajeFlash=sesion.getAttribute("mensajeFlash");
        if(mensajeFlash!=null){req.setAttribute("mensaje",mensajeFlash);sesion.removeAttribute("mensajeFlash");}
        String destino=req.getPathInfo()==null?"":req.getPathInfo(); int rol=usuario.getRolesIdRol();
        if("/mesero".equals(destino)&&rol==2){req.getRequestDispatcher("/vista/mesero.jsp").forward(req,resp);return;}
        if("/cocina".equals(destino)&&rol==3){req.getRequestDispatcher("/vista/cocina.jsp").forward(req,resp);return;}
        if("/cliente".equals(destino)&&rol==4){req.getRequestDispatcher("/vista/cliente.jsp").forward(req,resp);return;}
        if("/repartidor".equals(destino)&&rol==5){req.getRequestDispatcher("/vista/repartidor.jsp").forward(req,resp);return;}
        if("/cajero".equals(destino)&&rol==6){req.getRequestDispatcher("/vista/cajero.jsp").forward(req,resp);return;}
        req.setAttribute("mensaje","No tienes permisos para esa sección.");req.getRequestDispatcher("/vista/login.jsp").forward(req,resp);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {
        response.setStatus(HttpServletResponse.SC_METHOD_NOT_ALLOWED);
        response.setContentType("application/json;charset=UTF-8");
        response.getWriter().write("{\"ok\":false,\"mensaje\":\"Esta operación solo admite GET.\"}");
    }
}

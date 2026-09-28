package Servlet;

import Controlador.MenuDAO;
import Modelo.Menu;
import java.io.IOException;
import java.util.List;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

                                                                           
@WebServlet("/menu-catalogo")
public class MenuCatalogoServlet extends HttpServlet {
    private final MenuDAO menuDAO = new MenuDAO();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        response.setContentType("application/json;charset=UTF-8");
        try {
            if ("1".equals(request.getParameter("topVentas"))) {
                List<Menu> menus = menuDAO.topVentas(6);
                response.getWriter().write(menusJson(menus)); return;
            }
            if ("1".equals(request.getParameter("categorias"))) {
                List<java.util.Map<String,Object>> cats = menuDAO.listarCategorias();
                StringBuilder cb=new StringBuilder("[");
                for(int i=0;i<cats.size();i++){if(i>0)cb.append(",");java.util.Map<String,Object> c=cats.get(i);cb.append("{\"id\":").append(c.get("id")).append(",\"nombre\":").append(q(String.valueOf(c.get("nombre")))).append(",\"icono\":").append(q(String.valueOf(c.get("icono")))).append("}");}
                response.getWriter().write(cb.append("]").toString()); return;
            }
            List<Menu> menus = menuDAO.listar();
            response.getWriter().write(menusJson(menus));
        } catch (Exception e) {
            response.setStatus(500);
            response.getWriter().write("{\"ok\":false,\"mensaje\":\"No se pudo cargar el menú.\"}");
        }
    }

    private String menusJson(List<Menu> menus) {
        StringBuilder json = new StringBuilder("[");
        boolean first = true;
        for (Menu m : menus) {
            if (m.getEstado() != 1 || m.getDisponible() != 1) continue;
            if (!first) json.append(",");
            first = false;
            json.append("{\"idMenu\":").append(m.getIdMenu())
                .append(",\"nombre\":").append(q(m.getNombre()))
                .append(",\"descripcion\":").append(q(m.getDescripcion()))
                .append(",\"categoria\":").append(q(m.getCategoria()))
                .append(",\"imagen\":").append(q(m.getImagen()))
                .append(",\"precio\":").append(m.getPrecio())
                .append("}");
        }
        return json.append("]").toString();
    }

    private String q(String value) {
        if (value == null) return "null";
        return "\"" + value.replace("\\", "\\\\").replace("\"", "\\\"").replace("\r", "\\r").replace("\n", "\\n") + "\"";
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {
        response.setStatus(HttpServletResponse.SC_METHOD_NOT_ALLOWED);
        response.setContentType("application/json;charset=UTF-8");
        response.getWriter().write("{\"ok\":false,\"mensaje\":\"Esta operación solo admite GET.\"}");
    }
}

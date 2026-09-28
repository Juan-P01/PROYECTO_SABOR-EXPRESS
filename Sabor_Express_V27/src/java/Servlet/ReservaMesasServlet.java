package Servlet;
import Controlador.ReservaDAO;
import Modelo.Usuario;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;
import java.io.IOException;
import java.sql.Date;
import java.sql.Time;
@WebServlet("/ReservaMesas")
public class ReservaMesasServlet extends HttpServlet{
 private final ReservaDAO dao=new ReservaDAO();
 protected void doGet(HttpServletRequest req,HttpServletResponse resp)throws IOException{
  HttpSession s=req.getSession(false);Usuario u=s==null?null:(Usuario)s.getAttribute("usuarioLogueado");
  if(u==null||(u.getRolesIdRol()!=4 && u.getRolesIdRol()!=2)){resp.setStatus(403);resp.getWriter().print("[]");return;}
  try{Date f=Date.valueOf(req.getParameter("fecha"));Time h=Time.valueOf(req.getParameter("hora")+":00");resp.setContentType("application/json;charset=UTF-8");StringBuilder b=new StringBuilder("[");int i=0;for(java.util.Map<String,Object> m:dao.mesasDisponiblesParaReserva(f,h)){if(i++>0)b.append(',');b.append("{\"id\":").append(m.get("id")).append(",\"numero\":").append(m.get("numero")).append("}");}resp.getWriter().print(b.append(']'));}catch(Exception e){resp.setStatus(400);resp.getWriter().print("[]");}
 }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {
        response.setStatus(HttpServletResponse.SC_METHOD_NOT_ALLOWED);
        response.setContentType("application/json;charset=UTF-8");
        response.getWriter().write("{\"ok\":false,\"mensaje\":\"Esta operación solo admite GET.\"}");
    }
}

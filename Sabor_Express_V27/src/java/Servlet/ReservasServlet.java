package Servlet;

import Controlador.ReservaDAO;
import Modelo.Usuario;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;
import java.io.IOException;
import java.sql.Date;
import java.sql.Time;
import java.time.LocalDate;
import java.time.LocalTime;

                                                
@WebServlet("/Reservas")
public class ReservasServlet extends HttpServlet {
    private final ReservaDAO dao=new ReservaDAO();

    @Override protected void doGet(HttpServletRequest req,HttpServletResponse resp)throws ServletException,IOException{
        Usuario u=usuario(req);
        if(u==null||(u.getRolesIdRol()!=4 && u.getRolesIdRol()!=2)){resp.sendRedirect(req.getContextPath()+"/vista/login.jsp");return;}
        req.setAttribute("reservas",u.getRolesIdRol()==2 ? dao.reservasTodas() : dao.reservasCliente(u.getIdUsuario())); req.setAttribute("rolReserva",u.getRolesIdRol()); if(u.getRolesIdRol()==2) req.setAttribute("clientesReserva",dao.clientesParaReserva()); req.setAttribute("limiteReservaMin",LocalDate.now().toString()); req.setAttribute("limiteReserva",LocalDate.now().plusMonths(3).toString());
        req.getRequestDispatcher("/vista/reservas.jsp").forward(req,resp);
    }
    @Override protected void doPost(HttpServletRequest req,HttpServletResponse resp)throws ServletException,IOException{
        req.setCharacterEncoding("UTF-8"); Usuario u=usuario(req);
        if(u==null||(u.getRolesIdRol()!=4 && u.getRolesIdRol()!=2)){resp.sendRedirect(req.getContextPath()+"/vista/login.jsp");return;}
        try{
            if(u.getRolesIdRol()==2 && "confirmarLlegada".equals(req.getParameter("accion"))){
                int idReserva=Integer.parseInt(req.getParameter("idReserva"));
                if(!dao.confirmarLlegada(idReserva)) throw new IllegalArgumentException("La reserva no está disponible para confirmar la llegada.");
                req.getSession().setAttribute("mensajeFlash","Llegada del cliente confirmada correctamente.");
                resp.sendRedirect(req.getContextPath()+"/Reservas");
                return;
            }
            if(u.getRolesIdRol()==2) throw new IllegalArgumentException("El mesero solo puede consultar reservas y confirmar la llegada del cliente.");
            LocalDate f=LocalDate.parse(req.getParameter("fecha"));
            LocalTime h=LocalTime.parse(req.getParameter("hora"));
            int personas=Integer.parseInt(req.getParameter("personas"));
            int mesa=Integer.parseInt(req.getParameter("mesa"));
            if(u.getRolesIdRol()==2){ String cid=req.getParameter("clienteId"); if(cid==null||cid.isBlank()) throw new IllegalArgumentException("Selecciona el cliente para la reserva."); dao.crearReservaCliente(Integer.parseInt(cid),Date.valueOf(f),Time.valueOf(h),personas,mesa,req.getParameter("observaciones")); } else { dao.crearReserva(u.getIdUsuario(),Date.valueOf(f),Time.valueOf(h),personas,mesa,req.getParameter("observaciones")); }
            req.getSession().setAttribute("mensajeFlash","Reserva creada correctamente.");
        }catch(Exception e){req.setAttribute("errorReserva",e.getMessage());}
        doGet(req,resp);
    }
    private Usuario usuario(HttpServletRequest req){HttpSession s=req.getSession(false);return s==null?null:(Usuario)s.getAttribute("usuarioLogueado");}
}

package Servlet;

import Controlador.UsuarioDAO;
import org.mindrot.jbcrypt.BCrypt;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;
import java.io.IOException;

@WebServlet("/RecuperarContrasena")
public class RecuperarContrasenaServlet extends HttpServlet {
 protected void doGet(HttpServletRequest req,HttpServletResponse resp)throws ServletException,IOException{req.getRequestDispatcher("/vista/recuperar-contrasena.jsp").forward(req,resp);}
 protected void doPost(HttpServletRequest req,HttpServletResponse resp)throws ServletException,IOException{
  req.setCharacterEncoding("UTF-8");String correo=req.getParameter("correo"),pass=req.getParameter("pass"),confirmar=req.getParameter("confirmar");
  if(correo==null||correo.trim().isEmpty()){req.setAttribute("error","El correo electrónico es obligatorio.");doGet(req,resp);return;}
  if(pass==null||pass.length()<6){req.setAttribute("error","La nueva contraseña debe tener mínimo 6 caracteres.");doGet(req,resp);return;}
  if(!pass.equals(confirmar)){req.setAttribute("error","La contraseña y su confirmación no coinciden.");doGet(req,resp);return;}
  boolean ok=new UsuarioDAO().restablecerContrasena(correo.trim(),BCrypt.hashpw(pass,BCrypt.gensalt(12)));
  if(!ok){req.setAttribute("error","No encontramos una cuenta activa con ese correo.");doGet(req,resp);return;}
  req.setAttribute("mensaje","Contraseña actualizada correctamente. Ahora inicia sesión.");doGet(req,resp);
 }
}

package Servlet;

import Controlador.FlujoPedidoDAO;
import Modelo.Usuario;
import java.io.IOException;
import java.sql.SQLException;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

   
                                                         
                                                                       
                          
   
@WebServlet("/RepartidorServlet")
public class RepartidorServlet extends HttpServlet {

    private static final int ROL_REPARTIDOR = 5;
    private static final String VISTA = "/vista/repartidor.jsp";
    private static final String LOGIN = "/vista/login.jsp";

    private final FlujoPedidoDAO dao = new FlujoPedidoDAO();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        Usuario usuario = validarSesion(request, response);
        if (usuario == null) {
            return;
        }

        cargarDatos(request, usuario);
        request.getRequestDispatcher(VISTA).forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        request.setCharacterEncoding("UTF-8");

        Usuario usuario = validarSesion(request, response);
        if (usuario == null) {
            return;
        }

        String pedidoStr = request.getParameter("pedido");
        String accion = request.getParameter("accion");

        
        if (esVacio(pedidoStr)) {
            request.setAttribute("error", "Selecciona un pedido válido.");
            cargarDatos(request, usuario);
            request.getRequestDispatcher(VISTA).forward(request, response);
            return;
        }

        if (esVacio(accion) || !("tomar".equals(accion) || "entregado".equals(accion))) {
            request.setAttribute("error", "La acción sobre el pedido no es válida.");
            cargarDatos(request, usuario);
            request.getRequestDispatcher(VISTA).forward(request, response);
            return;
        }

        
        try {
            int idPedido = Integer.parseInt(pedidoStr.trim());
            boolean actualizado;
            if ("entregado".equals(accion)) {
                String pin = request.getParameter("pinEntrega");
                actualizado = dao.finalizarDomicilio(idPedido, usuario.getIdUsuario(), pin) >= 0;
            } else {
                actualizado = dao.actualizarPorRol(idPedido, usuario.getIdUsuario(), "REPARTIDOR", accion);
            }

            if (actualizado) {
                String mensaje = "tomar".equals(accion)
                        ? "Tomaste el pedido #" + idPedido + ". ¡Buena ruta!"
                        : "Entrega del pedido #" + idPedido + " confirmada correctamente.";
                request.setAttribute("mensaje", mensaje);
            } else {
                request.setAttribute("error", "No fue posible actualizar el pedido. Verifica su estado actual.");
            }
        } catch (NumberFormatException e) {
            request.setAttribute("error", "El identificador del pedido no es válido.");
        } catch (SQLException e) {
            request.setAttribute("error", e.getMessage());
        }

        
        cargarDatos(request, usuario);
        request.getRequestDispatcher(VISTA).forward(request, response);
    }

                                                                         
    private Usuario validarSesion(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        HttpSession sesion = request.getSession(false);
        Usuario usuario = sesion == null ? null : (Usuario) sesion.getAttribute("usuarioLogueado");

        if (usuario == null) {
            request.setAttribute("error", "Debes iniciar sesión para acceder a tu panel.");
            request.getRequestDispatcher(LOGIN).forward(request, response);
            return null;
        }

        if (usuario.getRolesIdRol() != ROL_REPARTIDOR) {
            request.setAttribute("error", "No tienes permisos para acceder a esta sección.");
            request.getRequestDispatcher(LOGIN).forward(request, response);
            return null;
        }

        return usuario;
    }

                                                                      
    private void cargarDatos(HttpServletRequest request, Usuario usuario) {
        request.setAttribute("listaPedidos", dao.pedidos("REPARTIDOR", usuario.getIdUsuario()));
    }

    private boolean esVacio(String valor) {
        return valor == null || valor.trim().isEmpty();
    }
}

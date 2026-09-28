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

   
                                                     
                                                                       
                                             
   
@WebServlet("/CocinaServlet")
public class CocinaServlet extends HttpServlet {

    private static final int ROL_COCINA = 3;
    private static final String VISTA = "/vista/cocina.jsp";
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
        String cambio = request.getParameter("cambio");

        
        if (esVacio(pedidoStr)) {
            request.setAttribute("error", "Selecciona un pedido válido.");
            cargarDatos(request, usuario);
            request.getRequestDispatcher(VISTA).forward(request, response);
            return;
        }

        if (esVacio(cambio) || !("iniciar".equals(cambio) || "listo".equals(cambio))) {
            request.setAttribute("error", "La acción de cambio de estado no es válida.");
            cargarDatos(request, usuario);
            request.getRequestDispatcher(VISTA).forward(request, response);
            return;
        }

        
        try {
            int idPedido = Integer.parseInt(pedidoStr.trim());
            boolean actualizado = dao.actualizarPorRol(idPedido, usuario.getIdUsuario(), "COCINERO", cambio);

            if (actualizado) {
                request.setAttribute("mensaje", "El estado del pedido #" + idPedido + " se actualizó correctamente.");
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

        if (usuario.getRolesIdRol() != ROL_COCINA) {
            request.setAttribute("error", "No tienes permisos para acceder a esta sección.");
            request.getRequestDispatcher(LOGIN).forward(request, response);
            return null;
        }

        return usuario;
    }

                                                                              
    private void cargarDatos(HttpServletRequest request, Usuario usuario) {
        request.setAttribute("listaPedidos", dao.pedidos("COCINA", usuario.getIdUsuario()));
    }

    private boolean esVacio(String valor) {
        return valor == null || valor.trim().isEmpty();
    }
}

package Servlet;

import Controlador.FlujoPedidoDAO;
import Modelo.Usuario;
import java.io.IOException;
import java.sql.SQLException;
import java.util.LinkedHashMap;
import java.util.Map;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

   
                                                     
                                                                              
                                                                          
   
@WebServlet("/MeseroServlet")
public class MeseroServlet extends HttpServlet {

    private static final int ROL_MESERO = 2;
    private static final String VISTA = "/vista/mesero.jsp";
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

        String accion = request.getParameter("accion");

        if ("cambiarEstado".equals(accion)) {
            procesarCambioEstado(request, usuario);
        } else {
            procesarNuevaComanda(request, usuario);
        }

        cargarDatos(request, usuario);
        request.getRequestDispatcher(VISTA).forward(request, response);
    }

                                                   
    private void procesarNuevaComanda(HttpServletRequest request, Usuario usuario) {
        String mesaStr = request.getParameter("mesa");
        String metodoPago = request.getParameter("metodoPago");
        String itemsStr = request.getParameter("items");

        if (esVacio(mesaStr)) {
            request.setAttribute("error", "Selecciona una mesa.");
            return;
        }

        if (esVacio(metodoPago)) {
            request.setAttribute("error", "Selecciona un método de pago.");
            return;
        }

        if (esVacio(itemsStr)) {
            request.setAttribute("error", "Agrega al menos un producto a la comanda.");
            return;
        }

        try {
            int idMesa = Integer.parseInt(mesaStr.trim());
            Map<Integer, Integer> items = parsearItems(itemsStr);
            int idPedido = dao.crearPedidoMesero(usuario.getIdUsuario(), idMesa, metodoPago, items);
            request.setAttribute("mensaje", "Comanda #" + idPedido + " enviada a cocina correctamente.");
        } catch (NumberFormatException e) {
            request.setAttribute("error", "El número de mesa no es válido.");
        } catch (IllegalArgumentException | SQLException e) {
            request.setAttribute("error", e.getMessage());
        }
    }

                                                         
    private void procesarCambioEstado(HttpServletRequest request, Usuario usuario) {
        String pedidoStr = request.getParameter("pedido");

        if (esVacio(pedidoStr)) {
            request.setAttribute("error", "Selecciona un pedido válido.");
            return;
        }

        try {
            int idPedido = Integer.parseInt(pedidoStr.trim());
            boolean actualizado = dao.actualizarPorRol(idPedido, usuario.getIdUsuario(), "MESERO", "servido");

            if (actualizado) {
                request.setAttribute("mensaje", "El pedido #" + idPedido + " fue marcado como servido.");
            } else {
                request.setAttribute("error", "No fue posible actualizar el pedido. Verifica su estado actual.");
            }
        } catch (NumberFormatException e) {
            request.setAttribute("error", "El identificador del pedido no es válido.");
        } catch (SQLException e) {
            request.setAttribute("error", e.getMessage());
        }
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

        if (usuario.getRolesIdRol() != ROL_MESERO) {
            request.setAttribute("error", "No tienes permisos para acceder a esta sección.");
            request.getRequestDispatcher(LOGIN).forward(request, response);
            return null;
        }

        return usuario;
    }

                                                                                  
    private void cargarDatos(HttpServletRequest request, Usuario usuario) {
        request.setAttribute("listaMenu", dao.menuDisponible());
        request.setAttribute("listaMesas", dao.mesasDisponibles());
        request.setAttribute("listaPedidos", dao.pedidos("MESERO", usuario.getIdUsuario()));
    }

                                                                                     
    private Map<Integer, Integer> parsearItems(String valor) {
        Map<Integer, Integer> salida = new LinkedHashMap<>();
        for (String item : valor.split(",")) {
            String[] partes = item.split(":");
            if (partes.length != 2) {
                throw new IllegalArgumentException("Los productos seleccionados no son válidos.");
            }
            try {
                salida.put(Integer.valueOf(partes[0].trim()), Integer.valueOf(partes[1].trim()));
            } catch (NumberFormatException e) {
                throw new IllegalArgumentException("Los productos seleccionados no son válidos.");
            }
        }
        return salida;
    }

    private boolean esVacio(String valor) {
        return valor == null || valor.trim().isEmpty();
    }
}

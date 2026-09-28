package Pruebas;

import Controlador.DetallePedidoDAO;
import Modelo.DetallePedido;
import java.util.Scanner;

public class PruebaConsultaDetallePedido {

    public static void main(String[] args) {
        DetallePedidoDAO dao = new DetallePedidoDAO();
        Scanner sc = new Scanner(System.in);

        System.out.println("Ingrese el ID del detalle de pedido: ");
        int id = sc.nextInt();
        DetallePedido detalle = dao.consultarDetallePedido(id);

        if (detalle != null) {
            System.out.println("ID Detalle: " + detalle.getId_detalle_pedido());
            System.out.println("Cantidad: " + detalle.getCantidad());
            System.out.println("Precio Unitario: " + detalle.getPrecio_unitario());
            System.out.println("Subtotal: " + detalle.getSubtotal());
            System.out.println("ID Menú: " + detalle.getMenus_id_menu());
            System.out.println("ID Pedido: " + detalle.getPedidos_id_pedido());
            System.out.println("ID Estado Pedido: " + detalle.getEstados_pedidos_id_estado_pedido());
            System.out.println("Estado: " + detalle.getEstado());
        } else {
            System.out.println("No se encontro el registro");
        }
        sc.close();
    }
}
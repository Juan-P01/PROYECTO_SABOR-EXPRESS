package Pruebas;

import Controlador.DetallePedidoDAO;
import Modelo.DetallePedido;
import java.math.BigDecimal;
import java.util.Scanner;

public class PruebaActualizarDetallePedido {

    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        DetallePedido detalle = new DetallePedido();
        DetallePedidoDAO dao = new DetallePedidoDAO();

        System.out.println("Ingrese el ID del detalle a actualizar: ");
        detalle.setId_detalle_pedido(sc.nextInt());

        System.out.println("Ingrese la nueva cantidad: ");
        int cant = sc.nextInt();
        detalle.setCantidad(cant);

        System.out.println("Ingrese el nuevo precio unitario: ");
        BigDecimal precio = sc.nextBigDecimal();
        detalle.setPrecio_unitario(precio);

        BigDecimal subtotal = precio.multiply(new BigDecimal(cant));
        detalle.setSubtotal(subtotal);

        System.out.println("Ingrese el nuevo ID de menú: ");
        detalle.setMenus_id_menu(sc.nextInt());

        System.out.println("Ingrese el nuevo ID de pedido: ");
        detalle.setPedidos_id_pedido(sc.nextInt());

        System.out.println("Ingrese el nuevo ID de estado de pedido: ");
        detalle.setEstados_pedidos_id_estado_pedido(sc.nextInt());

        boolean resultado = dao.actualizarDetallePedido(detalle);
        if (resultado) {
            System.out.println("Actualizado correctamente");
        } else {
            System.out.println("Error al actualizar");
        }
        sc.close();
    }
}
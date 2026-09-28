package Pruebas;

import Controlador.DetallePedidoDAO;
import Modelo.DetallePedido;
import java.math.BigDecimal;
import java.util.Scanner;

public class PruebaInsertarDetallePedido {

    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        DetallePedido detalle = new DetallePedido();
        DetallePedidoDAO dao = new DetallePedidoDAO();

        System.out.println("Ingrese la cantidad: ");
        int cant = sc.nextInt();
        detalle.setCantidad(cant);

        System.out.println("Ingrese el precio unitario: ");
        BigDecimal precio = sc.nextBigDecimal();
        detalle.setPrecio_unitario(precio);

        BigDecimal subtotal = precio.multiply(new BigDecimal(cant));
        detalle.setSubtotal(subtotal);

        System.out.println("Ingrese el ID del menú: ");
        detalle.setMenus_id_menu(sc.nextInt());

        System.out.println("Ingrese el ID del pedido: ");
        detalle.setPedidos_id_pedido(sc.nextInt());

        System.out.println("Ingrese el ID del estado del pedido (1: Pendiente, 2: Preparacion, 3: Servido, 4: Pagado): ");
        detalle.setEstados_pedidos_id_estado_pedido(sc.nextInt());

        detalle.setEstado(1);

        boolean resultado = dao.insertarDetallePedido(detalle);
        if (resultado) {
            System.out.println("Guardado exitosamente");
        } else {
            System.out.println("Error al guardar");
        }
        sc.close();
    }
}
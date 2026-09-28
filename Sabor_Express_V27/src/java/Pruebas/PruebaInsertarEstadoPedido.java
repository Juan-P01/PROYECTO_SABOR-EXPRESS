package Pruebas;

import Controlador.EstadoPedidoDAO;
import Modelo.EstadoPedido;
import java.util.Scanner;

public class PruebaInsertarEstadoPedido {
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        EstadoPedido miEstadoPedido = new EstadoPedido();
        EstadoPedidoDAO dao = new EstadoPedidoDAO();

        System.out.println("Por favor ingrese el ID del estado del pedido: ");
        miEstadoPedido.setIdEstadoPedido(sc.nextInt());
        sc.nextLine();

        System.out.println("Por favor ingrese el detalle del estado del pedido: ");
        miEstadoPedido.setDetalleEstadoPedido(sc.nextLine());

        boolean resultado = dao.InsertarEstadoPedido(miEstadoPedido);
        if (resultado) {
            System.out.println("El estado de pedido se guardo correctamente");
        } else {
            System.out.println("El estado de pedido no se pudo registrar");
        }
        sc.close();
    }
}
package Pruebas;

import Controlador.EstadoPedidoDAO;
import Modelo.EstadoPedido;
import java.util.Scanner;

public class PruebaActualizarEstadoPedido {
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        EstadoPedido miEstadoPedido = new EstadoPedido();
        EstadoPedidoDAO dao = new EstadoPedidoDAO();

        System.out.println("porfavor ingrese su ID actualizar");
        miEstadoPedido.setIdEstadoPedido(sc.nextInt());
        sc.nextLine();

        System.out.println("porfavor ingrese el detalle del estado actualizar");
        miEstadoPedido.setDetalleEstadoPedido(sc.nextLine());

        boolean resultado = dao.actualizarEstadoPedido(miEstadoPedido);
        if (resultado) {
            System.out.println("se actualizo correctamente");
        } else {
            System.out.println("el Estado Pedido no se actualizo correctamente intente por favor mas tarde");
        }
        sc.close();
    }
}
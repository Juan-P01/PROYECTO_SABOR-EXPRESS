package Pruebas;

import Controlador.EstadoPedidoDAO;
import java.util.Scanner;

public class PruebaActivarEstadoPedido {
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        EstadoPedidoDAO dao = new EstadoPedidoDAO();

        System.out.println("por favor ingrese el estado pedido a activar");
        int id = sc.nextInt();

        if (dao.ActivarEstadoPedido(id)) {
            System.out.println("Se activo con exito");
        } else {
            System.out.println("No se encontro el estado pedido");
        }
        sc.close();
    }
}
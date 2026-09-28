package Pruebas;

import Controlador.EstadoPedidoDAO;
import java.util.Scanner;

public class PruebaInactivarEstadoPedido {
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        EstadoPedidoDAO dao = new EstadoPedidoDAO();

        System.out.println("por favor ingrese el estado pedido a inactivar");
        int id = sc.nextInt();

        if (dao.inactivarEstadoPedido(id)) {
            System.out.println("Se inactivo con exito");
        } else {
            System.out.println("No se encontro el estado pedido");
        }
        sc.close();
    }
}
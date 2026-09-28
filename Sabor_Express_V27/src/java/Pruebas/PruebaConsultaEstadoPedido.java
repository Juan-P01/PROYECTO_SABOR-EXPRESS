package Pruebas;

import Controlador.EstadoPedidoDAO;
import Modelo.EstadoPedido;
import java.util.Scanner;

public class PruebaConsultaEstadoPedido {
    public static void main(String[] args) {
        EstadoPedidoDAO miEstadoPedidoDAO = new EstadoPedidoDAO();
        Scanner leer = new Scanner(System.in);
        System.out.println("Por favor ingrese el ID del estado pedido: ");
        int id = leer.nextInt();
        EstadoPedido miEstadoPedido = miEstadoPedidoDAO.consultarEstadoPedido(id);

        if (miEstadoPedido != null) {
            System.out.println("ID Estado Pedido: " + miEstadoPedido.getIdEstadoPedido());
            System.out.println("Detalle Estado: " + miEstadoPedido.getDetalleEstadoPedido());
            System.out.println("Estado: " + miEstadoPedido.getEstado());
        } else {
            System.out.println("No se encontro el estado de pedido");
        }
        leer.close();
    }
}
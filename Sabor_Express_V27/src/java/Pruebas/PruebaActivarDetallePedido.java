package Pruebas;

import Controlador.DetallePedidoDAO;
import java.util.Scanner;

public class PruebaActivarDetallePedido {

    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        DetallePedidoDAO dao = new DetallePedidoDAO();

        System.out.println("Ingrese el ID del detalle a activar: ");
        int id = sc.nextInt();

        boolean resultado = dao.activarDetallePedido(id);
        if (resultado) {
            System.out.println("Detalle activado correctamente");
        } else {
            System.out.println("Error al activar");
        }
        sc.close();
    }
}
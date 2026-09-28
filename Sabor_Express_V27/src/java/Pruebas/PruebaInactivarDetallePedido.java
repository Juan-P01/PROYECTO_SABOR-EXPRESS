package Pruebas;

import Controlador.DetallePedidoDAO;
import java.util.Scanner;

public class PruebaInactivarDetallePedido {

    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        DetallePedidoDAO dao = new DetallePedidoDAO();

        System.out.println("Ingrese el ID del detalle a inactivar: ");
        int id = sc.nextInt();

        boolean resultado = dao.inactivarDetallePedido(id);
        if (resultado) {
            System.out.println("Detalle inactivado correctamente");
        } else {
            System.out.println("Error al inactivar");
        }
        sc.close();
    }
}
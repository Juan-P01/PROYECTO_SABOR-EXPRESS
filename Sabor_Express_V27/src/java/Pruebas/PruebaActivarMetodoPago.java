package Pruebas;

import Controlador.MetodoPagoDAO;
import java.util.Scanner;

public class PruebaActivarMetodoPago {

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        MetodoPagoDAO dao = new MetodoPagoDAO();

        System.out.print("Ingrese el ID del método de pago que desea activar: ");
        int idActivar = scanner.nextInt();

        if (dao.activar(idActivar)) {
            System.out.println("Método de pago con ID " + idActivar + " activado correctamente (estado = 1).");
        } else {
            System.out.println("Error al activar el método de pago con ID " + idActivar + ".");
        }

        scanner.close();
    }
}
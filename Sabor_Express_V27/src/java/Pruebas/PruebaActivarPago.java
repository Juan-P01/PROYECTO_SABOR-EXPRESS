package Pruebas;

import Controlador.PagoDAO;
import java.util.Scanner;

public class PruebaActivarPago {

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        PagoDAO dao = new PagoDAO();

        System.out.print("Ingrese el ID del pago que desea activar: ");
        int idActivar = scanner.nextInt();

        if (dao.activar(idActivar)) {
            System.out.println("Pago con ID " + idActivar + " activado correctamente (estado = 1).");
        } else {
            System.out.println("Error al activar el pago con ID " + idActivar + ".");
        }

        scanner.close();
    }
}
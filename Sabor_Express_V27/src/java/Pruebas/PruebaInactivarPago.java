package Pruebas;

import Controlador.PagoDAO;
import java.util.Scanner;

public class PruebaInactivarPago {

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        PagoDAO dao = new PagoDAO();

        System.out.print("Ingrese el ID del pago que desea inactivar: ");
        int idInactivar = scanner.nextInt();

        if (dao.inactivar(idInactivar)) {
            System.out.println("Pago con ID " + idInactivar + " inactivado correctamente (estado = 0).");
        } else {
            System.out.println("Error al inactivar el pago con ID " + idInactivar + ".");
        }

        scanner.close();
    }
}
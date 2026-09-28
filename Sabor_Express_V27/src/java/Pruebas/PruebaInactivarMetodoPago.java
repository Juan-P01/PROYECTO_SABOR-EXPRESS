package Pruebas;

import Controlador.MetodoPagoDAO;
import java.util.Scanner;

public class PruebaInactivarMetodoPago {

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        MetodoPagoDAO dao = new MetodoPagoDAO();

        System.out.print("Ingrese el ID del método de pago que desea inactivar: ");
        int idInactivar = scanner.nextInt();

        if (dao.inactivar(idInactivar)) {
            System.out.println("Método de pago con ID " + idInactivar + " inactivado correctamente (estado = 0).");
        } else {
            System.out.println("Error al inactivar el método de pago con ID " + idInactivar + ".");
        }

        scanner.close();
    }
}
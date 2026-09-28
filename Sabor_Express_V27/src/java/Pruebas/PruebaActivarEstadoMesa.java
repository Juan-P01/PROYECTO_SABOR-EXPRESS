package Pruebas;

import Controlador.EstadoMesaDAO;
import java.util.Scanner;

public class PruebaActivarEstadoMesa {

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        EstadoMesaDAO dao = new EstadoMesaDAO();

        System.out.print("Ingrese el ID del estado de mesa que desea activar: ");
        int idActivar = scanner.nextInt();

        if (dao.activar(idActivar)) {
            System.out.println("Estado de mesa con ID " + idActivar + " activado correctamente (estado = 1).");
        } else {
            System.out.println("Error al activar el estado de mesa con ID " + idActivar + ".");
        }

        scanner.close();
    }
}
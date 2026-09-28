package Pruebas;

import Controlador.EstadoMesaDAO;
import java.util.Scanner;

public class PruebaInactivarEstadoMesa {

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        EstadoMesaDAO dao = new EstadoMesaDAO();

        System.out.print("Ingrese el ID del estado de mesa que desea inactivar: ");
        int idInactivar = scanner.nextInt();

        if (dao.inactivar(idInactivar)) {
            System.out.println("Estado de mesa con ID " + idInactivar + " inactivado correctamente (estado = 0).");
        } else {
            System.out.println("Error al inactivar el estado de mesa con ID " + idInactivar + ".");
        }

        scanner.close();
    }
}
package Pruebas;

import Controlador.MenuDAO;
import java.util.Scanner;

public class PruebaActivarMenu {

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        MenuDAO dao = new MenuDAO();

        System.out.print("Ingrese el ID del menú que desea activar: ");
        int idActivar = scanner.nextInt();

        if (dao.activar(idActivar)) {
            System.out.println("Menú con ID " + idActivar + " activado correctamente (estado = 1).");
        } else {
            System.out.println("Error al activar el menú con ID " + idActivar + ".");
        }

        scanner.close();
    }
}
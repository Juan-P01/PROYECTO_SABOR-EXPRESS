package Pruebas;

import Controlador.MenuDAO;
import java.util.Scanner;

public class PruebaInactivarMenu {

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        MenuDAO dao = new MenuDAO();

        System.out.print("Ingrese el ID del menú que desea inactivar: ");
        int idInactivar = scanner.nextInt();

        if (dao.inactivar(idInactivar)) {
            System.out.println("Menú con ID " + idInactivar + " inactivado correctamente (estado = 0).");
        } else {
            System.out.println("Error al inactivar el menú con ID " + idInactivar + ".");
        }

        scanner.close();
    }
}
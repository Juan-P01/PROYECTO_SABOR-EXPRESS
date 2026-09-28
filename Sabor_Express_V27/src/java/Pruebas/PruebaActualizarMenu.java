package Pruebas;

import Controlador.MenuDAO;
import Modelo.Menu;
import java.util.Scanner;

public class PruebaActualizarMenu {

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        MenuDAO dao = new MenuDAO();
        Menu m = new Menu();

        System.out.print("Ingrese el ID del menú que desea actualizar: ");
        m.setIdMenu(scanner.nextInt());
        scanner.nextLine();

        System.out.print("Ingrese el nuevo nombre: ");
        m.setNombre(scanner.nextLine());

        System.out.print("Ingrese la nueva descripción: ");
        m.setDescripcion(scanner.nextLine());

        System.out.print("Ingrese el nuevo precio: ");
        m.setPrecio(scanner.nextDouble());

        System.out.print("Ingrese el nuevo estado (1 activo, 0 inactivo): ");
        m.setEstado(scanner.nextInt());

        if (dao.actualizar(m)) {
            System.out.println("Menú actualizado correctamente.");
        } else {
            System.out.println("Error al actualizar menú.");
        }

        scanner.close();
    }
}
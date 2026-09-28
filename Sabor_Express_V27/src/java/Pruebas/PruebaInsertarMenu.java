package Pruebas;

import Controlador.MenuDAO;
import Modelo.Menu;
import java.util.Scanner;

public class PruebaInsertarMenu {

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        MenuDAO dao = new MenuDAO();
        Menu m = new Menu();

        System.out.print("Ingrese el nombre del menú: ");
        m.setNombre(scanner.nextLine());

        System.out.print("Ingrese la descripción del menú: ");
        m.setDescripcion(scanner.nextLine());

        System.out.print("Ingrese el precio del menú: ");
        m.setPrecio(scanner.nextDouble());

        m.setEstado(1);

        if (dao.insertar(m)) {
            System.out.println("Menú insertado correctamente.");
        } else {
            System.out.println("Error al insertar menú.");
        }

        scanner.close();
    }
}
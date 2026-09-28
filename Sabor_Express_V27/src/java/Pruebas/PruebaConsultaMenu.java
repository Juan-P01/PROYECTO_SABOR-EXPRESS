package Pruebas;

import Controlador.MenuDAO;
import Modelo.Menu;
import java.util.Scanner;

public class PruebaConsultaMenu {

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        MenuDAO dao = new MenuDAO();

        System.out.print("Ingrese el ID del menú a consultar: ");
        int idConsulta = scanner.nextInt();

        Menu m = dao.consultar(idConsulta);

        if (m.getIdMenu() != 0) {
            System.out.println("\n--- Registro Encontrado ---");
            System.out.println("ID Menú: " + m.getIdMenu());
            System.out.println("Nombre: " + m.getNombre());
            System.out.println("Descripción: " + m.getDescripcion());
            System.out.println("Precio: " + m.getPrecio());
            System.out.println("Estado: " + m.getEstado());
        } else {
            System.out.println("No se encontró el menú con ID: " + idConsulta);
        }

        scanner.close();
    }
}
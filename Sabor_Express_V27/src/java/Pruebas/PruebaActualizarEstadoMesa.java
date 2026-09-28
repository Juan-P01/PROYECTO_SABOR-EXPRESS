package Pruebas;

import Controlador.EstadoMesaDAO;
import Modelo.EstadoMesa;
import java.util.Scanner;

public class PruebaActualizarEstadoMesa {

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        EstadoMesaDAO dao = new EstadoMesaDAO();
        EstadoMesa em = new EstadoMesa();

        System.out.print("Ingrese el ID del estado de mesa que desea actualizar: ");
        em.setIdEstadoMesa(scanner.nextInt());
        scanner.nextLine();

        System.out.print("Ingrese el nuevo nombre: ");
        em.setNombre(scanner.nextLine());

        System.out.print("Ingrese el nuevo estado (1 activo, 0 inactivo): ");
        em.setEstado(scanner.nextInt());

        if (dao.actualizar(em)) {
            System.out.println("Estado de mesa actualizado correctamente.");
        } else {
            System.out.println("Error al actualizar estado de mesa.");
        }

        scanner.close();
    }
}
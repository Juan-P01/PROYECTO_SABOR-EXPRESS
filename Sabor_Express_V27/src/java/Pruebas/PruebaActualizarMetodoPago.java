package Pruebas;

import Controlador.MetodoPagoDAO;
import Modelo.MetodoPago;
import java.util.Scanner;

public class PruebaActualizarMetodoPago {

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        MetodoPagoDAO dao = new MetodoPagoDAO();
        MetodoPago mp = new MetodoPago();

        System.out.print("Ingrese el ID del método de pago que desea actualizar: ");
        mp.setIdMetodoPago(scanner.nextInt());
        scanner.nextLine();

        System.out.print("Ingrese el nuevo nombre: ");
        mp.setNombre(scanner.nextLine());

        System.out.print("Ingrese el nuevo estado (1 activo, 0 inactivo): ");
        mp.setEstado(scanner.nextInt());

        if (dao.actualizar(mp)) {
            System.out.println("Método de pago actualizado correctamente.");
        } else {
            System.out.println("Error al actualizar método de pago.");
        }

        scanner.close();
    }
}
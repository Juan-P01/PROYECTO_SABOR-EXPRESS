package Pruebas;

import Controlador.PagoDAO;
import Modelo.Pago;
import java.util.Scanner;

public class PruebaActualizarPago {

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        PagoDAO dao = new PagoDAO();
        Pago p = new Pago();

        System.out.print("Ingrese el ID del pago que desea actualizar: ");
        p.setIdPago(scanner.nextInt());

        System.out.print("Ingrese el nuevo ID de la factura: ");
        p.setIdFactura(scanner.nextInt());

        System.out.print("Ingrese el nuevo ID del método de pago: ");
        p.setIdMetodoPago(scanner.nextInt());

        System.out.print("Ingrese el nuevo monto: ");
        p.setMonto(scanner.nextDouble());
        scanner.nextLine();

        System.out.print("Ingrese la nueva fecha del pago (YYYY-MM-DD HH:MM:SS): ");
        p.setFechaPago(scanner.nextLine());

        System.out.print("Ingrese el nuevo estado (1 activo, 0 inactivo): ");
        p.setEstado(scanner.nextInt());

        if (dao.actualizar(p)) {
            System.out.println("Pago actualizado correctamente.");
        } else {
            System.out.println("Error al actualizar pago.");
        }

        scanner.close();
    }
}
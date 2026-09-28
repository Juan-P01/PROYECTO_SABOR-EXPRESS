package Pruebas;

import Controlador.PagoDAO;
import Modelo.Pago;
import java.util.Scanner;

public class PruebaInsertarPago {

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        PagoDAO dao = new PagoDAO();
        Pago p = new Pago();

        System.out.print("Ingrese el ID de la factura: ");
        p.setIdFactura(scanner.nextInt());

        System.out.print("Ingrese el ID del método de pago: ");
        p.setIdMetodoPago(scanner.nextInt());

        System.out.print("Ingrese el monto del pago: ");
        p.setMonto(scanner.nextDouble());
        scanner.nextLine();

        System.out.print("Ingrese la fecha del pago (YYYY-MM-DD HH:MM:SS): ");
        p.setFechaPago(scanner.nextLine());

        p.setEstado(1);

        if (dao.insertar(p)) {
            System.out.println("Pago insertado correctamente.");
        } else {
            System.out.println("Error al insertar pago.");
        }

        scanner.close();
    }
}
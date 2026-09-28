package Pruebas;

import Controlador.PagoDAO;
import Modelo.Pago;
import java.util.Scanner;

public class PruebaConsultaPago {

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        PagoDAO dao = new PagoDAO();

        System.out.print("Ingrese el ID del pago a consultar: ");
        int idConsulta = scanner.nextInt();

        Pago p = dao.consultar(idConsulta);

        if (p.getIdPago() != 0) {
            System.out.println("\n--- Registro Encontrado ---");
            System.out.println("ID Pago: " + p.getIdPago());
            System.out.println("ID Factura: " + p.getIdFactura());
            System.out.println("ID Método Pago: " + p.getIdMetodoPago());
            System.out.println("Monto: " + p.getMonto());
            System.out.println("Fecha Pago: " + p.getFechaPago());
            System.out.println("Estado: " + p.getEstado());
        } else {
            System.out.println("No se encontró el pago con ID: " + idConsulta);
        }

        scanner.close();
    }
}
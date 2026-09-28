package Pruebas;

import Controlador.MetodoPagoDAO;
import Modelo.MetodoPago;
import java.util.Scanner;

public class PruebaConsultaMetodoPago {

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        MetodoPagoDAO dao = new MetodoPagoDAO();

        System.out.print("Ingrese el ID del método de pago a consultar: ");
        int idConsulta = scanner.nextInt();

        MetodoPago mp = dao.consultar(idConsulta);

        if (mp.getIdMetodoPago() != 0) {
            System.out.println("\n--- Registro Encontrado ---");
            System.out.println("ID Método Pago: " + mp.getIdMetodoPago());
            System.out.println("Nombre: " + mp.getNombre());
            System.out.println("Estado: " + mp.getEstado());
        } else {
            System.out.println("No se encontró el método de pago con ID: " + idConsulta);
        }

        scanner.close();
    }
}
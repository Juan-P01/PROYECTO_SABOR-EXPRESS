package Pruebas;

import Controlador.MetodoPagoDAO;
import Modelo.MetodoPago;
import java.util.Scanner;

public class PruebaInsertarMetodoPago {

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        MetodoPagoDAO dao = new MetodoPagoDAO();
        MetodoPago mp = new MetodoPago();

        System.out.print("Ingrese el nombre del método de pago: ");
        mp.setNombre(scanner.nextLine());
        mp.setEstado(1);

        if (dao.insertar(mp)) {
            System.out.println("Método de pago insertado correctamente.");
        } else {
            System.out.println("Error al insertar método de pago.");
        }

        scanner.close();
    }
}
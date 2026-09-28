package Pruebas;

import Controlador.EstadoMesaDAO;
import Modelo.EstadoMesa;
import java.util.Scanner;

public class PruebaInsertarEstadoMesa {

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        EstadoMesaDAO dao = new EstadoMesaDAO();
        EstadoMesa em = new EstadoMesa();

        System.out.print("Ingrese el nombre del estado de mesa: ");
        em.setNombre(scanner.nextLine());
        em.setEstado(1);

        if (dao.insertar(em)) {
            System.out.println("Estado de mesa insertado correctamente.");
        } else {
            System.out.println("Error al insertar estado de mesa.");
        }

        scanner.close();
    }
}
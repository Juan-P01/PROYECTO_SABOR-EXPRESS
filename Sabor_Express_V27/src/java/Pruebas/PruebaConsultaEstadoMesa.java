package Pruebas;

import Controlador.EstadoMesaDAO;
import Modelo.EstadoMesa;
import java.util.Scanner;

public class PruebaConsultaEstadoMesa {

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        EstadoMesaDAO dao = new EstadoMesaDAO();

        System.out.print("Ingrese el ID del estado de mesa a consultar: ");
        int idConsulta = scanner.nextInt();

        EstadoMesa em = dao.consultar(idConsulta);

        if (em.getIdEstadoMesa() != 0) {
            System.out.println("\n--- Registro Encontrado ---");
            System.out.println("ID Estado Mesa: " + em.getIdEstadoMesa());
            System.out.println("Nombre: " + em.getNombre());
            System.out.println("Estado: " + em.getEstado());
        } else {
            System.out.println("No se encontró el estado de mesa con ID: " + idConsulta);
        }

        scanner.close();
    }
}
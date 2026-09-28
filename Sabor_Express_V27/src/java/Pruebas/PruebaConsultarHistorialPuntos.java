  
                                                                                                     
                                                                                         
   
package Pruebas;

import Controlador.HistorialPuntosDAO;
import Modelo.HistorialPuntos;
import java.util.Scanner;

   
  
                   
   
public class PruebaConsultarHistorialPuntos {

    public static void main(String[] args) {

        Scanner sc = new Scanner(System.in);
        HistorialPuntosDAO dao = new HistorialPuntosDAO();

        System.out.println("Ingrese el ID del historial de puntos a consultar: ");
        int idHistorial = sc.nextInt();

        HistorialPuntos miHistorial = dao.consultarHistorialPuntos(idHistorial);

        if (miHistorial != null) {
            System.out.println("--- DATOS DEL HISTORIAL DE PUNTOS CONSULTADO ---");
            System.out.println("ID Historial: " + miHistorial.getIdHistorialPuntos());
            System.out.println("Puntos: " + miHistorial.getPuntos());
            System.out.println("Fecha de Movimiento: " + miHistorial.getFechaMovimiento());
            System.out.println("Tipo de Movimiento: " + miHistorial.getTipoDeMovimiento());
            System.out.println("Puntos Restantes: " + miHistorial.getPuntosRestantes());
            System.out.println("ID Cliente: " + miHistorial.getClientesIdCliente());
            System.out.println("Estado: " + (miHistorial.isEstado() ? "Activo" : "Inactivo"));
        } else {
            System.out.println("No se encontro el historial con el ID ingresado.");
        }
        sc.close();
    }
}

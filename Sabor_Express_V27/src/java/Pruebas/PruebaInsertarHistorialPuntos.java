  
                                                                                                     
                                                                                         
   
package Pruebas;

import Controlador.HistorialPuntosDAO;
import Modelo.HistorialPuntos;
import java.time.Instant;
import java.util.Scanner;

   
  
                   
   
public class PruebaInsertarHistorialPuntos {

    public static void main(String[] args) {

        Scanner sc = new Scanner(System.in);
        HistorialPuntos miHistorial = new HistorialPuntos();
        HistorialPuntosDAO dao = new HistorialPuntosDAO();

        System.out.println("Por favor ingrese la cantidad de puntos del movimiento: ");
        miHistorial.setPuntos(sc.nextInt());
        sc.nextLine();

        System.out.println("Ingrese el tipo de movimiento (Ej: Acumulacion por compra, Canje): ");
        miHistorial.setTipoDeMovimiento(sc.nextLine());

        System.out.println("Ingrese los puntos restantes: ");
        miHistorial.setPuntosRestantes(sc.nextInt());

        System.out.println("Ingrese el ID del cliente: ");
        miHistorial.setClientesIdCliente(sc.nextInt());

        miHistorial.setFechaMovimiento(Instant.now());
        miHistorial.setEstado(true);

        boolean resultado = dao.insertarHistorialPuntos(miHistorial);
        if (resultado) {
            System.out.println("El historial de puntos se guardo correctamente");
        } else {
            System.out.println("El historial de puntos no se pudo registrar");
        }
        sc.close();
    }
}

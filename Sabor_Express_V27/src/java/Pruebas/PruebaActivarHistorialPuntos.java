  
                                                                                                     
                                                                                         
   
package Pruebas;

import Controlador.HistorialPuntosDAO;
import java.util.Scanner;

   
  
                   
   
public class PruebaActivarHistorialPuntos {

    public static void main(String[] args) {

        Scanner sc = new Scanner(System.in);
        HistorialPuntosDAO dao = new HistorialPuntosDAO();

        System.out.println("Ingrese el ID del historial de puntos a activar: ");
        int idHistorial = sc.nextInt();

        boolean resultado = dao.activarHistorialPuntos(idHistorial);
        if (resultado) {
            System.out.println("El historial de puntos se activo correctamente");
        } else {
            System.out.println("El historial de puntos no se pudo activar");
        }
        sc.close();
    }
}

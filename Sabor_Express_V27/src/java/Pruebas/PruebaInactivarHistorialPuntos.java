  
                                                                                                     
                                                                                         
   
package Pruebas;

import Controlador.HistorialPuntosDAO;
import java.util.Scanner;

   
  
                   
   
public class PruebaInactivarHistorialPuntos {

    public static void main(String[] args) {

        Scanner sc = new Scanner(System.in);
        HistorialPuntosDAO dao = new HistorialPuntosDAO();

        System.out.println("Ingrese el ID del historial de puntos a inactivar: ");
        int idHistorial = sc.nextInt();

        boolean resultado = dao.inactivarHistorialPuntos(idHistorial);
        if (resultado) {
            System.out.println("El historial de puntos se inactivo correctamente");
        } else {
            System.out.println("El historial de puntos no se pudo inactivar");
        }
        sc.close();
    }
}

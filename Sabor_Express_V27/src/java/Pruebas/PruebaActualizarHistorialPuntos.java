  
                                                                                                     
                                                                                         
   
package Pruebas;

import Controlador.HistorialPuntosDAO;
import Modelo.HistorialPuntos;
import java.util.Scanner;

   
  
                   
   
public class PruebaActualizarHistorialPuntos {

    public static void main(String[] args) {

        Scanner sc = new Scanner(System.in);
        HistorialPuntosDAO dao = new HistorialPuntosDAO();

        System.out.println("Ingrese el ID del historial a actualizar: ");
        int idHistorial = sc.nextInt();
        sc.nextLine();

        HistorialPuntos miHistorial = dao.consultarHistorialPuntos(idHistorial);

        if (miHistorial != null) {
            System.out.println("Ingrese la nueva cantidad de puntos: ");
            miHistorial.setPuntos(sc.nextInt());
            sc.nextLine();

            System.out.println("Ingrese el nuevo tipo de movimiento: ");
            miHistorial.setTipoDeMovimiento(sc.nextLine());

            System.out.println("Ingrese los nuevos puntos restantes: ");
            miHistorial.setPuntosRestantes(sc.nextInt());

            System.out.println("Ingrese el nuevo ID de cliente: ");
            miHistorial.setClientesIdCliente(sc.nextInt());

            System.out.println("Ingrese el estado del registro (1 para Activo / 0 para Inactivo): ");
            int est = sc.nextInt();
            miHistorial.setEstado(est == 1);

            boolean resultado = dao.actualizarHistorialPuntos(miHistorial);
            if (resultado) {
                System.out.println("El historial de puntos se actualizo correctamente");
            } else {
                System.out.println("El historial de puntos no se pudo actualizar");
            }
        } else {
            System.out.println("No existe un historial con el ID ingresado.");
        }
        sc.close();
    }
}

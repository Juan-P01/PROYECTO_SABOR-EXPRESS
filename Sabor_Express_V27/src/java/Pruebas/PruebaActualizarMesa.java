  
                                                                                                     
                                                                                         
   
package Pruebas;

import Controlador.MesaDAO;
import Modelo.Mesa;
import java.util.Scanner;

   
  
                   
   
public class PruebaActualizarMesa {

       
                                             
       
    public static void main(String[] args) {

        Scanner sc = new Scanner(System.in);
        MesaDAO dao = new MesaDAO();

        System.out.println("Ingrese el ID de la mesa a actualizar: ");
        int idMesa = sc.nextInt();
        sc.nextLine();

        Mesa miMesa = dao.consultarMesa(idMesa);

        if (miMesa != null) {
            System.out.println("Ingrese el nuevo numero de la mesa: ");
            miMesa.setNumeroMesa(sc.nextInt());

            System.out.println("Ingrese la nueva capacidad: ");
            miMesa.setCapacidad(sc.nextInt());
            sc.nextLine();

            System.out.println("Ingrese la nueva ubicacion: ");
            miMesa.setUbicacion(sc.nextLine());

            System.out.println("Ingrese el nuevo estado operativo (Ej: Disponible, Ocupada, Reservada): ");
            miMesa.setEstadoMesa(sc.nextLine());

            System.out.println("Ingrese el estado del registro (1 para Activo / 0 para Inactivo): ");
            int est = sc.nextInt();
            miMesa.setEstado(est == 1);

            boolean resultado = dao.actualizarMesa(miMesa);
            if (resultado) {
                System.out.println("La mesa se actualizo correctamente");
            } else {
                System.out.println("La mesa no se pudo actualizar");
            }
        } else {
            System.out.println("No existe una mesa con el ID ingresado.");
        }
        sc.close();
    }
}

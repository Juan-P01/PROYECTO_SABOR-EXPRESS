  
                                                                                                     
                                                                                         
   
package Pruebas;

import Controlador.MesaDAO;
import Modelo.Mesa;
import java.util.Scanner;

   
  
                   
   
public class PruebaInsertarMesa {

       
                                             
       
    public static void main(String[] args) {

        Scanner sc = new Scanner(System.in);
        Mesa miMesa = new Mesa();
        MesaDAO dao = new MesaDAO();

        System.out.println("Por favor ingrese el numero de la mesa: ");
        miMesa.setNumeroMesa(sc.nextInt());

        System.out.println("Ingrese la capacidad de la mesa (cantidad de personas): ");
        miMesa.setCapacidad(sc.nextInt());
        sc.nextLine();

        System.out.println("Ingrese la ubicacion (Ej: Terraza, Salón Principal, VIP): ");
        miMesa.setUbicacion(sc.nextLine());

        System.out.println("Ingrese el estado operativo de la mesa (Ej: Disponible, Ocupada, Reservada): ");
        miMesa.setEstadoMesa(sc.nextLine());

        miMesa.setEstado(true);

        boolean resultado = dao.insertarMesa(miMesa);
        if (resultado) {
            System.out.println("La mesa se guardo correctamente");
        } else {
            System.out.println("La mesa no se pudo registrar");
        }
        sc.close();
    }
}
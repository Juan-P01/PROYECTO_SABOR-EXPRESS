  
                                                                                                     
                                                                                         
   
package Pruebas;

import Controlador.MesaDAO;
import java.util.Scanner;

   
  
                   
   
public class PruebaActivarMesa {

       
                                             
       
    public static void main(String[] args) {

        Scanner sc = new Scanner(System.in);
        MesaDAO dao = new MesaDAO();

        System.out.println("Ingrese el ID de la mesa a activar: ");
        int idMesa = sc.nextInt();

        boolean resultado = dao.activarMesa(idMesa);
        if (resultado) {
            System.out.println("La mesa se activo correctamente");
        } else {
            System.out.println("La mesa no se pudo activar");
        }
        sc.close();
    }
}

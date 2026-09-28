  
                                                                                                     
                                                                                         
   
package Pruebas;

import Controlador.MesaDAO;
import java.util.Scanner;

   
  
                   
   
public class PruebaInactivarMesa {

       
                                             
       
    public static void main(String[] args) {

        Scanner sc = new Scanner(System.in);
        MesaDAO dao = new MesaDAO();

        System.out.println("Ingrese el ID de la mesa a inactivar: ");
        int idMesa = sc.nextInt();

        boolean resultado = dao.inactivarMesa(idMesa);
        if (resultado) {
            System.out.println("La mesa se inactivo correctamente");
        } else {
            System.out.println("La mesa no se pudo inactivar");
        }
        sc.close();
    }
}

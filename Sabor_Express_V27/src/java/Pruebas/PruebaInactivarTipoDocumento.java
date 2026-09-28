  
                                                                                                     
                                                                                         
   
package Pruebas;

import Controlador.TipoDocumentoDAO;
import java.util.Scanner;

   
  
                   
   
public class PruebaInactivarTipoDocumento {

       
                                             
       
    public static void main(String[] args) {

        Scanner sc = new Scanner(System.in);
        TipoDocumentoDAO dao = new TipoDocumentoDAO();

        System.out.println("Ingrese el ID del tipo de documento a inactivar: ");
        int idTipoDoc = sc.nextInt();

        boolean resultado = dao.inactivarTipoDocumento(idTipoDoc);
        if (resultado) {
            System.out.println("El tipo de documento se inactivo correctamente");
        } else {
            System.out.println("El tipo de documento no se pudo inactivar");
        }
        sc.close();
    }
}

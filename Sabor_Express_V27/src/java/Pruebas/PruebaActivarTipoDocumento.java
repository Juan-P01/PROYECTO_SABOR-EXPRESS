  
                                                                                                     
                                                                                         
   
package Pruebas;

import Controlador.TipoDocumentoDAO;
import java.util.Scanner;

   
  
                   
   
public class PruebaActivarTipoDocumento {

       
                                             
       
    public static void main(String[] args) {

        Scanner sc = new Scanner(System.in);
        TipoDocumentoDAO dao = new TipoDocumentoDAO();

        System.out.println("Ingrese el ID del tipo de documento a activar: ");
        int idTipoDoc = sc.nextInt();

        boolean resultado = dao.activarTipoDocumento(idTipoDoc);
        if (resultado) {
            System.out.println("El tipo de documento se activo correctamente");
        } else {
            System.out.println("El tipo de documento no se pudo activar");
        }
        sc.close();
    }
}

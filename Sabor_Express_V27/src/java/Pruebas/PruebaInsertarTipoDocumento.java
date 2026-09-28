  
                                                                                                     
                                                                                         
   
package Pruebas;

import Controlador.TipoDocumentoDAO;
import Modelo.TipoDocumento;
import java.util.Scanner;

   
  
                   
   
public class PruebaInsertarTipoDocumento {

       
                                             
       
    public static void main(String[] args) {

        Scanner sc = new Scanner(System.in);
        TipoDocumento miTipoDoc = new TipoDocumento();
        TipoDocumentoDAO dao = new TipoDocumentoDAO();

        System.out.println("Por favor ingrese la descripcion del tipo de documento (Ej: Permiso Especial): ");
        miTipoDoc.setDescripcionTipoDocumento(sc.nextLine());

        miTipoDoc.setEstado(true);

        boolean resultado = dao.insertarTipoDocumento(miTipoDoc);
        if (resultado) {
            System.out.println("El tipo de documento se guardo correctamente");
        } else {
            System.out.println("El tipo de documento no se pudo registrar");
        }
        sc.close();
    }
}

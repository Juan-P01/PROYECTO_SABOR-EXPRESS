  
                                                                                                     
                                                                                         
   
package Pruebas;

import Controlador.TipoDocumentoDAO;
import Modelo.TipoDocumento;
import java.util.Scanner;

   
  
                   
   
public class PruebaActualizarTipoDocumento {

       
                                             
       
    public static void main(String[] args) {

        Scanner sc = new Scanner(System.in);
        TipoDocumentoDAO dao = new TipoDocumentoDAO();

        System.out.println("Ingrese el ID del tipo de documento a actualizar: ");
        int idTipoDoc = sc.nextInt();
        sc.nextLine();

        TipoDocumento miTipoDoc = dao.consultarTipoDocumento(idTipoDoc);

        if (miTipoDoc != null) {
            System.out.println("Ingrese la nueva descripcion del tipo de documento: ");
            miTipoDoc.setDescripcionTipoDocumento(sc.nextLine());

            System.out.println("Ingrese el estado del registro (1 para Activo / 0 para Inactivo): ");
            int est = sc.nextInt();
            miTipoDoc.setEstado(est == 1);

            boolean resultado = dao.actualizarTipoDocumento(miTipoDoc);
            if (resultado) {
                System.out.println("El tipo de documento se actualizo correctamente");
            } else {
                System.out.println("El tipo de documento no se pudo actualizar");
            }
        } else {
            System.out.println("No existe un tipo de documento con el ID ingresado.");
        }
        sc.close();
    }
}

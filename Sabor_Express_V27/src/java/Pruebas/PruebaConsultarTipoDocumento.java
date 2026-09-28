  
                                                                                                     
                                                                                         
   
package Pruebas;

import Controlador.TipoDocumentoDAO;
import Modelo.TipoDocumento;
import java.util.Scanner;

   
  
                   
   
public class PruebaConsultarTipoDocumento {

       
                                             
       
    public static void main(String[] args) {

        Scanner sc = new Scanner(System.in);
        TipoDocumentoDAO dao = new TipoDocumentoDAO();

        System.out.println("Ingrese el ID del tipo de documento a consultar: ");
        int idTipoDoc = sc.nextInt();

        TipoDocumento miTipoDoc = dao.consultarTipoDocumento(idTipoDoc);

        if (miTipoDoc != null) {
            System.out.println("--- DATOS DEL TIPO DE DOCUMENTO CONSULTADO ---");
            System.out.println("ID Tipo Documento: " + miTipoDoc.getIdTipoDocumento());
            System.out.println("Descripcion: " + miTipoDoc.getDescripcionTipoDocumento());
            System.out.println("Estado: " + (miTipoDoc.isEstado() ? "Activo" : "Inactivo"));
        } else {
            System.out.println("No se encontro el tipo de documento con el ID ingresado.");
        }
        sc.close();
    }
}

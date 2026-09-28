  
                                                                                                     
                                                                                         
   
package Pruebas;

import Controlador.UsuarioDAO;
import java.util.Scanner;

   
  
                   
   
public class PruebaActivarUsuario {

       
                                             
       
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        UsuarioDAO dao = new UsuarioDAO();

        System.out.println("Por favor ingrese el ID del usuario a activar:");
        int id = sc.nextInt();

        if (dao.ActivarUsuario(id)) {
            System.out.println("Se activó con éxito");
        } else {
            System.out.println("No se pudo activar el usuario");
        }

        sc.close();
    }
}
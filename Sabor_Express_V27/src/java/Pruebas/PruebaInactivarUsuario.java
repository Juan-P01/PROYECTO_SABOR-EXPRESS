  
                                                                                                     
                                                                                         
   
package Pruebas;

import Controlador.UsuarioDAO;
import java.util.Scanner;

public class PruebaInactivarUsuario {

       
                                             
       
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        UsuarioDAO dao = new UsuarioDAO();

        System.out.println("por favor ingrese el usuario a inactivar");
        int id = sc.nextInt();

        if (dao.inactivarUsuario(id)) {
            System.out.println("Se inactivo con exito");
        } else {
            System.out.println("No se encontro el usuario");
        }
        
        sc.close();
    }
}
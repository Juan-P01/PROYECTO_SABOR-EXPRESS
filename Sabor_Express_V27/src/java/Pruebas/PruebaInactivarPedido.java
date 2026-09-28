  
                                                                                                     
                                                                                         
   
package Pruebas;

import Controlador.PedidoDAO;
import java.util.Scanner;

   
  
                   
   
public class PruebaInactivarPedido {

       
                                             
       
    public static void main(String[] args) {

        Scanner sc = new Scanner(System.in);
        PedidoDAO dao = new PedidoDAO();

        System.out.println("Ingrese el ID del pedido a inactivar: ");
        int idPedido = sc.nextInt();

        boolean resultado = dao.inactivarPedido(idPedido);
        if (resultado) {
            System.out.println("El pedido se inactivo correctamente");
        } else {
            System.out.println("El pedido no se pudo inactivar");
        }
        sc.close();
    }
}

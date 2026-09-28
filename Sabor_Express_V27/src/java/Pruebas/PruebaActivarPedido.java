  
                                                                                                     
                                                                                         
   
package Pruebas;

import Controlador.PedidoDAO;
import java.util.Scanner;

   
  
                   
   
public class PruebaActivarPedido {

       
                                             
       
    public static void main(String[] args) {

        Scanner sc = new Scanner(System.in);
        PedidoDAO dao = new PedidoDAO();

        System.out.println("Ingrese el ID del pedido a activar: ");
        int idPedido = sc.nextInt();

        boolean resultado = dao.activarPedido(idPedido);
        if (resultado) {
            System.out.println("El pedido se activo correctamente");
        } else {
            System.out.println("El pedido no se pudo activar");
        }
        sc.close();
    }
}

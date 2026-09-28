  
                                                                                                     
                                                                                         
   
package Pruebas;

import Controlador.PedidoDAO;
import Modelo.Pedido;
import java.util.Scanner;

   
  
                   
   
public class PruebaConsultarPedido {

       
                                             
       
    public static void main(String[] args) {

        Scanner sc = new Scanner(System.in);
        PedidoDAO dao = new PedidoDAO();

        System.out.println("Ingrese el ID del pedido a consultar: ");
        int idPedido = sc.nextInt();

        Pedido miPedido = dao.consultarPedido(idPedido);

        if (miPedido != null) {
            System.out.println("--- DATOS DEL PEDIDO CONSULTADO ---");
            System.out.println("ID Pedido: " + miPedido.getIdPedido());
            System.out.println("Total: " + miPedido.getTotal());
            System.out.println("Fecha Pedido: " + miPedido.getFechaPedido());
            System.out.println("ID Mesa: " + miPedido.getMesasIdMesa());
            System.out.println("ID Cliente: " + miPedido.getClientesIdCliente());
            System.out.println("ID Usuario: " + miPedido.getUsuariosIdUsuario());
            System.out.println("ID Estado Pedido: " + miPedido.getEstadosPedidosIdEstadoPedido());
            System.out.println("Estado: " + (miPedido.isEstado() ? "Activo" : "Inactivo"));
        } else {
            System.out.println("No se encontro el pedido con el ID ingresado.");
        }
        sc.close();
    }
}
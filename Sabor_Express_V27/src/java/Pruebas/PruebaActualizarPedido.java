  
                                                                                                     
                                                                                         
   
package Pruebas;

import Controlador.PedidoDAO;
import Modelo.Pedido;
import java.util.Scanner;

   
  
                   
   
public class PruebaActualizarPedido {

       
                                             
       
    public static void main(String[] args) {

        Scanner sc = new Scanner(System.in);
        PedidoDAO dao = new PedidoDAO();

        System.out.println("Ingrese el ID del pedido a actualizar: ");
        int idPedido = sc.nextInt();

        Pedido miPedido = dao.consultarPedido(idPedido);

        if (miPedido != null) {
            System.out.println("Ingrese el nuevo total del pedido: ");
            miPedido.setTotal(sc.nextDouble());

            System.out.println("Ingrese el nuevo ID de la mesa: ");
            miPedido.setMesasIdMesa(sc.nextInt());

            System.out.println("Ingrese el nuevo ID del cliente: ");
            miPedido.setClientesIdCliente(sc.nextInt());

            System.out.println("Ingrese el nuevo ID del usuario (Mesero/Empleado): ");
            miPedido.setUsuariosIdUsuario(sc.nextInt());

            System.out.println("Ingrese el nuevo ID del estado del pedido: ");
            miPedido.setEstadosPedidosIdEstadoPedido(sc.nextInt());

            System.out.println("Ingrese el estado del registro (1 para Activo / 0 para Inactivo): ");
            int est = sc.nextInt();
            miPedido.setEstado(est == 1);

            boolean resultado = dao.actualizarPedido(miPedido);
            if (resultado) {
                System.out.println("El pedido se actualizo correctamente");
            } else {
                System.out.println("El pedido no se pudo actualizar");
            }
        } else {
            System.out.println("No existe un pedido con el ID ingresado.");
        }
        sc.close();
    }
}

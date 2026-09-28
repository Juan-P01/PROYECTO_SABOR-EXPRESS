  
                                                                                                     
                                                                                         
   
package Pruebas;

import Controlador.PedidoDAO;
import Modelo.Pedido;
import java.time.Instant;
import java.util.Scanner;

   
  
                   
   
public class PruebaInsertarPedido {

       
                                             
       
    public static void main(String[] args) {

        Scanner sc = new Scanner(System.in);
        Pedido miPedido = new Pedido();
        PedidoDAO dao = new PedidoDAO();

        System.out.println("Por favor ingrese el total del pedido (Ej: 68500.00): ");
        miPedido.setTotal(sc.nextDouble());

        Instant ahora = Instant.now();
        miPedido.setFechaPedido(ahora);

        System.out.println("Ingrese el ID de la mesa: ");
        miPedido.setMesasIdMesa(sc.nextInt());

        System.out.println("Ingrese el ID del cliente: ");
        miPedido.setClientesIdCliente(sc.nextInt());

        System.out.println("Ingrese el ID del usuario (Mesero/Empleado): ");
        miPedido.setUsuariosIdUsuario(sc.nextInt());

        System.out.println("Ingrese el ID del estado del pedido (1: Pendiente, 2: En Preparacion, 3: Servido, 4: Pagado): ");
        miPedido.setEstadosPedidosIdEstadoPedido(sc.nextInt());

        miPedido.setEstado(true);

        boolean resultado = dao.insertarPedido(miPedido);
        if (resultado) {
            System.out.println("El pedido se guardo correctamente");
        } else {
            System.out.println("El pedido no se pudo registrar");
        }
        sc.close();
    }
}

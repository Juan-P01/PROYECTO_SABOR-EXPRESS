package Pruebas;

import Controlador.FacturaDAO;
import Modelo.Factura;
import java.sql.Date;
import java.util.Scanner;

public class PruebaInsertarFactura {
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        Factura miFactura = new Factura();
        FacturaDAO dao = new FacturaDAO();

        System.out.println("Por favor ingrese el numero de factura: ");
        miFactura.setNumeroFactura(sc.nextLine());

        System.out.println("Por favor ingrese el total de la factura: ");
        miFactura.setTotalFactura(sc.nextDouble());
        sc.nextLine();

        System.out.println("Ingrese la fecha (YYYY-MM-DD): ");
        String fecha = sc.nextLine();
        miFactura.setFechaFactura(Date.valueOf(fecha));

        System.out.println("Por favor ingrese el ID del pedido: ");
        miFactura.setPedidosIdPedido(sc.nextInt());

        System.out.println("Por favor ingrese el ID del cliente: ");
        miFactura.setClientesIdCliente(sc.nextInt());

        boolean resultado = dao.InsertarFactura(miFactura);
        if (resultado) {
            System.out.println("La factura se guardo correctamente");
        } else {
            System.out.println("La factura no se pudo registrar");
        }
        sc.close();
    }
}
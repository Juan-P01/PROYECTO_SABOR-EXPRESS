package Pruebas;

import Controlador.FacturaDAO;
import Modelo.Factura;
import java.util.Scanner;

public class PruebaConsultaFactura {
    public static void main(String[] args) {
        FacturaDAO miFacturaDAO = new FacturaDAO();
        Scanner leer = new Scanner(System.in);
        System.out.println("Por favor ingrese el numero de factura: ");
        String numFactura = leer.next();
        Factura miFactura = miFacturaDAO.consultarFactura(numFactura);

        if (miFactura != null) {
            System.out.println("ID Factura: " + miFactura.getIdFactura());
            System.out.println("Numero Factura: " + miFactura.getNumeroFactura());
            System.out.println("Total: " + miFactura.getTotalFactura());
            System.out.println("Fecha Factura: " + miFactura.getFechaFactura());
            System.out.println("ID Pedido: " + miFactura.getPedidosIdPedido());
            System.out.println("ID Cliente: " + miFactura.getClientesIdCliente());
            System.out.println("Estado: " + miFactura.getEstado());
        } else {
            System.out.println("No se encontro la factura");
        }
        leer.close();
    }
}
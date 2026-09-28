package Pruebas;

import Controlador.FacturaDAO;
import Modelo.Factura;
import java.sql.Date;
import java.util.Scanner;

public class PruebaActualizarFactura {
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        Factura miFactura = new Factura();
        FacturaDAO dao = new FacturaDAO();

        System.out.println("porfavor ingrese su ID actualizar");
        miFactura.setIdFactura(sc.nextInt());
        sc.nextLine();

        System.out.println("porfavor ingrese el numero de factura actualizar");
        miFactura.setNumeroFactura(sc.nextLine());

        System.out.println("porfavor ingrese el total actualizar");
        miFactura.setTotalFactura(sc.nextDouble());
        sc.nextLine();

        System.out.println("Ingrese la nueva fecha (YYYY-MM-DD): ");
        String fecha = sc.nextLine();
        miFactura.setFechaFactura(Date.valueOf(fecha));

        boolean resultado = dao.actualizarFactura(miFactura);
        if (resultado) {
            System.out.println("se actualizo correctamente");
        } else {
            System.out.println("la Factura no se actualizo correctamente intente por favor mas tarde");
        }
        sc.close();
    }
}
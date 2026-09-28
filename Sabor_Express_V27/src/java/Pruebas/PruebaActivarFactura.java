package Pruebas;

import Controlador.FacturaDAO;
import java.util.Scanner;

public class PruebaActivarFactura {
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        FacturaDAO dao = new FacturaDAO();

        System.out.println("por favor ingrese la factura a activar");
        int id = sc.nextInt();

        if (dao.ActivarFactura(id)) {
            System.out.println("Se activo con exito");
        } else {
            System.out.println("No se encontro la factura");
        }
        sc.close();
    }
}
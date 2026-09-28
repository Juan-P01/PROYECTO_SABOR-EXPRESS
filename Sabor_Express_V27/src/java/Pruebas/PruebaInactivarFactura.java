package Pruebas;

import Controlador.FacturaDAO;
import java.util.Scanner;

public class PruebaInactivarFactura {
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        FacturaDAO dao = new FacturaDAO();

        System.out.println("por favor ingrese la factura a inactivar");
        int id = sc.nextInt();

        if (dao.inactivarFactura(id)) {
            System.out.println("Se inactivo con exito");
        } else {
            System.out.println("No se encontro la factura");
        }
        sc.close();
    }
}
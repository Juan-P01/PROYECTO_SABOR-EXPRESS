package Pruebas;

import Controlador.RolDAO;
import java.util.Scanner;

public class PruebaInactivarRol {
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        RolDAO dao = new RolDAO();

        System.out.println("por favor ingrese el rol a inactivar");
        int id = sc.nextInt();

        if (dao.inactivarRol(id)) {
            System.out.println("Se inactivo con exito");
        } else {
            System.out.println("No se encontro el rol");
        }
        sc.close();
    }
}
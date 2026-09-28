package Pruebas;

import Controlador.RolDAO;
import java.util.Scanner;

public class PruebaActivarRol {
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        RolDAO dao = new RolDAO();

        System.out.println("por favor ingrese el rol a activar");
        int id = sc.nextInt();

        if (dao.ActivarRol(id)) {
            System.out.println("Se activo con exito");
        } else {
            System.out.println("No se encontro el rol");
        }
        sc.close();
    }
}
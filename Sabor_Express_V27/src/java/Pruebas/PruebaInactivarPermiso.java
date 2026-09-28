package Pruebas;

import Controlador.PermisoDAO;
import java.util.Scanner;

public class PruebaInactivarPermiso {
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        PermisoDAO dao = new PermisoDAO();

        System.out.println("por favor ingrese el id del permiso a inactivar");
        int id = sc.nextInt();

        if (dao.inactivarPermiso(id)) {
            System.out.println("Se inactivo con exito");
        } else {
            System.out.println("No se encontro el permiso");
        }
        sc.close();
    }
}
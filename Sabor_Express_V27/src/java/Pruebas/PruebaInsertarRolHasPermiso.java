package Pruebas;

import Controlador.RolHasPermisoDAO;
import Modelo.RolHasPermiso;
import java.util.Scanner;

public class PruebaInsertarRolHasPermiso {

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        RolHasPermisoDAO dao = new RolHasPermisoDAO();
        RolHasPermiso rp = new RolHasPermiso();

        System.out.print("Ingrese el ID del Rol: ");
        rp.setIdRol(scanner.nextInt());

        System.out.print("Ingrese el ID del Permiso: ");
        rp.setIdPermiso(scanner.nextInt());

        rp.setEstado(1);

        if (dao.insertar(rp)) {
            System.out.println("Relación Rol-Permiso insertada correctamente.");
        } else {
            System.out.println("Error al insertar la relación Rol-Permiso.");
        }

        scanner.close();
    }
}
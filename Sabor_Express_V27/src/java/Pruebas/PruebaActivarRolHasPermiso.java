package Pruebas;

import Controlador.RolHasPermisoDAO;
import java.util.Scanner;

public class PruebaActivarRolHasPermiso {

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        RolHasPermisoDAO dao = new RolHasPermisoDAO();

        System.out.print("Ingrese el ID del Rol: ");
        int idRol = scanner.nextInt();

        System.out.print("Ingrese el ID del Permiso a activar: ");
        int idPermiso = scanner.nextInt();

        if (dao.activar(idRol, idPermiso)) {
            System.out.println("Relación Rol (" + idRol + ") - Permiso (" + idPermiso + ") activada correctamente (estado = 1).");
        } else {
            System.out.println("Error al activar la relación.");
        }

        scanner.close();
    }
}
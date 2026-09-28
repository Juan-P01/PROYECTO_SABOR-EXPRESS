package Pruebas;

import Controlador.RolHasPermisoDAO;
import java.util.Scanner;

public class PruebaInactivarRolHasPermiso {

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        RolHasPermisoDAO dao = new RolHasPermisoDAO();

        System.out.print("Ingrese el ID del Rol: ");
        int idRol = scanner.nextInt();

        System.out.print("Ingrese el ID del Permiso a inactivar: ");
        int idPermiso = scanner.nextInt();

        if (dao.inactivar(idRol, idPermiso)) {
            System.out.println("Relación Rol (" + idRol + ") - Permiso (" + idPermiso + ") inactivada correctamente (estado = 0).");
        } else {
            System.out.println("Error al inactivar la relación.");
        }

        scanner.close();
    }
}
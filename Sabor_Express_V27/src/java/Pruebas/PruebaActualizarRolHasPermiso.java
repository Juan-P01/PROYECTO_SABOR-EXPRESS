package Pruebas;

import Controlador.RolHasPermisoDAO;
import Modelo.RolHasPermiso;
import java.util.Scanner;

public class PruebaActualizarRolHasPermiso {

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        RolHasPermisoDAO dao = new RolHasPermisoDAO();
        RolHasPermiso rp = new RolHasPermiso();

        System.out.print("Ingrese el ID del Rol actual a modificar: ");
        int idRolAntiguo = scanner.nextInt();

        System.out.print("Ingrese el ID del Permiso actual a modificar: ");
        int idPermisoAntiguo = scanner.nextInt();

        System.out.print("Ingrese el nuevo ID del Rol: ");
        rp.setIdRol(scanner.nextInt());

        System.out.print("Ingrese el nuevo ID del Permiso: ");
        rp.setIdPermiso(scanner.nextInt());

        System.out.print("Ingrese el nuevo estado (1 activo, 0 inactivo): ");
        rp.setEstado(scanner.nextInt());

        if (dao.actualizar(rp, idRolAntiguo, idPermisoAntiguo)) {
            System.out.println("Relación Rol-Permiso actualizada correctamente.");
        } else {
            System.out.println("Error al actualizar la relación Rol-Permiso.");
        }

        scanner.close();
    }
}
package Pruebas;

import Controlador.RolHasPermisoDAO;
import Modelo.RolHasPermiso;
import java.util.Scanner;

public class PruebaConsultaRolHasPermiso {

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        RolHasPermisoDAO dao = new RolHasPermisoDAO();

        System.out.print("Ingrese el ID del Rol a consultar: ");
        int idRol = scanner.nextInt();

        System.out.print("Ingrese el ID del Permiso a consultar: ");
        int idPermiso = scanner.nextInt();

        RolHasPermiso rp = dao.consultar(idRol, idPermiso);

        if (rp.getIdRol() != 0 && rp.getIdPermiso() != 0) {
            System.out.println("\n--- Registro Encontrado ---");
            System.out.println("ID Rol: " + rp.getIdRol());
            System.out.println("ID Permiso: " + rp.getIdPermiso());
            System.out.println("Estado: " + rp.getEstado());
        } else {
            System.out.println("No se encontró la relación para el Rol ID " + idRol + " y Permiso ID " + idPermiso);
        }

        scanner.close();
    }
}
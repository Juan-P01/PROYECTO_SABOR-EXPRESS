package Pruebas;

import Controlador.PermisoDAO;
import Modelo.Permiso;
import java.util.Scanner;

public class PruebaConsultaPermiso {
    public static void main(String[] args) {
        PermisoDAO miPermisoDAO = new PermisoDAO();
        Scanner leer = new Scanner(System.in);
        System.out.println("Por favor ingrese el ID del permiso: ");
        int id = leer.nextInt();
        Permiso miPermiso = miPermisoDAO.consultarPermiso(id);

        if (miPermiso != null) {
            System.out.println("ID Permiso: " + miPermiso.getIdPermisos());
            System.out.println("Descripcion: " + miPermiso.getDescripcion());
            System.out.println("Estado: " + miPermiso.getEstado());
        } else {
            System.out.println("No se encontro el permiso");
        }
        leer.close();
    }
}
package Pruebas;

import Controlador.PermisoDAO;
import Modelo.Permiso;
import java.util.Scanner;

public class PruebaInsertarPermiso {
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        Permiso miPermiso = new Permiso();
        PermisoDAO dao = new PermisoDAO();

        System.out.println("Por favor ingrese la descripcion del permiso: ");
        miPermiso.setDescripcion(sc.nextLine());

        boolean resultado = dao.InsertarPermiso(miPermiso);
        if (resultado) {
            System.out.println("El permiso se guardo correctamente");
        } else {
            System.out.println("El permiso no se pudo registrar");
        }
        sc.close();
    }
}
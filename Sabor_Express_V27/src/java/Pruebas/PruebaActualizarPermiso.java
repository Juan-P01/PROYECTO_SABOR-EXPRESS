package Pruebas;

import Controlador.PermisoDAO;
import Modelo.Permiso;
import java.util.Scanner;

public class PruebaActualizarPermiso {
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        Permiso miPermiso = new Permiso();
        PermisoDAO dao = new PermisoDAO();

        System.out.println("porfavor ingrese su ID actualizar");
        miPermiso.setIdPermisos(sc.nextInt());
        sc.nextLine();

        System.out.println("porfavor ingrese la descripcion actualizar");
        miPermiso.setDescripcion(sc.nextLine());

        boolean resultado = dao.actualizarPermiso(miPermiso);
        if (resultado) {
            System.out.println("se actualizo correctamente");
        } else {
            System.out.println("el Permiso no se actualizo correctamente intente por favor mas tarde");
        }
        sc.close();
    }
}
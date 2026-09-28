package Pruebas;

import Controlador.RolDAO;
import Modelo.Rol;
import java.util.Scanner;

public class PruebaActualizarRol {

    public static void main(String[] args) {

        Scanner sc = new Scanner(System.in);
        Rol miRol = new Rol();
        RolDAO dao = new RolDAO();

        System.out.println("Por favor ingrese el ID del rol a actualizar:");
        miRol.setIdRol(sc.nextInt());
        sc.nextLine();

        System.out.println("Por favor ingrese el nombre del rol:");
        miRol.setTipoDeRol(sc.nextLine());

        boolean resultado = dao.actualizarRol(miRol);

        if (resultado) {
            System.out.println("Se actualizó correctamente");
        } else {
            System.out.println("El rol no se actualizó correctamente. Intente nuevamente");
        }

        sc.close();
    }
}
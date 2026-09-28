package Pruebas;

import Controlador.RolDAO;
import Modelo.Rol;
import java.util.Scanner;

public class PruebaInsertarRol {

    public static void main(String[] args) {

        Scanner sc = new Scanner(System.in);
        Rol miRol = new Rol();
        RolDAO dao = new RolDAO();

        System.out.println("Por favor ingrese el tipo de rol:");
        miRol.setTipoDeRol(sc.nextLine());

        miRol.setEstado(1);

        if (dao.InsertarRol(miRol)) {
            System.out.println("Rol insertado exitosamente");
        } else {
            System.out.println("No se pudo insertar el rol");
        }

        sc.close();
    }
}
package Pruebas;

import Controlador.RolDAO;
import Modelo.Rol;
import java.util.Scanner;

public class PruebaConsultaRol {
    public static void main(String[] args) {
        RolDAO miRolDAO = new RolDAO();
        Scanner leer = new Scanner(System.in);
        System.out.println("Por favor ingrese el ID del rol: ");
        int id = leer.nextInt();
        Rol miRol = miRolDAO.consultarRol(id);

        if (miRol != null) {
            System.out.println("ID Rol: " + miRol.getIdRol());
            System.out.println("Tipo de Rol: " + miRol.getTipoDeRol());
            System.out.println("Estado: " + miRol.getEstado());
        } else {
            System.out.println("No se encontro el rol");
        }
        leer.close();
    }
}
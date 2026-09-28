package Pruebas;

import Controlador.ClienteDAO;
import java.util.Scanner;

public class PruebaInactivarCliente {
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        ClienteDAO dao = new ClienteDAO();

        System.out.println("por favor ingrese el cliente a inactivar");
        int id = sc.nextInt();

        if (dao.inactivarCliente(id)) {
            System.out.println("Se inactivo con exito");
        } else {
            System.out.println("No se encontro el cliente");
        }
        sc.close();
    }
}
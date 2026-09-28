package Pruebas;

import Controlador.ClienteDAO;
import java.util.Scanner;

public class PruebaActivarCliente {
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        ClienteDAO dao = new ClienteDAO();

        System.out.println("por favor ingrese el cliente a activar");
        int id = sc.nextInt();

        if (dao.ActivarCliente(id)) {
            System.out.println("Se activo con exito");
        } else {
            System.out.println("No se encontro el cliente");
        }
        sc.close();
    }
}
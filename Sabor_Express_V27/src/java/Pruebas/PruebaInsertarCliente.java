package Pruebas;

import Controlador.ClienteDAO;
import Modelo.Cliente;
import java.util.Scanner;

public class PruebaInsertarCliente {
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        Cliente miCliente = new Cliente();
        ClienteDAO dao = new ClienteDAO();

        System.out.println("Por favor ingrese los puntos de fidelizacion: ");
        miCliente.setPuntosFidelizacion(sc.nextInt());

        System.out.println("Por favor ingrese el ID del usuario asociado: ");
        miCliente.setUsuariosIdUsuario(sc.nextInt());

        boolean resultado = dao.InsertarCliente(miCliente);
        if (resultado) {
            System.out.println("El cliente se guardo correctamente");
        } else {
            System.out.println("El cliente no se pudo registrar");
        }
        sc.close();
    }
}
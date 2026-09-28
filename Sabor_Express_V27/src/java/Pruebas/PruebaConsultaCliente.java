package Pruebas;

import Controlador.ClienteDAO;
import Modelo.Cliente;
import java.util.Scanner;

public class PruebaConsultaCliente {
    public static void main(String[] args) {
        ClienteDAO miClienteDAO = new ClienteDAO();
        Scanner leer = new Scanner(System.in);
        System.out.println("Por favor ingrese el ID del cliente: ");
        int id = leer.nextInt();
        Cliente miCliente = miClienteDAO.consultarCliente(id);

        if (miCliente != null) {
            System.out.println("ID Cliente: " + miCliente.getIdCliente());
            System.out.println("Puntos Fidelizacion: " + miCliente.getPuntosFidelizacion());
            System.out.println("ID Usuario: " + miCliente.getUsuariosIdUsuario());
            System.out.println("Estado: " + miCliente.getEstado());
        } else {
            System.out.println("No se encontro el cliente");
        }
        leer.close();
    }
}
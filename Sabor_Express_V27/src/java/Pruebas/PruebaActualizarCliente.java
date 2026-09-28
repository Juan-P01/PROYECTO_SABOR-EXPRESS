package Pruebas;

import Controlador.ClienteDAO;
import Modelo.Cliente;
import java.util.Scanner;

public class PruebaActualizarCliente {
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        Cliente miCliente = new Cliente();
        ClienteDAO dao = new ClienteDAO();

        System.out.println("porfavor ingrese su ID actualizar");
        miCliente.setIdCliente(sc.nextInt());

        System.out.println("porfavor ingrese los puntos de fidelizacion actualizar");
        miCliente.setPuntosFidelizacion(sc.nextInt());

        System.out.println("porfavor ingrese el ID de usuario actualizar");
        miCliente.setUsuariosIdUsuario(sc.nextInt());

        boolean resultado = dao.actualizarCliente(miCliente);
        if (resultado) {
            System.out.println("se actualizo correctamente");
        } else {
            System.out.println("el Cliente no se actualizo correctamente intente por favor mas tarde");
        }
        sc.close();
    }
}
package Pruebas;

import Controlador.NotificacionDAO;
import Modelo.Notificacion;
import java.util.Scanner;

public class PruebaActualizarNotificacion {
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        Notificacion miNotificacion = new Notificacion();
        NotificacionDAO dao = new NotificacionDAO();

        System.out.println("porfavor ingrese su ID actualizar");
        miNotificacion.setIdNotificacion(sc.nextInt());
        sc.nextLine();

        System.out.println("porfavor ingrese el titulo actualizar");
        miNotificacion.setTitulo(sc.nextLine());

        System.out.println("porfavor ingrese el mensaje actualizar");
        miNotificacion.setMensaje(sc.nextLine());

        System.out.println("Leido (true/false): ");
        miNotificacion.setLeido(sc.nextBoolean());

        boolean resultado = dao.actualizarNotificacion(miNotificacion);
        if (resultado) {
            System.out.println("se actualizo correctamente");
        } else {
            System.out.println("la Notificacion no se actualizo correctamente intente por favor mas tarde");
        }
        sc.close();
    }
}
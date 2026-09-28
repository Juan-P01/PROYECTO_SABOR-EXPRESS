package Pruebas;

import Controlador.NotificacionDAO;
import Modelo.Notificacion;
import java.time.Instant;
import java.util.Scanner;

public class PruebaInsertarNotificacion {
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        Notificacion miNotificacion = new Notificacion();
        NotificacionDAO dao = new NotificacionDAO();

        System.out.println("Por favor ingrese el titulo: ");
        miNotificacion.setTitulo(sc.nextLine());

        System.out.println("Por favor ingrese el mensaje: ");
        miNotificacion.setMensaje(sc.nextLine());

        miNotificacion.setFechaEnvio(Instant.now());

        System.out.println("Leido (true/false): ");
        miNotificacion.setLeido(sc.nextBoolean());

        System.out.println("Por favor ingrese el ID del usuario: ");
        miNotificacion.setUsuariosIdUsuario(sc.nextInt());

        boolean resultado = dao.InsertarNotificacion(miNotificacion);
        if (resultado) {
            System.out.println("La notificacion se guardo correctamente");
        } else {
            System.out.println("La notificacion no se pudo registrar");
        }
        sc.close();
    }
}
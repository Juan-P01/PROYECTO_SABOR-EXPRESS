package Pruebas;

import Controlador.NotificacionDAO;
import Modelo.Notificacion;
import java.util.Scanner;

public class PruebaConsultaNotificacion {
    public static void main(String[] args) {
        NotificacionDAO miNotificacionDAO = new NotificacionDAO();
        Scanner leer = new Scanner(System.in);
        System.out.println("Por favor ingrese el ID de la notificacion: ");
        int id = leer.nextInt();
        Notificacion miNotificacion = miNotificacionDAO.consultarNotificacion(id);

        if (miNotificacion != null) {
            System.out.println("ID Notificacion: " + miNotificacion.getIdNotificacion());
            System.out.println("Titulo: " + miNotificacion.getTitulo());
            System.out.println("Mensaje: " + miNotificacion.getMensaje());
            System.out.println("Fecha Envio: " + miNotificacion.getFechaEnvio());
            System.out.println("Leido: " + miNotificacion.isLeido());
            System.out.println("ID Usuario: " + miNotificacion.getUsuariosIdUsuario());
            System.out.println("Estado: " + miNotificacion.getEstado());
        } else {
            System.out.println("No se encontro la notificacion");
        }
        leer.close();
    }
}
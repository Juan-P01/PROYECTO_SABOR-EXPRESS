package Pruebas;

import Controlador.NotificacionDAO;
import java.util.Scanner;

public class PruebaActivarNotificacion {
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        NotificacionDAO dao = new NotificacionDAO();

        System.out.println("por favor ingrese la notificacion a activar");
        int id = sc.nextInt();

        if (dao.ActivarNotificacion(id)) {
            System.out.println("Se activo con exito");
        } else {
            System.out.println("No se encontro la notificacion");
        }
        sc.close();
    }
}
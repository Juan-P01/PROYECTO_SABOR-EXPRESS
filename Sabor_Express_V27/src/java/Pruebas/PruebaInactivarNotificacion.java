package Pruebas;

import Controlador.NotificacionDAO;
import java.util.Scanner;

public class PruebaInactivarNotificacion {
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        NotificacionDAO dao = new NotificacionDAO();

        System.out.println("por favor ingrese la notificacion a inactivar");
        int id = sc.nextInt();

        if (dao.inactivarNotificacion(id)) {
            System.out.println("Se inactivo con exito");
        } else {
            System.out.println("No se encontro la notificacion");
        }
        sc.close();
    }
}
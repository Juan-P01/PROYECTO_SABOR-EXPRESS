  
                                                                                                     
                                                                                         
   
package Pruebas;

import Controlador.MesaDAO;
import Modelo.Mesa;
import java.util.Scanner;

   
  
                   
   
public class PruebaConsultarMesa {

       
                                             
       
    public static void main(String[] args) {

        Scanner sc = new Scanner(System.in);
        MesaDAO dao = new MesaDAO();

        System.out.println("Ingrese el ID de la mesa a consultar: ");
        int idMesa = sc.nextInt();

        Mesa miMesa = dao.consultarMesa(idMesa);

        if (miMesa != null) {
            System.out.println("--- DATOS DE LA MESA CONSULTADA ---");
            System.out.println("ID Mesa: " + miMesa.getIdMesa());
            System.out.println("Numero de Mesa: " + miMesa.getNumeroMesa());
            System.out.println("Capacidad: " + miMesa.getCapacidad() + " personas");
            System.out.println("Ubicacion: " + miMesa.getUbicacion());
            System.out.println("Estado Operativo: " + miMesa.getEstadoMesa());
            System.out.println("Estado del Registro: " + (miMesa.isEstado() ? "Activo" : "Inactivo"));
        } else {
            System.out.println("No se encontro la mesa con el ID ingresado.");
        }
        sc.close();
    }
}

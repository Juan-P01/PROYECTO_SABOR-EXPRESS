  
                                                                                                     
                                                                                         
   
package Pruebas;

import Controlador.UsuarioDAO;
import Modelo.Usuario;
import java.util.Scanner;

   
  
                   
   

public class PruebaConsultaUsuario {

       
                                             
       
    public static void main(String[] args) {

        UsuarioDAO miUsuarioDAO = new UsuarioDAO();
        Scanner leer = new Scanner(System.in);
        String correo;
        
        System.out.println("Por favor ingrese el correo: ");
        correo = leer.next();
        Usuario miUsuario = miUsuarioDAO.consultarUsuario(correo);

        if (miUsuario != null) {
            System.out.println("ID Usuario: " + miUsuario.getIdUsuario());
            System.out.println("Nombre: " + miUsuario.getNombre());
            System.out.println("Apellido: " + miUsuario.getApellido());
            System.out.println("Direccion: " + miUsuario.getDireccion());
            System.out.println("Telefono: " + miUsuario.getTelefono());
            System.out.println("Correo: " + miUsuario.getCorreo());
            System.out.println("Numero de documento: " + miUsuario.getNroDocumento());
            System.out.println("Ultimo Acceso: " + miUsuario.getUltimoAcceso());
            System.out.println("Fecha Creacion: " + miUsuario.getFechaDeCreacion());
            System.out.println("Fecha Nacimiento: " + miUsuario.getFechaNacimiento());
            System.out.println("Fecha Vencimiento Clave: " + miUsuario.getFechaVencimientoClave());
            System.out.println("Autorizacion Datos: " + miUsuario.getAutorizacionDatos());
            System.out.println("ID Rol: " + miUsuario.getRolesIdRol());
            System.out.println("ID Tipo Documento: " + miUsuario.getTiposDocumentoIdTipoDocumento());
            System.out.println("Estado: " + miUsuario.isEstado());
        } else {
            System.out.println("No se encontro el usuario");
        }
        
        leer.close();
    }
}
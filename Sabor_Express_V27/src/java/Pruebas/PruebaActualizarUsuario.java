  
                                                                                                     
                                                                                         
   
package Pruebas;

import Controlador.UsuarioDAO;
import Modelo.Usuario;
import java.time.Instant;
import java.util.Scanner;

   
  
                   
   
public class PruebaActualizarUsuario {

       
                                             
       
    public static void main(String[] args) {

        Scanner sc = new Scanner(System.in);
        Usuario miUsuario = new Usuario();
        UsuarioDAO dao = new UsuarioDAO();

        System.out.println("porfavor ingrese su ID actualizar");
        int actualizar = sc.nextInt();
        sc.nextLine();
        miUsuario.setIdUsuario(actualizar);

        System.out.println("porfavor ingrese su nombre actualizar");
        miUsuario.setNombre(sc.nextLine());
        System.out.println("porfavor ingrese su apellido actualizar");
        miUsuario.setApellido(sc.nextLine());
        System.out.println("porfavor ingrese su direccion actualizar");
        miUsuario.setDireccion(sc.nextLine());
        System.out.println("porfavor ingrese su numero de telefono actualizar");
        miUsuario.setTelefono(sc.nextLine());
        System.out.println("porfavor ingrese su correo actualizar");
        miUsuario.setCorreo(sc.nextLine());
        System.out.println("porfavor ingrese su nueva clave");
        miUsuario.setContrasena(sc.nextLine());
        System.out.println("porfavor ingrese su numero de documento actualizar");
        miUsuario.setNroDocumento(String.valueOf(sc.nextLong()));
        sc.nextLine();

        System.out.println("Ingrese la nueva fecha de nacimiento (YYYY-MM-DDTHH:MM:SSZ): ");
        String fechaNac = sc.nextLine();
        miUsuario.setFechaNacimiento(Instant.parse(fechaNac));

        System.out.println("Ingrese el nuevo ID de rol: ");
        miUsuario.setRolesIdRol(sc.nextInt());

        System.out.println("Ingrese el nuevo ID de tipo de documento: ");
        miUsuario.setTiposDocumentoIdTipoDocumento(sc.nextInt());

        System.out.println("Ingrese el estado del usuario (true/false): ");
        miUsuario.setEstado(sc.nextBoolean());

        boolean resultado = dao.actualizarUsuario(miUsuario);
        if (resultado) {
            System.out.println("se actualizo correctamente");
        } else {
            System.out.println("el Usuario no se actualizo correctamente intente por favor mas tarde");
        }
        sc.close();
    }
}
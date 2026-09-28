  
                                                                                                     
                                                                                         
   
package Pruebas;

import Controlador.UsuarioDAO;
import Modelo.Usuario;
import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Scanner;

   
  
                   
   
public class PruebaInsertarUsuario {

       
                                             
       
    public static void main(String[] args) {

        Scanner sc = new Scanner(System.in);
        Usuario miUsuario = new Usuario();
        UsuarioDAO dao = new UsuarioDAO();

        System.out.println("Por favor Ingrese su nombre: ");
        miUsuario.setNombre(sc.nextLine());
        System.out.println("Por favor ingrese su apellido: ");
        miUsuario.setApellido(sc.nextLine());
        System.out.println("Por favor ingrese su direccion: ");
        miUsuario.setDireccion(sc.nextLine());
        System.out.println("Por favor ingrese su numero de telefono: ");
        miUsuario.setTelefono(sc.nextLine());
        System.out.println("Por favor ingrese su correo: ");
        miUsuario.setCorreo(sc.nextLine());
        System.out.println("Por favor ingrese su contrasena: ");
        miUsuario.setContrasena(sc.nextLine());
        System.out.println("Por favor ingrese su numero de documento: ");
        miUsuario.setNroDocumento(String.valueOf(sc.nextLong()));
        sc.nextLine(); 

        Instant ahora = Instant.now();
        miUsuario.setUltimoAcceso(ahora);
        miUsuario.setFechaDeCreacion(ahora);

        System.out.println("Ingrese la fecha de nacimiento (YYYY-MM-DD) Ej: 1995-05-20 : ");
        String fechaNac = sc.nextLine();
        LocalDate ldNac = LocalDate.parse(fechaNac);
        miUsuario.setFechaNacimiento(ldNac.atStartOfDay(ZoneId.systemDefault()).toInstant());

        System.out.println("Ingrese la fecha de vencimiento de clave (YYYY-MM-DD) Ej: 2026-12-31 : ");
        String fechaVenc = sc.nextLine();
        LocalDate ldVenc = LocalDate.parse(fechaVenc);
        miUsuario.setFechaVencimientoClave(ldVenc.atStartOfDay(ZoneId.systemDefault()).toInstant());

        System.out.println("Autorizacion de datos (1 para Autorizado / 0 para No Autorizado): ");
        miUsuario.setAutorizacionDatos(sc.nextInt());

        System.out.println("Ingrese un ID para el rol (1: Admin, 2: Mesero, 3: Cocinero, 4: Cliente): ");
        miUsuario.setRolesIdRol(sc.nextInt());

        System.out.println("Ingrese un ID para el tipo de documento (1: CC, 2: CE, 3: Pasaporte): ");
        miUsuario.setTiposDocumentoIdTipoDocumento(sc.nextInt());

        miUsuario.setEstado(true);

        boolean resultado = dao.InsertarUsuario(miUsuario);
        if (resultado) {
            System.out.println("El usuario se guardo correctamente");
        } else {
            System.out.println("El usuario no se pudo registrar");
        }
        sc.close();
    }
}
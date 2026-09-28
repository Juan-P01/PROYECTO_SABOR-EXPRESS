  
                                                                                                     
                                                                                         
   
package Pruebas;

import Controlador.Conexion;
import java.sql.Connection;

public class PruebaConexion {

       
                                             
       
    public static void main(String[] args) {
        

        Conexion hd = new Conexion();
        Connection con = hd.getConn();

    }

}

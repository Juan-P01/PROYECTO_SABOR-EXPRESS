package Controlador;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class Conexion {

    private final String driver = "com.mysql.cj.jdbc.Driver";
    private final String user = "root";
    private final String password = "";
    private final String basedatos = "sabor_express";

      
                  
                                                                       
       
    private final String url =
            "jdbc:mysql://localhost:3307/" + basedatos
            + "?useTimezone=true"
            + "&serverTimezone=UTC"
            + "&useSSL=false"
            + "&allowPublicKeyRetrieval=true";

    public Conexion() {
        try {
            Class.forName(driver);
        } catch (ClassNotFoundException e) {
            System.err.println("ERROR: No se encontró MySQL Connector/J.");
            e.printStackTrace();
        }
    }

      
                                                                   
                                                                    
                                                      
       
    public Connection getConn() {
        String puertoPreferido=System.getProperty("sabor.mysql.port","3306");
        String[] puertos=puertoPreferido.equals("3307")?new String[]{"3307","3306"}:new String[]{puertoPreferido,"3307"};
        SQLException ultimo=null;
        for(String puerto:puertos){
            String url="jdbc:mysql://localhost:"+puerto+"/"+basedatos+"?useTimezone=true&serverTimezone=UTC&useSSL=false&allowPublicKeyRetrieval=true";
            try{return DriverManager.getConnection(url,user,password);}
            catch(SQLException e){ultimo=e;}
        }
        if(ultimo!=null) System.err.println("ERROR AL CONECTAR CON MYSQL/MARIADB: "+ultimo.getMessage());
        return null;
    }
}
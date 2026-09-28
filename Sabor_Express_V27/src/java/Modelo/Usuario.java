  
                                                                                                     
                                                                                          
   
package Modelo;

import java.time.Instant;


   
  
                   
   
public class Usuario {

    public int getIdUsuario() {
        return idUsuario;
    }

    public void setIdUsuario(int idUsuario) {
        this.idUsuario = idUsuario;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getApellido() {
        return apellido;
    }

    public void setApellido(String apellido) {
        this.apellido = apellido;
    }

    public String getDireccion() {
        return direccion;
    }

    public void setDireccion(String direccion) {
        this.direccion = direccion;
    }

    public String getTelefono() {
        return telefono;
    }

    public void setTelefono(String telefono) {
        this.telefono = telefono;
    }

    public String getCorreo() {
        return correo;
    }

    public void setCorreo(String correo) {
        this.correo = correo;
    }

    public String getContrasena() {
        return contrasena;
    }

    public void setContrasena(String contrasena) {
        this.contrasena = contrasena;
    }

    public String getNroDocumento() {
        return nroDocumento;
    }

    public void setNroDocumento(String nroDocumento) {
        this.nroDocumento = nroDocumento;
    }

    public Instant getUltimoAcceso() {
        return ultimoAcceso;
    }

    public void setUltimoAcceso(Instant ultimoAcceso) {
        this.ultimoAcceso = ultimoAcceso;
    }

    public Instant getFechaDeCreacion() {
        return fechaDeCreacion;
    }

    public void setFechaDeCreacion(Instant fechaDeCreacion) {
        this.fechaDeCreacion = fechaDeCreacion;
    }

    public Instant getFechaNacimiento() {
        return fechaNacimiento;
    }

    public void setFechaNacimiento(Instant fechaNacimiento) {
        this.fechaNacimiento = fechaNacimiento;
    }

    public Instant getFechaVencimientoClave() {
        return fechaVencimientoClave;
    }

    public void setFechaVencimientoClave(Instant fechaVencimientoClave) {
        this.fechaVencimientoClave = fechaVencimientoClave;
    }

    public int getAutorizacionDatos() {
        return autorizacionDatos;
    }

    public void setAutorizacionDatos(int autorizacionDatos) {
        this.autorizacionDatos = autorizacionDatos;
    }

    public int getRolesIdRol() {
        return rolesIdRol;
    }

    public void setRolesIdRol(int rolesIdRol) {
        this.rolesIdRol = rolesIdRol;
    }

    public int getTiposDocumentoIdTipoDocumento() {
        return tiposDocumentoIdTipoDocumento;
    }

    public void setTiposDocumentoIdTipoDocumento(int tiposDocumentoIdTipoDocumento) {
        this.tiposDocumentoIdTipoDocumento = tiposDocumentoIdTipoDocumento;
    }

    public boolean isEstado() {
        return estado;
    }

    public void setEstado(boolean estado) {
        this.estado = estado;
    }
    
    private int idUsuario;
    private String nombre;
    private String apellido;
    private String direccion;
    private String telefono;
    private String correo;
    private String contrasena;
    private String nroDocumento;
    private Instant ultimoAcceso;
    private Instant fechaDeCreacion;
    private Instant fechaNacimiento;
    private Instant fechaVencimientoClave;
    private int autorizacionDatos;
    private int rolesIdRol;
    private int tiposDocumentoIdTipoDocumento;
    private boolean estado;


}

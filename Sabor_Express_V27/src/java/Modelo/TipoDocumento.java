  
                                                                                                     
                                                                                          
   
package Modelo;

   
  
                   
   
public class TipoDocumento {

    public int getIdTipoDocumento() {
        return idTipoDocumento;
    }

    public void setIdTipoDocumento(int idTipoDocumento) {
        this.idTipoDocumento = idTipoDocumento;
    }

    public String getDescripcionTipoDocumento() {
        return descripcionTipoDocumento;
    }

    public void setDescripcionTipoDocumento(String descripcionTipoDocumento) {
        this.descripcionTipoDocumento = descripcionTipoDocumento;
    }

    public boolean isEstado() {
        return estado;
    }

    public void setEstado(boolean estado) {
        this.estado = estado;
    }
    
    private int idTipoDocumento;
    private String descripcionTipoDocumento;
    private boolean estado;
    
}

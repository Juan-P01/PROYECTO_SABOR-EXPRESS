  
                                                                                                     
                                                                                          
   
package Modelo;

import java.time.Instant;

   
  
                   
   
public class HistorialPuntos {

    public int getIdHistorialPuntos() {
        return idHistorialPuntos;
    }

    public void setIdHistorialPuntos(int idHistorialPuntos) {
        this.idHistorialPuntos = idHistorialPuntos;
    }

    public int getPuntos() {
        return puntos;
    }

    public void setPuntos(int puntos) {
        this.puntos = puntos;
    }

    public Instant getFechaMovimiento() {
        return fechaMovimiento;
    }

    public void setFechaMovimiento(Instant fechaMovimiento) {
        this.fechaMovimiento = fechaMovimiento;
    }

    public String getTipoDeMovimiento() {
        return tipoDeMovimiento;
    }

    public void setTipoDeMovimiento(String tipoDeMovimiento) {
        this.tipoDeMovimiento = tipoDeMovimiento;
    }

    public int getPuntosRestantes() {
        return puntosRestantes;
    }

    public void setPuntosRestantes(int puntosRestantes) {
        this.puntosRestantes = puntosRestantes;
    }

    public int getClientesIdCliente() {
        return clientesIdCliente;
    }

    public void setClientesIdCliente(int clientesIdCliente) {
        this.clientesIdCliente = clientesIdCliente;
    }

    public boolean isEstado() {
        return estado;
    }

    public void setEstado(boolean estado) {
        this.estado = estado;
    }
    private int idHistorialPuntos;
    private int puntos;
    private Instant fechaMovimiento;
    private String tipoDeMovimiento;
    private int puntosRestantes;
    private int clientesIdCliente;
    private boolean estado;
    
}

package Modelo;

public class Mesa {
    private int idMesa;
    private int numeroMesa;
    private int estadosMesasIdEstadoMesa;
    private String estadoMesa;
    private boolean estado;

    
    private int capacidad;
    private String ubicacion;

    public int getIdMesa() { return idMesa; }
    public void setIdMesa(int idMesa) { this.idMesa = idMesa; }
    public int getNumeroMesa() { return numeroMesa; }
    public void setNumeroMesa(int numeroMesa) { this.numeroMesa = numeroMesa; }
    public int getEstadosMesasIdEstadoMesa() { return estadosMesasIdEstadoMesa; }
    public void setEstadosMesasIdEstadoMesa(int id) { this.estadosMesasIdEstadoMesa = id; }
    public String getEstadoMesa() { return estadoMesa; }
    public void setEstadoMesa(String estadoMesa) { this.estadoMesa = estadoMesa; }
    public boolean isEstado() { return estado; }
    public void setEstado(boolean estado) { this.estado = estado; }
    public int getCapacidad() { return capacidad; }
    public void setCapacidad(int capacidad) { this.capacidad = capacidad; }
    public String getUbicacion() { return ubicacion; }
    public void setUbicacion(String ubicacion) { this.ubicacion = ubicacion; }
}

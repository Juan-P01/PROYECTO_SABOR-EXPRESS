package Modelo;

public class Rol {

    private int idRol;
    private String tipoDeRol;
    private int estado;

    public int getIdRol() {
        return idRol;
    }

    public void setIdRol(int idRol) {
        this.idRol = idRol;
    }

    public String getTipoDeRol() {
        return tipoDeRol;
    }

    public void setTipoDeRol(String tipoDeRol) {
        this.tipoDeRol = tipoDeRol;
    }

    public int getEstado() {
        return estado;
    }

    public void setEstado(int estado) {
        this.estado = estado;
    }
}
package Modelo;

public class RolHasPermiso {
    private int idRol;
    private int idPermiso;
    private int estado;

    public RolHasPermiso() {
    }

    public RolHasPermiso(int idRol, int idPermiso, int estado) {
        this.idRol = idRol;
        this.idPermiso = idPermiso;
        this.estado = estado;
    }

    public int getIdRol() {
        return idRol;
    }

    public void setIdRol(int idRol) {
        this.idRol = idRol;
    }

    public int getIdPermiso() {
        return idPermiso;
    }

    public void setIdPermiso(int idPermiso) {
        this.idPermiso = idPermiso;
    }

    public int getEstado() {
        return estado;
    }

    public void setEstado(int estado) {
        this.estado = estado;
    }
}
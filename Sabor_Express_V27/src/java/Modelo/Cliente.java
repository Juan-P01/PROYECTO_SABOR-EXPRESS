package Modelo;

public class Cliente {
    private int idCliente;
    private int puntosFidelizacion;
    private int usuariosIdUsuario;
    private int estado;

    public int getIdCliente() {
        return idCliente;
    }

    public void setIdCliente(int idCliente) {
        this.idCliente = idCliente;
    }

    public int getPuntosFidelizacion() {
        return puntosFidelizacion;
    }

    public void setPuntosFidelizacion(int puntosFidelizacion) {
        this.puntosFidelizacion = puntosFidelizacion;
    }

    public int getUsuariosIdUsuario() {
        return usuariosIdUsuario;
    }

    public void setUsuariosIdUsuario(int usuariosIdUsuario) {
        this.usuariosIdUsuario = usuariosIdUsuario;
    }

    public int getEstado() {
        return estado;
    }

    public void setEstado(int estado) {
        this.estado = estado;
    }
}
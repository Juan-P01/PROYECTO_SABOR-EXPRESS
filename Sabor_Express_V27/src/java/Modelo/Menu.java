package Modelo;

public class Menu {
    private int idMenu;
    private String nombre;
    private String descripcion;
    private String categoria;
    private int idCategoria;
    private int puntosFidelizacion;
    private String imagen;
    private double precio;
    private int disponible;
    private int estado;

    public Menu() {}

    public Menu(int idMenu, String nombre, String descripcion, double precio, int disponible, int estado) {
        this.idMenu = idMenu;
        this.nombre = nombre;
        this.descripcion = descripcion;
        this.precio = precio;
        this.disponible = disponible;
        this.estado = estado;
    }

    public int getIdMenu() { return idMenu; }
    public void setIdMenu(int idMenu) { this.idMenu = idMenu; }
    public String getNombre() { return nombre; }
    public void setNombre(String nombre) { this.nombre = nombre; }
    public String getDescripcion() { return descripcion; }
    public void setDescripcion(String descripcion) { this.descripcion = descripcion; }
    public String getCategoria() { return categoria; }
    public void setCategoria(String categoria) { this.categoria = categoria; }
    public int getIdCategoria() { return idCategoria; }
    public void setIdCategoria(int idCategoria) { this.idCategoria = idCategoria; }
    public int getPuntosFidelizacion() { return puntosFidelizacion; }
    public void setPuntosFidelizacion(int puntosFidelizacion) { this.puntosFidelizacion = puntosFidelizacion; }
    public String getImagen() { return imagen; }
    public void setImagen(String imagen) { this.imagen = imagen; }
    public double getPrecio() { return precio; }
    public void setPrecio(double precio) { this.precio = precio; }
    public int getDisponible() { return disponible; }
    public void setDisponible(int disponible) { this.disponible = disponible; }
    public int getEstado() { return estado; }
    public void setEstado(int estado) { this.estado = estado; }
}

package guillermo.project;
public class Producto {
    private String claveProducto;
    private String descripcion;
    private double costoUnitario;
    private double impuesto;
    private double total;

    // Constructor
    public Producto(String claveProducto, String descripcion, double costoUnitario, double impuesto, double total) {
        this.claveProducto = claveProducto;
        this.descripcion = descripcion;
        this.costoUnitario = costoUnitario;
        this.impuesto = impuesto;
        this.total = total;
    }

    // Getters y Setters
    public String getClaveProducto() {
        return claveProducto;
    }

    public void setClaveProducto(String claveProducto) {
        this.claveProducto = claveProducto;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public double getCostoUnitario() {
        return costoUnitario;
    }

    public void setCostoUnitario(double costoUnitario) {
        this.costoUnitario = costoUnitario;
    }

    public double getImpuesto() {
        return impuesto;
    }

    public void setImpuesto(double impuesto) {
        this.impuesto = impuesto;
    }

    public double getTotal() {
        return total;
    }

    public void setTotal(double total) {
        this.total = total;
    }

    // Método toString para facilitar la impresión de los datos del producto
    @Override
    public String toString() {
        return "Producto{" +
                "claveProducto='" + claveProducto + '\'' +
                ", descripcion='" + descripcion + '\'' +
                ", costoUnitario=" + costoUnitario +
                ", impuesto=" + impuesto +
                ", total=" + total +
                '}';
    }
}

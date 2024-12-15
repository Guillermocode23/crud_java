public class Producto {
    private String claveProducto;
    private String descripcion;
    private double costoUnitario;
    private double impuesto;
    private double total;

    public Producto(String claveProducto, String descripcion, String costoUnitario, String impuesto, String total) {
        this.claveProducto = claveProducto;
        this.descripcion = descripcion;
        this.costoUnitario = Double.parseDouble(costoUnitario);
        this.impuesto = Double.parseDouble(impuesto);
        this.total = Double.parseDouble(total);
    }

    public String getClaveProducto() {
        return claveProducto;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public double getCostoUnitario() {
        return costoUnitario;
    }

    public double getImpuesto() {
        return impuesto;
    }

    public double getTotal() {
        return total;
    }
}

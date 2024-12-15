import java.util.Random;

public class Proveedor {
    private String idProveedor;
    private String razonSocial;

    public Proveedor(String razonSocial) {
        this.razonSocial = razonSocial;
        this.idProveedor = generarIdAleatorio(razonSocial, 7);
    }

    private String generarIdAleatorio(String razonSocial, int longitud) {
        // Formatea la razón social para crear la base del ID
        String base = razonSocial.replaceAll("\\s", "").toUpperCase();
        base = base.length() >= 3 ? base.substring(0, 3) : base;

        // Genera la parte aleatoria del ID
        String caracteres = "ABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";
        Random random = new Random();
        StringBuilder id = new StringBuilder(base);

        for (int i = 0; i < longitud; i++) {
            id.append(caracteres.charAt(random.nextInt(caracteres.length())));
        }
        return id.toString();
    }

    public String getIdProveedor() {
        return idProveedor;
    }

    public String getRazonSocial() {
        return razonSocial;
    }
}

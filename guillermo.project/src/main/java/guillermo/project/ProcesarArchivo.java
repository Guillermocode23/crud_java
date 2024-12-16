package guillermo.project;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.Random;

public class ProcesarArchivo {
    public static void main(String[] args) {
        // Ruta del archivo que contiene los datos a procesar
        String rutaArchivo = "C:\\Users\\HP\\OneDrive\\Desktop\\tab.txt";

        // Configuración de conexión a la base de datos MySQL
        String url = "jdbc:mysql://localhost:3306/crud_proyecto"; // Asegúrate de que la base de datos exista
        String usuario = "root"; // Cambia según tu usuario
        String contrasena = "patitocuak"; // Cambia según tu contraseña

        try (BufferedReader br = new BufferedReader(new FileReader(rutaArchivo));
             Connection conexion = DriverManager.getConnection(url, usuario, contrasena)) {

            // Inicia el procesamiento de las líneas del archivo
            procesarLineas(br, conexion);
            System.out.println("Procesamiento completado correctamente.");

        } catch (IOException e) {
            // Manejo de errores al leer el archivo
            System.out.println("Error al leer el archivo: " + e.getMessage());
        } catch (Exception e) {
            // Manejo de errores en la conexión con la base de datos
            System.out.println("Error en la base de datos: " + e.getMessage());
        }
    }

    // Método recursivo para procesar cada línea del archivo
    public static void procesarLineas(BufferedReader br, Connection conexion) throws IOException {
        // Lee una línea del archivo
        String linea = br.readLine();

        // Caso base: Si no hay más líneas, termina la recursión
        if (linea == null) {
            return;
        }

        // Elimina espacios en blanco al inicio y al final
        String[] columnas = linea.split("\\|");

        System.out.println("Procesando línea: " + linea);

        // Verifica que la línea tenga el número correcto de columnas
        if (columnas.length < 6) {
            System.out.println("Error en la línea: " + linea + " (no tiene el número esperado de columnas)");
        } else {
            try {
                // Procesa el proveedor y el producto de la línea
                String idProveedor = procesarProveedor(conexion, columnas[2]);
                procesarProducto(conexion, columnas, idProveedor);
            } catch (Exception e) {
                // Manejo de errores al procesar la línea
                System.out.println("Error procesando la línea: " + e.getMessage());
            }
        }

        // Llamada recursiva para procesar la siguiente línea
        procesarLineas(br, conexion);
    }

    // Método que procesa la información del proveedor
    public static String procesarProveedor(Connection conexion, String razonSocial) throws Exception {
        String queryProveedorExiste = "SELECT ID_PROVEEDOR FROM tb_proveedor WHERE RAZON_SOCIAL = ?";
        String queryProveedorInsertar = "INSERT INTO tb_proveedor (ID_PROVEEDOR, RAZON_SOCIAL, GIRO) VALUES (?, ?, ?)";
        String idProveedor = null;

        try (PreparedStatement stmtProveedorExiste = conexion.prepareStatement(queryProveedorExiste);
             PreparedStatement stmtProveedorInsertar = conexion.prepareStatement(queryProveedorInsertar)) {

            // Verifica si el proveedor ya existe en la base de datos
            stmtProveedorExiste.setString(1, razonSocial); // Establece la razón social del proveedor
            try (ResultSet rs = stmtProveedorExiste.executeQuery()) {
                if (rs.next()) {
                    // Si el proveedor existe, obtiene su ID
                    idProveedor = rs.getString("ID_PROVEEDOR");
                    System.out.println("Proveedor encontrado con ID: " + idProveedor);
                } else {
                    // Si el proveedor no existe, genera un nuevo ID
                    String razonSocialClean = razonSocial.replaceAll("\\s", "").toUpperCase();
                    idProveedor = razonSocialClean.length() >= 3 ? razonSocialClean.substring(0, 3) : razonSocialClean;
                    idProveedor += generarIdAleatorio(7); // Genera un ID aleatorio de 7 caracteres

                    // Inserta el nuevo proveedor en la base de datos
                    stmtProveedorInsertar.setString(1, idProveedor);
                    stmtProveedorInsertar.setString(2, razonSocial);
                    stmtProveedorInsertar.setString(3, "Giro por definir");
                    stmtProveedorInsertar.executeUpdate();
                    System.out.println("Proveedor insertado con ID: " + idProveedor);
                }
            }
        }
        return idProveedor;
    }

    // Método que procesa la información del producto
    public static void procesarProducto(Connection conexion, String[] columnas, String idProveedor) throws Exception {
        String queryProducto = "INSERT INTO tb_producto (CLAVE_PRODUCTO, DESCRIPCION, COSTO_UNITARIO, IMPUESTO, TOTAL, ID_PROVEEDOR) VALUES (?, ?, ?, ?, ?, ?)";
        try (PreparedStatement stmtProducto = conexion.prepareStatement(queryProducto)) {
            // Establece los valores del producto en la consulta SQL
            stmtProducto.setString(1, columnas[0]); // ClaveProducto
            stmtProducto.setString(2, columnas[1]); // DescripcionProducto
            stmtProducto.setDouble(3, Double.parseDouble(columnas[3])); // CostoUnitario
            stmtProducto.setDouble(4, Double.parseDouble(columnas[4])); // Impuesto
            stmtProducto.setDouble(5, Double.parseDouble(columnas[5])); // Total
            stmtProducto.setString(6, idProveedor); // Relación con el proveedor
            // Inserta el producto en la base de datos
            stmtProducto.executeUpdate();
            System.out.println("Producto insertado con clave: " + columnas[0]);
        }
    }

    // Método para generar un ID aleatorio de longitud especificada
    public static String generarIdAleatorio(int longitud) {
        String caracteres = "ABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";
        Random random = new Random();
        StringBuilder id = new StringBuilder();

        // Genera una cadena aleatoria de longitud "longitud"
        for (int i = 0; i < longitud; i++) {
            int indice = random.nextInt(caracteres.length());
            id.append(caracteres.charAt(indice));
        }

        return id.toString();
    }
}

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
        // Ruta del archivo
        String rutaArchivo = "C:\\Users\\HP\\OneDrive\\Desktop\\tab.txt";

        // Configuración de conexión a MySQL
        String url = "jdbc:mysql://localhost:3306/crud_proyecto"; // Asegúrate de que la base de datos exista
        String usuario = "root"; // Cambia según tu usuario
        String contrasena = "patitocuak"; // Cambia según tu contraseña

        // Procesar el archivo
        try (BufferedReader br = new BufferedReader(new FileReader(rutaArchivo));
             Connection conexion = DriverManager.getConnection(url, usuario, contrasena)) {

            String linea;
            while ((linea = br.readLine()) != null) {
                linea = linea.trim(); // Elimina espacios en blanco al inicio y al final
                String[] columnas = linea.split("\\|");

                // Verifica que el número de columnas sea correcto
                if (columnas.length < 6) {
                    System.out.println("Error en la línea: " + linea + " (no tiene el número esperado de columnas)");
                    continue;  // Salta la línea con error
                }

                // Verifica si el proveedor ya existe
                String queryProveedorExiste = "SELECT ID_PROVEEDOR FROM tb_proveedor WHERE RAZON_SOCIAL = ?";
                String queryProveedorInsertar = "INSERT INTO tb_proveedor (ID_PROVEEDOR, RAZON_SOCIAL, GIRO) VALUES (?, ?, ?)";
                String idProveedor = null;

                try (PreparedStatement stmtProveedorExiste = conexion.prepareStatement(queryProveedorExiste);
                     PreparedStatement stmtProveedorInsertar = conexion.prepareStatement(queryProveedorInsertar)) {

                    // Verifica si el proveedor ya existe por su razón social
                    stmtProveedorExiste.setString(1, columnas[2]); // Razón social
                    try (ResultSet rs = stmtProveedorExiste.executeQuery()) {
                        if (rs.next()) {
                            // El proveedor ya existe, usa el ID encontrado
                            idProveedor = rs.getString("ID_PROVEEDOR");
                            System.out.println("Proveedor encontrado con ID: " + idProveedor);
                        } else {
                            // Genera un nuevo ID si el proveedor no existe
                            String razonSocial = columnas[2].replaceAll("\\s", "").toUpperCase(); // Sin espacios y en mayúsculas
                            idProveedor = razonSocial.length() >= 3 ? razonSocial.substring(0, 3) : razonSocial;
                            idProveedor += generarIdAleatorio(7); // Agrega 7 caracteres aleatorios

                            // Inserta el nuevo proveedor
                            stmtProveedorInsertar.setString(1, idProveedor); // ID del proveedor
                            stmtProveedorInsertar.setString(2, columnas[2]); // Razón Social
                            stmtProveedorInsertar.setString(3, "Giro por definir"); // Giro
                            stmtProveedorInsertar.executeUpdate();
                            System.out.println("Proveedor insertado con ID: " + idProveedor);
                        }
                    }
                } catch (Exception e) {
                    System.out.println("Error al procesar proveedor: " + e.getMessage());
                    continue;
                }

                // Inserta el producto con referencia al proveedor
                String queryProducto = "INSERT INTO tb_producto (CLAVE_PRODUCTO, DESCRIPCION, COSTO_UNITARIO, IMPUESTO, TOTAL, ID_PROVEEDOR) VALUES (?, ?, ?, ?, ?, ?)";
                try (PreparedStatement stmtProducto = conexion.prepareStatement(queryProducto)) {
                    stmtProducto.setString(1, columnas[0]); // ClaveProducto
                    stmtProducto.setString(2, columnas[1]); // DescripcionProducto
                    stmtProducto.setDouble(3, Double.parseDouble(columnas[3])); // CostoUnitario
                    stmtProducto.setDouble(4, Double.parseDouble(columnas[4])); // Impuesto
                    stmtProducto.setDouble(5, Double.parseDouble(columnas[5])); // Total
                    stmtProducto.setString(6, idProveedor); // Relación con el proveedor
                    stmtProducto.executeUpdate();
                    System.out.println("Producto insertado con clave: " + columnas[0]);
                } catch (Exception e) {
                    System.out.println("Error al insertar en tb_producto: " + e.getMessage());
                }
            }

            System.out.println("Procesamiento completado correctamente.");
        } catch (IOException e) {
            System.out.println("Error al leer el archivo: " + e.getMessage());
        } catch (Exception e) {
            System.out.println("Error en la base de datos: " + e.getMessage());
        }
    }

    // Método para generar un ID aleatorio de la longitud especificada
    public static String generarIdAleatorio(int longitud) {
        String caracteres = "ABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";
        Random random = new Random();
        StringBuilder id = new StringBuilder();

        for (int i = 0; i < longitud; i++) {
            int indice = random.nextInt(caracteres.length());
            id.append(caracteres.charAt(indice));
        }

        return id.toString();
    }
}

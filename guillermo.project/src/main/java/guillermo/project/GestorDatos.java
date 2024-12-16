package guillermo.project;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

public class GestorDatos {
    // Método principal que procesa un proveedor y un producto
    public void procesarProveedorYProducto(Proveedor proveedor, Producto producto) {
        try (Connection conexion = ConexionBD.obtenerConexion()) {
            // Verificar si el proveedor ya existe en la base de datos
            if (!existeProveedor(conexion, proveedor)) {
                // Si el proveedor no existe, insertarlo en la base de datos
                insertarProveedor(conexion, proveedor);
            }
            // Insertar el producto asociado al proveedor
            insertarProducto(conexion, producto, proveedor.getIdProveedor());
        } catch (Exception e) {
            // Imprimir el error en caso de que ocurra una excepción
            e.printStackTrace(); // Muestra el stack trace para depuración
        }
    }

    // Método que verifica si el proveedor ya existe en la base de datos
    private boolean existeProveedor(Connection conexion, Proveedor proveedor) throws Exception {
        // Consulta SQL para verificar la existencia del proveedor
        String query = "SELECT ID_PROVEEDOR FROM tb_proveedor WHERE ID_PROVEEDOR = ?";
        try (PreparedStatement stmt = conexion.prepareStatement(query)) {
            // Establecer el valor del parámetro ID_PROVEEDOR en la consulta
            stmt.setString(1, proveedor.getIdProveedor());
            try (ResultSet rs = stmt.executeQuery()) {
                // Si existe un resultado, el proveedor ya está registrado
                return rs.next();
            }
        }
    }

    // Método que inserta un nuevo proveedor en la base de datos
    private void insertarProveedor(Connection conexion, Proveedor proveedor) throws Exception {
        // Consulta SQL para insertar un proveedor
        String query = "INSERT INTO tb_proveedor (ID_PROVEEDOR, RAZON_SOCIAL, GIRO) VALUES (?, ?, ?)";
        try (PreparedStatement stmt = conexion.prepareStatement(query)) {
            // Establecer los valores del proveedor en la consulta SQL
            stmt.setString(1, proveedor.getIdProveedor());
            stmt.setString(2, proveedor.getRazonSocial());
            stmt.setString(3, "Giro por definir"); // Valor predeterminado para el giro
            // Ejecutar la inserción
            stmt.executeUpdate();
            // Imprimir mensaje de confirmación de inserción
            System.out.println("Proveedor insertado: " + proveedor.getIdProveedor());
        }
    }

    // Método que inserta un producto en la base de datos, asociado a un proveedor
    private void insertarProducto(Connection conexion, Producto producto, String idProveedor) throws Exception {
        // Consulta SQL para insertar un producto
        String query = "INSERT INTO tb_producto (CLAVE_PRODUCTO, DESCRIPCION, COSTO_UNITARIO, IMPUESTO, TOTAL, ID_PROVEEDOR) VALUES (?, ?, ?, ?, ?, ?)";
        try (PreparedStatement stmt = conexion.prepareStatement(query)) {
            // Establecer los valores del producto y el ID del proveedor en la consulta SQL
            stmt.setString(1, producto.getClaveProducto());
            stmt.setString(2, producto.getDescripcion());
            stmt.setDouble(3, producto.getCostoUnitario());
            stmt.setDouble(4, producto.getImpuesto());
            stmt.setDouble(5, producto.getTotal());
            stmt.setString(6, idProveedor);
            // Ejecutar la inserción
            stmt.executeUpdate();
            // Imprimir mensaje de confirmación de inserción
            System.out.println("Producto insertado: " + producto.getClaveProducto());
        }
    }
}

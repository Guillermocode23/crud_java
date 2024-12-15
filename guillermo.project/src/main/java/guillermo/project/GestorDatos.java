package guillermo.project;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

public class GestorDatos {
    public void procesarProveedorYProducto(Proveedor proveedor, Producto producto) {
        try (Connection conexion = ConexionBD.obtenerConexion()) {
            if (!existeProveedor(conexion, proveedor)) {
                insertarProveedor(conexion, proveedor);
            }
            insertarProducto(conexion, producto, proveedor.getIdProveedor());
        } catch (Exception e) {
            e.printStackTrace(); // Muestra el stack trace para depuración
        }
    }

    private boolean existeProveedor(Connection conexion, Proveedor proveedor) throws Exception {
        String query = "SELECT ID_PROVEEDOR FROM tb_proveedor WHERE ID_PROVEEDOR = ?";
        try (PreparedStatement stmt = conexion.prepareStatement(query)) {
            stmt.setString(1, proveedor.getIdProveedor());
            try (ResultSet rs = stmt.executeQuery()) {
                return rs.next();
            }
        }
    }

    private void insertarProveedor(Connection conexion, Proveedor proveedor) throws Exception {
        String query = "INSERT INTO tb_proveedor (ID_PROVEEDOR, RAZON_SOCIAL, GIRO) VALUES (?, ?, ?)";
        try (PreparedStatement stmt = conexion.prepareStatement(query)) {
            stmt.setString(1, proveedor.getIdProveedor());
            stmt.setString(2, proveedor.getRazonSocial());
            stmt.setString(3, "Giro por definir");
            stmt.executeUpdate();
            System.out.println("Proveedor insertado: " + proveedor.getIdProveedor());
        }
    }

    private void insertarProducto(Connection conexion, Producto producto, String idProveedor) throws Exception {
        String query = "INSERT INTO tb_producto (CLAVE_PRODUCTO, DESCRIPCION, COSTO_UNITARIO, IMPUESTO, TOTAL, ID_PROVEEDOR) VALUES (?, ?, ?, ?, ?, ?)";
        try (PreparedStatement stmt = conexion.prepareStatement(query)) {
            stmt.setString(1, producto.getClaveProducto());
            stmt.setString(2, producto.getDescripcion());
            stmt.setDouble(3, producto.getCostoUnitario());
            stmt.setDouble(4, producto.getImpuesto());
            stmt.setDouble(5, producto.getTotal());
            stmt.setString(6, idProveedor);
            stmt.executeUpdate();
            System.out.println("Producto insertado: " + producto.getClaveProducto());
        }
    }
}

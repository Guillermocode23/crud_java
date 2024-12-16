package guillermo.project;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class ConexionBD {
    // URL de la base de datos MySQL
    private static final String URL = "jdbc:mysql://localhost:3306/crud_proyecto";
    // Usuario de la base de datos
    private static final String USUARIO = "root";
    // Contraseña de la base de datos
    private static final String CONTRASENA = "patitocuak";

    // Método que establece la conexión con la base de datos
    public static Connection obtenerConexion() throws SQLException {
        // Utiliza DriverManager para obtener una conexión a la base de datos
        return DriverManager.getConnection(URL, USUARIO, CONTRASENA);
    }
}

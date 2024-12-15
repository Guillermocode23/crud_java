import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
//conecta a base de datos
public class ConexionBD {
    private static final String URL = "jdbc:mysql://localhost:3306/crud_proyecto";
    private static final String USUARIO = "root";
    private static final String CONTRASENA = "patitocuak";

    public static Connection obtenerConexion() throws SQLException {
        return DriverManager.getConnection(URL, USUARIO, CONTRASENA);
    }
}

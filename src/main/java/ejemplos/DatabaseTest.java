package ejemplos;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/**
 *
 * @author Ruper
 */
public class DatabaseTest {
    public static void main(String[] args) {
        String url = "jdbc:mysql://192.168.0.27:3306/EmpleadosDB";
        String user = "alejandro";
        String password = "J0selu1s100%";
        System.out.println("Conectando... ");

        try (Connection conexion = DriverManager.getConnection(url, user, password)) {
            System.out.println("Conexión exitosa!");
        } catch (SQLException e) {
            System.out.println("Error en la conexión: " + e.getMessage());
            System.err.println("Número que representa el error: " + e.getErrorCode());
        }
    }
}

package tarea_David;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/**
 *
 * @author david
 */
public class DataBaseTest {

          public static void main(String[] args) {
                    String url = "jdbc:mysql://192.168.80.152:3306/EmpleadosDB";
                    String user = "alejandro";
                    String password = "J0selu1s100%";

                    try (Connection con = DriverManager.getConnection(url, user, password)) {
                              System.out.println("Conexión exitosa");
                    } catch (SQLException e) {
                              System.out.println("Error en la conexión: " + e.getMessage());
                    }

          }
}

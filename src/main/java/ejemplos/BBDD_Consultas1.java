package ejemplos;

import java.sql.*;

/**
 * Clase para manejar consultas a la base de datos de empleados.
 * @autor Ruper
 */
public class BBDD_Consultas1 {
    private Connection miConexion;
    private Statement miStatement;
    private ResultSet miResultSet;

    public static void main(String[] args) {
        BBDD_Consultas1 consultas = new BBDD_Consultas1();
        try {
            consultas.conectar();
            consultas.crearSentencia();
            consultas.miResultSet();
            consultas.mostrarResultados();
        } catch (SQLException e) {
            System.out.println("Error: " + e.getMessage());
        } finally {
            try {
                consultas.cerrarConexion();
            } catch (SQLException e) {
                System.out.println("Error al cerrar la conexión: " + e.getMessage());
            }
        }
    }

    public void conectar() throws SQLException {
        String url = "jdbc:mysql://192.168.0.27:3306/EmpleadosDB?allowPublicKeyRetrieval=true&useSSL=false&serverTimeZone=UTC";
        String user = "alejandro";
        String password = "J0selu1s100%";
        miConexion = DriverManager.getConnection(url, user, password);
        if (miConexion != null) {
            System.out.println("Conexion establecida!");
        }
    }

    public void crearSentencia() throws SQLException {
        miStatement = miConexion.createStatement();
    }

    public void miResultSet() throws SQLException {
        String consulta = "SELECT * FROM Empleados";
        miResultSet = miStatement.executeQuery(consulta);
    }

    public void mostrarResultados() throws SQLException {
        System.out.println("\nDATOS TABLA EMPLEADOS:");
        while (miResultSet.next()) {
            System.out.println(
                miResultSet.getString("nombre") + " " + 
                miResultSet.getString("apellidos") + " " +
                miResultSet.getDate("fechaNacimiento") + " " + 
                miResultSet.getDate("fechaIngreso") + " " + 
                miResultSet.getString("puesto") + " " + 
                miResultSet.getDouble("salario"));
        }
    }

    public void cerrarConexion() throws SQLException {
        if (miResultSet != null) miResultSet.close();
        if (miStatement != null) miStatement.close();
        if (miConexion != null) miConexion.close();
    }
}

package ejemplos;

import java.sql.*;

/**
 *
 * @author Ruper
 */
public class BBDD_Consultas {
    public static void main(String[] args) {
        //Datos conexion a MYSQL    Conexión segura con SSL
        String url = "jdbc:mysql://192.168.0.27:3306/EmpleadosDB?allowPublicKeyRetrieval=true&useSSL=false&serverTimeZone=UTC";
        String user = "alejandro";
        String password = "J0selu1s100%";
        
        try{
        //1º: Crear conexión
            Connection miConexion = DriverManager.getConnection(url, user, password);
            if(miConexion!=null){
                System.out.println("Conexion establecida!");
            }//Fin if
            
        //2º: Crear objeto Statment
            Statement miStatement = miConexion.createStatement();
                 
        //3ºPaso: Ejecutar SQL
            String consulta1 = "SELECT * FROM Empleados";
            //Guardo el resultado de la consulta
            ResultSet miResultSet = miStatement.executeQuery(consulta1);
                           
        //4ºPaso: Recorrer el ResultSet para ver los resultados
            System.out.println("\nDATOS TABLA EMPLEADOS:");
            while(miResultSet.next()){
                System.out.println(
                        miResultSet.getString("nombre") + " " + 
                        miResultSet.getString("apellidos")+ " " +
                        miResultSet.getDate("fechaNacimiento")+ " " + 
                        miResultSet.getDate("fechaIngreso")+ " " + 
                        miResultSet.getString("puesto")+ " " + 
                        miResultSet.getDouble("salario"));
            }
        }catch(SQLException e1){
            System.out.println("Error: "+e1.getMessage());
        }           
    }//Fin main        
}//Fin class

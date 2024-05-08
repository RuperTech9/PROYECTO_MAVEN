
package ejemplos;

import java.sql.*;

/**
 *
 * @author Ruper
 */
public class BBDD_ConsultasPreparadas {
    public static void main(String[] args) {
        //Datos conexion a MYSQL    Conexión segura con SSL
        String url = "jdbc:mysql://192.168.0.27:3306/prueba?allowPublicKeyRetrieval=true&useSSL=false&serverTimeZone=UTC";
        String user = "alex";
        String password = "J0selu1s100%";
        
        try{
        //1º: Crear conexión
            Connection miConexion = DriverManager.getConnection(url, user, password);
            if(miConexion!=null){
                System.out.println("Conexion establecida!");
            }//Fin if
            
        //2º: Preparar Consulta
            PreparedStatement miStatement = miConexion.prepareStatement("SELECT * FROM personas WHERE apellidos=? AND telefono=?");
                 
        //3ºPaso: Establecer parámetros de consulta
            miStatement.setString(1, "Martin");
            miStatement.setString(2, "666334455");

            //Guardo el resultado de la consulta
            ResultSet miResultSet = miStatement.executeQuery();
                           
        //4ºPaso: Recorrer el ResultSet para ver los resultados
            System.out.println("\nDATOS TABLA PERSONAS:");
            while(miResultSet.next()){
                System.out.println(
                        miResultSet.getString("nombre") + " " + 
                        miResultSet.getString("apellidos")+ " " + 
                        miResultSet.getString("telefono"));
            }
            miResultSet.close();
            
        // REUTILIZACION DE CONSULTA SQL    
            System.out.println("\nEJECUCION SEGUNDA CONSULTA");
            System.out.println("");
            
            miStatement.setString(1, "apellido1");
            miStatement.setString(2, "666666666");

            //Guardo el resultado de la consulta
            miResultSet = miStatement.executeQuery();
                           
        //4ºPaso: Recorrer el ResultSet para ver los resultados
            System.out.println("DATOS TABLA PERSONAS:");
            while(miResultSet.next()){
                System.out.println(
                        miResultSet.getString("nombre") + " " + 
                        miResultSet.getString("apellidos")+ " " + 
                        miResultSet.getString("telefono"));
            }
            miResultSet.close();
            
        }catch(SQLException e1){
            System.out.println("Error: "+e1.getMessage());
        }           
    }//Fin main        
}//Fin class
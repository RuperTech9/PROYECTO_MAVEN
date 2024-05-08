
package ejemplos;

import java.sql.*;

/**
 *
 * @author Ruper
 */
public class BBDD_Modificar {
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
            
        //2º: Crear objeto Statment
            Statement miStatement = miConexion.createStatement();
                         
        //3ºPaso: Ejecutar SQL
            String instruccionSQL_Insert = "INSERT INTO personas VALUES ('nombre1', 'apellido1', '666666666');";
            miStatement.executeUpdate(instruccionSQL_Insert);
            System.out.println("Datos insertados correctamente");
            
            String instruccionSQL_Update = "UPDATE personas SET nombre = 'DAVID' WHERE nombre = 'david';";
            miStatement.executeUpdate(instruccionSQL_Update);
            System.out.println("Datos actualizados correctamente");
            
            String instruccionSQL_Delete = "DELETE FROM personas WHERE nombre = 'Alejandro';";
            miStatement.executeUpdate(instruccionSQL_Delete);
            System.out.println("Datos borrados correctamente");


        }catch(SQLException e1){
            System.out.println("Error: "+e1.getMessage());
        }           
    }//Fin main        
}//Fin class
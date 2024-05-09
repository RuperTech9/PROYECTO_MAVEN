package tarea_David;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.LocalDate;

/**
 *
 * @author david
 */
public class test {

          public static void main(String[] args) {
                    //1ºPaso: crear conexion a MYSQL    Conexión segura con SSL
                    String url = "jdbc:mysql://192.168.56.102:3306/empresa?allowPublicKeyRetrieval=true&useSSL=false&serverTimeZone=UTC";
                    String user = "david4";
                    String password = "David!1234";

                    // TABLA EMPLEADOS
                    try {
                              //2ºpaso: Creo el objeto de conexion
                              Connection conexion = DriverManager.getConnection(url, user, password);

                              //3ºPaso: Me creo un objeto de tipo Statment
                              Statement instruccion = conexion.createStatement();
                              if (conexion != null) {
                                        System.out.println("Conexion establecida!");
                              }//Fin if

                              //4ºPaso: Hacer una consulta , no hace falta poner los ; al final
                              String consulta1 = "SELECT  * FROM empleados";
                              //Guardo el resultado
                              ResultSet resultado = instruccion.executeQuery(consulta1);

                              //5ºPaso: procesar el resultado
                              System.out.println("\n--- Datos tabla empleados ---");
                              
                              //Mostrar enumerados
                              int num = 1;
                              
                              while (resultado.next()) {
                                        // Otra forma de hacerlo: 
                                        String nombre = resultado.getString("nombre");
                                        String apellidos = resultado.getString("apellidos");
                                        String fechaNacimiento = resultado.getString("fechaNacimiento");
                                        String fechaIngreso = resultado.getString("fechaIngreso");
                                        String puesto = resultado.getString("puesto");
                                        double salario = resultado.getDouble("salario");
                                        // Imprimir
                                        System.out.println(num+".");
                                        System.out.println("Nombre: "+nombre); 
                                        System.out.println("Apellidos: "+apellidos);
                                        System.out.println("FechaNacimiento: "+fechaNacimiento);
                                        System.out.println("FechaIngreso: "+fechaIngreso);
                                        System.out.println("Puesto: "+puesto);
                                        System.out.println("Salario: "+salario);
                                        System.out.println(); //salto de linea
                                        
                                        num++;
                              }//Fin while

                              //Cerramos 
                              resultado.close();

                              
                              // Ahora realizamos la consulta para la tabla empleadosAntiguos
                             String consulta2 = "SELECT  * FROM empleadosAntiguos";
                             ResultSet resultado2 = instruccion.executeQuery(consulta2);

                             // Mostrar los datos de la tabla empleadosAntiguos
                             System.out.println("\n--- Datos tabla empleadosAntiguos ---");

                             int numAntiguos = 1;

                             while (resultado2.next()) {
                                      String nombre = resultado2.getString("nombre");
                                      String apellidos = resultado2.getString("apellidos");
                                      String fechaNacimiento = resultado2.getString("fechaNacimiento");
                                      String fechaIngreso = resultado2.getString("fechaIngreso");
                                      String fechaFinalizacion = resultado2.getString("fechaFinalizacion");

                                      // Imprimir
                                      System.out.println(numAntiguos + ".");
                                      System.out.println("Nombre: " + nombre);
                                      System.out.println("Apellidos: " + apellidos);
                                      System.out.println("FechaNacimiento: " + fechaNacimiento);
                                      System.out.println("FechaIngreso: " + fechaIngreso);
                                      System.out.println("FechaFinalizacion: " + fechaFinalizacion);
                                      System.out.println(); // salto de linea

                                      numAntiguos++;
                             }

                             // Cerramos el resultado de la segunda consulta y la conexión
                             resultado2.close();
                             instruccion.close();

                              
                              
                    } catch (SQLException e1) {
                              System.out.println("Error: " + e1.getMessage());
                    }//Fin try-catch

                   
                    
          }//Fin main

}//Fin class

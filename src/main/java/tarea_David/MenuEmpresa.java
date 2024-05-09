package tarea_David;

/**
 *
 * @author david
 */

import java.time.format.DateTimeFormatter;
import java.util.InputMismatchException;
import java.util.Scanner;

/**
 * Menú dónde muestra las opciones a elegir y ejecuta los métodos elegidos
 *
 * @version 1.0
 * @author david
 */
public class MenuEmpresa {

         public static void inicializarMenu() {
                  // Formateo la fecha para que el patron sea día-mes-año.
                  DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy");

                  // Menú
                  boolean salir = false;
                  Scanner scanner = new Scanner(System.in);

                  do {
                            System.out.println();
                           System.out.println("---------------------- Menú ----------------------");
                           System.out.println("--- 1. Añadir empleado");
                           System.out.println("--- 2. Eliminar empleado");
                           System.out.println("--- 3. Buscar empleado");
                           System.out.println("--- 4. Imprimir empleados ordenados por:");
                           System.out.println("--- 5. Calcular el gasto total de los empleados.");
                           System.out.println("--- 6. Salir");
                            System.out.println("--------------------------------------------------");
                           System.out.print("Ingrese su opción: ");

                           try {
                                    int opcion = scanner.nextInt();
                                     System.out.println();
                                     
                                    switch (opcion) {
                                             case 1:
                                                      FuncionesEmpleados.agregarEmpleado(formatter);
                                                      break;
                                             case 2:
                                                       FuncionesEmpleados.eliminarEmpleado();
                                                      break;
                                             case 3:
                                                       FuncionesEmpleados.buscarEmpleado();
                                                      break;
                                             case 4:
                                                       FuncionesEmpleados.ordenarEmpleados();
                                                      break;
                                             case 5:
                                                       FuncionesEmpleados.calcularSumaSalarios();
                                                      break;
                                             case 6:
                                                       System.out.println("Exit ...");
                                                      salir = true;
                                                      break;
                                             default:
                                                      throw new IllegalArgumentException("Opción no válida. Debe ser una opción disponible (1 - 6).");
                                    }//Fin switch

                           } catch (InputMismatchException e1) {
                                    System.err.println("Debe introducir un número válido.");
                                    scanner.nextLine(); // Limpiar el buffer del scanner
                           } catch (IllegalArgumentException e2) {  //Excepcion del default
                                    System.err.println(e2.getMessage());
                           } catch (Exception e3) {
                                    System.err.println("Error: " + e3.toString());
                           }//Fin try-catch

                  } while (!salir);

                   System.out.println();
                  scanner.close(); // Cerramos el scanner
                  
         }//Fin inicializarMenu
          
}//Fin class


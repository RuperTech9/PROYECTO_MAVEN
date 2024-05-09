package tarea_David;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

import java.util.Scanner;
import static tarea_David.MenuEmpresa.inicializarMenu;

/**
 *
 * @author david
 */
public class FuncionesEmpleados {
          
          public static void main(String[] args) {
                    inicializarMenu();
          }//Fin main
          
          public static void agregarEmpleado(DateTimeFormatter formatter) {
                    Scanner scanner = new Scanner(System.in);

                    // Nombre
                    String nombreNuevo;
                    do {
                              try {
                                        System.out.println("Ingrese el nombre del nuevo empleado:");
                                        nombreNuevo = scanner.nextLine();
                                        if (!nombreNuevo.matches("[a-zA-ZáéíóúÁÉÍÓÚñÑ ]+")) {
                                                  throw new Exception("Nombre no válido. Debe contener solo letras y espacios.");
                                        }
                              } catch (Exception e) {
                                        System.err.println("ERROR: " + e.getMessage());
                                        nombreNuevo = ""; // Reiniciar el valor del nombre
                              }
                    } while (nombreNuevo.isEmpty());

                    // Apellido 
                    String apellidoNuevo;
                    do {
                              try {
                                        System.out.println("Ingrese el apellido del empleado:");
                                        apellidoNuevo = scanner.nextLine();
                                        if (!apellidoNuevo.matches("[a-zA-ZáéíóúÁÉÍÓÚñÑ ]+")) {
                                                  throw new Exception("Apellido no válido. Debe contener solo letras y espacios.");
                                        }
                              } catch (Exception e) {
                                        System.err.println("ERROR: " + e.getMessage());
                                        apellidoNuevo = ""; // Reiniciar el valor del apellido
                              }
                    } while (apellidoNuevo.isEmpty());

                    // Fecha de Nacimiento
                    LocalDate fechaNacimientoNuevo = LocalDate.now();
                    boolean fechaNacimientoValida = false;
                    do {
                              try {
                                        System.out.println("Ingrese la fecha de nacimiento del empleado (dd-mm-aaaa):");
                                        String fechaNacimientoStr = scanner.nextLine();
                                        fechaNacimientoNuevo = LocalDate.parse(fechaNacimientoStr, formatter);
                                        if (fechaNacimientoNuevo.isAfter(LocalDate.now())) {
                                                  System.err.println("ERROR: La fecha de nacimiento no puede ser en el futuro.");
                                        } else {
                                                  fechaNacimientoValida = true;
                                        }
                              } catch (Exception e) {
                                        System.err.println("ERROR: Formato de fecha incorrecto. Intente nuevamente.");
                              }
                    } while (!fechaNacimientoValida);

                    // Fecha de Ingreso
                    LocalDate fechaIngresoNuevo= null; // Se inicializa a null para esperar la entrada del usuario
                    boolean fechaIngresoValida = false;
                    do {
                              try {
                                        System.out.println("Ingrese la fecha de ingreso del empleado (dd-mm-aaaa):");
                                        String fechaIngresoStr = scanner.nextLine();
                                        fechaIngresoNuevo = LocalDate.parse(fechaIngresoStr, formatter);
                                        if (fechaIngresoNuevo.isAfter(LocalDate.now())) {
                                                  System.err.println("ERROR: La fecha de ingreso no puede ser en el futuro.");
                                        } else if (fechaIngresoNuevo.isBefore(fechaNacimientoNuevo)) {
                                                  System.err.println("ERROR: La fecha de ingreso no puede ser anterior a la fecha de nacimiento.");
                                        } else {
                                                  fechaIngresoValida = true;
                                        }
                              } catch (Exception e) {
                                        System.err.println("ERROR: Formato de fecha incorrecto. Intente nuevamente.");
                              }
                    } while (!fechaIngresoValida);

                    // Puesto
                    String puestoNuevo;
                    do {
                              try {
                                        System.out.println("Ingrese el puesto del empleado:");
                                        puestoNuevo = scanner.nextLine();
                                        if (puestoNuevo.isEmpty()) {
                                                  throw new Exception("El puesto no puede estar vacío.");
                                        }
                                        // Verificar si el puesto contiene números
                                        if (puestoNuevo.matches(".*\\d.*")) {
                                                  throw new Exception("El puesto no puede contener números.");
                                        }
                              } catch (Exception e) {
                                        System.err.println("ERROR: " + e.getMessage());
                                        puestoNuevo = ""; // Reiniciar el valor del puesto
                              }
                    } while (puestoNuevo.isEmpty() || puestoNuevo.matches(".*\\d.*"));

                    // Salario
                    double salarioNuevo = 0;
                    do {
                              try {
                                        System.out.println("Ingrese el salario del empleado:");
                                        salarioNuevo = scanner.nextDouble();
                                        if (salarioNuevo <= 0) {
                                                  System.err.println("ERROR: El salario debe ser mayor que cero.");
                                        }
                              } catch (Exception e) {
                                        System.err.println("ERROR: El salario debe ser un número válido.");
                                        scanner.nextLine(); // Limpiar el buffer del scanner
                              }
                    } while (salarioNuevo <= 0);

                    // Limpiar el buffer del scanner
                    scanner.nextLine();
                    
                    // Instancia de Consultas_Metodos
                    Consultas_Metodos consultas = new Consultas_Metodos();

                    // Consulta SQL para insertar los datos del nuevo empleado
                    String consultaInsert = "INSERT INTO empleados (nombre, apellidos, fechaNacimiento, fechaIngreso, puesto, salario) "
                            + "VALUES ('" + nombreNuevo + "', '" + apellidoNuevo + "', '" + fechaNacimientoNuevo + "', '"
                            + fechaIngresoNuevo + "', '" + puestoNuevo + "', " + salarioNuevo + ")";

                    // Ejecutamos la consulta
                    try {
                              consultas.ejecutarInstruccion(consultaInsert);
                              System.out.println("Empleado insertado correctamente en la base de datos.");
                    } catch (SQLException e) {
                              System.err.println("Error al insertar empleado en la base de datos: " + e.getMessage());
                    }

          }//fin agregarEmpleado
          
          public static void eliminarEmpleado() {
                    boolean empleadoEncontrado = false;
                    Scanner scanner = new Scanner(System.in);

                    do {
                              try {
                                        System.out.println("Ingrese el nombre del empleado a borrar:");
                                        String nombreBorrar = scanner.nextLine();
                                        if (!nombreBorrar.matches("[a-zA-ZáéíóúÁÉÍÓÚñÑ ]+")) {
                                                  throw new IllegalArgumentException("El nombre solo puede contener letras y/o espacios.");
                                        }

                                        System.out.println("Ingrese el apellido del empleado a borrar:");
                                        String apellidoBorrar = scanner.nextLine();
                                        if (!apellidoBorrar.matches("[a-zA-ZáéíóúÁÉÍÓÚñÑ ]+")) {
                                                  throw new IllegalArgumentException("El apellido solo puede contener letras y/o espacios.");
                                        }

                                        // Consulta SQL para agregar el empleado a la tabla empleadosAntiguos
                                        LocalDate fechaFinalizacion = LocalDate.now();
                                        String consultaAgregarAntiguo = "INSERT INTO empleadosAntiguos (nombre, apellidos, fechaNacimiento,"
                                                + " fechaIngreso, fechaFinalizacion) "
                                                + "SELECT nombre, apellidos, fechaNacimiento, fechaIngreso, '" + fechaFinalizacion + "' "
                                                + "FROM empleados WHERE nombre = '" + nombreBorrar + "' AND apellidos = '" + apellidoBorrar + "'";

                                        // Consulta SQL para eliminar el empleado de la tabla empleados
                                        String consultaEliminar = "DELETE FROM empleados WHERE nombre = '" + nombreBorrar + "' AND apellidos = '" + apellidoBorrar + "'";

                                        // Instancia de Consultas_Metodos
                                        Consultas_Metodos consultas = new Consultas_Metodos();

                                        // Ejecutar la consulta para agregar el empleado a empleadosAntiguos
                                        consultas.ejecutarInstruccion(consultaAgregarAntiguo);

                                        // Ejecutar la consulta para eliminar el empleado
                                        int filasAfectadas = consultas.ejecutarInstruccion(consultaEliminar);

                                        // Comprobar si se encontró y eliminó al empleado
                                        if (filasAfectadas == 0) {
                                                  System.out.println("Empleado no encontrado.");
                                        } else {
                                                  // Establecer empleadoEncontrado en true ya que la eliminación se realizó sin errores
                                                  empleadoEncontrado = true;
                                                  // Imprimir mensaje de éxito
                                                  System.out.println("Se ha borrado el empleado correctamente.");
                                        }
                              } catch (Exception e) {
                                        System.err.println("ERROR: " + e.getMessage());
                              }
                    } while (!empleadoEncontrado);
          }//Fin eliminarEmpleado

        
         
          public static void buscarEmpleado() {
                  Scanner scanner = new Scanner(System.in);

                  try {
                           System.out.println("Ingrese el nombre del empleado:");
                           String nombreBuscar = scanner.nextLine();
                           if (!nombreBuscar.matches("[a-zA-ZáéíóúÁÉÍÓÚñÑ ]+")) {
                                    throw new IllegalArgumentException("El nombre solo puede contener letras y/o espacios.");
                           }

                           System.out.println("Ingrese el apellido del empleado:");
                           String apellidoBuscar = scanner.nextLine();
                           if (!apellidoBuscar.matches("[a-zA-ZáéíóúÁÉÍÓÚñÑ ]+")) {
                                    throw new IllegalArgumentException("El apellido solo puede contener letras y/o espacios.");
                           }

                           // Instancia de Consultas_Metodos
                           Consultas_Metodos consultas = new Consultas_Metodos();

                           // Consulta SQL para buscar el empleado
                           String consultaBuscar = "SELECT * FROM empleados WHERE nombre = '" + nombreBuscar + 
                                   "' AND apellidos = '" + apellidoBuscar + "'";

                           // Consultar y mostrar empleado
                           consultas.imprimirEmpleado(consultaBuscar);

                  } catch (Exception e) {
                           System.err.println("ERROR: " + e.getMessage());
                  }
         }//Fin buscarEmpleado

          
          public static void ordenarEmpleados() {
                    boolean salir = false;
                    Scanner scanner = new Scanner(System.in);
                    Consultas_Metodos consultas = new Consultas_Metodos(); // Instancia de Consultas_Metodos

                    do {
                              try {
                                        System.out.println("a) Por antigüedad");
                                        System.out.println("b) Por salario");
                                        System.out.println("c) Por apellido");
                                        System.out.println("Seleccione la forma de ordenamiento:");
                                        String ordenOpcion = scanner.nextLine().toLowerCase();
                                        System.out.println();

                                        switch (ordenOpcion) {
                                                  case "a":
                                                            // Consulta SQL para ordenar por antigüedad
                                                            String consultaAntiguedad = "SELECT * FROM empleados ORDER BY fechaIngreso";
                                                            consultas.imprimirEmpleado(consultaAntiguedad); // Imprimir empleados ordenados por antigüedad
                                                            salir = true; // Salir del bucle
                                                            break;
                                                  case "b":
                                                            // Consulta SQL para ordenar por salario
                                                            String consultaSalario = "SELECT * FROM empleados ORDER BY salario";
                                                            consultas.imprimirEmpleado(consultaSalario); // Imprimir empleados ordenados por salario
                                                            salir = true; // Salir del bucle
                                                            break;
                                                  case "c":
                                                            // Consulta SQL para ordenar por apellido
                                                            String consultaApellido = "SELECT * FROM empleados ORDER BY apellidos";
                                                            consultas.imprimirEmpleado(consultaApellido); // Imprimir empleados ordenados por apellido
                                                            salir = true; // Salir del bucle
                                                            break;
                                                  default:
                                                            throw new IllegalArgumentException("Opción no válida. Introduzca 'a', 'b' o 'c'.");
                                        }
                              } catch (Exception e) {
                                        System.out.println("ERROR: " + e.getMessage());
                              }
                    } while (!salir);
          }//Fin ordernarEmpleados
          
          
          public static void calcularSumaSalarios() {
                    try {
                              // Instancia de Consultas_Metodos
                              Consultas_Metodos consultas = new Consultas_Metodos();

                              // Consulta SQL para calcular la suma del salario de todos los empleados
                              String consulta = "SELECT SUM(salario) AS total_salarios FROM empleados";

                              // Ejecutar la consulta
                              ResultSet resultado = consultas.ejecutarConsulta(consulta);

                              // Mostrar el resultado
                              if (resultado.next()) {
                                        double totalSalarios = resultado.getDouble("total_salarios");
                                        System.out.println("El gasto total en salarios ha sido: " + totalSalarios +" euros");
                              } else {
                                        System.out.println("No hay salarios...");
                              }

                              // Cerrar el ResultSet
                              resultado.close();
                    } catch (SQLException e) {
                              System.err.println("ERROR: " + e.getMessage());
                    }
          }//Fin calcularSumaSalarios
          
          
}//Fin class

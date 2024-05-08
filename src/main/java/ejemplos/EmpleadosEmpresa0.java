
package ejemplos;


import java.util.ArrayList;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Iterator;
import java.util.Comparator;
import java.time.format.DateTimeParseException;
import java.util.InputMismatchException;
import java.util.Scanner;
import java.sql.*;

/**
 *
 * @author Ruper
 */
public class EmpleadosEmpresa0 {
    // ArrayList de Objetos
    static ArrayList<Empleado> empleados = new ArrayList<>();
    static Scanner sc = new Scanner(System.in);
    // Datos de conexión a MySQL
    private static final String url = "jdbc:mysql://192.168.0.27:3306/EmpleadosDB?allowPublicKeyRetrieval=true&useSSL=false&serverTimeZone=UTC";
    private static final String user = "alejandro";
    private static final String password = "J0selu1s100%";

    // Método para obtener conexión
    private static Connection obtenerConexion() throws SQLException {
        return DriverManager.getConnection(url, user, password);
    }

    // Método para añadir un empleado a la base de datos
    private static void añadirEmpleadoDB(Empleado empleado) {
        String sql = "INSERT INTO Empleados (nombre, apellidos, fechaNacimiento, fechaIngreso, puesto, salario) VALUES (?, ?, ?, ?, ?, ?)";
        try (Connection conexion = obtenerConexion(); PreparedStatement miStatement = conexion.prepareStatement(sql)) {
            miStatement.setString(1, empleado.getNombre());
            miStatement.setString(2, empleado.getApellidos());
            miStatement.setDate(3, Date.valueOf(empleado.getFechaNacimiento()));
            miStatement.setDate(4, Date.valueOf(empleado.getFechaIngreso()));
            miStatement.setString(5, empleado.getPuesto());
            miStatement.setDouble(6, empleado.getSalario());
            int filasAfectadas = miStatement.executeUpdate();
            if (filasAfectadas > 0) {
                System.out.println("Empleado añadido con éxito a la base de datos.");
            }
        } catch (SQLException e) {
            System.err.println("Error al añadir el empleado: " + e.getMessage());
            System.err.println("Número que representa el error: " + e.getErrorCode());
        }
    }

    // Método para eliminar un empleado de la base de datos
    private static void eliminarEmpleadoDB(String nombre, String apellidos) {
        String sql = "DELETE FROM Empleados WHERE nombre = ? AND apellidos = ?";
        try (Connection conexion = obtenerConexion(); PreparedStatement miStatement = conexion.prepareStatement(sql)) {
            miStatement.setString(1, nombre);
            miStatement.setString(2, apellidos);
            int filasAfectadas = miStatement.executeUpdate();
            if (filasAfectadas > 0) {
                System.out.println("Empleado eliminado con éxito de la base de datos.");
            } else {
                System.out.println("No se encontró ningún empleado con esos datos.");
            }
        } catch (SQLException e) {
            System.err.println("Error al eliminar el empleado: " + e.getMessage());
            System.err.println("Número que representa el error: " + e.getErrorCode());
        }
    }
    
    // Método para actualizar un empleado en la base de datos
    private static void actualizarEmpleadoDB(String nombre, String apellidos, Empleado empleadoNuevo) {
        String sql = "UPDATE Empleados SET nombre = ?, apellidos = ?, fechaNacimiento = ?, fechaIngreso = ?, puesto = ?, salario = ? WHERE nombre = ? AND apellidos = ?";
        try (Connection conexion = obtenerConexion(); PreparedStatement miStatement = conexion.prepareStatement(sql)) {
            miStatement.setString(1, empleadoNuevo.getNombre());
            miStatement.setString(2, empleadoNuevo.getApellidos());
            miStatement.setDate(3, Date.valueOf(empleadoNuevo.getFechaNacimiento()));
            miStatement.setDate(4, Date.valueOf(empleadoNuevo.getFechaIngreso()));
            miStatement.setString(5, empleadoNuevo.getPuesto());
            miStatement.setDouble(6, empleadoNuevo.getSalario());
            miStatement.setString(7, nombre);
            miStatement.setString(8, apellidos);
        
            int filasAfectadas = miStatement.executeUpdate();
            if (filasAfectadas > 0) {
                System.out.println("Empleado actualizado con éxito.");
            } else {
                System.out.println("No se encontró ningún empleado con esos datos para actualizar.");
            }
        } catch (SQLException e) {
            System.err.println("Error al actualizar el empleado: " + e.getMessage());
            System.err.println("Número que representa el error: " + e.getErrorCode());
        }
    }

    // Método para buscar empleados en la base de datos
    private static void buscarEmpleadoDB(String nombre, String apellidos) {
        String sql = "SELECT * FROM Empleados WHERE nombre = ? AND apellidos = ?";
        try (Connection conexion = obtenerConexion(); PreparedStatement miStatement = conexion.prepareStatement(sql)) {
            miStatement.setString(1, nombre);
            miStatement.setString(2, apellidos);

            ResultSet rs = miStatement.executeQuery();
            if (rs.next()) {
                System.out.println("Empleado encontrado: ");
                System.out.println("Nombre: " + rs.getString("nombre"));
                System.out.println("Apellidos: " + rs.getString("apellidos"));
                System.out.println("Fecha de Nacimiento: " + rs.getDate("fechaNacimiento"));
                System.out.println("Fecha de Ingreso: " + rs.getDate("fechaIngreso"));
                System.out.println("Puesto: " + rs.getString("puesto"));
                System.out.println("Salario: " + rs.getDouble("salario"));
            } else {
                System.out.println("No se encontró ningún empleado con esos datos.");
            }
        } catch (SQLException e) {
            System.err.println("Error en la búsqueda: " + e.getMessage());
            System.err.println("Número que representa el error: " + e.getErrorCode());
        }
    }
    
    // METODO para añadir un empleado.
    private static void añadirEmpleado() {
        // NOMBRE Y APELLIDOS
        System.out.println("Introduce el nombre del empleado:");
        String nombre = sc.nextLine();
        if (nombre == null) return; // Salgo del método si presiono cancelar
        System.out.println("Introduce los apellidos del empleado:");
        String apellidos = sc.nextLine();
        if (apellidos == null) return;
        
        // FECHA DE NACIMIENTO
        LocalDate fechaNacimiento = null; // Inicializo variable
        do {
            System.out.println("Introduce la fecha de nacimiento del empleado (DD/MM/YYYY):");
            String strFechaNacimiento = sc.nextLine();
            if (strFechaNacimiento == null) return; // Sale del método si se presiona cancelar
            try {
                DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy"); // Cambio el formato de la fecha.
                fechaNacimiento = LocalDate.parse(strFechaNacimiento, formatter);
                break; // Salgo si la fecha es correcta
            } catch (DateTimeParseException e) {
                System.err.println("ERROR. Introduce el formato correcto DD/MM/YYYY.");
            } // FIN TRY-CATCH
        } while (fechaNacimiento == null); // FIN DO-WHILE
        
        // FECHA DE INGRESO
        LocalDate fechaIngreso = null; // Inicializo variable
        do {
            System.out.println("Introduce la fecha de ingreso del empleado (DD/MM/YYYY):");
            String strFechaIngreso = sc.nextLine();
            try {
                DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy"); // Cambio el formato de la fecha.
                fechaIngreso = LocalDate.parse(strFechaIngreso, formatter);
                if (fechaIngreso != null && fechaNacimiento != null && fechaIngreso.isBefore(fechaNacimiento)) { // Verifico que la fecha de ingreso no sea anterior a la fecha de nacimiento
                    System.out.println("La fecha de ingreso no puede ser anterior a la fecha de nacimiento. Por favor, introduce una fecha válida.");
                    fechaIngreso = null; // Restablezco la fechaIngreso para repetir el bucle
                } else {
                    break; // Salgo si la fecha es correcta
                }
            } catch (DateTimeParseException e) {
                System.err.println("ERROR. Introduce el formato correcto DD/MM/YYYY.");
            } // FIN TRY-CATCH
        } while (fechaIngreso == null); // FIN DO-WHILE
        
        // PUESTO
        System.out.println("Introduce el puesto del empleado:");
        String puesto = sc.nextLine();
        
        // SALARIO
        double salario = 0; // Inicializo variable.
        do {
            System.out.println("Introduce el salario del empleado:");
            String strSalario = sc.nextLine();
            try {
                salario = Double.parseDouble(strSalario);
                if (salario > 0) {
                    break; // Salgo si el salario es correcto y positivo.
                } else {
                    System.err.println("El salario debe ser mayor que 0. Intentalo de nuevo.");
                } // FIN IF
            } catch (InputMismatchException e) {
                System.err.println("ERROR. Se esperaba un número para el salario. Intentalo de nuevo.");
            } // FIN TRY-CATCH
        } while (true); // FIN DO-WHILE
        
        // Aquí construyo el objeto empleado
        Empleado nuevoEmpleado = new Empleado(nombre, apellidos, fechaNacimiento, fechaIngreso, puesto, salario);
        // Ahora llamas al método que añade el empleado a la base de datos
        añadirEmpleadoDB(nuevoEmpleado);
        
        empleados.add(nuevoEmpleado);                                                       
        System.out.println("\nEl empleado " + nombre + " ha sido añadido a la lista.");
    }
    
    // METODO que elimina un empleado por nombre y apellidos
    private static void eliminarEmpleado() {
        System.out.println("Introduce el nombre del empleado a eliminar:");
        String nombre = sc.nextLine();
        if (nombre == null) return; // Salgo del método si presiono cancelar
        System.out.println("Introduce el apellido del empleado a eliminar:");
        String apellido = sc.nextLine();
        if (apellido == null) return;
        
        boolean encontrado = false; // Inicializo variable.
        
        // ITERATOR Me permite recorrer una colección y modificarla (en este caso, una lista "empleados").
        Iterator<Empleado> iterator = empleados.iterator();
        while (iterator.hasNext()) { // Mientras haya más elementos en la lista...
            Empleado empleado = iterator.next(); // Recorro la Lista de Empleados.
            if (empleado.getNombre().equalsIgnoreCase(nombre) && empleado.getApellidos().equalsIgnoreCase(apellido)) { // Compruebo si el nombre y el apellido coinciden.
                
                // Llamo al método para eliminar el empleado de la BBDD
                eliminarEmpleadoDB(nombre, apellido);
                
                iterator.remove(); // Elimino el empleado.
                encontrado = true;
                break;
            } // FIN IF
        }
        if (encontrado) {
            System.out.println("\nEl empleado " + nombre + " ha sido eliminado de la lista.");
        } else {
            System.err.println("\nNo se encontró ningún empleado con ese nombre.");
        } // FIN IF
    } // FIN METODO
    
    // METODO que busca un empleado por nombre y apellidos
    private static void buscarEmpleado() {
        System.out.println("Introduce el nombre del empleado:");
        String nombre = sc.nextLine();

        System.out.println("Introduce el apellido del empleado:");
        String apellido = sc.nextLine();
        
        // Llamada al método de búsqueda en la BBDD
        buscarEmpleadoDB(nombre, apellido);
    
        boolean encontrado = false; // Inicializo variable.
        
        // Imprimo los empleados.
        Iterator<Empleado> iterator = empleados.iterator();
        while (iterator.hasNext()) { // Mientras haya más elementos en la lista...
            Empleado empleado = iterator.next(); // Recorro la Lista de Empleados.
            if (empleado.getNombre().equalsIgnoreCase(nombre) && empleado.getApellidos().equalsIgnoreCase(apellido)) {
                System.out.println(empleado.toString()); // Muestro los datos del empleado con el nombre especificado.
                encontrado = true;
                // El bucle continuará hasta el final de la lista de empleados por si hay 2 usuarios con el mismo nombre y apellidos.
            } // FIN IF
        } // FIN WHILE
        if (!encontrado) {
            System.err.println("No se encontró ningún empleado con ese nombre.");
        } // FIN IF
    } // FIN METODO
    
    private static void actualizarEmpleado() {
        System.out.println("Introduce el nombre del empleado a actualizar:");
        String nombre = sc.nextLine();
        System.out.println("Introduce el apellido del empleado a actualizar:");
        String apellido = sc.nextLine();

        // Aquí deberías recolectar los nuevos datos del empleado de manera similar a añadirEmpleado
        System.out.println("Introduce nuevos datos para este empleado...");

        // Construcción del nuevo objeto empleado con los nuevos datos
        //Empleado empleadoActualizado = new Empleado(nuevoNombre, nuevoApellidos, nuevaFechaNacimiento, nuevaFechaIngreso, nuevoPuesto, nuevoSalario);
    
        // Llamada al método de actualización de la base de datos
        //actualizarEmpleadoDB(nombre, apellido, empleadoActualizado);
    }

    // METODO que ordena la lista de empleados por fecha de ingreso
    private static void ordenadosPorAntigüedad() {
        // COMPARATOR Flexibilidad para ordenar objetos según múltiples criterios y en diferentes órdenes, no limitándose al orden natural de los objetos.
        empleados.sort(Comparator.comparing(Empleado::getFechaIngreso)); // El Comparator le dice al método sort que compare los objetos Empleado entre sí por su fecha de ingreso.
        
        // Imprimo los empleados ya ordenados.
        Iterator<Empleado> itAntiguos = empleados.iterator();
        String ordenAntiguos = "EMPLEADOS ORDENADOS POR ANTIGÜEDAD:\n\n";
        int contador = 1; // Para numerar los empleados.
        while (itAntiguos.hasNext()) {
            Empleado empleado = itAntiguos.next();
            ordenAntiguos += contador + "- " + empleado.toString() + "\n";
            contador++; // Para el siguiente empleado.
        }
        System.out.println(ordenAntiguos);
    } // FIN METODO
    
    // METODO que ordena la lista de empleados por salario (de mayor a menor)
    private static void ordenadosPorSalario() {
        empleados.sort(Comparator.comparingDouble(Empleado::getSalario).reversed());
        
        // Imprimo los empleados ya ordenados
        Iterator<Empleado> itSalario = empleados.iterator();
        String ordenSalario = "EMPLEADOS ORDENADOS POR SALARIO:\n\n";
        int contador = 1; // Para numerar los empleados.
        while (itSalario.hasNext()) {
            Empleado empleado = itSalario.next();
            ordenSalario += contador + "- " + empleado.toString() + "\n";
            contador++; // Para el siguiente empleado.
        }
        System.out.println(ordenSalario);
    } // FIN METODO
    
    // METODO que ordena la lista de empleados por apellido
    private static void ordenadosPorApellido() {
        empleados.sort(Comparator.comparing(Empleado::getApellidos));
    
        // Imprimo los empleados ya ordenados
        Iterator<Empleado> itApellido = empleados.iterator();
        String ordenApellidos = "EMPLEADOS ORDENADOS POR APELLIDO:\n\n";
        int contador = 1; // Para numerar los empleados.
        while (itApellido.hasNext()) {
            Empleado empleado = itApellido.next();
            ordenApellidos += contador + "- " + empleado.toString() + "\n";
            contador++; // Para el siguiente empleado.
        }
        System.out.println(ordenApellidos);
    } // FIN METODO
    
    // METODO para calcular el gasto total sumando los salarios de todos los empleados.
    private static void calcularGastoTotal() {
        double gastoTotal = 0; // Inicializo variable.

        for (Empleado empleado : empleados) {
            gastoTotal += empleado.getSalario(); // Sumo el salario del empleado actual al total.
        } // FIN FOR
        System.out.println("\nEl gasto total en salarios de los empleados es: " + gastoTotal);
        
        /* CON ITERATOR
        Iterator<Empleado> iterator = empleados.iterator();
        while (iterator.hasNext()) {
            Empleado empleado = iterator.next();
            gastoTotal += empleado.getSalario();
        }
        */
    } // FIN METODO
    
    // METODO para mostrar el menú
    public void mostrarMenu() {
        int opcion = 0;
        do {     
            try {
            String menu = "\n               GESTIÓN DE EMPLEADOS" +
                          "\n---------------------------------------------------------" +
                          "\n1- Añadir Empleado" +
                          "\n2- Eliminar Empleado" +
                          "\n3- Buscar Empleado" +
                          "\n4- Imprimir empleados ordenados por:" +
                          "\n   a) Por antigüedad" +
                          "\n   b) Por salario" +
                          "\n   c) Por apellido" +
                          "\n5- Calcular gasto total de los empleados" +
                          "\n6- Salir" +
                          "\n---------------------------------------------------------" +
                          "\nSelecciona una opción: ";
            
            System.out.print(menu);
            opcion = sc.nextInt();
            sc.nextLine(); // Salto de línea
            
                switch (opcion) {
                    case 1:
                        añadirEmpleado();
                        break;
                    case 2:
                        eliminarEmpleado();
                        break;
                    case 3:
                        buscarEmpleado();
                        break;
                    case 4:
                        String opcionOrdenamiento;
                        do {
                            String subMenu = "Elige una opción:\n" +
                                            "a) Por antigüedad\n" +
                                            "b) Por salario\n" +
                                            "c) Por apellido\n" +
                                            "s) Volver al menú principal";
                            System.out.println(subMenu);
                            opcionOrdenamiento = sc.nextLine();
                            
                            // Si presiono cancelar o elijo 's', rompo el bucle y vuelvo al menú principal.
                            if (opcionOrdenamiento == null || opcionOrdenamiento.equalsIgnoreCase("s")) {
                                break;
                            } // FIN IF
                            switch (opcionOrdenamiento.toLowerCase()) {
                                case "a":
                                    ordenadosPorAntigüedad();
                                    break;
                                case "b":
                                    ordenadosPorSalario();
                                    break;
                                case "c":
                                    ordenadosPorApellido();
                                    break;
                                default:
                                    System.err.println("Opción no válida. Introduce 'a', 'b', 'c' o 's' para volver.");
                            } // FIN SWITCH
                            
                        } while (true); // FIN DO-WHILE
                        break;
                    case 5:
                        calcularGastoTotal();
                        break;
                    case 6:
                        System.out.println("\nSaliendo...");
                        break;
                    default:
                        System.err.println("\nOpción no válida. Introduce un número entre 1 y 6.");
                        break;
                } // FIN SWITCH
            } catch (InputMismatchException e) {
                System.err.println("ERROR. Entrada no válida, inténtalo de nuevo.");
                sc.nextLine(); // Salto de línea
            } // FIN TRY-CATCH
        } while (opcion != 6); // FIN DO-WHILE
    } // FIN METODO 
} // FIN CLASE
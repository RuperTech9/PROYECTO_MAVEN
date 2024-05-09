
package empleados;


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
public class PRUEBAS {
    // ArrayList de Objetos
    static ArrayList<Empleado> empleados = new ArrayList<>();
    static Scanner sc = new Scanner(System.in);
    
    
    // METODOS JDBC
    private static Connection conectar() {
        String url = "jdbc:mysql://192.168.80.152:3306/EmpleadosDB";
        String user = "alejandro";
        String password = "J0selu1s100%";
        try {
            return DriverManager.getConnection(url, user, password);
        } catch (SQLException e) {
            System.out.println("Error en la conexión: " + e.getMessage());
            return null;
        }
    }

    public void cargarEmpleadosDesdeDB() {
        String sql = "SELECT * FROM Empleados";
        try (Connection con = conectar();
             PreparedStatement pstmt = con.prepareStatement(sql);
             ResultSet rs = pstmt.executeQuery()) {
            while (rs.next()) {
                Empleado empleado = new Empleado(
                    rs.getString("codigo"),
                    rs.getString("nombre"),
                    rs.getString("apellidos"),
                    rs.getDate("fechaNacimiento").toLocalDate(),
                    rs.getDate("fechaIngreso").toLocalDate(),
                    rs.getString("puesto"),
                    rs.getDouble("salario")
                );
                empleados.add(empleado);
            }
        } catch (SQLException e) {
            System.out.println("Error al cargar empleados: " + e.getMessage());
            System.err.println("Número del error: " + e.getErrorCode());
        }
    }
    
    // METODO para añadir un empleado.
    private static void añadirEmpleado() {
    // CODIGO EMPLEADO
        System.out.println("Introduce el codigo del empleado: ");
        String codigo = sc.nextLine();
    // NOMBRE Y APELLIDOS
        System.out.println("Introduce el nombre del empleado:");
        String nombre = sc.nextLine();
        System.out.println("Introduce los apellidos del empleado:");
        String apellidos = sc.nextLine();
    // FECHA DE NACIMIENTO
        LocalDate fechaNacimiento = null; // Inicializo variable
        do {
            System.out.println("Introduce la fecha de nacimiento del empleado (DD/MM/YYYY):");
            String strFechaNacimiento = sc.nextLine();
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
        Empleado empleado = new Empleado(codigo, nombre, apellidos, fechaNacimiento, fechaIngreso, puesto, salario);
        
        Connection con = null;
        PreparedStatement ps = null;
        try {
            con = conectar();
            String sql = "INSERT INTO Empleados (codigo, nombre, apellidos, fechaNacimiento, fechaIngreso, puesto, salario) VALUES (?, ?, ?, ?, ?, ?, ?)";
            ps = con.prepareStatement(sql);
            ps.setString(1, empleado.getCodigo());
            ps.setString(2, empleado.getNombre());
            ps.setString(3, empleado.getApellidos());
            ps.setDate(4, Date.valueOf(empleado.getFechaNacimiento()));
            ps.setDate(5, Date.valueOf(empleado.getFechaIngreso()));
            ps.setString(6, empleado.getPuesto());
            ps.setDouble(7, empleado.getSalario());
            int result = ps.executeUpdate();
            if (result > 0) {
                empleados.add(empleado);
                System.out.println("\nEl empleado " + empleado.getNombre() + " ha sido añadido a la base de datos y a la lista.");
            }
        } catch (SQLException e) {
            System.out.println("Error al añadir empleado: " + e.getMessage());
        } finally {
            cerrarConexion(con, ps, null);
        }
    }
    
    // METODO que elimina un empleado por nombre y apellidos
    private static void eliminarEmpleado() {
        System.out.println("Introduce el nombre del empleado a eliminar:");
        String nombre = sc.nextLine();
        System.out.println("Introduce el apellido del empleado a eliminar:");
        String apellido = sc.nextLine();
        
        Connection con = null;
        PreparedStatement ps = null;
        try {
            con = conectar();
            String sql = "DELETE FROM Empleados WHERE nombre = ? AND apellidos = ?";
            ps = con.prepareStatement(sql);
            ps.setString(1, nombre);
            ps.setString(2, apellido);
            int result = ps.executeUpdate();
            if (result > 0) {
                empleados.removeIf(e -> e.getNombre().equalsIgnoreCase(nombre) && e.getApellidos().equalsIgnoreCase(apellido));
                System.out.println("\nEl empleado ha sido eliminado de la base de datos y de la lista.");
            } else {
                System.out.println("\nNo se encontró ningún empleado con ese nombre para eliminar.");
            }
        } catch (SQLException e) {
            System.out.println("Error al eliminar empleado: " + e.getMessage());
        } finally {
            cerrarConexion(con, ps, null);
        }
    }
    
    // Método para solicitar la actualización de un empleado
    private static void actualizarEmpleado() {
        System.out.println("Introduce el nombre del empleado a actualizar:");
        String nombre = sc.nextLine();
        System.out.println("Introduce el apellido del empleado a actualizar:");
        String apellido = sc.nextLine();
        
        // Obtener nuevo puesto y salario
        System.out.println("Introduce el nuevo puesto del empleado:");
        String nuevoPuesto = sc.nextLine();
        System.out.println("Introduce el nuevo salario del empleado:");
        double nuevoSalario;
        while (true) {
            try {
                nuevoSalario = Double.parseDouble(sc.nextLine());
                if (nuevoSalario >= 0) break;
                System.out.println("El salario debe ser un número positivo. Inténtalo de nuevo.");
            } catch (NumberFormatException e) {
                System.out.println("Por favor, introduce un número válido para el salario.");
            }
        }
        
        Connection con = null;
        PreparedStatement ps = null;
        try {
            con = conectar();
            String sql = "UPDATE Empleados SET puesto = ?, salario = ? WHERE nombre = ? AND apellidos = ?";
            ps = con.prepareStatement(sql);
            ps.setString(1, nuevoPuesto);
            ps.setDouble(2, nuevoSalario);
            ps.setString(3, nombre);
            ps.setString(4, apellido);
            int resultado = ps.executeUpdate();
            if (resultado > 0) {
                // Actualizar la lista en memoria
                empleados.forEach(empleado -> {
                    if (empleado.getNombre().equalsIgnoreCase(nombre) && empleado.getApellidos().equalsIgnoreCase(apellido)) {
                        empleado.setPuesto(nuevoPuesto);
                        empleado.setSalario(nuevoSalario);
                    }
                });
                System.out.println("Empleado actualizado correctamente en la base de datos y en la lista.");
            } else {
                System.out.println("No se encontró ningún empleado con ese nombre y apellido para actualizar.");
            }
        } catch (SQLException e) {
            System.out.println("Error al actualizar empleado: " + e.getMessage());
        } finally {
            cerrarConexion(con, ps, null);
        }
    }
    
    // METODO que busca un empleado por nombre y apellidos
    private static void buscarEmpleado() {
        System.out.println("Introduce el nombre del empleado:");
        String nombre = sc.nextLine();
        System.out.println("Introduce el apellido del empleado:");
        String apellido = sc.nextLine();
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

    // METODO QUE ORDENA la lista de empleados por fecha de ingreso
    private static void ordenadosPorAntigüedad() {
        // COMPARATOR Flexibilidad para ordenar objetos según múltiples criterios y en diferentes órdenes, no limitándose al orden natural de los objetos.
        empleados.sort(Comparator.comparing(Empleado::getFechaIngreso)); // El Comparator le dice al método sort que compare los objetos Empleado entre sí por su fecha de ingreso.
        // Imprimo los empleados ya ordenados.
        Iterator<Empleado> itAntiguos = empleados.iterator();
        String ordenAntiguos = "\nEMPLEADOS ORDENADOS POR ANTIGÜEDAD:\n";
        int contador = 1; // Para numerar los empleados.
        while (itAntiguos.hasNext()) {
            Empleado empleado = itAntiguos.next();
            ordenAntiguos += contador + "- " + empleado.toString() + "\n";
            contador++; // Para el siguiente empleado.
        }
        System.out.println(ordenAntiguos);
    } // FIN METODO
    
    // METODO QUE ORDENA la lista de empleados por salario (de mayor a menor)
    private static void ordenadosPorSalario() {
        empleados.sort(Comparator.comparingDouble(Empleado::getSalario).reversed());
        // Imprimo los empleados ya ordenados
        Iterator<Empleado> itSalario = empleados.iterator();
        String ordenSalario = "\nEMPLEADOS ORDENADOS POR SALARIO:\n";
        int contador = 1; // Para numerar los empleados.
        while (itSalario.hasNext()) {
            Empleado empleado = itSalario.next();
            ordenSalario += contador + "- " + empleado.toString() + "\n";
            contador++; // Para el siguiente empleado.
        }
        System.out.println(ordenSalario);
    } // FIN METODO
    
    // METODO QUE ORDENA la lista de empleados por apellido
    private static void ordenadosPorApellido() {
        empleados.sort(Comparator.comparing(Empleado::getApellidos));
        // Imprimo los empleados ya ordenados
        Iterator<Empleado> itApellido = empleados.iterator();
        String ordenApellidos = "\nEMPLEADOS ORDENADOS POR APELLIDO:\n";
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
                          "\n3- Actualizar Empleado" +
                          "\n4- Buscar Empleado" +
                          "\n5- Imprimir empleados ordenados:" +
                          "\n6- Calcular gasto total de los empleados" +
                          "\n7- Salir" +
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
                        actualizarEmpleado();
                        break;
                    case 4:
                        buscarEmpleado();
                        break;
                    case 5:
                        String opcionOrdenamiento;
                        do {
                            String subMenu = "\nElige una opción:\n" +
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
                    case 6:
                        calcularGastoTotal();
                        break;
                    case 7:
                        System.out.println("\nSaliendo...");
                        break;
                    default:
                        System.err.println("\nOpción no válida. Introduce un número entre 1 y 7.");
                        break;
                } // FIN SWITCH
            } catch (InputMismatchException e) {
                System.err.println("ERROR. Entrada no válida, inténtalo de nuevo.");
                sc.nextLine(); // Salto de línea
            } // FIN TRY-CATCH
        } while (opcion != 7); // FIN DO-WHILE
    } // FIN METODO
    
    private static void cerrarConexion(Connection con, PreparedStatement ps, ResultSet rs) {
        if (rs != null) {
            try {
                rs.close();
            } catch (SQLException e) {
                System.err.println("Error al cerrar ResultSet: " + e.getMessage());
            }
        }
        if (ps != null) {
            try {
                ps.close();
            } catch (SQLException e) {
                System.err.println("Error al cerrar PreparedStatement: " + e.getMessage());
            }
        }
        if (con != null) {
            try {
                con.close();
            } catch (SQLException e) {
                System.err.println("Error al cerrar Connection: " + e.getMessage());
            }
        }
    }
} // FIN CLASE
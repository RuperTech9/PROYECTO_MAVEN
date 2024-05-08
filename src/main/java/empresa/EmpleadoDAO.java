
package empresa;

import java.sql.*;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.InputMismatchException;
import java.util.Iterator;
import java.util.Scanner;

/**
 *
 * @author Ruper
 */
public class EmpleadoDAO {
    // ArrayList de Objetos
    static ArrayList<Empleado> empleados = new ArrayList<>();
    static Scanner sc = new Scanner(System.in);
    
    
    // METODOS JDBC
    private static Connection conectar() {
        String url = "jdbc:mysql://192.168.0.27:3306/EmpresaDB";
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
                    rs.getInt("numemp"),
                    rs.getString("nombre"),
                    rs.getInt("edad"),
                    rs.getInt("oficina"),
                    rs.getString("puesto"),
                    rs.getDate("contrato").toLocalDate()
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
        System.out.println("Introduce el codigo del empleado: ");
        int numemp = sc.nextInt();
        sc.nextLine();
        System.out.println("Introduce el nombre del empleado:");
        String nombre = sc.nextLine();
        System.out.println("Introduce la edad del empleado:");
        int edad = sc.nextInt();
        System.out.println("Introduce el código de la oficina asignada:");
        int oficina = sc.nextInt();
        sc.nextLine();
        System.out.println("Introduce el puesto del empleado:");
        String puesto = sc.nextLine();
        // FECHA DE CONTRATACION
        LocalDate fechaContratacion = null; // Inicializo variable
        do {
            System.out.println("Introduce la fecha de contratación del empleado (DD/MM/YYYY):");
            String strFechaContratacion = sc.nextLine();
            try {
                DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy"); // Cambio el formato de la fecha.
                fechaContratacion = LocalDate.parse(strFechaContratacion, formatter);
                break; // Salgo si la fecha es correcta
            } catch (DateTimeParseException e) {
                System.err.println("ERROR. Introduce el formato correcto DD/MM/YYYY.");
            } // FIN TRY-CATCH
        } while (fechaContratacion == null); // FIN DO-WHILE
        
        
        // Aquí construyo el objeto empleado
        Empleado nuevoEmpleado = new Empleado(numemp, nombre, edad, oficina, puesto, fechaContratacion);

        añadirEmpleadoDB(nuevoEmpleado);    
    }
    private static void añadirEmpleadoDB(Empleado empleado) {
        Connection con = null;
        PreparedStatement ps = null;
        try {
            con = conectar();
            String sql = "INSERT INTO Empleados (numemp, nombre, edad, oficina, puesto, contrato) VALUES (?, ?, ?, ?, ?, ?)";
            ps = con.prepareStatement(sql);
            ps.setInt(1, empleado.getNumemp());
            ps.setString(2, empleado.getNombre());
            ps.setInt(3, empleado.getEdad());
            ps.setInt(4, empleado.getOficina());
            ps.setString(5, empleado.getPuesto());
            ps.setDate(6, Date.valueOf(empleado.getContrato()));
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
        System.out.println("Introduce el número del empleado a eliminar:");
        int numemp = sc.nextInt();
        
        eliminarEmpleadoDB(nombre, numemp);
    } // FIN METODO
    private static void eliminarEmpleadoDB(String nombre, int numemp) {
        Connection con = null;
        PreparedStatement ps = null;
        try {
            con = conectar();
            String sql = "DELETE FROM Empleados WHERE nombre = ? AND numemp = ?";
            ps = con.prepareStatement(sql);
            ps.setString(1, nombre);
            ps.setInt(2, numemp);
            int result = ps.executeUpdate();
            if (result > 0) {
                empleados.removeIf(e -> e.getNombre().equalsIgnoreCase(nombre) && e.getNumemp() == numemp);
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
        System.out.println("Introduce el número del empleado a actualizar:");
        int numemp = sc.nextInt();
        sc.nextLine();
        
        // Obtener nuevo puesto y oficina
        System.out.println("Introduce el nuevo puesto del empleado:");
        String nuevoPuesto = sc.nextLine();
        System.out.println("Introduce la nueva oficina del empleado:");
        int nuevaOficina;
        while (true) {
            try {
                nuevaOficina = (int) Double.parseDouble(sc.nextLine());
                if (nuevaOficina >= 0) break;
                System.out.println("La oficina debe ser un número positivo. Inténtalo de nuevo.");
            } catch (NumberFormatException e) {
                System.out.println("Por favor, introduce un número válido para la oficina.");
            }
        }
        actualizarEmpleadoDB(nombre, numemp, nuevaOficina, nuevoPuesto);
    }
    // Método para actualizar un empleado en la base de datos
    private static void actualizarEmpleadoDB(String nombre, int numemp, int nuevaOficina, String nuevoPuesto) {
        Connection con = null;
        PreparedStatement ps = null;
        try {
            con = conectar();
            String sql = "UPDATE Empleados SET oficina = ?, puesto = ? WHERE nombre = ? AND numemp = ?";
            ps = con.prepareStatement(sql);
            ps.setInt(1, nuevaOficina);
            ps.setString(2, nuevoPuesto);
            ps.setString(3, nombre);
            ps.setInt(4, numemp);
            int resultado = ps.executeUpdate();
            if (resultado > 0) {
                // Actualizar la lista en memoria
                empleados.forEach(empleado -> {
                    if (empleado.getNombre().equalsIgnoreCase(nombre) && empleado.getNumemp() == numemp) {
                        empleado.setOficina(nuevaOficina);
                        empleado.setPuesto(nuevoPuesto);
                    }
                });
                System.out.println("Empleado actualizado correctamente en la base de datos y en la lista.");
            } else {
                System.out.println("No se encontró ningún empleado con ese nombre y número para actualizar.");
            }
        } catch (SQLException e) {
            System.out.println("Error al actualizar empleado: " + e.getMessage());
        } finally {
            cerrarConexion(con, ps, null);
        }
    }
    
    // METODO que busca un empleado por nombre y apellidos
    private static void buscarEmpleado() {
        System.out.println("Introduce el nombre del empleado a actualizar:");
        String nombre = sc.nextLine();
        System.out.println("Introduce el número del empleado a actualizar:");
        int numemp = sc.nextInt();
        boolean encontrado = false; // Inicializo variable.
        // Imprimo los empleados.
        Iterator<Empleado> iterator = empleados.iterator();
        while (iterator.hasNext()) { // Mientras haya más elementos en la lista...
            Empleado empleado = iterator.next(); // Recorro la Lista de Empleados.
            if (empleado.getNombre().equalsIgnoreCase(nombre) && empleado.getNumemp() == numemp) {
                System.out.println(empleado.toString()); // Muestro los datos del empleado con el nombre especificado.
                encontrado = true;
                // El bucle continuará hasta el final de la lista de empleados por si hay 2 usuarios con el mismo nombre y apellidos.
            } // FIN IF
        } // FIN WHILE
        if (!encontrado) {
            System.err.println("No se encontró ningún empleado con ese número.");
        } // FIN IF
    } // FIN METODO
    
    // Método para mostrar empleados por rango de edad
    public static void mostrarEmpleadosPorEdad() {
        System.out.print("Introduce la edad mínima: ");
        int minEdad = sc.nextInt();
        System.out.print("Introduce la edad máxima: ");
        int maxEdad = sc.nextInt();

        boolean encontrado = false;
        System.out.println("Empleados entre " + minEdad + " y " + maxEdad + " años:");
        for (Empleado empleado : empleados) {
            if (empleado.getEdad() >= minEdad && empleado.getEdad() <= maxEdad) {
                System.out.println(empleado);
                encontrado = true;
            }
        }

        if (!encontrado) {
            System.out.println("No se encontraron empleados en el rango de edad especificado.");
        }
    }

    // METODO QUE ORDENA la lista de empleados por fecha de ingreso
    private static void ordenadosPorAntigüedad() {
        // COMPARATOR Flexibilidad para ordenar objetos según múltiples criterios y en diferentes órdenes, no limitándose al orden natural de los objetos.
        empleados.sort(Comparator.comparing(Empleado::getContrato)); // El Comparator le dice al método sort que compare los objetos Empleado entre sí por su fecha de ingreso.
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
    private static void ordenadosPorNumeroEmpleado() {
        empleados.sort(Comparator.comparingDouble(Empleado::getNumemp));
        // Imprimo los empleados ya ordenados
        Iterator<Empleado> itNumero = empleados.iterator();
        String ordenSalario = "\nEMPLEADOS ORDENADOS POR NUMERO:\n";
        int contador = 1; // Para numerar los empleados.
        while (itNumero.hasNext()) {
            Empleado empleado = itNumero.next();
            ordenSalario += empleado.toString() + "\n";

        }
        System.out.println(ordenSalario);
    } // FIN METODO
    
    // METODO QUE ORDENA la lista de empleados por apellido
    private static void ordenadosPorNombre() {
        empleados.sort(Comparator.comparing(Empleado::getNombre));
        // Imprimo los empleados ya ordenados
        Iterator<Empleado> itApellido = empleados.iterator();
        String ordenApellidos = "\nEMPLEADOS ORDENADOS POR NOMBRE:\n";
        int contador = 1; // Para numerar los empleados.
        while (itApellido.hasNext()) {
            Empleado empleado = itApellido.next();
            ordenApellidos += contador + "- " + empleado.toString() + "\n";
            contador++; // Para el siguiente empleado.
        }
        System.out.println(ordenApellidos);
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
                          "\n5- Buscar Empleados por rango de edad:" +
                          "\n6- Imprimir Empleados ordenados:" +
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
                        mostrarEmpleadosPorEdad();
                        break;
                    case 6:
                        String opcionOrdenamiento;
                        do {
                            String subMenu = "\nElige una opción:\n" +
                                            "a) Por antigüedad\n" +
                                            "b) Por codigo\n" +
                                            "c) Por nombre\n" +
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
                                    ordenadosPorNumeroEmpleado();
                                    break;
                                case "c":
                                    ordenadosPorNombre();
                                    break;
                                default:
                                    System.err.println("Opción no válida. Introduce 'a', 'b', 'c' o 's' para volver.");
                            } // FIN SWITCH
                            
                        } while (true); // FIN DO-WHILE
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
        } while (opcion != 6); // FIN DO-WHILE
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

package empleados;


import java.util.ArrayList;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.InputMismatchException;
import java.util.Scanner;
import java.sql.*;

/**
 *
 * @author Ruper
 */
public class EmpleadosEmpresa {
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
            System.err.println("Error en la conexión: " + e.getMessage());
            System.err.println("Número que representa el error: " + e.getErrorCode());
            return null;
        }
    }

    public void cargarEmpleadosDB() {
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
            System.err.println("Error al cargar empleados: " + e.getMessage());
            System.err.println("Número del error: " + e.getErrorCode());
        }
    } // FIN METODO
    
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
        LocalDate fechaNacimiento = obtenerFecha("Introduce la fecha de nacimiento del empleado (DD/MM/YYYY):");
        // FECHA DE INGRESO
        LocalDate fechaIngreso = obtenerFecha("Introduce la fecha de ingreso del empleado (DD/MM/YYYY):");
        // PUESTO
        System.out.println("Introduce el puesto del empleado:");
        String puesto = sc.nextLine();
        // SALARIO
        double salario = obtenerSalario();
        
        // Aquí construyo el objeto empleado
        Empleado nuevoEmpleado = new Empleado(codigo, nombre, apellidos, fechaNacimiento, fechaIngreso, puesto, salario);
        
        guardarEmpleadoDB(nuevoEmpleado);    
    } // FIN METODO
    
    // METODO para guardar un empleado en la BBDD.
    private static void guardarEmpleadoDB(Empleado empleado) {
        String sql = "INSERT INTO Empleados (codigo, nombre, apellidos, fechaNacimiento, fechaIngreso, puesto, salario) VALUES (?, ?, ?, ?, ?, ?, ?)";
        Connection con = null;
        PreparedStatement ps = null;
        try {
            con = conectar();
            ps = con.prepareStatement(sql);
            ps.setString(1, empleado.getCodigo());
            ps.setString(2, empleado.getNombre());
            ps.setString(3, empleado.getApellidos());
            ps.setDate(4, Date.valueOf(empleado.getFechaNacimiento()));
            ps.setDate(5, Date.valueOf(empleado.getFechaIngreso()));
            ps.setString(6, empleado.getPuesto());
            ps.setDouble(7, empleado.getSalario());
            int resultado = ps.executeUpdate();
            if (resultado > 0) {
                empleados.add(empleado); // Lo añado a la lista.
                System.out.println("\nEl empleado " + empleado.getNombre() + " ha sido añadido a la base de datos.");
            }
        } catch (SQLException e) {
            System.err.println("Error al añadir empleado: " + e.getMessage());
            System.err.println("Número que representa el error: " + e.getErrorCode());
        } finally {
            cerrarConexion(con, ps, null);
        }
    } // FIN METODO
    
    // METODO que elimina un empleado por nombre y apellidos
    private static void eliminarEmpleado() {
        System.out.println("Introduce el código del empleado a eliminar:");
        String codigo = sc.nextLine();
        
        // Primero, intento insertar el empleado en EmpleadosAntiguos
        String sqlInsert = "INSERT INTO EmpleadosAntiguos (codigo, nombre, apellidos, fechaNacimiento, fechaIngreso, puesto, salario, fechaBaja) SELECT codigo, nombre, apellidos, fechaNacimiento, fechaIngreso, puesto, salario, ? FROM Empleados WHERE codigo = ?";
        String sqlDelete = "DELETE FROM Empleados WHERE codigo = ?";
        Connection con = null;
        PreparedStatement psInsert = null;
        PreparedStatement psDelete = null;
        try {
            con = conectar();
            psInsert = con.prepareStatement(sqlInsert);
            psInsert.setDate(1, Date.valueOf(LocalDate.now()));
            psInsert.setString(2, codigo);
            int resultadoInsert = psInsert.executeUpdate();
            if (resultadoInsert > 0) {
                psDelete = con.prepareStatement(sqlDelete);
                psDelete.setString(1, codigo);
                int resultadoDelete = psDelete.executeUpdate();
                if (resultadoDelete > 0) {
                    empleados.removeIf(e -> e.getCodigo().equals(codigo));
                }
            }
        } catch (SQLException e) {
            System.err.println("Error al eliminar empleado: " + e.getMessage());
            System.err.println("Número que representa el error: " + e.getErrorCode());
        } finally {
            cerrarConexion(con, psInsert, null);
            cerrarConexion(con, psDelete, null);
        }
    } // FIN METODO
    
    // Método para solicitar la actualización de un empleado
    private static void actualizarEmpleado() {
        System.out.println("Introduce el código del empleado a actualizar:");
        String codigo = sc.nextLine();
        
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
        actualizarEmpleadoDB(codigo, nuevoPuesto, nuevoSalario);
    } // FIN METODO
    
    // Método para actualizar un empleado en la base de datos
    private static void actualizarEmpleadoDB(String codigo, String nuevoPuesto, double nuevoSalario) {
        String sql = "UPDATE Empleados SET puesto = ?, salario = ? WHERE codigo = ?";
        Connection con = null;
        PreparedStatement ps = null;
        try {
            con = conectar();
            ps = con.prepareStatement(sql);
            ps.setString(1, nuevoPuesto);
            ps.setDouble(2, nuevoSalario);
            ps.setString(3, codigo);
            int resultado = ps.executeUpdate();
            if (resultado > 0) {
                // Actualizo la lista
                empleados.forEach(empleado -> {
                    if (empleado.getCodigo().equalsIgnoreCase(codigo)) {
                        empleado.setPuesto(nuevoPuesto);
                        empleado.setSalario(nuevoSalario);
                    }
                });
                System.out.println("Empleado actualizado correctamente en la base de datos.");
            } else {
                System.out.println("No se encontró ningún empleado con ese código para actualizar.");
            }
        } catch (SQLException e) {
            System.err.println("Error al actualizar empleado: " + e.getMessage());
            System.err.println("Número que representa el error: " + e.getErrorCode());
        } finally {
            cerrarConexion(con, ps, null);
        }
    } // FIN METODO
    
    // METODO que busca un empleado por nombre y apellidos
    private static void buscarEmpleado() {
        System.out.println("Introduce el codigo del empleado:");
        String codigo = sc.nextLine();
        
        String sql = "SELECT * FROM Empleados WHERE codigo = ?";
        Connection con = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        try {
            con = conectar();
            ps = con.prepareStatement(sql);
            ps.setString(1, codigo);
            rs = ps.executeQuery();
            if (!rs.next()) {
                System.err.println("No se encontró ningún empleado con ese codigo.");
            } else {
                do {
                    Empleado empleado = new Empleado(
                    rs.getString("codigo"),
                    rs.getString("nombre"),
                    rs.getString("apellidos"),
                    rs.getDate("fechaNacimiento").toLocalDate(),
                    rs.getDate("fechaIngreso").toLocalDate(),
                    rs.getString("puesto"),
                    rs.getDouble("salario")
                    );
                    System.out.println(empleado.toString()); // Muestra los datos del empleado encontrado
                } while (rs.next());
            }
        } catch (SQLException e) {
            System.err.println("Error al buscar empleado: " + e.getMessage());
            System.err.println("Número que representa el error: " + e.getErrorCode());
        } finally {
            cerrarConexion(con, ps, rs);
        }
    } // FIN METODO
    
    // METODO que busca un empleado por nombre y apellidos
    private static void mostrarEmpleadosAntiguos() {
        String sql = "SELECT * FROM EmpleadosAntiguos";
        Connection con = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        try {
            con = conectar();
            ps = con.prepareStatement(sql);
            rs = ps.executeQuery();
            System.out.println("\nLista de Empleados Antiguos:");
            while (rs.next()) {
                EmpleadoAntiguo empleadoAntiguo = new EmpleadoAntiguo(
                    rs.getString("codigo"),
                    rs.getString("nombre"),
                    rs.getString("apellidos"),
                    rs.getDate("fechaNacimiento").toLocalDate(),
                    rs.getDate("fechaIngreso").toLocalDate(),
                    rs.getString("puesto"),
                    rs.getDouble("salario"),
                    rs.getDate("fechaBaja").toLocalDate()
                );
                System.out.println(empleadoAntiguo.toString());
            }
        } catch (SQLException e) {
            System.err.println("Error al buscar empleado: " + e.getMessage());
            System.err.println("Número que representa el error: " + e.getErrorCode());
        } finally {
            cerrarConexion(con, ps, rs);
        }
    } // FIN METODO

    // METODO QUE ORDENA la lista de empleados por fecha de ingreso
    private static void ordenadosPorAntigüedad() {
        String sql = "SELECT * FROM Empleados ORDER BY fechaIngreso ASC";
        Connection con = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        
        try {
            con = conectar();
            ps = con.prepareStatement(sql);
            rs = ps.executeQuery();
            
            empleados.clear(); // Limpia la lista antes de añadir elementos
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
            // Imprimo los empleados ordenados por antigüedad.
            System.out.println("\nEMPLEADOS ORDENADOS POR ANTIGÜEDAD:\n");
            for (int i = 0; i < empleados.size(); i++) {
                System.out.println((i + 1) + "- " + empleados.get(i).toString());
            }
        } catch (SQLException e) {
            System.err.println("Error al obtener empleados ordenados por antigüedad: " + e.getMessage());
            System.err.println("Número que representa el error: " + e.getErrorCode());
        } finally {
            cerrarConexion(con, ps, rs);
        }
    }  // FIN METODO
    
    
    // METODO QUE ORDENA la lista de empleados por salario (de mayor a menor)
    private static void ordenadosPorSalario() {
        String sql = "SELECT * FROM Empleados ORDER BY salario DESC";
        Connection con = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        try {
            con = conectar();
            ps = con.prepareStatement(sql);
            rs = ps.executeQuery();
            
            empleados.clear(); // Limpia la lista antes de añadir elementos
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
            // Imprimo los empleados ordenados por salario.
            System.out.println("\nEMPLEADOS ORDENADOS POR SALARIO:\n");
            for (int i = 0; i < empleados.size(); i++) {
                System.out.println((i + 1) + "- " + empleados.get(i).toString());
            }
        } catch (SQLException e) {
            System.err.println("Error al obtener empleados ordenados por salario: " + e.getMessage());
            System.err.println("Número que representa el error: " + e.getErrorCode());
        } finally {
            cerrarConexion(con, ps, rs);
        }
    } // FIN METODO
    
    // METODO QUE ORDENA la lista de empleados por apellido
    private static void ordenadosPorApellido() {
        String sql = "SELECT * FROM Empleados ORDER BY apellidos ASC";
        Connection con = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        try {
            con = conectar();
            ps = con.prepareStatement(sql);
            rs = ps.executeQuery();
            
            empleados.clear(); // Limpia la lista antes de añadir elementos
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
            // Imprimo los empleados ordenados por apellidos.
            System.out.println("\nEMPLEADOS ORDENADOS POR APELLIDOS:\n");
            for (int i = 0; i < empleados.size(); i++) {
                System.out.println((i + 1) + "- " + empleados.get(i).toString());
            }
        } catch (SQLException e) {
            System.err.println("Error al obtener empleados ordenados por apellidos: " + e.getMessage());
            System.err.println("Número que representa el error: " + e.getErrorCode());
        } finally {
            cerrarConexion(con, ps, rs);
        }
    } // FIN METODO
    
    // METODO para calcular el gasto total sumando los salarios de todos los empleados.
    private static void calcularGastoTotal() {
        String sql = "SELECT SUM(salario) AS totalSalarios FROM Empleados"; // Consulta SQL para calcular la suma de los salarios
        Connection con = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        try {
            con = conectar();
            ps = con.prepareStatement(sql);
            rs = ps.executeQuery();
            if (rs.next()) {
                double gastoTotal = rs.getDouble("totalSalarios"); // Obtiene el total de los salarios desde la base de datos
                System.out.println("\nEl gasto total en salarios de los empleados es: " + gastoTotal);
            } else {
                System.out.println("No fue posible calcular el gasto total de salarios.");
            }
        } catch (SQLException e) {
            System.err.println("Error al calcular el gasto total en salarios: " + e.getMessage());
            System.err.println("Número que representa el error: " + e.getErrorCode());
        } finally {
            cerrarConexion(con, ps, rs);
        }
    } // FIN METODO
    
    private static void mostrarSalarioMaximoYMinimo() {
        String sql = "SELECT MIN(salario) AS salarioMinimo, MAX(salario) AS salarioMaximo FROM Empleados"; // Consulta SQL para obtener el salario mínimo y máximo
        Connection con = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        try {
            con = conectar();
            ps = con.prepareStatement(sql);
            rs = ps.executeQuery();
            if (rs.next()) {
                double salarioMinimo = rs.getDouble("salarioMinimo");
                double salarioMaximo = rs.getDouble("salarioMaximo");
                System.out.println("\nEl salario mínimo de los empleados es: " + salarioMinimo);
                System.out.println("El salario máximo de los empleados es: " + salarioMaximo);
            } else {
                System.out.println("No fue posible obtener los salarios mínimos y máximos.");
            }
        } catch (SQLException e) {
            System.err.println("Error al obtener los salarios mínimos y máximos: " + e.getMessage());
            System.err.println("Número que representa el error: " + e.getErrorCode());
        } finally {
            cerrarConexion(con, ps, rs);
        }
    } // FIN METODO
    
    private static void mostrarSalarioMedio() {
        String sql = "SELECT AVG(salario) AS salarioMedio FROM Empleados"; // Consulta SQL para calcular la media de los salarios
        Connection con = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        try {
            con = conectar();
            ps = con.prepareStatement(sql);
            rs = ps.executeQuery();
            if (rs.next()) {
                double salarioMedio = rs.getDouble("salarioMedio");
                System.out.println("\nEl salario medio de los empleados es: " + salarioMedio);
            } else {
                System.out.println("No fue posible calcular el salario medio.");
            }
        } catch (SQLException e) {
            System.err.println("Error al calcular el salario medio: " + e.getMessage());
            System.err.println("Número que representa el error: " + e.getErrorCode());
        } finally {
            cerrarConexion(con, ps, rs);
        }
    } // FIN METODO
    
    // METODO para mostrar empleados con fecha de ingreso anterior a un año determinado
    private static void mostrarEmpleadosConIngresoAnteriorA() {
        System.out.println("Introduce el año límite (YYYY):");
        int anioLimite = sc.nextInt();
        sc.nextLine(); // Salto de línea

        String sql = "SELECT * FROM Empleados WHERE YEAR(fechaIngreso) < ?";
        Connection con = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        try {
            con = conectar();
            ps = con.prepareStatement(sql);
            ps.setInt(1, anioLimite);
            rs = ps.executeQuery();
            System.out.println("\nEmpleados con fecha de ingreso anterior a " + anioLimite + ":\n");
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
                System.out.println(empleado.toString());
            }
        } catch (SQLException e) {
            System.err.println("Error al obtener empleados con fecha de ingreso anterior a " + anioLimite + ": " + e.getMessage());
            System.err.println("Número que representa el error: " + e.getErrorCode());
        } finally {
            cerrarConexion(con, ps, rs);
        }
    } // FIN METODO
    
    //METODO para obtener la fecha
    private static LocalDate obtenerFecha(String mensaje) {
        LocalDate fecha = null;
        do {
            System.out.println(mensaje);
            String strFecha = sc.nextLine();
            try {
                DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
                fecha = LocalDate.parse(strFecha, formatter);
                break;
            } catch (DateTimeParseException e) {
                System.err.println("ERROR. Introduce el formato correcto DD/MM/YYYY.");
            }
        } while (fecha == null);
        return fecha;
    } // FIN METODO
    
    //METODO para obtener el salario
    private static double obtenerSalario() {
        double salario;
        do {
            System.out.println("Introduce el salario del empleado:");
            try {
                salario = Double.parseDouble(sc.nextLine());
                if (salario > 0) {
                    return salario;
                } else {
                    System.err.println("El salario debe ser mayor que 0. Inténtalo de nuevo.");
                }
            } catch (NumberFormatException e) {
                System.err.println("ERROR. Se esperaba un número para el salario. Inténtalo de nuevo.");
            }
        } while (true);
    } // FIN METODO
    
    private static void cerrarConexion(Connection con, PreparedStatement ps, ResultSet rs) {
        if (rs != null) {
            try {
                rs.close();
            } catch (SQLException e) {
                System.err.println("Error al cerrar ResultSet: " + e.getMessage());
                System.err.println("Número que representa el error: " + e.getErrorCode());
            }
        }
        if (ps != null) {
            try {
                ps.close();
            } catch (SQLException e) {
                System.err.println("Error al cerrar PreparedStatement: " + e.getMessage());
                System.err.println("Número que representa el error: " + e.getErrorCode());
            }
        }
        if (con != null) {
            try {
                con.close();
            } catch (SQLException e) {
                System.err.println("Error al cerrar Connection: " + e.getMessage());
                System.err.println("Número que representa el error: " + e.getErrorCode());
            }
        }
    }
    
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
                          "\n7- Mostrar salario máximo y mínimo" +
                          "\n8- Mostrar salario medio" +
                          "\n9- Mostrar empleados antiguos" +
                          "\n10- Mostrar empleados con ingreso anterior a un año" +
                          "\n0- Salir" +
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
                        mostrarSalarioMaximoYMinimo();
                        break;
                    case 8:
                        mostrarSalarioMedio();
                        break;
                    case 9:
                        mostrarEmpleadosAntiguos();
                        break;
                    case 10:
                        mostrarEmpleadosConIngresoAnteriorA();
                        break;
                    case 0:
                        System.out.println("\nSaliendo...");
                        break;
                    default:
                        System.err.println("\nOpción no válida. Introduce un número entre 1 y 10.");
                        break;
                } // FIN SWITCH
            } catch (InputMismatchException e) {
                System.err.println("ERROR. Entrada no válida, inténtalo de nuevo.");
                sc.nextLine(); // Salto de línea
            } // FIN TRY-CATCH
        } while (opcion != 0); // FIN DO-WHILE
    } // FIN METODO
} // FIN CLASE

package interfaces;


import java.util.ArrayList;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.Scanner;
import java.sql.*;
import javax.swing.JOptionPane;

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
        String url = "jdbc:mysql://192.168.0.27:3306/EmpleadosDB";
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
    public void añadirEmpleado() {
        // CODIGO EMPLEADO
        System.out.println("Introduce el codigo del empleado: ");
        String codigo = sc.nextLine();
        // NOMBRE Y APELLIDOS
        String nombre = JOptionPane.showInputDialog("Introduce el nombre del empleado:");
        String apellidos = JOptionPane.showInputDialog("Introduce los apellidos del empleado:");
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
    public void guardarEmpleadoDB(Empleado empleado) {
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
    public void eliminarEmpleado(String codigo) {
        String sqlInsert = "INSERT INTO EmpleadosAntiguos SELECT * FROM Empleados WHERE codigo = ?"; // Primero, intento insertar el empleado en EmpleadosAntiguos
        String sqlDelete = "DELETE FROM Empleados WHERE codigo = ?";
        Connection con = null;
        PreparedStatement psInsert = null;
        PreparedStatement psDelete = null;
        
        try {
            con = conectar();
            psInsert = con.prepareStatement(sqlInsert); // Insertar en EmpleadosAntiguos
            psInsert.setString(1, codigo);
            int resultadoInsert = psInsert.executeUpdate();
        
            if (resultadoInsert > 0) {
                psDelete = con.prepareStatement(sqlDelete); // Si la inserción fue exitosa, procedo a eliminar
                psDelete.setString(1, codigo);
                int resultadoDelete = psDelete.executeUpdate();
                if (resultadoDelete > 0) {
                    empleados.removeIf(e -> e.getCodigo().equals(codigo)); // También se elimina de la lista 
                    System.out.println("\nEl empleado ha sido eliminado de la base de datos.");
                } else {
                    System.out.println("\nNo se encontró ningún empleado con ese nombre para eliminar.");
                }
            } else {
                System.out.println("\nNo se encontró el empleado o no se pudo insertar en EmpleadosAntiguos");
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
    public void actualizarEmpleado() {
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
    public void actualizarEmpleadoDB(String codigo, String nuevoPuesto, double nuevoSalario) {
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
    public void buscarEmpleado(String codigo) {
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
    public void mostrarEmpleadosAntiguos() {
        String sql = "SELECT * FROM EmpleadosAntiguos";
        Connection con = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        try {
            con = conectar();
            ps = con.prepareStatement(sql);
            rs = ps.executeQuery();
            System.out.println("Lista de Empleados Antiguos:");
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
                    System.out.println(empleado.toString()); // Muestra los datos del empleado encontrado
            }
        } catch (SQLException e) {
            System.err.println("Error al buscar empleado: " + e.getMessage());
            System.err.println("Número que representa el error: " + e.getErrorCode());
        } finally {
            cerrarConexion(con, ps, rs);
        }
    } // FIN METODO

    // METODO QUE ORDENA la lista de empleados por fecha de ingreso
    public void ordenadosPorAntigüedad() {
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
            System.out.println("EMPLEADOS ORDENADOS POR ANTIGÜEDAD:\n");
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
    public void ordenadosPorSalario() {
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
            System.out.println("EMPLEADOS ORDENADOS POR SALARIO:\n");
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
    public void ordenadosPorApellido() {
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
            System.out.println("EMPLEADOS ORDENADOS POR APELLIDOS:\n");
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
    public double calcularGastoTotal() {
        String sql = "SELECT SUM(salario) AS totalSalarios FROM Empleados"; // Consulta SQL para calcular la suma de los salarios
        double gastoTotal = 0;
        Connection con = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        try {
            con = conectar();
            ps = con.prepareStatement(sql);
            rs = ps.executeQuery();
            if (rs.next()) {
                gastoTotal = rs.getDouble("totalSalarios"); // Obtiene el total de los salarios desde la base de datos
                System.out.println("El gasto total en salarios de los empleados es: " + gastoTotal);
            } else {
                System.out.println("No fue posible calcular el gasto total de salarios.");
            }
        } catch (SQLException e) {
            System.err.println("Error al calcular el gasto total en salarios: " + e.getMessage());
            System.err.println("Número que representa el error: " + e.getErrorCode());
        } finally {
            cerrarConexion(con, ps, rs);
        }
        return gastoTotal;
    } // FIN METODO
    
    //METODO para obtener la fecha
    public LocalDate obtenerFecha(String mensaje) {
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
    public double obtenerSalario() {
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
} // FIN CLASE
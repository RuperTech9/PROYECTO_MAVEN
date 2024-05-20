
package interfaces;

import java.util.ArrayList;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Comparator;
import java.time.format.DateTimeParseException;
import java.util.InputMismatchException;
import javax.swing.JOptionPane;
import java.io.*;
import java.sql.*;

/**
 *
 * @author Ruper
 */
public class EmpleadosEmpresa1 {
    // ArrayList de Objetos
    static ArrayList<Empleado> empleados = new ArrayList<>();
    // METODOS JDBC
    private static Connection conectar() {
        String url = "jdbc:mysql://192.168.0.27:3306/EmpleadosDB";
        String user = "alejandro";
        String password = "J0selu1s100%";
        try {
            return DriverManager.getConnection(url, user, password);
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null,"Error en la conexión: " + e.getMessage());
            JOptionPane.showMessageDialog(null,"Número que representa el error: " + e.getErrorCode());
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
            JOptionPane.showMessageDialog(null,"Error al cargar empleados: " + e.getMessage());
            JOptionPane.showMessageDialog(null,"Número del error: " + e.getErrorCode());
        }
    } // FIN METODO
    
    // METODO para añadir un empleado.
    private static void añadirEmpleado() {
        // CODIGO
        String codigo = JOptionPane.showInputDialog("Introduce el codigo del empleado:");
        if (codigo == null) return; // Salgo del método si presiono cancelar
        // NOMBRE Y APELLIDOS
        String nombre = JOptionPane.showInputDialog("Introduce el nombre del empleado:");
        if (nombre == null) return; // Salgo del método si presiono cancelar
        String apellidos = JOptionPane.showInputDialog("Introduce los apellidos del empleado:");
        if (apellidos == null) return;
        
        // FECHA DE NACIMIENTO
        LocalDate fechaNacimiento = null; // Inicializo variable
        do {
            String strFechaNacimiento = JOptionPane.showInputDialog("Introduce la fecha de nacimiento del empleado (DD/MM/YYYY):");
            if (strFechaNacimiento == null) return; // Sale del método si se presiona cancelar
            try {
                DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy"); // Cambio el formato de la fecha.
                fechaNacimiento = LocalDate.parse(strFechaNacimiento, formatter);
                break; // Salgo si la fecha es correcta
            } catch (DateTimeParseException e) {
                JOptionPane.showMessageDialog(null, "ERROR. Introduce el formato correcto DD/MM/YYYY.");
            } // FIN TRY-CATCH
        } while (fechaNacimiento == null); // FIN DO-WHILE
        
        // FECHA DE INGRESO
        LocalDate fechaIngreso = null; // Inicializo variable
        do {
            String strFechaIngreso = JOptionPane.showInputDialog("Introduce la fecha de ingreso del empleado (DD/MM/YYYY):");
            if (strFechaIngreso == null) return;
            try {
                DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy"); // Cambio el formato de la fecha.
                fechaIngreso = LocalDate.parse(strFechaIngreso, formatter);
                if (fechaIngreso != null && fechaNacimiento != null && fechaIngreso.isBefore(fechaNacimiento)) { // Verifico que la fecha de ingreso no sea anterior a la fecha de nacimiento
                    JOptionPane.showMessageDialog(null, "La fecha de ingreso no puede ser anterior a la fecha de nacimiento. Por favor, introduce una fecha válida.");
                    fechaIngreso = null; // Restablezco la fechaIngreso para repetir el bucle
                } else {
                    break; // Salgo si la fecha es correcta
                }
            } catch (DateTimeParseException e) {
                JOptionPane.showMessageDialog(null, "ERROR. Introduce el formato correcto DD/MM/YYYYD.");
            } // FIN TRY-CATCH
        } while (fechaIngreso == null); // FIN DO-WHILE
        
        // PUESTO
        String puesto = JOptionPane.showInputDialog(null, "Introduce el puesto del empleado:");
        
        // SALARIO
        double salario = 0; // Inicializo variable.
        do {
            String strSalario = JOptionPane.showInputDialog("Introduce el salario del empleado:");
            try {
                salario = Double.parseDouble(strSalario);
                if (salario > 0) {
                    break; // Salgo si el salario es correcto y positivo.
                } else {
                    JOptionPane.showMessageDialog(null, "El salario debe ser mayor que 0. Intentalo de nuevo.");
                } // FIN IF
            } catch (InputMismatchException e) {
                JOptionPane.showMessageDialog(null, "ERROR. Se esperaba un número para el salario. Intentalo de nuevo.");
            } // FIN TRY-CATCH
        } while (true); // FIN DO-WHILE
        
        empleados.add(new Empleado(codigo, nombre, apellidos, fechaNacimiento, fechaIngreso, puesto, salario));                                                       
        JOptionPane.showMessageDialog(null, "El empleado " + nombre + " ha sido añadido a la lista.");
 
    }
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
            JOptionPane.showMessageDialog(null,"Error al añadir empleado: " + e.getMessage());
            JOptionPane.showMessageDialog(null,"Número que representa el error: " + e.getErrorCode());
        } finally {
            cerrarConexion(con, ps, null);
        }
    } // FIN METODO
    
    // METODO que elimina un empleado por nombre y apellidos
    private static void eliminarEmpleado() {
        String codigo = JOptionPane.showInputDialog("Introduce el codigo del empleado a eliminar:");
        if (codigo == null) return; // Salgo del método si presiono cancelar
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
                    JOptionPane.showMessageDialog(null, "El empleado ha sido eliminado de la BBDD.");
                } else {
                    JOptionPane.showMessageDialog(null,"\nNo se encontró ningún empleado con ese codigo para eliminar.");
                }
            } else {
                JOptionPane.showMessageDialog(null, "\nNo se encontró ningún empleado con ese codigo.");
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null,"Error al eliminar empleado: " + e.getMessage());
            JOptionPane.showMessageDialog(null,"Número que representa el error: " + e.getErrorCode());
        } finally {
            cerrarConexion(con, psInsert, null);
            cerrarConexion(con, psDelete, null); 
        }
    } // FIN METODO
    
    // METODO que busca un empleado por nombre y apellidos
    private static void buscarEmpleado() {
        String codigo = JOptionPane.showInputDialog("Introduce el codigo del empleado a buscar:");
        if (codigo == null) return; // Salgo del método si presiono cancelar
    
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
                JOptionPane.showMessageDialog(null,"No se encontró ningún empleado con ese codigo.");
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
                    JOptionPane.showMessageDialog(null,empleado.toString()); // Muestra los datos del empleado encontrado
                } while (rs.next());
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null,"Error al buscar empleado: " + e.getMessage());
            JOptionPane.showMessageDialog(null,"Número que representa el error: " + e.getErrorCode());
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
            JOptionPane.showMessageDialog(null,"\nLista de Empleados Antiguos:");
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
                    JOptionPane.showMessageDialog(null,empleado.toString()); // Muestra los datos del empleado encontrado
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null,"Error al buscar empleado: " + e.getMessage());
            JOptionPane.showMessageDialog(null,"Número que representa el error: " + e.getErrorCode());
        } finally {
            cerrarConexion(con, ps, rs);
        }
    } // FIN METODO
    
    // METODO que ordena la lista de empleados por fecha de ingreso
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
            JOptionPane.showMessageDialog(null,"\nEMPLEADOS ORDENADOS POR ANTIGÜEDAD:\n");
            for (int i = 0; i < empleados.size(); i++) {
                System.out.println((i + 1) + "- " + empleados.get(i).toString());
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null,"Error al obtener empleados ordenados por antigüedad: " + e.getMessage());
            JOptionPane.showMessageDialog(null,"Número que representa el error: " + e.getErrorCode());
        } finally {
            cerrarConexion(con, ps, rs);
        }
    } // FIN METODO
    
    // METODO que ordena la lista de empleados por salario (de mayor a menor)
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
            JOptionPane.showMessageDialog(null,"\nEMPLEADOS ORDENADOS POR SALARIO:\n");
            for (int i = 0; i < empleados.size(); i++) {
                System.out.println((i + 1) + "- " + empleados.get(i).toString());
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null,"Error al obtener empleados ordenados por salario: " + e.getMessage());
            JOptionPane.showMessageDialog(null,"Número que representa el error: " + e.getErrorCode());
        } finally {
            cerrarConexion(con, ps, rs);
        }
    } // FIN METODO
    
    // METODO que ordena la lista de empleados por apellido
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
            JOptionPane.showMessageDialog(null,"\nEMPLEADOS ORDENADOS POR APELLIDOS:\n");
            for (int i = 0; i < empleados.size(); i++) {
                System.out.println((i + 1) + "- " + empleados.get(i).toString());
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null,"Error al obtener empleados ordenados por apellidos: " + e.getMessage());
            JOptionPane.showMessageDialog(null,"Número que representa el error: " + e.getErrorCode());
        } finally {
            cerrarConexion(con, ps, rs);
        }
    } // FIN METODO
    
    // METODO para calcular el gasto total sumando los salarios de todos los empleados.
    private double calcularGastoTotal() {
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
                JOptionPane.showMessageDialog(null,"\nEl gasto total en salarios de los empleados es: " + gastoTotal);
            } else {
                JOptionPane.showMessageDialog(null,"No fue posible calcular el gasto total de salarios.");
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null,"Error al calcular el gasto total en salarios: " + e.getMessage());
            JOptionPane.showMessageDialog(null,"Número que representa el error: " + e.getErrorCode());
        } finally {
            cerrarConexion(con, ps, rs);
        }
        return gastoTotal;
    } // FIN METODO
    
    private static void cerrarConexion(Connection con, PreparedStatement ps, ResultSet rs) {
        if (rs != null) {
            try {
                rs.close();
            } catch (SQLException e) {
                JOptionPane.showMessageDialog(null,"Error al cerrar ResultSet: " + e.getMessage());
                JOptionPane.showMessageDialog(null,"Número que representa el error: " + e.getErrorCode());
            }
        }
        if (ps != null) {
            try {
                ps.close();
            } catch (SQLException e) {
                JOptionPane.showMessageDialog(null,"Error al cerrar PreparedStatement: " + e.getMessage());
                JOptionPane.showMessageDialog(null,"Número que representa el error: " + e.getErrorCode());
            }
        }
        if (con != null) {
            try {
                con.close();
            } catch (SQLException e) {
                JOptionPane.showMessageDialog(null,"Error al cerrar Connection: " + e.getMessage());
                JOptionPane.showMessageDialog(null,"Número que representa el error: " + e.getErrorCode());
            }
        }
    }
    
    // METODO para mostrar el menú
    public void mostrarMenu() {
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
        
        int opcion = 0;
        do {     
            try {
                String strOpcion = JOptionPane.showInputDialog(menu);
                
                // Cierro el programa si pulso cancelar en el menú principal.
                if (strOpcion == null) {
                    JOptionPane.showMessageDialog(null, "\nSaliendo...");
                    System.exit(0); 
                }
                
                opcion = Integer.parseInt(strOpcion);
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
                        do {
                            String subMenu = "Elige una opción:\n" +
                                            "a) Por antigüedad\n" +
                                            "b) Por salario\n" +
                                            "c) Por apellido\n" +
                                            "s) Volver al menú principal";
                            String opcionOrdenamiento = JOptionPane.showInputDialog(subMenu);
                            
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
                                    JOptionPane.showMessageDialog(null, "Opción no válida. Introduce una opción válida o 's' para volver.");
                            } // FIN SWITCH
                            
                        } while (true); // FIN DO-WHILE
                        break;
                    case 5:
                        calcularGastoTotal();
                        break;
                    case 6:
                        JOptionPane.showMessageDialog(null, "\nSaliendo...");
                        break;
                    default:
                        JOptionPane.showMessageDialog(null, "\nOpción no válida. Introduce un número entre 1 y 6.");
                        break;
                } // FIN SWITCH
            } catch (NumberFormatException e1) {
                JOptionPane.showMessageDialog(null, "ERROR. Entrada no válida, inténtalo de nuevo.");
            } // FIN TRY-CATCH
        } while (opcion != 6); // FIN DO-WHILE
        
    } // FIN METODO
    
} // FIN CLASE
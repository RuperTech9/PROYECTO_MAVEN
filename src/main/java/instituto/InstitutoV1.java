
package instituto;


import java.sql.Connection;
import java.sql.Date;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.InputMismatchException;
import java.util.Scanner;

/**
 *
 * @author Ruper
 */
public class InstitutoV1 {
    // ArrayList de Objetos
    static ArrayList<Alumno> alumnos = new ArrayList<>();
    static Scanner sc = new Scanner(System.in);
    
    
    // METODOS JDBC
    private static Connection conectar() {
        String url = "jdbc:mysql://192.168.0.27:3306/InstitutoDB";
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

    public void cargarAlumnosDB() {
        String sql = "SELECT * FROM Alumnos";
        try (Connection con = conectar();
             PreparedStatement pstmt = con.prepareStatement(sql);
             ResultSet rs = pstmt.executeQuery()) {
            while (rs.next()) {
                Alumno alumno = new Alumno(
                    rs.getString("dni"),
                    rs.getString("nombre"),
                    rs.getString("apellidos"),
                    rs.getDate("fechaNacimiento").toLocalDate(),
                    rs.getString("curso"),
                    rs.getDouble("nota")
                );
                alumnos.add(alumno);
            }
        } catch (SQLException e) {
            System.err.println("Error al cargar alumnos: " + e.getMessage());
            System.err.println("Número del error: " + e.getErrorCode());
        }
    } // FIN METODO
    
    // METODO para añadir un empleado.
    public void añadirAlumno() {
        // CODIGO EMPLEADO
        System.out.println("Introduce el dni del alumno: ");
        String dni = sc.nextLine();
        // NOMBRE Y APELLIDOS
        System.out.println("Introduce el nombre del alumno:");
        String nombre = sc.nextLine();
        System.out.println("Introduce los apellidos del alumno:");
        String apellidos = sc.nextLine();
        // FECHA DE NACIMIENTO
        LocalDate fechaNacimiento = obtenerFecha("Introduce la fecha de nacimiento del alumno (DD/MM/YYYY):");
        // PUESTO
        System.out.println("Introduce el curso del alumno:");
        String curso = sc.nextLine();
        // SALARIO
        double nota = obtenerNota();
        
        // Aquí construyo el objeto empleado
        Alumno nuevoAlumno = new Alumno(dni, nombre, apellidos, fechaNacimiento, curso, nota);
        
        guardarAlumnoDB(nuevoAlumno);    
    } // FIN METODO
    
    // METODO para guardar un empleado en la BBDD.
    public void guardarAlumnoDB(Alumno alumno) {
        String sql = "INSERT INTO Alumnos (dni, nombre, apellidos, fechaNacimiento, curso, nota) VALUES (?, ?, ?, ?, ?, ?)";
        Connection con = null;
        PreparedStatement ps = null;
        try {
            con = conectar();
            ps = con.prepareStatement(sql);
            ps.setString(1, alumno.getDni());
            ps.setString(2, alumno.getNombre());
            ps.setString(3, alumno.getApellidos());
            ps.setDate(4, Date.valueOf(alumno.getFechaNacimiento()));
            ps.setString(6, alumno.getCurso());
            ps.setDouble(7, alumno.getNota());
            int resultado = ps.executeUpdate();
            if (resultado > 0) {
                alumnos.add(alumno); // Lo añado a la lista.
                System.out.println("\nEl alumno " + alumno.getNombre() + " ha sido añadido a la base de datos.");
            }
        } catch (SQLException e) {
            System.err.println("Error al añadir alumno: " + e.getMessage());
            System.err.println("Número que representa el error: " + e.getErrorCode());
        } finally {
            cerrarConexion(con, ps, null);
        }
    } // FIN METODO
    
    // METODO que elimina un empleado por nombre y apellidos
    public void eliminarAlumno() {
        System.out.println("Introduce el dni del alumno a eliminar:");
        String dni = sc.nextLine();
        
        // Primero, intento insertar el alumno en AlumnosAprobados
        String sqlInsert = "INSERT INTO AlumnosEliminados SELECT * FROM Alumnos WHERE dni = ?";
        String sqlDelete = "DELETE FROM Alumnos WHERE dni = ?";
        Connection con = null;
        PreparedStatement psInsert = null;
        PreparedStatement psDelete = null;
        
        try {
            con = conectar();
            psInsert = con.prepareStatement(sqlInsert); // Insertar en EmpleadosAntiguos
            psInsert.setString(1, dni);
            int resultadoInsert = psInsert.executeUpdate();
        
            if (resultadoInsert > 0) {
                psDelete = con.prepareStatement(sqlDelete); // Si la inserción fue exitosa, procedo a eliminar
                psDelete.setString(1, dni);
                int resultadoDelete = psDelete.executeUpdate();
                if (resultadoDelete > 0) {
                    alumnos.removeIf(e -> e.getDni().equals(dni));
                    System.out.println("\nEl alumno ha sido eliminado de la base de datos.");
                } else {
                    System.out.println("\nNo se encontró ningún alumno con ese nombre para eliminar.");
                }
            } else {
                System.out.println("\nNo se encontró el alumno o no se pudo insertar en AlumnosAntiguos");
            }
        } catch (SQLException e) {
            System.err.println("Error al eliminar alumno: " + e.getMessage());
            System.err.println("Número que representa el error: " + e.getErrorCode());
        } finally {
            cerrarConexion(con, psInsert, null);
            cerrarConexion(con, psDelete, null); 
        }
    } // FIN METODO
    
    // Método para solicitar la actualización de un empleado
    public void actualizarAlumno() {
        System.out.println("Introduce el dni del alumno a actualizar:");
        String dni = sc.nextLine();
        
        // Obtener nuevo puesto y salario
        System.out.println("Introduce el nuevo curso del alumno:");
        String nuevoCurso = sc.nextLine();
        System.out.println("Introduce la nueva nota del alumno:");
        double nuevaNota;
        while (true) {
            try {
                nuevaNota = Double.parseDouble(sc.nextLine());
                if (nuevaNota >= 0) break;
                System.out.println("La nota debe ser un número positivo. Inténtalo de nuevo.");
            } catch (NumberFormatException e) {
                System.out.println("Por favor, introduce un número válido para el salario.");
            }
        }
        actualizarAlumnoDB(dni, nuevoCurso, nuevaNota);
    } // FIN METODO
    
    // Método para actualizar un empleado en la base de datos
    public void actualizarAlumnoDB(String dni, String nuevoCurso, double nuevaNota) {
        String sql = "UPDATE Alumnos SET curso = ?, nota = ? WHERE dni = ?";
        Connection con = null;
        PreparedStatement ps = null;
        try {
            con = conectar();
            ps = con.prepareStatement(sql);
            ps.setString(1, nuevoCurso);
            ps.setDouble(2, nuevaNota);
            ps.setString(3, dni);
            int resultado = ps.executeUpdate();
            if (resultado > 0) {
                // Actualizo la lista
                alumnos.forEach(empleado -> {
                    if (empleado.getDni().equalsIgnoreCase(dni)) {
                        empleado.setCurso(nuevoCurso);
                        empleado.setNota(nuevaNota);
                    }
                });
                System.out.println("Alumno actualizado correctamente en la base de datos.");
            } else {
                System.out.println("No se encontró ningún alumno con ese DNI para actualizar.");
            }
        } catch (SQLException e) {
            System.err.println("Error al actualizar alumno: " + e.getMessage());
            System.err.println("Número que representa el error: " + e.getErrorCode());
        } finally {
            cerrarConexion(con, ps, null);
        }
    } // FIN METODO
    
    // METODO que busca un empleado por nombre y apellidos
    private static void buscarAlumno() {
        System.out.println("Introduce el dni del alumno:");
        String dni = sc.nextLine();
        
        String sql = "SELECT * FROM Alumnos WHERE dni = ?";
        Connection con = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        try {
            con = conectar();
            ps = con.prepareStatement(sql);
            ps.setString(1, dni);
            rs = ps.executeQuery();
            if (!rs.next()) {
                System.err.println("No se encontró ningún alumno con ese dni.");
            } else {
                do {
                    Alumno alumno = new Alumno(
                    rs.getString("dni"),
                    rs.getString("nombre"),
                    rs.getString("apellidos"),
                    rs.getDate("fechaNacimiento").toLocalDate(),
                    rs.getString("curso"),
                    rs.getDouble("nota")
                    );
                    System.out.println(alumno.toString()); // Muestra los datos del empleado encontrado
                } while (rs.next());
            }
        } catch (SQLException e) {
            System.err.println("Error al buscar alumno: " + e.getMessage());
            System.err.println("Número que representa el error: " + e.getErrorCode());
        } finally {
            cerrarConexion(con, ps, rs);
        }
    } // FIN METODO
    
    // METODO que busca un empleado por nombre y apellidos
    public void mostrarAlumnosAntiguos() {
        String sql = "SELECT * FROM AlumnosEliminados";
        Connection con = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        try {
            con = conectar();
            ps = con.prepareStatement(sql);
            rs = ps.executeQuery();
            System.out.println("\nLista de Alumnos Eliminados:");
            while (rs.next()) {
                    Alumno alumno = new Alumno(
                    rs.getString("dni"),
                    rs.getString("nombre"),
                    rs.getString("apellidos"),
                    rs.getDate("fechaNacimiento").toLocalDate(),
                    rs.getString("curso"),
                    rs.getDouble("nota")
                    );
                    System.out.println(alumno.toString()); // Muestra los datos del empleado encontrado
            }
        } catch (SQLException e) {
            System.err.println("Error al buscar alumno: " + e.getMessage());
            System.err.println("Número que representa el error: " + e.getErrorCode());
        } finally {
            cerrarConexion(con, ps, rs);
        }
    } // FIN METODO
    

    // METODO QUE ORDENA la lista de empleados por fecha de ingreso
    public void ordenadosPorAntigüedad() {
        String sql = "SELECT * FROM Alumnos ORDER BY fechaNacimiento ASC";
        Connection con = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        
        try {
            con = conectar();
            ps = con.prepareStatement(sql);
            rs = ps.executeQuery();
            
            alumnos.clear(); // Limpia la lista antes de añadir elementos
            while (rs.next()) {
                Alumno alumno = new Alumno(
                    rs.getString("dni"),
                    rs.getString("nombre"),
                    rs.getString("apellidos"),
                    rs.getDate("fechaNacimiento").toLocalDate(),
                    rs.getString("curso"),
                    rs.getDouble("nota")
                );
                alumnos.add(alumno);
            }
            // Imprimo los empleados ordenados por antigüedad.
            System.out.println("\nALUMNOS ORDENADOS POR ANTIGÜEDAD:\n");
            for (int i = 0; i < alumnos.size(); i++) {
                System.out.println((i + 1) + "- " + alumnos.get(i).toString());
            }
        } catch (SQLException e) {
            System.err.println("Error al obtener alumnos ordenados por antigüedad: " + e.getMessage());
            System.err.println("Número que representa el error: " + e.getErrorCode());
        } finally {
            cerrarConexion(con, ps, rs);
        }
    }  // FIN METODO
    
    
    // METODO QUE ORDENA la lista de empleados por salario (de mayor a menor)
    public void ordenadosPorSalario() {
        String sql = "SELECT * FROM Alumnos ORDER BY nota DESC";
        Connection con = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        try {
            con = conectar();
            ps = con.prepareStatement(sql);
            rs = ps.executeQuery();
            
            alumnos.clear(); // Limpia la lista antes de añadir elementos
            while (rs.next()) {
                Alumno alumno = new Alumno(
                    rs.getString("dni"),
                    rs.getString("nombre"),
                    rs.getString("apellidos"),
                    rs.getDate("fechaNacimiento").toLocalDate(),
                    rs.getString("curso"),
                    rs.getDouble("nota")
                );
                alumnos.add(alumno);
            }
            // Imprimo los empleados ordenados por salario.
            System.out.println("\nALUMNOS ORDENADOS POR NOTA:\n");
            for (int i = 0; i < alumnos.size(); i++) {
                System.out.println((i + 1) + "- " + alumnos.get(i).toString());
            }
        } catch (SQLException e) {
            System.err.println("Error al obtener alumnos ordenados por nota: " + e.getMessage());
            System.err.println("Número que representa el error: " + e.getErrorCode());
        } finally {
            cerrarConexion(con, ps, rs);
        }
    } // FIN METODO
    
    // METODO QUE ORDENA la lista de empleados por apellido
    public void ordenadosPorApellido() {
        String sql = "SELECT * FROM Alumnos ORDER BY apellidos ASC";
        Connection con = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        try {
            con = conectar();
            ps = con.prepareStatement(sql);
            rs = ps.executeQuery();
            
            alumnos.clear(); // Limpia la lista antes de añadir elementos
            while (rs.next()) {
                Alumno alumno = new Alumno(
                    rs.getString("dni"),
                    rs.getString("nombre"),
                    rs.getString("apellidos"),
                    rs.getDate("fechaNacimiento").toLocalDate(),
                    rs.getString("curso"),
                    rs.getDouble("nota")
                );
                alumnos.add(alumno);
            }
            // Imprimo los empleados ordenados por apellidos.
            System.out.println("\nALUMNOS ORDENADOS POR APELLIDOS:\n");
            for (int i = 0; i < alumnos.size(); i++) {
                System.out.println((i + 1) + "- " + alumnos.get(i).toString());
            }
        } catch (SQLException e) {
            System.err.println("Error al obtener alumnos ordenados por apellidos: " + e.getMessage());
            System.err.println("Número que representa el error: " + e.getErrorCode());
        } finally {
            cerrarConexion(con, ps, rs);
        }
    } // FIN METODO
    
    // METODO para calcular el gasto total sumando los salarios de todos los empleados.
    public double calcularMedia() {
        String sql = "SELECT AVG(nota) AS mediaNotas FROM Alumnos"; // Consulta SQL para calcular la suma de los salarios
        double media = 0;
        Connection con = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        try {
            con = conectar();
            ps = con.prepareStatement(sql);
            rs = ps.executeQuery();
            if (rs.next()) {
                media = rs.getDouble("mediaNotas"); // Obtiene el total de los salarios desde la base de datos
                System.out.println("\nLa media de las notas de los alumnos es: " + media);
            } else {
                System.out.println("No fue posible calcular la media de los alumnos.");
            }
        } catch (SQLException e) {
            System.err.println("Error al calcular la media de los alumnos: " + e.getMessage());
            System.err.println("Número que representa el error: " + e.getErrorCode());
        } finally {
            cerrarConexion(con, ps, rs);
        }
        return media;
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
    private static double obtenerNota() {
        double nota;
        do {
            System.out.println("Introduce la nota del Alumno:");
            try {
                nota = Double.parseDouble(sc.nextLine());
                if (nota > 0) {
                    return nota;
                } else {
                    System.err.println("La nota debe ser mayor que 0. Inténtalo de nuevo.");
                }
            } catch (NumberFormatException e) {
                System.err.println("ERROR. Se esperaba un número para la nota. Inténtalo de nuevo.");
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
                          "\n1- Añadir Alumno" +
                          "\n2- Eliminar Alumno" +
                          "\n3- Actualizar Alumno" +
                          "\n4- Buscar Alumno" +
                          "\n5- Imprimir alumnos ordenados:" +
                          "\n6- Calcular nota media de los alumnos" +
                          "\n7- Mostrar alumnos antiguos" +
                          "\n8- Salir" +
                          "\n---------------------------------------------------------" +
                          "\nSelecciona una opción: ";
            
            System.out.print(menu);
            opcion = sc.nextInt();
            sc.nextLine(); // Salto de línea
            
                switch (opcion) {
                    case 1:
                        añadirAlumno();
                        break;
                    case 2:
                        eliminarAlumno();
                        break;
                    case 3:
                        actualizarAlumno();
                        break;
                    case 4:
                        buscarAlumno();
                        break;
                    case 5:
                        String opcionOrdenamiento;
                        do {
                            String subMenu = "\nElige una opción:\n" +
                                            "a) Por antigüedad\n" +
                                            "b) Por nota\n" +
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
                        calcularMedia();
                        break;
                    case 7:
                        mostrarAlumnosAntiguos();
                        break;
                    case 8:
                        System.out.println("\nSaliendo...");
                        break;
                    default:
                        System.err.println("\nOpción no válida. Introduce un número entre 1 y 8.");
                        break;
                } // FIN SWITCH
            } catch (InputMismatchException e) {
                System.err.println("ERROR. Entrada no válida, inténtalo de nuevo.");
                sc.nextLine(); // Salto de línea
            } // FIN TRY-CATCH
        } while (opcion != 8); // FIN DO-WHILE
    } // FIN METODO
} // FIN CLASE
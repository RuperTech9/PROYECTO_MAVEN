
package instituto;

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
public class Instituto {
    // ArrayList de Objetos
    static ArrayList<Alumno> alumnos = new ArrayList<>();
    static Scanner sc = new Scanner(System.in);
    
    
    // METODOS JDBC
    private static Connection conectar() {
        String url = "jdbc:mysql://192.168.80.152:3306/InstitutoDB";
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
                    rs.getString("codigo"),
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
    private static void añadirAlumno() {
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
    private static void guardarAlumnoDB(Alumno alumno) {
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
            ps.setString(5, alumno.getCurso());
            ps.setDouble(6, alumno.getNota());
            int resultado = ps.executeUpdate();
            if (resultado > 0) {
                alumnos.add(alumno); // Lo añado a la lista.
                System.out.println("\nEl alumno " + alumno.getNombre() + " ha sido añadido a la base de datos.");
            }
        } catch (SQLException e) {
            System.err.println("Error al añadir empleado: " + e.getMessage());
            System.err.println("Número que representa el error: " + e.getErrorCode());
        } finally {
            cerrarConexion(con, ps, null);
        }
    } // FIN METODO
    
    // METODO que elimina un empleado por nombre y apellidos
    private static void eliminarAlumno() {
        System.out.println("Introduce el dni del empleado a eliminar:");
        String dni = sc.nextLine();
        
        // Primero, intento insertar el empleado en EmpleadosAntiguos
        String sqlInsert = "INSERT INTO AlumnosAntiguos (dni, nombre, apellidos, fechaNacimiento, curso, nota, fechaBaja) SELECT dni, nombre, apellidos, fechaNacimiento, curso, nota, ? FROM Alumnos WHERE dni = ?";
        String sqlDelete = "DELETE FROM Alumnos WHERE dni = ?";
        Connection con = null;
        PreparedStatement psInsert = null;
        PreparedStatement psDelete = null;
        try {
            con = conectar();
            psInsert = con.prepareStatement(sqlInsert);
            psInsert.setDate(1, Date.valueOf(LocalDate.now()));
            psInsert.setString(2, dni);
            int resultadoInsert = psInsert.executeUpdate();
            if (resultadoInsert > 0) {
                psDelete = con.prepareStatement(sqlDelete);
                psDelete.setString(1, dni);
                int resultadoDelete = psDelete.executeUpdate();
                if (resultadoDelete > 0) {
                    alumnos.removeIf(e -> e.getDni().equals(dni));
                }
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
    private static void actualizarAlumno() {
        System.out.println("Introduce el dni del alumno a actualizar:");
        String dni = sc.nextLine();
        
        // Obtener nuevo puesto y salario
        System.out.println("Introduce el nuevo curso del empleado:");
        String nuevoCurso = sc.nextLine();
        System.out.println("Introduce el nuevo salario del empleado:");
        double nuevaNota;
        while (true) {
            try {
                nuevaNota = Double.parseDouble(sc.nextLine());
                if (nuevaNota >= 0) break;
                System.out.println("El salario debe ser un número positivo. Inténtalo de nuevo.");
            } catch (NumberFormatException e) {
                System.out.println("Por favor, introduce un número válido para el salario.");
            }
        }
        actualizarAlumnoDB(dni, nuevoCurso, nuevaNota);
    } // FIN METODO
    
    // Método para actualizar un empleado en la base de datos
    private static void actualizarAlumnoDB(String dni, String nuevoCurso, double nuevaNota) {
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
                System.out.println("No se encontró ningún alumno con ese dni para actualizar.");
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
                System.err.println("No se encontró ningún empleado con ese codigo.");
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
                    System.out.println(alumno.toString()); // Muestra los datos del alumno encontrado
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
    private static void mostrarAlumnosAntiguos() {
        String sql = "SELECT * FROM AlumnosAntiguos";
        Connection con = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        try {
            con = conectar();
            ps = con.prepareStatement(sql);
            rs = ps.executeQuery();
            System.out.println("\nLista de alumnos Antiguos:");
            while (rs.next()) {
                AlumnoAntiguo alumnoAntiguo = new AlumnoAntiguo(
                    rs.getString("dni"),
                    rs.getString("nombre"),
                    rs.getString("apellidos"),
                    rs.getDate("fechaNacimiento").toLocalDate(),
                    rs.getString("curso"),
                    rs.getDouble("nota"),
                    rs.getDate("fechaBaja").toLocalDate()
                );
                System.out.println(alumnoAntiguo.toString());
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
    private static void ordenadosPorSalario() {
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
            System.out.println("\nALUMNOS ORDENADOS POR SALARIO:\n");
            for (int i = 0; i < alumnos.size(); i++) {
                System.out.println((i + 1) + "- " + alumnos.get(i).toString());
            }
        } catch (SQLException e) {
            System.err.println("Error al obtener alumnos ordenados por salario: " + e.getMessage());
            System.err.println("Número que representa el error: " + e.getErrorCode());
        } finally {
            cerrarConexion(con, ps, rs);
        }
    } // FIN METODO
    
    // METODO QUE ORDENA la lista de empleados por apellido
    private static void ordenadosPorApellido() {
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
            System.out.println("\nEMPLEADOS ORDENADOS POR APELLIDOS:\n");
            for (int i = 0; i < alumnos.size(); i++) {
                System.out.println((i + 1) + "- " + alumnos.get(i).toString());
            }
        } catch (SQLException e) {
            System.err.println("Error al obtener empleados ordenados por apellidos: " + e.getMessage());
            System.err.println("Número que representa el error: " + e.getErrorCode());
        } finally {
            cerrarConexion(con, ps, rs);
        }
    } // FIN METODO
    
    // METODO para calcular el gasto total sumando los salarios de todos los empleados.
    private static void calcularNotaTotal() {
        String sql = "SELECT SUM(nota) AS totalNotas FROM Alumnos"; // Consulta SQL para calcular la suma de los salarios
        Connection con = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        try {
            con = conectar();
            ps = con.prepareStatement(sql);
            rs = ps.executeQuery();
            if (rs.next()) {
                double gastoTotal = rs.getDouble("totalNotas"); // Obtiene el total de los salarios desde la base de datos
                System.out.println("\nEl total en notas de los alumnos es: " + gastoTotal);
            } else {
                System.out.println("No fue posible calcular la nota total.");
            }
        } catch (SQLException e) {
            System.err.println("Error al calcular la nota total: " + e.getMessage());
            System.err.println("Número que representa el error: " + e.getErrorCode());
        } finally {
            cerrarConexion(con, ps, rs);
        }
    } // FIN METODO
    
    private static void mostrarSalarioMaximoYMinimo() {
        String sql = "SELECT MIN(nota) AS notaMinima, MAX(nota) AS notaMaxima FROM Alumnos"; // Consulta SQL para obtener el salario mínimo y máximo
        Connection con = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        try {
            con = conectar();
            ps = con.prepareStatement(sql);
            rs = ps.executeQuery();
            if (rs.next()) {
                double notaMinima = rs.getDouble("notaMinima");
                double notaMaxima = rs.getDouble("notaMaxima");
                System.out.println("\nLa nota mínima de los alumnos es: " + notaMinima);
                System.out.println("La nota máxima de los alumnos es: " + notaMaxima);
            } else {
                System.out.println("No fue posible obtener las notas mínimas y máximas.");
            }
        } catch (SQLException e) {
            System.err.println("Error al obtener las notas mínimas y máximas: " + e.getMessage());
            System.err.println("Número que representa el error: " + e.getErrorCode());
        } finally {
            cerrarConexion(con, ps, rs);
        }
    } // FIN METODO
    
    private static void mostrarSalarioMedio() {
        String sql = "SELECT AVG(nota) AS notaMedia FROM Alumnos"; // Consulta SQL para calcular la media de los salarios
        Connection con = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        try {
            con = conectar();
            ps = con.prepareStatement(sql);
            rs = ps.executeQuery();
            if (rs.next()) {
                double notaMedia = rs.getDouble("notaMedia");
                System.out.println("\nLa nota media de los alumnos es: " + notaMedia);
            } else {
                System.out.println("No fue posible calcular la nota media.");
            }
        } catch (SQLException e) {
            System.err.println("Error al calcular la nota media: " + e.getMessage());
            System.err.println("Número que representa el error: " + e.getErrorCode());
        } finally {
            cerrarConexion(con, ps, rs);
        }
    } // FIN METODO
    
    private static void mostrarAlumnosPorCurso() {
        System.out.println("Introduce el curso para ver los alumnos:");
        String curso = sc.nextLine();

        String sql = "SELECT * FROM Alumnos WHERE curso = ?";
        Connection con = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        try {
            con = conectar();
            ps = con.prepareStatement(sql);
            ps.setString(1, curso);
            rs = ps.executeQuery();
            System.out.println("\nLista de alumnos en el curso " + curso + ":");
            while (rs.next()) {
                Alumno alumno = new Alumno(
                    rs.getString("dni"),
                    rs.getString("nombre"),
                    rs.getString("apellidos"),
                    rs.getDate("fechaNacimiento").toLocalDate(),
                    rs.getString("curso"),
                    rs.getDouble("nota")
                );
                System.out.println(alumno.toString());
            }
        } catch (SQLException e) {
            System.err.println("Error al buscar alumnos por curso: " + e.getMessage());
            System.err.println("Número que representa el error: " + e.getErrorCode());
        } finally {
            cerrarConexion(con, ps, rs);
        }
    }
    
    private static void mostrarAlumnosPorRangoNotas() {
        System.out.println("Introduce la nota mínima:");
        double notaMinima = obtenerNota();
        System.out.println("Introduce la nota máxima:");
        double notaMaxima = obtenerNota();

        String sql = "SELECT * FROM Alumnos WHERE nota BETWEEN ? AND ?";
        Connection con = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        try {
            con = conectar();
            ps = con.prepareStatement(sql);
            ps.setDouble(1, notaMinima);
            ps.setDouble(2, notaMaxima);
            rs = ps.executeQuery();
            System.out.println("\nLista de alumnos con notas entre " + notaMinima + " y " + notaMaxima + ":");
            while (rs.next()) {
                Alumno alumno = new Alumno(
                    rs.getString("dni"),
                    rs.getString("nombre"),
                    rs.getString("apellidos"),
                    rs.getDate("fechaNacimiento").toLocalDate(),
                    rs.getString("curso"),
                    rs.getDouble("nota")
                );
                System.out.println(alumno.toString());
            }
        } catch (SQLException e) {
            System.err.println("Error al buscar alumnos por rango de notas: " + e.getMessage());
            System.err.println("Número que representa el error: " + e.getErrorCode());
        } finally {
            cerrarConexion(con, ps, rs);
        }
    }

    private static void contarAlumnosPorCurso() {
        String sql = "SELECT curso, COUNT(*) AS cantidad FROM Alumnos GROUP BY curso";
        Connection con = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        try {
            con = conectar();
            ps = con.prepareStatement(sql);
            rs = ps.executeQuery();
            System.out.println("\nCantidad de alumnos por curso:");
            while (rs.next()) {
                String curso = rs.getString("curso");
                int cantidad = rs.getInt("cantidad");
                System.out.println("Curso: " + curso + " - Cantidad de alumnos: " + cantidad);
            }
        } catch (SQLException e) {
            System.err.println("Error al contar alumnos por curso: " + e.getMessage());
            System.err.println("Número que representa el error: " + e.getErrorCode());
        } finally {
            cerrarConexion(con, ps, rs);
        }
    }
    
    private static void mostrarAlumnosPorRangoFechaNacimiento() {
        LocalDate fechaInicio = obtenerFecha("Introduce la fecha de inicio (DD/MM/YYYY):");
        LocalDate fechaFin = obtenerFecha("Introduce la fecha de fin (DD/MM/YYYY):");

        String sql = "SELECT * FROM Alumnos WHERE fechaNacimiento BETWEEN ? AND ?";
        Connection con = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        try {
            con = conectar();
            ps = con.prepareStatement(sql);
            ps.setDate(1, Date.valueOf(fechaInicio));
            ps.setDate(2, Date.valueOf(fechaFin));
            rs = ps.executeQuery();
            System.out.println("\nLista de alumnos nacidos entre " + fechaInicio + " y " + fechaFin + ":");
            while (rs.next()) {
                Alumno alumno = new Alumno(
                    rs.getString("dni"),
                    rs.getString("nombre"),
                    rs.getString("apellidos"),
                    rs.getDate("fechaNacimiento").toLocalDate(),
                    rs.getString("curso"),
                    rs.getDouble("nota")
                );
                System.out.println(alumno.toString());
            }
        } catch (SQLException e) {
            System.err.println("Error al buscar alumnos por rango de fecha de nacimiento: " + e.getMessage());
            System.err.println("Número que representa el error: " + e.getErrorCode());
        } finally {
            cerrarConexion(con, ps, rs);
        }
    }
    
    private static void actualizarFechaBajaAlumnoAntiguo() {
        System.out.println("Introduce el dni del alumno antiguo a actualizar la fecha de baja:");
        String dni = sc.nextLine();
        LocalDate nuevaFechaBaja = obtenerFecha("Introduce la nueva fecha de baja (DD/MM/YYYY):");

        String sql = "UPDATE AlumnosAntiguos SET fechaBaja = ? WHERE dni = ?";
        Connection con = null;
        PreparedStatement ps = null;
        try {
            con = conectar();
            ps = con.prepareStatement(sql);
            ps.setDate(1, Date.valueOf(nuevaFechaBaja));
            ps.setString(2, dni);
            int resultado = ps.executeUpdate();
            if (resultado > 0) {
                System.out.println("Fecha de baja actualizada correctamente en la base de datos.");
            } else {
                System.out.println("No se encontró ningún alumno antiguo con ese dni para actualizar.");
            }
        } catch (SQLException e) {
            System.err.println("Error al actualizar la fecha de baja del alumno antiguo: " + e.getMessage());
            System.err.println("Número que representa el error: " + e.getErrorCode());
        } finally {
            cerrarConexion(con, ps, null);
        }
    }
    
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
            System.out.println("Introduce la nota del alumno:");
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
            String menu = "\n               GESTIÓN DE ALUMNOS" +
                          "\n---------------------------------------------------------" +
                          "\n1- Añadir Alumno" +
                          "\n2- Eliminar Alumno" +
                          "\n3- Actualizar Alumno" +
                          "\n4- Buscar Empleado" +
                          "\n5- Imprimir alumnos ordenados:" +
                          "\n6- Calcular nota total de los alumnos" +
                          "\n7- Mostrar nota máxima y mínima" +
                          "\n8- Mostrar nota media" +
                          "\n9- Mostrar alumnos antiguos" +
                          "\n10- Mostrar Alumnos por curso" +
                          "\n11- Mostrar por rango de notas" +
                          "\n12- Contar Alumnos por curso" +
                          "\n13- Mostrar por rango de fecha" +
                          "\n14- Actualizar fecha de baja alumno antiguo" +
                          "\n0- Salir" +
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
                        calcularNotaTotal();
                        break;
                    case 7:
                        mostrarSalarioMaximoYMinimo();
                        break;
                    case 8:
                        mostrarSalarioMedio();
                        break;
                    case 9:
                        mostrarAlumnosAntiguos();
                        break;
                    case 10:
                        mostrarAlumnosPorCurso();
                        break;
                    case 11:
                        mostrarAlumnosPorRangoNotas();
                        break;
                    case 12:
                        contarAlumnosPorCurso();
                        break;
                    case 13:
                        mostrarAlumnosPorRangoFechaNacimiento();
                        break;
                    case 14:
                        actualizarFechaBajaAlumnoAntiguo();
                        break;
                    case 0:
                        System.out.println("\nSaliendo...");
                        break;
                    default:
                        System.err.println("\nOpción no válida. Introduce un número entre 0 y 9.");
                        break;
                } // FIN SWITCH
            } catch (InputMismatchException e) {
                System.err.println("ERROR. Entrada no válida, inténtalo de nuevo.");
                sc.nextLine(); // Salto de línea
            } // FIN TRY-CATCH
        } while (opcion != 0); // FIN DO-WHILE
    } // FIN METODO
    
    
    
} // FIN CLASE
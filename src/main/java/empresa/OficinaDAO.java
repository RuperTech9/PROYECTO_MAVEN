
package empresa;

import java.sql.*;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.InputMismatchException;
import java.util.Iterator;
import java.util.Scanner;

/**
 *
 * @author Ruper
 */
public class OficinaDAO {
    static ArrayList<Oficina> oficinas = new ArrayList<>();
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

    public void cargarOficinasDB() {
        String sql = "SELECT * FROM Oficinas";
        try (Connection con = conectar();
             Statement st = con.createStatement();
             ResultSet rs = st.executeQuery(sql)) {
            while (rs.next()) {
                Oficina oficina = new Oficina(
                    rs.getInt("oficina"),
                    rs.getString("ciudad"),
                    rs.getDouble("superficie"),
                    rs.getDouble("ventas")
                );
                oficinas.add(oficina);
            }
        }catch (SQLException e) {
                System.out.println("Error al cargar oficinas: " + e.getMessage());
        }
    }
    
    // METODO para añadir un empleado.
    private static void añadirOficina() {
        System.out.println("Introduce el codigo de la oficina: ");
        int oficina = sc.nextInt();
        System.out.println("Introduce la ciudad de la oficina:");
        String ciudad = sc.nextLine();
        System.out.println("Introduce la superficie de la oficina:");
        Double superficie = sc.nextDouble();
        double ventas = 0; // Inicializo variable.
        do {
            System.out.println("Introduce las ventas de la oficina:");
            String strVentas = sc.nextLine();
            try {
                ventas = Double.parseDouble(strVentas);
                if (ventas > 0) {
                    break; // Salgo si el salario es correcto y positivo.
                } else {
                    System.err.println("Las ventas debe ser mayor que 0. Intentalo de nuevo.");
                } // FIN IF
            } catch (InputMismatchException e) {
                System.err.println("ERROR. Se esperaba un número para las ventas. Intentalo de nuevo.");
            } // FIN TRY-CATCH
        } while (true); // FIN DO-WHILE
    
        
        // Aquí construyo el objeto empleado
        Oficina nuevaOficina= new Oficina(oficina, ciudad, superficie, ventas);

        añadirOficinaDB(nuevaOficina);    
    }
    
    private static void añadirOficinaDB(Oficina oficina) {
        Connection con = null;
        PreparedStatement ps = null;
        try {
            con = conectar();
            String sql = "INSERT INTO Oficinas (oficina, ciudad, superficie, ventas) VALUES (?, ?, ?, ?)";
            ps = con.prepareStatement(sql);
            ps.setInt(1, oficina.getOficina());
            ps.setString(2, oficina.getCiudad());
            ps.setDouble(3, oficina.getSuperficie());
            ps.setDouble(4, oficina.getVentas());
            int result = ps.executeUpdate();
            if (result > 0) {
                oficinas.add(oficina);
                System.out.println("\nLa oficina de " + oficina.getCiudad() + " ha sido añadida a la base de datos y a la lista.");
            }
        } catch (SQLException e) {
            System.out.println("Error al añadir empleado: " + e.getMessage());
        } finally {
            cerrarConexion(con, ps, null);
        }
    }
    
    // METODO que elimina un empleado por nombre y apellidos
    private static void eliminarOficina() {
        System.out.println("Introduce la ciudad de la oficina a eliminar:");
        String ciudad = sc.nextLine();
        System.out.println("Introduce el codigo de la oficina a eliminar:");
        int oficina = sc.nextInt();
        
        eliminarOficinaDB(ciudad, oficina);
    } // FIN METODO
    
    private static void eliminarOficinaDB(String ciudad, int oficina) {
        Connection con = null;
        PreparedStatement ps = null;
        try {
            con = conectar();
            String sql = "DELETE FROM Oficinas WHERE ciudad = ? AND oficina = ?";
            ps = con.prepareStatement(sql);
            ps.setString(1, ciudad);
            ps.setInt(2, oficina);
            int result = ps.executeUpdate();
            if (result > 0) {
                oficinas.removeIf(o -> o.getCiudad().equalsIgnoreCase(ciudad) && o.getOficina() == oficina);
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
    private static void actualizarOficina() {
        System.out.println("Introduce la ciudad de la oficina a eliminar:");
        String ciudad = sc.nextLine();
        System.out.println("Introduce el codigo de la oficina a eliminar:");
        int oficina = sc.nextInt();
        
        // Obtener nuevo puesto y salario
        System.out.println("Introduce la nueva ciudad de la oficina:");
        String nuevaCiudad = sc.nextLine();
        System.out.println("Introduce las ventas actualizadas:");
        double nuevaVentas;
        while (true) {
            try {
                nuevaVentas = Double.parseDouble(sc.nextLine());
                if (nuevaVentas >= 0) break;
                System.out.println("Las ventas debe ser un número positivo. Inténtalo de nuevo.");
            } catch (NumberFormatException e) {
                System.out.println("Por favor, introduce un número válido para las ventas.");
            }
        }
        actualizarOficinaDB(ciudad, oficina, nuevaCiudad, nuevaVentas);
    }
    // Método para actualizar un empleado en la base de datos
    private static void actualizarOficinaDB(String ciudad, int oficina, String nuevaCiudad, double nuevaVentas) {
        Connection con = null;
        PreparedStatement ps = null;
        try {
            con = conectar();
            String sql = "UPDATE Empleados SET ciudad = ?, ventas = ? WHERE ciudad = ? AND oficina = ?";
            ps = con.prepareStatement(sql);
            ps.setString(1, nuevaCiudad);
            ps.setDouble(2, nuevaVentas);
            ps.setString(3, ciudad);
            ps.setInt(4, oficina);
            int resultado = ps.executeUpdate();
            if (resultado > 0) {
                // Actualizar la lista en memoria
                oficinas.forEach(empleado -> {
                    if (empleado.getCiudad().equalsIgnoreCase(ciudad) && empleado.getOficina() == oficina) {
                        empleado.setCiudad(nuevaCiudad);
                        empleado.setVentas(nuevaVentas);
                    }
                });
                System.out.println("Oficina actualizada correctamente en la base de datos y en la lista.");
            } else {
                System.out.println("No se encontró ningúna oficina con esa ciudad y numero de oficina para actualizar.");
            }
        } catch (SQLException e) {
            System.out.println("Error al actualizar oficina: " + e.getMessage());
        } finally {
            cerrarConexion(con, ps, null);
        }
    }
    
    // METODO que busca un empleado por nombre y apellidos
    private static void buscarOficina() {
        System.out.println("Introduce la ciudad de la oficina a eliminar:");
        String ciudad = sc.nextLine();
        System.out.println("Introduce el codigo de la oficina a eliminar:");
        int oficinaBuscar = sc.nextInt();
        
        boolean encontrado = false; // Inicializo variable.
        // Imprimo los empleados.
        Iterator<Oficina> iterator = oficinas.iterator();
        while (iterator.hasNext()) { // Mientras haya más elementos en la lista...
            Oficina oficina = iterator.next(); // Recorro la Lista de Empleados.
            if (oficina.getCiudad().equalsIgnoreCase(ciudad) && oficina.getOficina() == oficinaBuscar) {
                System.out.println(oficina.toString()); // Muestro los datos del empleado con el nombre especificado.
                encontrado = true;
                // El bucle continuará hasta el final de la lista de empleados por si hay 2 usuarios con el mismo nombre y apellidos.
            } // FIN IF
        } // FIN WHILE
        if (!encontrado) {
            System.err.println("No se encontró ninguna oficina con ese código y ciudad.");
        } // FIN IF
    } // FIN METODO
    
    // METODO QUE ORDENA la lista de empleados por salario (de mayor a menor)
    private static void ordenarVentas() {
        oficinas.sort(Comparator.comparingDouble(Oficina::getVentas).reversed());
        // Imprimo los empleados ya ordenados
        Iterator<Oficina> itVentas = oficinas.iterator();
        String ordenVentas = "\nOFICINAS ORDENADAS POR VENTAS:\n";
        int contador = 1; // Para numerar los empleados.
        while (itVentas.hasNext()) {
            Oficina oficina = itVentas.next();
            ordenVentas += contador + "- " + oficina.toString() + "\n";
            contador++; // Para el siguiente empleado.
        }
        System.out.println(ordenVentas);
    } // FIN METODO
    
    // METODO QUE ORDENA la lista de empleados por apellido
    private static void ordenarCiudad() {
        oficinas.sort(Comparator.comparing(Oficina::getCiudad));
        // Imprimo los empleados ya ordenados
        Iterator<Oficina> itCiudad = oficinas.iterator();
        String ordenCiudad = "\nOFICINAS ORDENADAS POR CIUDAD:\n";
        int contador = 1; // Para numerar los empleados.
        while (itCiudad.hasNext()) {
            Oficina oficina = itCiudad.next();
            ordenCiudad += contador + "- " + oficina.toString() + "\n";
            contador++; // Para el siguiente empleado.
        }
        System.out.println(ordenCiudad);
    } // FIN METODO
    
    // METODO para calcular el gasto total sumando los salarios de todos los empleados.
    private static void calcularVentasTotal() {
        double ventasTotal = 0; // Inicializo variable.

        for (Oficina oficina : oficinas) {
            ventasTotal += oficina.getVentas(); // Sumo el salario del empleado actual al total.
        } // FIN FOR
        System.out.println("\nLas ventas totales de las oficinas es: " + ventasTotal);
    } // FIN METODO
    
    // METODO para mostrar el menú
    public void mostrarMenu() {
        int opcion = 0;
        do {     
            try {
            String menu = "\n               GESTIÓN DE EMPLEADOS" +
                          "\n---------------------------------------------------------" +
                          "\n1- Añadir Oficina" +
                          "\n2- Eliminar Oficina" +
                          "\n3- Actualizar Oficina" +
                          "\n4- Buscar Oficina por ciudad" +
                          "\n5- Imprimir Oficinas ordenadas:" +
                          "\n6- Calcular total de ventas" +
                          "\n7- Salir" +
                          "\n---------------------------------------------------------" +
                          "\nSelecciona una opción: ";
            
            System.out.print(menu);
            opcion = sc.nextInt();
            sc.nextLine(); // Salto de línea
            
                switch (opcion) {
                    case 1:
                        añadirOficina();
                        break;
                    case 2:
                        eliminarOficina();
                        break;
                    case 3:
                        actualizarOficina();
                        break;
                    case 4:
                        buscarOficina();
                        break;
                    case 5:
                        String opcionOrdenamiento;
                        do {
                            String subMenu = "\nElige una opción:\n" +
                                            "a) Por ciudad\n" +
                                            "b) Por ventas\n" +
                                            "s) Volver al menú principal";
                            System.out.println(subMenu);
                            opcionOrdenamiento = sc.nextLine();
                            
                            // Si presiono cancelar o elijo 's', rompo el bucle y vuelvo al menú principal.
                            if (opcionOrdenamiento == null || opcionOrdenamiento.equalsIgnoreCase("s")) {
                                break;
                            } // FIN IF
                            switch (opcionOrdenamiento.toLowerCase()) {
                                case "a":
                                    ordenarCiudad();
                                    break;
                                case "b":
                                    ordenarVentas();
                                    break;
                                default:
                                    System.err.println("Opción no válida. Introduce 'a', 'b', 'c' o 's' para volver.");
                            } // FIN SWITCH
                            
                        } while (true); // FIN DO-WHILE
                        break;
                    case 6:
                        calcularVentasTotal();
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
    
    // Método para mostrar las oficinas de una ciudad específica
    public static void mostrarOficinasPorCiudad() {
        System.out.println("Introduce el nombre de la ciudad:");
        String ciudad = sc.nextLine().trim();
        boolean encontrada = false;

        for (Oficina oficina : oficinas) {
            if (oficina.getCiudad().equalsIgnoreCase(ciudad)) {
                System.out.println(oficina);
                encontrada = true;
            }
        }

        if (!encontrada) {
            System.out.println("No hay oficinas registradas en " + ciudad + ".");
        }
    }
    
    // Método para mostrar oficinas por superficie mínima
    public static void mostrarOficinasPorSuperficie() {
        System.out.print("Introduce la superficie mínima en metros cuadrados: ");
        double minSuperficie = sc.nextDouble();

        boolean encontrado = false;
        System.out.println("Oficinas con una superficie mayor a " + minSuperficie + " metros cuadrados:");
        for (Oficina oficina : oficinas) {
            if (oficina.getSuperficie() > minSuperficie) {
                System.out.println(oficina);
                encontrado = true;
            }
        }

        if (!encontrado) {
            System.out.println("No se encontraron oficinas con una superficie superior a la especificada.");
        }
    }
} // FIN CLASE

package empleados;

import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.InputMismatchException;
import java.util.Scanner;

/**
 *
 * @author alumno
 */
public class ESTRUCTURA {
    static ArrayList<Empleado> empleados = new ArrayList<>();
    static Scanner sc = new Scanner(System.in);

    // Método para conectar a la base de datos
    private static Connection conectar() throws SQLException {
        // TODO: Implementar la conexión a la base de datos
        return null;
    }

    public void cargarEmpleadosDB() {
        // TODO: Implementar la lógica para cargar empleados desde la base de datos
    }

    private static void añadirEmpleado() {
        // TODO: Implementar la lógica para añadir un nuevo empleado
    }

    private static void guardarEmpleadoDB(Empleado empleado) {
        // TODO: Implementar la lógica para guardar un empleado en la base de datos
    }

    private static void eliminarEmpleado() {
        // TODO: Implementar la lógica para eliminar un empleado
    }

    private static void actualizarEmpleado() {
        // TODO: Implementar la lógica para solicitar la actualización de un empleado
    }

    private static void actualizarEmpleadoDB(String codigo, String nuevoPuesto, double nuevoSalario) {
        // TODO: Implementar la lógica para actualizar un empleado en la base de datos
    }

    private static void buscarEmpleado() {
        // TODO: Implementar la lógica para buscar un empleado
    }

    private static void mostrarEmpleadosAntiguos() {
        // TODO: Implementar la lógica para mostrar empleados antiguos
    }

    private static void ordenadosPorAntigüedad() {
        // TODO: Implementar la lógica para ordenar empleados por antigüedad
    }

    private static void ordenadosPorSalario() {
        // TODO: Implementar la lógica para ordenar empleados por salario
    }

    private static void ordenadosPorApellido() {
        // TODO: Implementar la lógica para ordenar empleados por apellido
    }

    private static void calcularGastoTotal() {
        // TODO: Implementar la lógica para calcular el gasto total de salarios
    }

    private static void mostrarSalarioMaximoYMinimo() {
        // TODO: Implementar la lógica para mostrar el salario máximo y mínimo
    }

    private static void mostrarSalarioMedio() {
        // TODO: Implementar la lógica para mostrar el salario medio
    }

    private static void mostrarEmpleadosConIngresoAnteriorA() {
        // TODO: Implementar la lógica para mostrar empleados con ingreso anterior a un año específico
    }

    private static LocalDate obtenerFecha(String mensaje) {
        // TODO: Implementar la lógica para obtener una fecha desde la entrada del usuario
        return null;
    }

    private static double obtenerSalario() {
        // TODO: Implementar la lógica para obtener un salario desde la entrada del usuario
        return 0;
    }

    private static void cerrarConexion(Connection con, PreparedStatement ps, ResultSet rs) {
        // TODO: Implementar la lógica para cerrar la conexión a la base de datos y manejar excepciones
    }

    // Método para mostrar el menú
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
                        // TODO: Implementar submenú para ordenamiento
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
                }
            } catch (InputMismatchException e) {
                System.err.println("ERROR. Entrada no válida, inténtalo de nuevo.");
                sc.nextLine(); // Salto de línea
            }
        } while (opcion != 0);
    }
}
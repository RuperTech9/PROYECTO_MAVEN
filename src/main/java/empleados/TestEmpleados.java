
package empleados;

/**
 *
 * @author Ruper
 */
public class TestEmpleados {
    public static void main(String[] args){
        // Objeto EmpleadosEmpresaV0
        EmpleadosEmpresa empresa = new EmpleadosEmpresa();
        empresa.cargarEmpleadosDesdeDB();
        empresa.mostrarMenu();
    } // FIN MAIN
}

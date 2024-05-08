
package empresa;

/**
 *
 * @author Ruper
 */
public class TestEmpleados {
    public static void main(String[] args){
        // Objeto EmpleadosEmpresaV0
        EmpleadoDAO empresa = new EmpleadoDAO();
        empresa.cargarEmpleadosDesdeDB();
        empresa.mostrarMenu();
    } // FIN MAIN
}

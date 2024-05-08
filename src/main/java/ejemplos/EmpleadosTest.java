
package ejemplos;

/**
 *
 * @author Ruper
 */
public class EmpleadosTest {
    public static void main(String[] args){
        EmpleadosEmpresa1 empresa = new EmpleadosEmpresa1();
        empresa.cargarEmpleadosDesdeDB();
        empresa.mostrarMenu();
    } // FIN MAIN
}

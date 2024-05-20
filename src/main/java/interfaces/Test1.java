
package interfaces;

/**
 *
 * @author Ruper
 */
public class Test1 {
    public static void main(String[] args){
        // Objeto EmpleadosEmpresaV0
        EmpleadosEmpresa1 empresa = new EmpleadosEmpresa1();
        empresa.cargarEmpleadosDB();
        empresa.mostrarMenu();
    } // FIN MAIN
}

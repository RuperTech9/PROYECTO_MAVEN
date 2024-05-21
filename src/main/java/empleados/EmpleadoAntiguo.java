
package empleados;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

/**
 *
 * @author Ruper
 */
public class EmpleadoAntiguo extends Empleado {
    private LocalDate fechaBaja;

    public EmpleadoAntiguo(String codigo, String nombre, String apellidos, LocalDate fechaNacimiento, LocalDate fechaIngreso, String puesto, double salario, LocalDate fechaBaja) {
        super(codigo, nombre, apellidos, fechaNacimiento, fechaIngreso, puesto, salario);
        this.fechaBaja = fechaBaja;
    }

    public LocalDate getFechaBaja() {
        return fechaBaja;
    }

    public void setFechaBaja(LocalDate fechaBaja) {
        this.fechaBaja = fechaBaja;
    }

    @Override
    public String toString() {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
        String fechaNacimientoFormatted = getFechaNacimiento().format(formatter);
        String fechaIngresoFormatted = getFechaIngreso().format(formatter);
        String fechaBajaFormatted = fechaBaja.format(formatter);
        
        return "CODIGO: " + getCodigo() +
           "    NOMBRE: " + getNombre() + " " + getApellidos() +
           "    FECHA NACIMIENTO: " + fechaNacimientoFormatted +
           "    FECHA INGRESO: " + fechaIngresoFormatted +
           "    FECHA BAJA: " + fechaBajaFormatted +
           "    PUESTO: " + getPuesto() +
           "    SALARIO: " + getSalario();
    }
}
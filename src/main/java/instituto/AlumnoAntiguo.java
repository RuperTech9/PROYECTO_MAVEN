
package instituto;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

/**
 *
 * @author Ruper
 */
public class AlumnoAntiguo extends Alumno {
    private LocalDate fechaBaja;

    public AlumnoAntiguo(String dni, String nombre, String apellidos, LocalDate fechaNacimiento, String curso, double nota, LocalDate fechaBaja) {
        super(dni, nombre, apellidos, fechaNacimiento, curso, nota);
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
        String fechaBajaFormatted = fechaBaja.format(formatter);
        
        return "DNI: " + getDni() +
           "    NOMBRE: " + getNombre() + " " + getApellidos() +
           "    FECHA NACIMIENTO: " + fechaNacimientoFormatted +
           "    FECHA BAJA: " + fechaBajaFormatted +
           "    CURSO: " + getCurso() +
           "    NOTA: " + getNota();
    }
}
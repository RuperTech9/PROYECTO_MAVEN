
package empleados;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;

/**
 *
 * @author Ruper
 */
public class Jornada {
    private String dni;
    private LocalDate fecha;
    private LocalDateTime entrada;
    private LocalDateTime salida;

    public Jornada(String dni, LocalDate fecha, LocalDateTime entrada, LocalDateTime salida) {
        this.dni = dni;
        this.entrada = entrada;
        this.salida = salida;
    }

    public String getDni() {
        return dni;
    }
    
    public void setDni(String dni) {
        this.dni = dni;
    }
    public LocalDate getFecha() {
        return fecha;
    }

    public void setFecha(LocalDate fecha) {
        this.fecha = fecha;
    }

    public LocalDateTime getEntrada() {
        return entrada;
    }

    public void setEntrada(LocalDateTime entrada) {
        this.entrada = entrada;
    }

    public LocalDateTime getSalida() {
        return salida;
    }

    public void setSalida(LocalDateTime salida) {
        this.salida = salida;
    }

    public long calcularDuracion() {
        if (salida != null) {
            return ChronoUnit.MINUTES.between(entrada, salida);
        }
        return 0;
    }

    @Override
    public String toString() {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss");
        String entradaFormatted = entrada.format(formatter);
        String salidaFormatted = (salida != null) ? salida.format(formatter) : "N/A";
        long duracion = calcularDuracion();
        return "DNI: " + dni +
               "    FECHA: " + entrada.toLocalDate() +
               "    ENTRADA: " + entradaFormatted +
               "    SALIDA: " + salidaFormatted +
               "    DURACION: " + duracion + " minutos";
    }
}

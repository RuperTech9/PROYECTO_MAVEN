
package examen;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

/**
 *
 * @author Ruper
 */
public class Partida {
    private String dni;
    private LocalDate fechaPartida;
    double puntuacion;
    int intentos;

    public Partida(String dni, LocalDate fechaPartida, double puntuacion, int intentos) {
        this.dni = dni;
        this.fechaPartida = fechaPartida;
        this.puntuacion = puntuacion;
        this.intentos = intentos;
    }

    public String getDni() {
        return dni;
    }

    public void setDni(String dni) {
        this.dni = dni;
    }

    public LocalDate getFechaPartida() {
        return fechaPartida;
    }

    public void setFechaPartida(LocalDate fechaPartida) {
        this.fechaPartida = fechaPartida;
    }

    public double getPuntuacion() {
        return puntuacion;
    }

    public void setPuntuacion(double puntuacion) {
        this.puntuacion = puntuacion;
    }

    public int getIntentos() {
        return intentos;
    }

    public void setIntentos(int intentos) {
        this.intentos = intentos;
    }

    
    @Override
    public String toString() {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
        String fechaPartidaFormatted = fechaPartida.format(formatter);
        
        return "DNI: " + getDni() +
           "    FECHA PARTIDA: " + fechaPartidaFormatted +
           "    PUNTUACION: " + getPuntuacion() +
           "    INTENTOS: " + getIntentos();
    }
    
    
}
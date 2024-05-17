
package instituto;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

/**
 *
 * @author Ruper
 */
public class Alumno {
    // ATRIBUTOS
    private String dni;
    private String nombre;
    private String apellidos;
    private LocalDate fechaNacimiento;
    private String curso;
    private double nota;
    
    // CONSTRUCTOR
    public Alumno(String dni, String nombre, String apellidos, LocalDate fechaNacimiento, String curso, double nota) {
        this.dni = dni;
        this.nombre = nombre;
        this.apellidos = apellidos;
        this.fechaNacimiento = fechaNacimiento;
        this.curso = curso;
        this.nota = nota;
    }
    
    // GETTERS
    public String getDni() {
        return dni;
    }
    public String getNombre() {
        return nombre;
    }
    public String getApellidos() {
        return apellidos;
    }
    public LocalDate getFechaNacimiento() {
        return fechaNacimiento;
    }
    public String getCurso() {
        return curso;
    }
    public double getNota() {
        return nota;
    }
    
    // SETTERS
    public void setDni(String dni) {
        this.dni = dni;
    }
    public void setNombre(String nombre) {
        this.nombre = nombre;
    }
    public void setApellidos(String apellidos) {
        this.apellidos = apellidos;
    }
    public void setFechaNacimiento(LocalDate fechaNacimiento) {    
        this.fechaNacimiento = fechaNacimiento;
    } 
    public void setCurso(String curso) {
        this.curso = curso;
    }
    public void setNota(double nota) {
        this.nota = nota;
    }
    
    
    // METODO toString
    @Override
    public String toString() {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
        String fechaNacimientoFormatted = fechaNacimiento.format(formatter);
        
        return "DNI: " + dni +
           "    NOMBRE: " + nombre + " " + apellidos +
           "    FECHA NACIMIENTO: " + fechaNacimientoFormatted +
           "    CURSO: " + curso +
           "    NOTA: " + nota;
    }
}
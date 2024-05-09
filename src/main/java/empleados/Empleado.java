
package empleados;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

/**
 *
 * @author Ruper
 */
public class Empleado {
    // ATRIBUTOS
    private String codigo;
    private String nombre;
    private String apellidos;
    private LocalDate fechaNacimiento;
    private LocalDate fechaIngreso;
    private String puesto;
    private double salario;
    
    // CONSTRUCTOR
    public Empleado(String codigo, String nombre, String apellidos, LocalDate fechaNacimiento, LocalDate fechaIngreso, String puesto, double salario) {
        this.codigo = codigo;
        this.nombre = nombre;
        this.apellidos = apellidos;
        this.fechaNacimiento = fechaNacimiento;
        this.fechaIngreso = fechaIngreso;
        this.puesto = puesto;
        this.salario = salario;
    }
    
    // GETTERS
    public String getCodigo() {
        return codigo;
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
    public LocalDate getFechaIngreso() {
        return fechaIngreso;
    }
    public String getPuesto() {
        return puesto;
    }
    public double getSalario() {
        return salario;
    }
    
    // SETTERS
    public void setCodigo(String codigo) {
        this.codigo = codigo;
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
    public void setFechaIngreso(LocalDate fechaIngreso) {
        this.fechaIngreso = fechaIngreso;
    }
    public void setPuesto(String puesto) {
        this.puesto = puesto;
    }
    public void setSalario(double salario) {
        this.salario = salario;
    }
    
    
    // METODO toString
    @Override
    public String toString() {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
        String fechaNacimientoFormatted = fechaNacimiento.format(formatter);
        String fechaIngresoFormatted = fechaIngreso.format(formatter);
        
        return codigo +
           "    NOMBRE: " + nombre + " " + apellidos +
           "    FECHA NACIMIENTO: " + fechaNacimientoFormatted +
           "    FECHA INGRESO: " + fechaIngresoFormatted +
           "    PUESTO: " + puesto +
           "    SALARIO: " + salario;
    }
}

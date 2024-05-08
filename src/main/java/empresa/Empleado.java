
package empresa;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

/**
 *
 * @author Ruper
 */
public class Empleado {
    private int numemp;
    private String nombre;
    private int edad;
    private int oficina;
    private String puesto;
    private LocalDate contrato;

    public Empleado(int numemp, String nombre, int edad, int oficina, String puesto, LocalDate contrato) {
        this.numemp = numemp;
        this.nombre = nombre;
        this.edad = edad;
        this.oficina = oficina;
        this.puesto = puesto;
        this.contrato = contrato;
    }

    // Getters y Setters
    public int getNumemp() { return numemp; }
    public String getNombre() { return nombre; }
    public int getEdad() { return edad; }
    public int getOficina() { return oficina; }
    public String getPuesto() { return puesto; }
    public LocalDate getContrato() { return contrato; }

    public void setNumemp(int numemp) { this.numemp = numemp; }
    public void setNombre(String nombre) { this.nombre = nombre; }
    public void setEdad(int edad) { this.edad = edad; }
    public void setOficina(int oficina) { this.oficina = oficina; }
    public void setPuesto(String puesto) { this.puesto = puesto; }
    public void setContrato(LocalDate contrato) { this.contrato = contrato; }
    
    @Override
    public String toString() {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
        String contratoFormatted = contrato.format(formatter);
        
        return "CODIGO: " +numemp +
           "    NOMBRE: " + nombre +
           "    EDAD: " + edad +
           "    OFICINA: " + oficina +
           "    PUESTO: " + puesto +
           "    FECHA INGRESO: " + contratoFormatted;
    }
}
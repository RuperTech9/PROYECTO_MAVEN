
package examen;

/**
 *
 * @author alumno
 */
public class Jugador {
    // ATRIBUTOS
    private String dni;
    private String nombreCompleto;
    
    // CONSTRUCTOR
    public Jugador(String dni, String nombreCompleto) {
        this.dni = dni;
        this.nombreCompleto = nombreCompleto;
    }
    
    // GETTERS
    public String getDni() {
        return dni;
    }
    public String getNombreCompleto() {
        return nombreCompleto;
    }

    
    // SETTERS
    public void setDni(String dni) {
        this.dni = dni;
    }
    public void setNombreCompleto(String nombre) {
        this.nombreCompleto = nombre;
    }

    
    
    // METODO toString
    @Override
    public String toString() {
        return "DNI: " + dni +
           "    NOMBRE: " + nombreCompleto;
    }
}
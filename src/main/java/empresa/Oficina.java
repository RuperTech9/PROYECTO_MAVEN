
package empresa;

/**
 *
 * @author Ruper
 */
public class Oficina {
    private int oficina;
    private String ciudad;
    private double superficie;
    private double ventas;

    public Oficina(int oficina, String ciudad, double superficie, double ventas) {
        this.oficina = oficina;
        this.ciudad = ciudad;
        this.superficie = superficie;
        this.ventas = ventas;
    }

    // Getters y Setters
    public int getOficina() { return oficina; }
    public String getCiudad() { return ciudad; }
    public double getSuperficie() { return superficie; }
    public double getVentas() { return ventas; }

    public void setOficina(int oficina) { this.oficina = oficina; }
    public void setCiudad(String ciudad) { this.ciudad = ciudad; }
    public void setSuperficie(double superficie) { this.superficie = superficie; }
    public void setVentas(double ventas) { this.ventas = ventas; }
    
    @Override
    public String toString() {
        return "Oficina{" +
               "oficina=" + oficina +
               ", ciudad='" + ciudad + '\'' +
               ", superficie=" + superficie +
               ", ventas=" + ventas +
               '}';
    }
}
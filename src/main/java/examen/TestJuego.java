
package examen;

/**
 *
 * @author alumno
 */
public class TestJuego {
    public static void main(String [] args){
        Juego juego = new Juego();
        juego.cargarJugadoresDB();
        juego.cargarPartidasDB();
        juego.mostrarMenu();
    }
}

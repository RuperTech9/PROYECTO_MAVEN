
package examen;


import java.util.ArrayList;
import java.time.LocalDate;
import java.util.InputMismatchException;
import java.util.Scanner;
import java.sql.*;


/**
 *
 * @author Ruper
 */
public class Juego {
    // ArrayList de Objetos
    static ArrayList<Jugador> jugadores = new ArrayList<>();
    static ArrayList<Partida> partidas = new ArrayList<>();
    static Scanner sc = new Scanner(System.in);

    // METODOS JDBC
    private static Connection conectar() {
        String url = "jdbc:mysql://192.168.80.152:3306/JuegoDB";
        String user = "alejandro";
        String password = "J0selu1s100%";
        try {
            return DriverManager.getConnection(url, user, password);
        } catch (SQLException e) {
            System.err.println("Error en la conexión: " + e.getMessage());
            System.err.println("Número que representa el error: " + e.getErrorCode());
            return null;
        }
    }

    public void cargarJugadoresDB() {
        String sql = "SELECT * FROM Jugadores";
        try (Connection con = conectar();
             PreparedStatement pstmt = con.prepareStatement(sql);
             ResultSet rs = pstmt.executeQuery()) {
            while (rs.next()) {
                Jugador jugador = new Jugador(
                    rs.getString("dni"),
                    rs.getString("nombreCompleto")
                );
                jugadores.add(jugador);
            }
        } catch (SQLException e) {
            System.err.println("Error al cargar jugadores: " + e.getMessage());
            System.err.println("Número del error: " + e.getErrorCode());
        }
    } // FIN METODO
    
    public void cargarPartidasDB() {
        String sql = "SELECT * FROM Partidas";
        try (Connection con = conectar();
             PreparedStatement pstmt = con.prepareStatement(sql);
             ResultSet rs = pstmt.executeQuery()) {
            while (rs.next()) {
                Partida partida = new Partida(
                    rs.getString("dni"),
                    rs.getDate("fechaPartida").toLocalDate(),
                    rs.getDouble("puntuacion"),
                    rs.getInt("intentos")
                );
                partidas.add(partida);
            }
        } catch (SQLException e) {
            System.err.println("Error al cargar jugadores: " + e.getMessage());
            System.err.println("Número del error: " + e.getErrorCode());
        }
    } // FIN METODO
    
    // METODO para añadir un jugador.
    public void registrarJugador() {
        // CODIGO EMPLEADO
        System.out.println("Introduce el dni del jugador: ");
        String dni = sc.nextLine();
        // NOMBRE Y APELLIDOS
        System.out.println("Introduce el nombre del jugador:");
        String nombreCompleto = sc.nextLine();

        
        // Aquí construyo el objeto empleado
        Jugador nuevoJugador = new Jugador(dni, nombreCompleto);
        
        guardarJugadorDB(nuevoJugador);    
    } // FIN METODO
    
    // METODO para guardar un jugador en la BBDD.
    private void guardarJugadorDB(Jugador jugador) {
        String sql = "INSERT INTO Jugadores (dni, nombreCompleto) VALUES (?, ?)";
        Connection con = null;
        PreparedStatement ps = null;
        try {
            con = conectar();
            ps = con.prepareStatement(sql);
            ps.setString(1, jugador.getDni());
            ps.setString(2, jugador.getNombreCompleto());
            int resultado = ps.executeUpdate();
            if (resultado > 0) {
                jugadores.add(jugador); // Lo añado a la lista.
                System.out.println("\nEl jugador " + jugador.getNombreCompleto() + " ha sido añadido a la base de datos.");
            }
        } catch (SQLException e) {
            System.err.println("Error al añadir empleado: " + e.getMessage());
            System.err.println("Número que representa el error: " + e.getErrorCode());
        } finally {
            cerrarConexion(con, ps, null);
        }
    } // FIN METODO
    
   
    // METODO jugar
    public void jugar() {
        System.out.println("DNI");
        String dni = sc.nextLine();
        
        Jugador jugador = buscarJugadorPorDni(dni);
        if (jugador == null) {
            System.out.println("no registrado");
            registrarJugador();
            jugador = buscarJugadorPorDni(dni);
            
        } if (jugador != null){
            iniciarPartida(jugador);
        }
    }
    
    // METODO buscar jugador por DNI
    private Jugador buscarJugadorPorDni (String dni){
        for (Jugador jugador : jugadores) {
            if(jugador.getDni().equalsIgnoreCase(dni)){
                return jugador;
            }
        }
        return null;
    }
    
    // METODO para iniciar partida
    private void iniciarPartida(Jugador jugador) {
        int numeroSecreto = (int)(Math.random()*10);
        int intentos = 0;
        double puntuacion = 10.0;
        boolean acertado = false;
        
        while(intentos < 10 && !acertado){
            System.out.println("Introduce un número: ");
            int intento = sc.nextInt();
            sc.nextLine();
            intentos++;
            if(intento == numeroSecreto){
                acertado = true;
            } else {
                if (intentos <= 5) {
                    puntuacion -= 2.0;
                } else { // pasado el 5 intento
                    puntuacion -= 0.1;
                }
                if (intento < numeroSecreto){
                    System.out.println("Es mayor");
                } else {
                    System.out.println("Es menor");
                }
            }
        }
        if (puntuacion <0) {
            puntuacion = 0;
        }
        Partida nuevaPartida = new Partida(jugador.getDni(), LocalDate.now(), puntuacion, intentos);
        guardarPartidaDB(nuevaPartida);
        System.out.println("Partida finalizada. Puntuacion: " + puntuacion + " en " + intentos + " intentos.");
    }
    
    // METODO para guardar una partida en la BBDD.
    private void guardarPartidaDB(Partida partida) {
        String sql = "INSERT INTO Partidas (dni, fechaPartida, puntuacion, intentos) VALUES (?, ?, ?, ?)";
        Connection con = null;
        PreparedStatement ps = null;
        try {
            con = conectar();
            ps = con.prepareStatement(sql);
            ps.setString(1, partida.getDni());
            ps.setDate(2, Date.valueOf(partida.getFechaPartida()));
            ps.setDouble(3, partida.getPuntuacion());
            ps.setInt(4, partida.getIntentos());
            int resultado = ps.executeUpdate();
            if (resultado > 0) {
                partidas.add(partida); // Lo añado a la lista.
                System.out.println("\nEl partida ha sido guardada en la base de datos.");
            }
        } catch (SQLException e) {
            System.err.println("Error al añadir empleado: " + e.getMessage());
            System.err.println("Número que representa el error: " + e.getErrorCode());
        } finally {
            cerrarConexion(con, ps, null);
        }
    } // FIN METODO
    
    // Método para mostrar Top 10
    public void mostrarTop10(){
        partidas.sort((p1, p2)-> Double.compare(p2.getPuntuacion(), p1.getPuntuacion()));
        
        System.out.println("Top 10");
        
        for (int i = 0; i < Math.min(10, partidas.size()); i++) {
            System.out.println((i+1) + "- " + partidas.get(i).toString());
            
        }
    }

    // METODO para cerrar conexión
    private static void cerrarConexion(Connection con, PreparedStatement ps, ResultSet rs) {
        if (rs != null) {
            try {
                rs.close();
            } catch (SQLException e) {
                System.err.println("Error al cerrar ResultSet: " + e.getMessage());
                System.err.println("Número que representa el error: " + e.getErrorCode());
            }
        }
        if (ps != null) {
            try {
                ps.close();
            } catch (SQLException e) {
                System.err.println("Error al cerrar PreparedStatement: " + e.getMessage());
                System.err.println("Número que representa el error: " + e.getErrorCode());
            }
        }
        if (con != null) {
            try {
                con.close();
            } catch (SQLException e) {
                System.err.println("Error al cerrar Connection: " + e.getMessage());
                System.err.println("Número que representa el error: " + e.getErrorCode());
            }
        }
    }
    
    // METODO para mostrar el menú
    public void mostrarMenu() {
        int opcion = 0;
        do {     
            try {
            String menu = "\n               JUEGO ADIVINA" +
                          "\n---------------------------------------------------------" +
                          "\n1- Registrar Jugador" +
                          "\n2- Jugar Partida" +
                          "\n3- Mostrar Records" +
                          "\n0- Salir" +
                          "\n---------------------------------------------------------" +
                          "\nSelecciona una opción: ";
            
            System.out.print(menu);
            opcion = sc.nextInt();
            sc.nextLine(); // Salto de línea
            
                switch (opcion) {
                    case 1:
                        registrarJugador();
                        break;
                    case 2:
                        jugar();
                        break;
                    case 3:
                        mostrarTop10();
                        break;
                    case 0:
                        System.out.println("\nSaliendo...");
                        break;
                    default:
                        System.err.println("\nOpción no válida. Introduce un número entre 0 y 3.");
                        break;
                } // FIN SWITCH
            } catch (InputMismatchException e) {
                System.err.println("ERROR. Entrada no válida, inténtalo de nuevo.");
                sc.nextLine(); // Salto de línea
            } // FIN TRY-CATCH
        } while (opcion != 0); // FIN DO-WHILE
    } // FIN METODO
} // FIN CLASE
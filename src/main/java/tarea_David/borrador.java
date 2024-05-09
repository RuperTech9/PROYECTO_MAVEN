
package tarea_David;

/**
 *
 * @author david
 * 
 * 
 * 
 * 
 * 
 * 
 * 
 * 
 * 
 * 
 * 
 * 
 * 
 * 
 * 
 * 
 * 
 */
public class borrador {
          /**
           * public static void buscarEmpleado() { Scanner scanner =
           * new Scanner(System.in); try { System.out.println("Ingrese
           * el nombre del empleado:"); String nombreBuscar =
           * scanner.nextLine(); if
           * (!nombreBuscar.matches("[a-zA-ZáéíóúÁÉÍÓÚñÑ ]+")) { throw
           * new IllegalArgumentException("El nombre solo puede
           * contener letras y/o espacios."); }
           *
           * System.out.println("Ingrese el apellido del empleado:");
           * String apellidoBuscar = scanner.nextLine(); if
           * (!apellidoBuscar.matches("[a-zA-ZáéíóúÁÉÍÓÚñÑ ]+")) {
           * throw new IllegalArgumentException("El apellido solo
           * puede contener letras y/o espacios."); }
           *
           * // Consulta SQL para buscar el empleado por nombre y
           * apellido String consultaBuscar = "SELECT * FROM empleados
           * WHERE nombre = '" + nombreBuscar + "' AND apellidos = '"
           * + apellidoBuscar + "'";
           *
           * // Realizar la conexión String url =
           * "jdbc:mysql://192.168.80.253:3306/empresa?allowPublicKeyRetrieval=true&useSSL=false&serverTimeZone=UTC";
           * String user = "david4"; String password = "David!1234";
           * Connection conexion = DriverManager.getConnection(url,
           * user, password); Statement sentencia =
           * conexion.createStatement();
           *
           * // Ejecutar la consulta para buscar al empleado ResultSet
           * resultado = sentencia.executeQuery(consultaBuscar);
           *
           * // Verificar si se encontró el empleado if
           * (!resultado.next()) { System.out.println("No se encontró
           * ningún empleado con el nombre y apellido
           * especificados."); } else { // Mostrar los datos del
           * empleado do { String nombre =
           * resultado.getString("nombre"); String apellidos =
           * resultado.getString("apellidos"); LocalDate
           * fechaNacimiento =
           * resultado.getDate("fechaNacimiento").toLocalDate();
           * LocalDate fechaIngreso =
           * resultado.getDate("fechaIngreso").toLocalDate(); String
           * puesto = resultado.getString("puesto"); double salario =
           * resultado.getDouble("salario");
           *
           *
           * System.out.println("--- Datos del empleado ---");
           * System.out.println("Nombre: " + nombre);
           * System.out.println("Apellidos: " + apellidos);
           * System.out.println("Fecha de Nacimiento: " +
           * fechaNacimiento); System.out.println("Fecha de Ingreso: "
           * + fechaIngreso); System.out.println("Puesto: " + puesto);
           * System.out.println("Salario: " + salario); } while
           * (resultado.next()); }
           *
           * // Cerrar la conexión resultado.close();
           * sentencia.close(); conexion.close(); } catch
           * (SQLException e) { System.out.println("Error en la
           * conexión a la base de datos: " + e.getMessage()); }
           * }//Fin buscarEmpleado
           *
           * /
           *
           * /**
           * public static void buscarEmpleado2() { boolean
           * empleadoEncontrado = false; Scanner scanner = new
           * Scanner(System.in); ResultSet resultadoDatos = null;
           *
           * do { try { System.out.println("Ingrese el nombre del
           * empleado:"); String nombreBuscar = scanner.nextLine(); if
           * (!nombreBuscar.matches("[a-zA-ZáéíóúÁÉÍÓÚñÑ ]+")) { throw
           * new IllegalArgumentException("El nombre solo puede
           * contener letras y/o espacios."); }
           *
           * System.out.println("Ingrese el apellido del empleado:");
           * String apellidoBuscar = scanner.nextLine(); if
           * (!apellidoBuscar.matches("[a-zA-ZáéíóúÁÉÍÓÚñÑ ]+")) {
           * throw new IllegalArgumentException("El apellido solo
           * puede contener letras y/o espacios."); }
           *
           * // Instancia de Consultas_Metodos Consultas_Metodos
           * consultas = new Consultas_Metodos();
           *
           * // Consulta SQL para eliminar el empleado de la tabla
           * empleados String consultaBuscar = "SELECT * FROM
           * empleados WHERE nombre = '" + nombreBuscar + "' AND
           * apellidos = '" + apellidoBuscar + "'";
           *
           * // Ejecutar la consulta para eliminar el empleado
           * resultadoDatos =
           * consultas.consultarEmpleados(consultaBuscar);
           *
           * // Mostrar los emplados buscados int num = 1; while
           * (resultadoDatos.next()) { String nombre =
           * resultadoDatos.getString("nombre"); String apellidos =
           * resultadoDatos.getString("apellidos"); String
           * fechaNacimiento =
           * resultadoDatos.getString("fechaNacimiento"); String
           * fechaIngreso = resultadoDatos.getString("fechaIngreso");
           * String puesto = resultadoDatos.getString("puesto");
           * double salario = resultadoDatos.getDouble("salario");
           *
           * // Imprimir System.out.println(num + ".");
           * System.out.println("Nombre: " + nombre);
           * System.out.println("Apellidos: " + apellidos);
           * System.out.println("FechaNacimiento: " +
           * fechaNacimiento); System.out.println("FechaIngreso: " +
           * fechaIngreso); System.out.println("Puesto: " + puesto);
           * System.out.println("Salario: " + salario);
           * System.out.println(); // salto de linea
           *
           * num++; };
           *
           * if (num > 1){ empleadoEncontrado = true; }else {
           * System.out.println("Empleado no encontrado ..."); }
           *
           *
           * } catch (Exception e) { System.out.println("ERROR: " +
           * e.getMessage()); } finally { // Cerrar el ResultSet if
           * (resultadoDatos != null) { try { resultadoDatos.close();
           * } catch (SQLException ex) { System.out.println("Error al
           * cerrar el ResultSet: " + ex.getMessage()); } } }//Fin
           * try-catch
           *
           * } while (!empleadoEncontrado); }//Fin buscarEmpleado2
           */

          
          /**
           * public void gastoTotalSalarios2() { try { // Instancia de
           * Consultas_Metodos Consultas_Metodos consultas = new
           * Consultas_Metodos();
           *
           * // Consulta SQL para buscar el total de salarios String
           * consultaSalario = "SELECT SUM(salario) AS total_salarios
           * FROM empleados";
           *
           * // Ejecutar la consulta ResultSet resultado =
           * consultas.consultarEmpleados(consultaSalario);
           *
           * // Mostrar el total de salarios if (resultado.next()) {
           * double totalSalarios =
           * resultadoSalario.getDouble("total_salarios");
           * System.out.println("El gasto total en salarios es: " +
           * totalSalarios); } else { System.out.println("No se
           * encontraron datos de salarios."); }
           *
           * // Cerrar el ResultSet if (resultadoSalario != null) {
           * resultadoSalario.close(); } } catch (Exception e) {
           * System.out.println("ERROR: " + e.getMessage()); } }
           *
           * public void gastoTotalSalarios() { try { // Instancia de
           * Consultas_Metodos Consultas_Metodos consultas = new
           * Consultas_Metodos();
           *
           * // Consulta SQL para buscar el total de salarios String
           * consultaSalario = "SELECT SUM(salario) AS total_salarios
           * FROM empleados";
           *
           * // Ejecutar la consulta ResultSet resultadoSalarios =
           * consultas.ejecutarConsulta(consultaSalario);
           *
           * // Mostrar el total de salarios if
           * (resultadoSalarios.next()) { double totalSalarios =
           * resultadoSalarios.getDouble("total_salarios");
           * System.out.println("El gasto total en salarios es: " +
           * totalSalarios); } else { System.out.println("No se
           * encontraron datos de salarios."); }
           *
           * // Cerrar el ResultSet if (resultadoSalarios != null) {
           * resultadoSalarios.close(); } } catch (Exception e) {
           * System.out.println("ERROR: " + e.getMessage()); } }
           *
           * public void gastoTotalSalarios3() { try { // Instancia de
           * Consultas_Metodos Consultas_Metodos consultas = new
           * Consultas_Metodos();
           *
           * // Consulta SQL para buscar el total de salarios String
           * consultaSalario = "SELECT SUM(salario) AS total_salarios
           * FROM empleados";
           *
           * // Ejecutar la consulta ResultSet resultadoSalarios =
           * consultas.ejecutarConsulta(consultaSalario);
           *
           * // Mostrar el total de salarios if
           * (resultadoSalarios.next()) { double totalSalarios =
           * resultadoSalarios.getDouble("total_salarios");
           * System.out.println("El gasto total en salarios es: " +
           * totalSalarios); } else { System.out.println("No se
           * encontraron datos de salarios."); }
           *
           * // Cerrar el ResultSet if (resultadoSalarios != null) {
           * resultadoSalarios.close(); } } catch (Exception e) {
           * System.out.println("ERROR: " + e.getMessage()); } }

* 
* 
* 
* public static void eliminarEmpleado2() { boolean
           * empleadoEncontrado = false; Scanner scanner = new
           * Scanner(System.in); do { try {
           * System.out.println("Ingrese el nombre del empleado a
           * borrar:"); String nombreBorrar = scanner.nextLine(); if
           * (!nombreBorrar.matches("[a-zA-ZáéíóúÁÉÍÓÚñÑ ]+")) { throw
           * new IllegalArgumentException("El nombre solo puede
           * contener letras y/o espacios."); }
           *
           * System.out.println("Ingrese el apellido del empleado a
           * borrar:"); String apellidoBorrar = scanner.nextLine(); if
           * (!apellidoBorrar.matches("[a-zA-ZáéíóúÁÉÍÓÚñÑ ]+")) {
           * throw new IllegalArgumentException("El apellido solo
           * puede contener letras y/o espacios."); }
           *
           * // Consulta SQL para agregar el empleado a la tabla
           * empleadosAntiguos LocalDate fechaFinalizacion =
           * LocalDate.now(); String consultaAgregarAntiguo = "INSERT
           * INTO empleadosAntiguos (nombre, apellidos,
           * fechaNacimiento," + " fechaIngreso, fechaFinalizacion) "
           * + "SELECT nombre, apellidos, fechaNacimiento,
           * fechaIngreso, '" + fechaFinalizacion + "' " + "FROM
           * empleados WHERE nombre = '" + nombreBorrar + "' AND
           * apellidos = '" + apellidoBorrar + "'";
           *
           * // Consulta SQL para eliminar el empleado de la tabla
           * empleados String consultaEliminar = "DELETE FROM
           * empleados WHERE nombre = '" + nombreBorrar + "' AND
           * apellidos = '" + apellidoBorrar + "'";
           *
           *
           * // Instancia de Consultas_Metodos Consultas_Metodos
           * consultas = new Consultas_Metodos();
           *
           * // Ejecutar la consulta para agregar el empleado a
           * empleadosAntiguos
           * consultas.ejecutarInstruccion(consultaAgregarAntiguo);
           *
           * // Ejecutar la consulta para eliminar el empleado
           * consultas.ejecutarInstruccion(consultaEliminar);
           *
           * // Ejecutar la consulta para eliminar el empleado int
           * filasAfectadas =
           * consultas.ejecutarInstruccion(consultaEliminar);
           *
           * // Comprobar si se encontró y eliminó al empleado if
           * (filasAfectadas == 0) { System.out.println("Empleado no
           * encontrado."); } else { // Establecer empleadoEncontrado
           * en true ya que la eliminación se realizó sin errores
           * empleadoEncontrado = true; // Imprimir mensaje de éxito
           * System.out.println("Se ha borrado el empleado
           * correctamente y se ha agregado a la tabla
           * empleadosAntiguos."); } } catch (Exception e) {
           * System.err.println("ERROR: " + e.getMessage()); }//Fin
           * try-catch } while (!empleadoEncontrado); }//Fin
           * eliminarEmpleado
           */
}

NUMERO: 0
ERROR: Communications link failure.
SOLUCION: Verificar la conectividad de red, ajustar configuraciones del servidor, y actualizar el driver JDBC.
En este caso simplemente tenía apagada la maquina virtual.

NUMERO: 1044
ERROR: Access denied for user 'alejandro'@'%' to database 'EmpleadosB'
SOLUCION: En el mensaje de error menciona 'EmpleadosB', pero en mi código la base de datos se llama 'EmpleadosDB'

NUMERO: 1045
ERROR: Access denied for user 'alejandro'@'192.168.0.18' (using password: YES)
SOLUCION: Verifico que el usuario y la contraseña sean correctos y que el usuario tenga permisos adecuados.

NUMERO: 1054
ERROR: Unknown column 'odigo' in 'where clause'
SOLUCION: Verifico que la columna de la clausula WHERE sea correcta.

NUMERO: 1062
ERROR: Duplicate entry '1' for key 'EmpleadosAntiguos.PRIMARY'
SOLUCION: No puedo duplicar una primary key

NUMERO: 1130
ERROR: null,  message from server: "Host '1DAM-001' is not allowed to connect to this MySQL server"
SOLUCION: Verifico que la dirección ip de la conexión sea correcta.

NUMERO: 1146
ERROR: Table 'EmpleadosDB.Epleados' doesn't exist
SOLUCION: Verifico que el nombre de la tabla sea correcto.
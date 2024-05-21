-- Borrar la Base de Datos
DROP DATABASE IF EXISTS EmpleadosDB;

-- Creación de la Base de Datos
CREATE DATABASE IF NOT EXISTS EmpleadosDB;

-- Creación del usuario en MYSQL (En terminal UBUNTU)
-- CREATE USER 'alejandro'@'%' IDENTIFIED BY 'J0selu1s100%';

-- Ver usuarios creados en MYSQL
-- SELECT user, host FROM mysql.user;

-- Asignar permisos al usuario creado en MYSQL
-- GRANT ALL PRIVILEGES ON EmpleadosDB.* TO 'alejandro'@'%';
-- FLUSH PRIVILEGES;

-- Creación de la Tabla Empleados
USE EmpleadosDB;
CREATE TABLE Empleados (
    codigo VARCHAR(15) PRIMARY KEY,
    nombre VARCHAR(50),
    apellidos VARCHAR(100),
    fechaNacimiento DATE,
    fechaIngreso DATE,
    puesto VARCHAR(50),
    salario DOUBLE
);

CREATE TABLE EmpleadosAntiguos (
    codigo VARCHAR(15) PRIMARY KEY,
    nombre VARCHAR(50),
    apellidos VARCHAR(100),
    fechaNacimiento DATE,
    fechaIngreso DATE,
    puesto VARCHAR(50),
    salario DOUBLE,
    fechaBaja DATE
);

-- Inserción de Datos
INSERT INTO Empleados VALUES
('jperez', 'Juan', 'Perez', '1990-01-15', '2020-06-15', 'Gerente', 50000),
('alopez', 'Ana', 'Lopez', '1981-03-01', '2023-06-05', 'Analista', 30000),
('cruiz', 'Carlos', 'Ruiz', '1993-04-22', '2021-09-25', 'Ingeniero', 60000);
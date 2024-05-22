DROP DATABASE IF EXISTS InstitutoDB;

-- Creación de la Base de Datos
CREATE DATABASE IF NOT EXISTS InstitutoDB;

-- Creación del usuario en MYSQL
CREATE USER 'alejandro'@'%' IDENTIFIED BY 'J0selu1s100%';

-- Asignar permisos al usuario creado en MYSQL
GRANT ALL PRIVILEGES ON InstitutoDB.* TO 'alejandro'@'%';
FLUSH PRIVILEGES;


USE InstitutoDB;

-- Creación de la Tabla Oficinas
CREATE TABLE Alumnos (
    dni VARCHAR(9) PRIMARY KEY,
    nombre VARCHAR(50),
    apellidos VARCHAR(100),
    fechaNacimiento DATE,
    curso VARCHAR(50),
    nota DOUBLE
);

CREATE TABLE AlumnosAntiguos (
    dni VARCHAR(9) PRIMARY KEY,
    nombre VARCHAR(50),
    apellidos VARCHAR(100),
    fechaNacimiento DATE,
    curso VARCHAR(50),
    nota DOUBLE,
    fechaBaja DATE
);

-- Inserción de Datos
INSERT INTO Alumnos (dni, nombre, apellidos, fechaNacimiento, curso, nota) VALUES
('00000000A', 'Alejandro', 'Ruperez', '1989-03-18', 'PRIMERO', 9),
('11111111B', 'Mario', 'Lopez', '1991-04-19', 'SEGUNDO', 5.5),
('22222222C', 'Dani', 'Villalba', '1982-08-24', 'TERCERO', 6),
('33333333D', 'Adrian', 'Sanz', '2021-07-06', 'PRIMERO', 7.75);


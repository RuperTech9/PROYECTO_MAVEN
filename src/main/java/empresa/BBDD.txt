-- Creación de la Base de Datos
CREATE DATABASE IF NOT EXISTS Empresa;

-- Creación del usuario en MYSQL
CREATE USER 'alejandro'@'%' IDENTIFIED BY 'J0selu1s100%';

-- Asignar permisos al usuario creado en MYSQL
GRANT ALL PRIVILEGES ON Empresa.* TO 'alejandro'@'%';
FLUSH PRIVILEGES;


USE Empresa;

-- Creación de la Tabla Oficinas
CREATE TABLE Oficinas (
    oficina INT PRIMARY KEY,
    ciudad VARCHAR(50),
    superficie DOUBLE,
    ventas DOUBLE
);

-- Creación de la Tabla Empleados
CREATE TABLE Empleados (
    numemp INT PRIMARY KEY,
    nombre VARCHAR(50),
    edad INT,
    oficina INT,
    puesto VARCHAR(100),
    contrato DATE,
    FOREIGN KEY (oficina) REFERENCES Oficinas(oficina)
);

-- Inserción de Datos
INSERT INTO Oficinas (oficina, ciudad, superficie, ventas) VALUES
(1, 'Madrid', 500, 100000),
(2, 'Barcelona', 300, 150000),
(3, 'Sevilla', 250, 95000);

INSERT INTO Empleados (numemp, nombre, edad, oficina, puesto, contrato) VALUES
(1, 'Juan Perez', 30, 1, 'Gerente', '2021-01-15'),
(2, 'Ana López', 25, 2, 'Analista', '2021-06-20'),
(3, 'Carlos Ruiz', 40, 3, 'Ingeniero', '2020-11-05');
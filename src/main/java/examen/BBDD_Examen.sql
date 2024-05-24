-- Creación del usuario en MYSQL
-- CREATE USER 'alejandro'@'%' IDENTIFIED BY 'J0selu1s100%';

-- Asignar permisos al usuario creado en MYSQL
-- GRANT ALL PRIVILEGES ON JuegoDB.* TO 'alejandro'@'%';
-- FLUSH PRIVILEGES;

-- Creación de la Base de Datos
CREATE DATABASE IF NOT EXISTS JuegoDB;

USE JuegoDB;

-- Creación de la Tabla Oficinas
CREATE TABLE Jugadores (
    dni VARCHAR(9) PRIMARY KEY,
    nombreCompleto VARCHAR(50)
);

CREATE TABLE Partidas (
    dni VARCHAR(9),
    fechaPartida DATE,
    puntuacion DOUBLE,
    intentos INT,
    FOREIGN KEY (dni) REFERENCES Jugadores(dni)
);

-- Inserción de Datos
INSERT INTO Jugadores (dni, nombreCompleto) VALUES
('00000000A', 'Juan Torres'),
('11111111B', 'Sara Gonzalez');

INSERT INTO Partidas (dni, fechaPartida, puntuacion, intentos) VALUES
('00000000A', '2024-05-19', 2, 5),
('00000000A', '2024-05-19', 10, 1),
('11111111B', '2024-05-20', 1.9, 6),
('11111111B', '2024-05-20', 1.8, 7);
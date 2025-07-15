SET NAMES utf8mb4;
SET CHARACTER SET utf8mb4;

-- Script para crear la base de datos y las tablas necesarias
CREATE DATABASE IF NOT EXISTS patients_db
CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;

USE patients_db;

-- Tabla patient
CREATE TABLE patients (
    id INT AUTO_INCREMENT PRIMARY KEY,
    dni VARCHAR(20) NOT NULL UNIQUE,
    first_name VARCHAR(50) NOT NULL,
    last_name VARCHAR(50) NOT NULL,
    social_security VARCHAR(50) NOT NULL,
    email VARCHAR(100) NOT NULL UNIQUE,
    phone_number VARCHAR(20) NOT NULL UNIQUE,
    member_number VARCHAR(20) NOT NULL UNIQUE,
    birth_date DATE NOT NULL,
    is_active BOOLEAN DEFAULT TRUE
);

-- Datos de ejemplo para la tabla patient
INSERT INTO patients (dni, first_name, last_name, social_security, email, phone_number, member_number, birth_date, is_active) VALUES
('12345678', 'Carlos', 'Pérez', 'OSDE', 'carlos.perez@example.com', '111-1111', '1234567890', '1980-01-01', TRUE),
('23456789', 'Ana', 'Gómez', 'Swiss Medical', 'ana.gomez@example.com', '222-2222', '2345678901', '1990-02-02', TRUE),
('34567890', 'Luis', 'Martínez', 'OSDE', 'luis.martinez@example.com', '333-3333', '3456789012', '1985-03-03', TRUE),
('45678901', 'María', 'López', 'Galeno', 'maria.lopez@example.com', '444-4444', '4567890123', '1995-04-04', TRUE),
('56789012', 'Jorge', 'Sánchez', 'OSDE', 'jorge.sanchez@example.com', '555-5555', '5678901234', '1988-05-05', TRUE),
('67890123', 'Lucía', 'Fernández', 'Swiss Medical', 'lucia.fernandez@example.com', '666-6666', '6789012345', '1992-06-06', TRUE),
('78901234', 'Pedro', 'Ramírez', 'Medicus', 'pedro.ramirez@example.com', '777-7777', '7890123456', '1983-07-07', TRUE),
('89012345', 'Laura', 'Suárez', 'Galeno', 'laura.suarez@example.com', '888-8888', '8901234567', '1991-08-08', TRUE),
('90123456', 'Sofía', 'Gutiérrez', 'OSDE', 'sofia.gutierrez@example.com', '999-9999', '9012345678', '1987-09-09', TRUE),
('01234567', 'Diego', 'Herrera', 'Medicus', 'diego.herrera@example.com', '101-0101', '0123456789', '1984-10-10', TRUE);


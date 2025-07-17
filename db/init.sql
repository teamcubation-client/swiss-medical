SET NAMES 'utf8mb4';
SET CHARACTER SET utf8mb4;

CREATE DATABASE IF NOT EXISTS pacientes_db
DEFAULT CHARACTER SET utf8mb4
COLLATE utf8mb4_unicode_ci;

USE pacientes_db;

CREATE TABLE IF NOT EXISTS pacientes (
                                         id BIGINT AUTO_INCREMENT PRIMARY KEY,
                                         first_name VARCHAR(255) NOT NULL,
    last_name VARCHAR(255) NOT NULL,
    dni VARCHAR(20) NOT NULL UNIQUE,
    health_insurance VARCHAR(255),
    health_plan VARCHAR(255),
    address VARCHAR(255),
    phone_number VARCHAR(50),
    email VARCHAR(255),
    active BOOLEAN NOT NULL DEFAULT TRUE,
    creation_date DATETIME NOT NULL,
    last_modified_date DATETIME
    );

INSERT INTO pacientes (
    first_name, last_name, dni, health_insurance, health_plan, email, phone_number, address, active, creation_date
) VALUES
      ('Carlos', 'Pérez', '12345678', 'OSDE', 'Plan 210', 'carlos.perez@example.com', '111-1111', 'Calle Falsa 123', true, NOW()),
      ('Ana', 'Gómez', '23456789', 'Swiss Medical', 'SMG 500', 'ana.gomez@example.com', '222-2222', 'Av. Libertador 456', true, NOW()),
      ('Luis', 'Martínez', '34567890', 'OSDE', 'Plan 310', 'luis.martinez@example.com', '333-3333', 'Calle 9 de Julio 789', true, NOW()),
      ('María', 'López', '45678901', 'Galeno', 'Plan Premium', 'maria.lopez@example.com', '444-4444', 'Diag. Norte 321', true, NOW()),
      ('Jorge', 'Sánchez', '56789012', 'OSDE', 'Plan 210', 'jorge.sanchez@example.com', '555-5555', 'San Martín 654', true, NOW()),
      ('Lucía', 'Fernández', '67890123', 'Swiss Medical', 'SMG 400', 'lucia.fernandez@example.com', '666-6666', 'Independencia 987', true, NOW()),
      ('Pedro', 'Ramírez', '78901234', 'Medicus', 'Clásico', 'pedro.ramirez@example.com', '777-7777', 'Belgrano 741', true, NOW()),
      ('Laura', 'Suárez', '89012345', 'Galeno', 'Plan Joven', 'laura.suarez@example.com', '888-8888', 'Alem 852', true, NOW()),
      ('Sofía', 'Gutiérrez', '90123456', 'OSDE', 'Plan 410', 'sofia.gutierrez@example.com', '999-9999', 'Mitre 963', true, NOW()),
      ('Diego', 'Herrera', '01234567', 'Medicus', 'Premium Plus', 'diego.herrera@example.com', '101-0101', 'Urquiza 159', true, NOW());

DELIMITER //

CREATE PROCEDURE find_patient_by_dni(IN dni VARCHAR(20))
BEGIN
SELECT id, dni, first_name, last_name, health_insurance, health_plan, address, email, phone_number, active, creation_date, last_modified_date
FROM pacientes
WHERE pacientes.dni = dni;
END;
//

CREATE PROCEDURE find_patients_by_first_name(IN first_name VARCHAR(50))
BEGIN
SELECT id, dni, first_name, last_name, health_insurance, health_plan, address, email, phone_number, active, creation_date, last_modified_date
FROM pacientes
WHERE LOWER(pacientes.first_name) LIKE CONCAT('%', LOWER(first_name), '%');
END;
//

CREATE PROCEDURE find_patients_by_health_insurance_paginated(
    IN p_health_insurance VARCHAR(50),
    IN p_limit INT,
    IN p_offset INT
)
BEGIN
SELECT id, dni, first_name, last_name, health_insurance, health_plan, address, email, phone_number, active, creation_date, last_modified_date
FROM pacientes
WHERE health_insurance = p_health_insurance
    LIMIT p_limit OFFSET p_offset;
END;
//

DELIMITER ;


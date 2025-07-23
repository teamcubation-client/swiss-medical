-- Configurar conexión y base de datos
SET NAMES utf8mb4 COLLATE utf8mb4_unicode_ci;
SET CHARACTER SET utf8mb4;
SET collation_connection = utf8mb4_unicode_ci;

-- Dropear la tabla y procedimientos si existen
DROP PROCEDURE IF EXISTS buscar_paciente_por_dni;
DROP PROCEDURE IF EXISTS buscar_pacientes_por_nombre;
DROP PROCEDURE IF EXISTS buscar_pacientes_por_obra_social_paginado;
DROP TABLE IF EXISTS pacientes;

-- Crear tabla
CREATE TABLE IF NOT EXISTS pacientes (
    dni VARCHAR(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL PRIMARY KEY,
    nombre VARCHAR(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL,
    apellido VARCHAR(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL,
    obra_social VARCHAR(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci,
    email VARCHAR(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci,
    telefono VARCHAR(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci
) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;

-- Insertar datos
INSERT INTO pacientes (dni, nombre, apellido, obra_social, email, telefono) VALUES
                                                                               ('12345678', 'Carlos', 'Pérez', 'OSDE', 'carlos.perez@example.com', '111111111'),
                                                                               ('23456789', 'Ana', 'Gómez', 'Swiss Medical', 'ana.gomez@example.com', '222222222'),
                                                                               ('34567890', 'Luis', 'Martínez', 'OSDE', 'luis.martinez@example.com', '333333333'),
                                                                               ('45678901', 'María', 'López', 'Galeno', 'maria.lopez@example.com', '444444444'),
                                                                               ('56789012', 'Jorge', 'Sánchez', 'OSDE', 'jorge.sanchez@example.com', '555555555'),
                                                                               ('67890123', 'Lucía', 'Fernández', 'Swiss Medical', 'lucia.fernandez@example.com', '666666666'),
                                                                               ('78901234', 'Pedro', 'Ramírez', 'Medicus', 'pedro.ramirez@example.com', '777777777'),
                                                                               ('89012345', 'Laura', 'Suárez', 'Galeno', 'laura.suarez@example.com', '888888888'),
                                                                               ('90123456', 'Sofía', 'Gutiérrez', 'OSDE', 'sofia.gutierrez@example.com', '999999999'),
                                                                               ('01234567', 'Diego', 'Herrera', 'Medicus', 'diego.herrera@example.com', '101010101');

-- Procedimiento 1: buscar paciente por DNI
DELIMITER //

CREATE PROCEDURE buscar_paciente_por_dni(
    IN p_dni VARCHAR(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci
)
BEGIN
SELECT dni, nombre, apellido, obra_social, email, telefono
FROM pacientes
WHERE dni COLLATE utf8mb4_unicode_ci = p_dni COLLATE utf8mb4_unicode_ci;
END;
//

-- Procedimiento 2: buscar pacientes por nombre parcial (case-insensitive)
CREATE PROCEDURE buscar_pacientes_por_nombre(
    IN p_nombre VARCHAR(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci
)
BEGIN
SELECT dni, nombre, apellido, obra_social, email, telefono
FROM pacientes
WHERE LOWER(nombre COLLATE utf8mb4_unicode_ci) LIKE CONCAT('%', LOWER(p_nombre COLLATE utf8mb4_unicode_ci), '%');
END;
//

-- Procedimiento 3: buscar pacientes por obra social con paginación
CREATE PROCEDURE buscar_pacientes_por_obra_social_paginado(
    IN p_obra_social VARCHAR(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci,
    IN p_limit INT,
    IN p_offset INT
)
BEGIN
SELECT dni, nombre, apellido, obra_social, email, telefono
FROM pacientes
WHERE obra_social COLLATE utf8mb4_unicode_ci = p_obra_social COLLATE utf8mb4_unicode_ci
    LIMIT p_limit OFFSET p_offset;
END;
//

DELIMITER ;

-- agregué esto porque si no me tiraba error al crear el container.
CREATE TABLE IF NOT EXISTS pacientes (
    dni VARCHAR(20) NOT NULL PRIMARY KEY,
    nombre VARCHAR(255) NOT NULL,
    apellido VARCHAR(255) NOT NULL,
    obra_social VARCHAR(255),
    email VARCHAR(255),
    telefono VARCHAR(50)
);


INSERT INTO pacientes (dni, nombre, apellido, obra_social, email, telefono) VALUES
('12345678', 'Carlos', 'Pérez', 'OSDE', 'carlos.perez@example.com', '111-1111'),
('23456789', 'Ana', 'Gómez', 'Swiss Medical', 'ana.gomez@example.com', '222-2222'),
('34567890', 'Luis', 'Martínez', 'OSDE', 'luis.martinez@example.com', '333-3333'),
('45678901', 'María', 'López', 'Galeno', 'maria.lopez@example.com', '444-4444'),
('56789012', 'Jorge', 'Sánchez', 'OSDE', 'jorge.sanchez@example.com', '555-5555'),
('67890123', 'Lucía', 'Fernández', 'Swiss Medical', 'lucia.fernandez@example.com', '666-6666'),
('78901234', 'Pedro', 'Ramírez', 'Medicus', 'pedro.ramirez@example.com', '777-7777'),
('89012345', 'Laura', 'Suárez', 'Galeno', 'laura.suarez@example.com', '888-8888'),
('90123456', 'Sofía', 'Gutiérrez', 'OSDE', 'sofia.gutierrez@example.com', '999-9999'),
('01234567', 'Diego', 'Herrera', 'Medicus', 'diego.herrera@example.com', '101-0101');

-- Procedimiento 1: buscar paciente por DNI
DELIMITER //

CREATE PROCEDURE buscar_paciente_por_dni(IN p_dni VARCHAR(20))
BEGIN
    SELECT dni, nombre, apellido, obra_social, email, telefono
    FROM pacientes
    WHERE dni = p_dni;
END;
//

-- Procedimiento 2: buscar pacientes por nombre parcial (case-insensitive)
CREATE PROCEDURE buscar_pacientes_por_nombre(IN p_nombre VARCHAR(50))
BEGIN
    SELECT dni, nombre, apellido, obra_social, email, telefono
    FROM pacientes
    WHERE LOWER(nombre) LIKE CONCAT('%', LOWER(p_nombre), '%');
END;
//

-- Procedimiento 3: buscar pacientes por obra social con paginación
CREATE PROCEDURE buscar_pacientes_por_obra_social_paginado(
    IN p_obra_social VARCHAR(50),
    IN p_limit INT,
    IN p_offset INT
)
BEGIN
    SELECT dni, nombre, apellido, obra_social, email, telefono
    FROM pacientes
    WHERE obra_social = p_obra_social
    LIMIT p_limit OFFSET p_offset;
END;
//

DELIMITER ;

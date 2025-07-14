CREATE TABLE IF NOT EXISTS paciente (
    id INT AUTO_INCREMENT PRIMARY KEY,
    dni VARCHAR(255),
    nombre VARCHAR(255),
    apellido VARCHAR(255),
    obra_social VARCHAR(255),
    email VARCHAR(255),
    telefono VARCHAR(255),
    domicilio VARCHAR(255),
    estado_civil VARCHAR(255),
    fecha_nacimiento DATE
);


INSERT INTO paciente (dni, nombre, apellido, obra_social, email, telefono, domicilio, estado_civil, fecha_nacimiento) VALUES
('12345678', 'Carlos', 'Pérez', 'OSDE', 'carlos.perez@example.com', '111-1111', 'Av. Rivadavia 1000', 'Casado', '1980-05-10'),
('23456789', 'Ana', 'Gómez', 'Swiss Medical', 'ana.gomez@example.com', '222-2222', 'Calle Falsa 123', 'Soltera', '1990-08-15'),
('34567890', 'Luis', 'Martínez', 'OSDE', 'luis.martinez@example.com', '333-3333', 'San Martín 456', 'Divorciado', '1975-12-01'),
('45678901', 'María', 'López', 'Galeno', 'maria.lopez@example.com', '444-4444', 'Av. Belgrano 789', 'Casada', '1988-03-22'),
('56789012', 'Jorge', 'Sánchez', 'OSDE', 'jorge.sanchez@example.com', '555-5555', 'Mitre 345', 'Soltero', '1979-07-30'),
('67890123', 'Lucía', 'Fernández', 'Swiss Medical', 'lucia.fernandez@example.com', '666-6666', 'Corrientes 2200', 'Soltera', '1993-11-05'),
('78901234', 'Pedro', 'Ramírez', 'Medicus', 'pedro.ramirez@example.com', '777-7777', 'Lavalle 1340', 'Casado', '1985-09-18'),
('89012345', 'Laura', 'Suárez', 'Galeno', 'laura.suarez@example.com', '888-8888', 'Anchorena 210', 'Viuda', '1991-02-27'),
('90123456', 'Sofía', 'Gutiérrez', 'OSDE', 'sofia.gutierrez@example.com', '999-9999', 'French 1500', 'Soltera', '1996-06-12'),
('01234567', 'Diego', 'Herrera', 'Medicus', 'diego.herrera@example.com', '101-0101', 'Urquiza 800', 'Casado', '1982-10-09');



DELIMITER //

CREATE PROCEDURE buscar_paciente_por_dni(IN p_dni VARCHAR(20))
BEGIN
    SELECT id, dni, nombre, apellido, obra_social, email, telefono, domicilio, estado_civil, fecha_nacimiento
    FROM paciente
    WHERE dni = p_dni;
END;
//

CREATE PROCEDURE buscar_pacientes_por_nombre(IN p_nombre VARCHAR(50))
BEGIN
    SELECT id, dni, nombre, apellido, obra_social, email, telefono, domicilio, estado_civil, fecha_nacimiento
    FROM paciente
    WHERE LOWER(nombre) LIKE CONCAT('%', LOWER(p_nombre), '%');
END;
//


CREATE PROCEDURE buscar_pacientes_por_obra_social_paginado(
    IN p_obra_social VARCHAR(50),
    IN p_limit INT,
    IN p_offset INT
)
BEGIN
    SELECT id, dni, nombre, apellido, obra_social, email, telefono, domicilio, estado_civil, fecha_nacimiento
    FROM paciente
    WHERE obra_social = p_obra_social
    LIMIT p_limit OFFSET p_offset;
END;
//

DELIMITER ;

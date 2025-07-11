CREATE TABLE paciente (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    nombre VARCHAR(100) NOT NULL,
    apellido VARCHAR(100) NOT NULL,
    dni VARCHAR(20) NOT NULL UNIQUE,
    obra_social VARCHAR(100) NOT NULL,
    email VARCHAR(150) NOT NULL,
    telefono VARCHAR(50) NOT NULL,
    tipo_plan_obra_social VARCHAR(100) NOT NULL,
    fecha_alta DATE NOT NULL,
    estado BOOLEAN NOT NULL
);


INSERT INTO paciente (nombre, apellido, dni, obra_social, email, telefono, tipo_plan_obra_social, fecha_alta, estado) VALUES
('Carlos', 'Perez', '12345678', 'OSDE', 'carlos.perez@example.com', '111-1111', 'Premium', '2023-01-10', true),
('Ana', 'Gomez', '23456789', 'Swiss Medical', 'ana.gomez@example.com', '222-2222', 'Clasico', '2022-11-15', true),
('Luis', 'Martinez', '34567890', 'OSDE', 'luis.martinez@example.com', '333-3333', 'Premium', '2023-05-01', true),
('Maria', 'Lopez', '45678901', 'Galeno', 'maria.lopez@example.com', '444-4444', 'Plan Azul', '2021-09-12', false),
('Jorge', 'Sanchez', '56789012', 'OSDE', 'jorge.sanchez@example.com', '555-5555', 'Basico', '2020-03-30', true),
('Lucia', 'Fernandez', '67890123', 'Swiss Medical', 'lucia.fernandez@example.com', '666-6666', 'Clasico', '2023-02-20', true),
('Pedro', 'Ramirez', '78901234', 'Medicus', 'pedro.ramirez@example.com', '777-7777', 'Integral', '2022-07-18', false),
('Laura', 'Suarez', '89012345', 'Galeno', 'laura.suarez@example.com', '888-8888', 'Plan Azul', '2023-06-05', true),
('Sofia', 'Gutierrez', '90123456', 'OSDE', 'sofia.gutierrez@example.com', '999-9999', 'Premium', '2022-10-10', true),
('Diego', 'Herrera', '01234567', 'Medicus', 'diego.herrera@example.com', '101-0101', 'Integral', '2021-12-01', true);


DELIMITER //

CREATE PROCEDURE buscar_paciente_por_dni (IN p_dni VARCHAR(20))
BEGIN
    SELECT  id, nombre, apellido, dni, obra_social, email, telefono, tipo_plan_obra_social, fecha_alta, estado
    FROM paciente
    WHERE dni = p_dni;
END //


CREATE PROCEDURE buscar_pacientes_por_nombre (IN p_nombre VARCHAR(50))
BEGIN
    SELECT  id, nombre, apellido, dni, obra_social, email, telefono, tipo_plan_obra_social, fecha_alta, estado
    FROM paciente
    WHERE LOWER(nombre) LIKE CONCAT('%', LOWER(p_nombre), '%');
END //

CREATE PROCEDURE buscar_pacientes_por_obra_social_paginado(
    IN p_obra_social VARCHAR(50),
    IN p_limit INT,
    IN p_offset INT
)
BEGIN
    SELECT  id, nombre, apellido, dni, obra_social, email, telefono, tipo_plan_obra_social, fecha_alta, estado
    FROM paciente
    WHERE obra_social = p_obra_social
    LIMIT p_limit OFFSET p_offset;
END //

DELIMITER ;
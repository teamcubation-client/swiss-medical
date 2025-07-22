CREATE TABLE IF NOT EXISTS paciente  (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    nombre VARCHAR(100) NOT NULL,
    apellido VARCHAR(100) NOT NULL,
    dni VARCHAR(20) NOT NULL UNIQUE,
    obra_social VARCHAR(100),
    email VARCHAR(150),
    telefono VARCHAR(50)
);

INSERT INTO paciente (nombre, apellido, dni, obra_social, email, telefono)
VALUES
('Juan', 'Pérez', '12345678', 'Swiss Medical', 'juan.perez@mail.com', '1122334455'),
('Juan', 'Gómez', '87654321', 'Swiss Medical', 'juan.gomez@mail.com', '1199887766'),
('Ana', 'Rodríguez', '45678912', 'Swiss Medical', 'ana.rodriguez@mail.com', '1144556677'),
('Ana', 'Fernández', '23456789', 'Swiss Medical', 'ana.fernandez@mail.com', '1166778899'),
('María', 'Martínez', '34567890', 'Swiss Medical', 'maria.martinez@mail.com', '1177889900'),
('María', 'López', '56789012', 'Swiss Medical', 'maria.lopez@mail.com', '1188990011'),
('Diego', 'Sánchez', '67890123', 'Swiss Medical', 'diego.sanchez@mail.com', '1199001122'),
('Diego', 'Torres', '78901234', 'Swiss Medical', 'diego.torres@mail.com', '1100112233'),
('Martín', 'Ramírez', '89012345', 'Swiss Medical', 'martin.ramirez@mail.com', '1111223344'),
('Martín', 'Vargas', '90123456', 'Swiss Medical', 'martin.vargas@mail.com', '1122334455')
ON DUPLICATE KEY UPDATE
  nombre = VALUES(nombre),
  apellido = VALUES(apellido),
  obra_social = VALUES(obra_social),
  email = VALUES(email),
  telefono = VALUES(telefono);
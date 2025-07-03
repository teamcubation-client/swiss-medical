CREATE TABLE pacientes (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    nombre VARCHAR(255) NOT NULL,
    apellido VARCHAR(255) NOT NULL,
    dni VARCHAR(50) NOT NULL,
    obra_social VARCHAR(255),
    email VARCHAR(255),
    telefono VARCHAR(50)
);
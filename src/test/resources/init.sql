SET NAMES utf8mb4 COLLATE utf8mb4_unicode_ci;
SET CHARACTER SET utf8mb4;
SET collation_connection = utf8mb4_unicode_ci;

DROP PROCEDURE IF EXISTS buscar_paciente_por_dni;
DROP PROCEDURE IF EXISTS buscar_pacientes_por_nombre;
DROP PROCEDURE IF EXISTS buscar_pacientes_por_obra_social_paginado;
DROP TABLE IF EXISTS pacientes;

CREATE TABLE IF NOT EXISTS pacientes (
    dni VARCHAR(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL PRIMARY KEY,
    nombre VARCHAR(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL,
    apellido VARCHAR(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL,
    obra_social VARCHAR(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci,
    email VARCHAR(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci,
    telefono VARCHAR(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci
) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;

CREATE PROCEDURE buscar_paciente_por_dni(IN p_dni VARCHAR(20))
BEGIN
SELECT dni, nombre, apellido, obra_social, email, telefono
FROM pacientes
WHERE dni = p_dni;
END;

CREATE PROCEDURE buscar_pacientes_por_nombre(IN p_nombre VARCHAR(50))
BEGIN
SELECT dni, nombre, apellido, obra_social, email, telefono
FROM pacientes
WHERE LOWER(nombre) LIKE CONCAT('%', LOWER(p_nombre), '%');
END;

CREATE PROCEDURE buscar_pacientes_por_obra_social_paginado(IN p_obra_social VARCHAR(50), IN p_limit INT, IN p_offset INT)
BEGIN
SELECT dni, nombre, apellido, obra_social, email, telefono
FROM pacientes
WHERE obra_social = p_obra_social
    LIMIT p_limit OFFSET p_offset;
END;

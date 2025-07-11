SET NAMES utf8mb4;
SET CHARACTER SET utf8mb4;


-- Procedimiento 1: buscar paciente por DNI
DELIMITER $$

CREATE PROCEDURE buscar_paciente_por_dni(IN p_dni VARCHAR(20))
BEGIN
    SELECT dni, first_name, last_name, social_security, email, phone_number, member_number, birth_date, is_active
    FROM pacients
    WHERE dni = p_dni;
END$$

-- Procedimiento 2: buscar pacientes por first_name parcial (case-insensitive)
CREATE PROCEDURE buscar_pacientes_por_first_name(IN p_first_name VARCHAR(50))
BEGIN
    SELECT dni, first_name, last_name, social_security, email, phone_number, member_number, birth_date, is_active
    FROM pacients
    WHERE LOWER(first_name) LIKE CONCAT('%', LOWER(p_first_name), '%');
END$$

-- Procedimiento 3: buscar pacientes por obra social con paginación
CREATE PROCEDURE buscar_pacientes_por_social_security_paginado(
    IN p_social_security VARCHAR(50),
    IN p_limit INT,
    IN p_offset INT
)
BEGIN
    SELECT dni, first_name, last_name, social_security, email, phone_number, member_number, birth_date, is_active
    FROM pacients
    WHERE social_security = p_social_security
    LIMIT p_limit OFFSET p_offset;
END$$

DELIMITER ;

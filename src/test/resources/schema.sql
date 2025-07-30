CREATE TABLE IF NOT EXISTS pacientes (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    first_name VARCHAR(255),
    last_name VARCHAR(255),
    dni VARCHAR(255) NOT NULL UNIQUE,
    birth_date DATE NOT NULL,
    health_insurance VARCHAR(255),
    health_plan VARCHAR(255),
    address VARCHAR(255),
    phone_number VARCHAR(255),
    email VARCHAR(255),
    active BOOLEAN DEFAULT TRUE,
    creation_date TIMESTAMP NOT NULL,
    last_modified_date TIMESTAMP
    );

CREATE ALIAS IF NOT EXISTS find_patient_by_dni AS '
    ResultSet findPatientByDni(Connection conn, String dni) throws SQLException {
        PreparedStatement stmt = conn.prepareStatement(
            "SELECT * FROM pacientes WHERE dni = ? AND active = true"
        );
        stmt.setString(1, dni);
        return stmt.executeQuery();
    }
';

CREATE ALIAS IF NOT EXISTS find_patients_by_first_name AS '
    ResultSet findPatientsByFirstName(Connection conn, String firstName) throws SQLException {
        PreparedStatement stmt = conn.prepareStatement(
            "SELECT * FROM pacientes WHERE LOWER(first_name) LIKE LOWER(?) AND active = true"
        );
        stmt.setString(1, "%" + firstName + "%");
        return stmt.executeQuery();
    }
';

CREATE ALIAS IF NOT EXISTS find_patients_by_health_insurance_paginated AS '
    ResultSet findPatientsByHealthInsurance(Connection conn, String healthInsurance, Integer limit, Integer offset) throws SQLException {
        PreparedStatement stmt = conn.prepareStatement(
            "SELECT * FROM pacientes WHERE health_insurance = ? AND active = true LIMIT ? OFFSET ?"
        );
        stmt.setString(1, healthInsurance);
        stmt.setInt(2, limit != null ? limit : 10);
        stmt.setInt(3, offset != null ? offset : 0);
        return stmt.executeQuery();
    }
';
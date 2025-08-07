CREATE TABLE IF NOT EXISTS paciente(
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


DROP ALIAS IF EXISTS buscar_paciente_por_dni;
CREATE ALIAS buscar_paciente_por_dni AS '
    java.sql.ResultSet buscarPacientePorDni(java.sql.Connection conn, String p_dni) throws java.sql.SQLException {
        java.sql.PreparedStatement stmt = conn.prepareStatement(
            "SELECT * "
          + "FROM paciente WHERE dni = ?"
        );
        stmt.setString(1, p_dni);
        return stmt.executeQuery();
    }
';

DROP ALIAS IF EXISTS buscar_pacientes_por_nombre;
CREATE ALIAS buscar_pacientes_por_nombre AS '
    java.sql.ResultSet buscarPacientesPorNombre(
        java.sql.Connection conn,
        String p_nombre
    ) throws java.sql.SQLException {
        java.sql.PreparedStatement stmt = conn.prepareStatement(
            "SELECT * FROM paciente WHERE LOWER(nombre) LIKE ''%'' || LOWER(?) || ''%''"
        );
        stmt.setString(1, p_nombre);
        return stmt.executeQuery();
    }
';


DROP ALIAS IF EXISTS buscar_pacientes_por_obra_social_paginado;
CREATE ALIAS buscar_pacientes_por_obra_social_paginado AS '
    java.sql.ResultSet buscarPacientesPorObraSocialPaginado(
        java.sql.Connection conn,
        String p_obra_social,
        int p_limit,
        int p_offset
    ) throws java.sql.SQLException {
        java.sql.PreparedStatement stmt = conn.prepareStatement(
            "SELECT *"
          + "FROM paciente WHERE obra_social = ? LIMIT ? OFFSET ?"
        );
        stmt.setString(1, p_obra_social);
        stmt.setInt(2, p_limit);
        stmt.setInt(3, p_offset);
        return stmt.executeQuery();
    }
';

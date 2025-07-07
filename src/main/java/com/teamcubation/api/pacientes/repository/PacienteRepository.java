package com.teamcubation.api.pacientes.repository;

import com.teamcubation.api.pacientes.model.Paciente;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Repository
public class PacienteRepository implements IPacienteRepository {

    private final JdbcTemplate jdbcTemplate;

    public PacienteRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public Paciente guardar(Paciente paciente) {
        String sql = "INSERT INTO pacientes (nombre, apellido, dni, obra_social, email, telefono) VALUES (?, ?, ?, ?, ?, ?)";

        KeyHolder keyHolder = new GeneratedKeyHolder();

        jdbcTemplate.update(connection -> {
            PreparedStatement ps = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            ps.setString(1, paciente.getNombre());
            ps.setString(2, paciente.getApellido());
            ps.setString(3, paciente.getDni());
            ps.setString(4, paciente.getObraSocial());
            ps.setString(5, paciente.getEmail());
            ps.setString(6, paciente.getTelefono());
            return ps;
        }, keyHolder);

        paciente.setId(keyHolder.getKey().longValue());
        return paciente;

    }

    @Override
    public Optional<Paciente> buscarPorID(Long id) {
        String sql = "SELECT * FROM pacientes WHERE id = ?";
        try {
            Paciente paciente = jdbcTemplate.queryForObject(
                    sql,
                    new Object[]{id},
                    new RowMapper<Paciente>() {
                        @Override
                        public Paciente mapRow(ResultSet rs, int rowNum) throws SQLException {
                            Paciente paciente = new Paciente();
                            paciente.setId(rs.getLong("id"));
                            paciente.setNombre(rs.getString("nombre"));
                            paciente.setApellido(rs.getString("apellido"));
                            paciente.setDni(rs.getString("dni"));
                            paciente.setObraSocial(rs.getString("obra_social"));
                            paciente.setEmail(rs.getString("email"));
                            paciente.setTelefono(rs.getString("telefono"));
                            return paciente;
                        }
                    }
            );
            return Optional.of(paciente);
        } catch (EmptyResultDataAccessException e) {
            return Optional.empty();
        }
    }

    @Override
    public List<Paciente> buscarTodos(String dni, String nombre) {
        StringBuilder sql = new StringBuilder("SELECT * FROM pacientes WHERE 1=1");
        List<Object> params = new ArrayList<>();

        if (dni != null && !dni.isEmpty()) {
            sql.append(" AND dni = ?");
            params.add(dni);
        }

        if (nombre != null && !nombre.isEmpty()) {
            sql.append(" AND LOWER(nombre) LIKE ?");
            params.add("%" + nombre.toLowerCase() + "%");
        }

        return jdbcTemplate.query(sql.toString(), params.toArray(), new RowMapper<Paciente>() {
            @Override
            public Paciente mapRow(ResultSet rs, int rowNum) throws SQLException {
                Paciente paciente = new Paciente();
                paciente.setId(rs.getLong("id"));
                paciente.setNombre(rs.getString("nombre"));
                paciente.setApellido(rs.getString("apellido"));
                paciente.setDni(rs.getString("dni"));
                paciente.setObraSocial(rs.getString("obra_social"));
                paciente.setEmail(rs.getString("email"));
                paciente.setTelefono(rs.getString("telefono"));
                return paciente;
            }
        });
    }

    @Override
    public boolean actualizarPorID(Long id, Paciente paciente) {
        String sql = "UPDATE pacientes SET nombre = ?, apellido = ?, dni = ?, obra_social = ?, email = ?, telefono = ? WHERE id = ?";

        int filasAfectadas = jdbcTemplate.update(
                sql,
                paciente.getNombre(),
                paciente.getApellido(),
                paciente.getDni(),
                paciente.getObraSocial(),
                paciente.getEmail(),
                paciente.getTelefono(),
                paciente.getId()
        );

        return filasAfectadas > 0;
    }

    @Override
    public void borrarPorID(Long id) {
        String sql = "DELETE FROM pacientes WHERE id = ?";
        int filasAfectadas = jdbcTemplate.update(sql, id);
    }
}

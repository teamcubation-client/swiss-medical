package com.tq.pacientes.dtos;

public record PacienteDTO(
        String nombre,
        String apellido,
        String dni,
        String obraSocial) {
}

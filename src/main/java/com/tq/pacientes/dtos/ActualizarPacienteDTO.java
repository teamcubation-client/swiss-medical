package com.tq.pacientes.dtos;

public record ActualizarPacienteDTO(
        String dni,
        String nombre,
        String apellido,
        String obraSocial,
        String email,
        String telefono) {
}

package microservice.pacientes.infrastructure.adapter.in.controller;

import org.junit.jupiter.api.Test;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;


public class PacienteDTOTest {

    @Test
    void builderTest() {
        LocalDate fecha = LocalDate.now();
        PacienteDTO dto1 = PacienteDTO.builder()
                .id(1L)
                .dni("12345678")
                .nombre("Ana")
                .apellido("Lopez")
                .obraSocial("OSDE")
                .email("ana@mail.com")
                .telefono("112233456")
                .tipoPlanObraSocial("PlanA")
                .fechaAlta(fecha)
                .estado(true)
                .build();

        assertEquals(1L, dto1.getId());
        assertEquals("12345678", dto1.getDni());
        assertEquals("Ana", dto1.getNombre());
        assertEquals("Lopez", dto1.getApellido());
        assertEquals("OSDE", dto1.getObraSocial());
        assertEquals("ana@mail.com", dto1.getEmail());
        assertEquals("112233456", dto1.getTelefono());
        assertEquals("PlanA", dto1.getTipoPlanObraSocial());
        assertEquals(fecha, dto1.getFechaAlta());
        assertTrue(dto1.isEstado());
    }
}
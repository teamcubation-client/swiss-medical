package microservice.pacientes.infrastructure.adapter.in.controller;

import org.junit.jupiter.api.Test;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;


public class PacienteDTOTest {

    private static final Long id = 1L;
    private static final String dni = "12345678";
    private static final String nombre = "Ana";
    private static final String apellido = "Lopez";
    private static final String obraSocial = "OSDE";
    private static final String email = "ana@mail.com";
    private static final String telefono = "112233456";
    private static final String tipoPlan = "PlanA";
    private static final LocalDate fechaAlta = LocalDate.of(2025, 7, 30);
    private static final Boolean estado = true;

    @Test
    void builderTest() {
        PacienteDTO dto1 = PacienteDTO.builder()
                .id(id)
                .dni(dni)
                .nombre(nombre)
                .apellido(apellido)
                .obraSocial(obraSocial)
                .email(email)
                .telefono(telefono)
                .tipoPlanObraSocial(tipoPlan)
                .fechaAlta(fechaAlta)
                .estado(estado)
                .build();

        assertAll("Validar campos PacienteDTO",
                () -> assertEquals(id, dto1.getId()),
                () -> assertEquals(dni, dto1.getDni()),
                () -> assertEquals(nombre, dto1.getNombre()),
                () -> assertEquals(apellido, dto1.getApellido()),
                () -> assertEquals(obraSocial, dto1.getObraSocial()),
                () -> assertEquals(email, dto1.getEmail()),
                () -> assertEquals(telefono, dto1.getTelefono()),
                () -> assertEquals(tipoPlan, dto1.getTipoPlanObraSocial()),
                () -> assertEquals(fechaAlta, dto1.getFechaAlta()),
                () -> assertTrue(dto1.getEstado()),
                () -> assertNotNull(dto1)
                );
    }
}
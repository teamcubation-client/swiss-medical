package microservice.pacientes.infrastructure.adapter.in.controller;

import microservice.pacientes.application.domain.model.Paciente;
import microservice.pacientes.shared.PacienteNullException;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;
import static org.assertj.core.api.Assertions.assertThat;

public class PacienteResponseMapperTest {


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
    void toDTO_givenValidPaciente_returnsPacienteDTOWithSameFields() {
        Paciente model = Paciente.builder()
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

        PacienteDTO dto = PacienteResponseMapper.toDTO(model);

        assertNotNull(dto);
        assertEquals(model.getId(), dto.getId());
        assertEquals(model.getDni(), dto.getDni());
        assertEquals(model.getNombre(), dto.getNombre());
        assertEquals(model.getApellido(), dto.getApellido());
        assertEquals(model.getObraSocial(), dto.getObraSocial());
        assertEquals(model.getEmail(), dto.getEmail());
        assertEquals(model.getTelefono(), dto.getTelefono());
        assertEquals(model.getTipoPlanObraSocial(), dto.getTipoPlanObraSocial());
        assertEquals(model.getFechaAlta(), dto.getFechaAlta());
        assertTrue(dto.getEstado());
    }

    @Test
    void toModel_givenValidDTO_returnsPacienteWithSameFields() {
        PacienteDTO dto = PacienteDTO.builder()
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

        Paciente model = PacienteResponseMapper.toModel(dto);

        assertNotNull(model);
        assertThat(model)
                .usingRecursiveComparison()
                .isEqualTo(Paciente.builder()
                        .dni(dto.getDni())
                        .nombre(dto.getNombre())
                        .apellido(dto.getApellido())
                        .obraSocial(dto.getObraSocial())
                        .email(dto.getEmail())
                        .telefono(dto.getTelefono())
                        .tipoPlanObraSocial(dto.getTipoPlanObraSocial())
                        .fechaAlta(dto.getFechaAlta())
                        .estado(dto.getEstado())
                        .build());
    }

    @Test
    void toDTO_givenNull_throwsPacienteNullException(){
        assertThrows(PacienteNullException.class,
                () -> PacienteResponseMapper.toDTO(null));
    }

    @Test
    void toModel_givenNull_throwsPacienteNullException() {
        assertThrows(PacienteNullException.class,
                () -> PacienteResponseMapper.toModel(null));
    }
}

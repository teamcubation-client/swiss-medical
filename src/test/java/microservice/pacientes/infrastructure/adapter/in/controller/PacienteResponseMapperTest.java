package microservice.pacientes.infrastructure.adapter.in.controller;

import microservice.pacientes.application.domain.model.Paciente;
import microservice.pacientes.shared.PacienteNullException;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;

public class PacienteResponseMapperTest {

    @Test
    void PacienteResponseMapper_DTO_Model() {
        Paciente model = Paciente.builder()
                .id(1L)
                .dni("12345678")
                .nombre("Ana")
                .apellido("Lopez")
                .obraSocial("OSDE")
                .email("ana@mail.com")
                .telefono("112233456")
                .tipoPlanObraSocial("PlanA")
                .fechaAlta(LocalDate.now())
                .estado(true)
                .build();

        // Modelo → DTO
        PacienteDTO dto = PacienteResponseMapper.toDTO(model);
        assertNotNull(dto);
        assertEquals(model.getId(), dto.getId());
        assertEquals(model.getNombre(), dto.getNombre());
        assertEquals(model.getApellido(), dto.getApellido());
        assertEquals(model.getObraSocial(), dto.getObraSocial());
        assertEquals(model.getEmail(), dto.getEmail());
        assertEquals(model.getTelefono(), dto.getTelefono());
        assertEquals(model.getTipoPlanObraSocial(), dto.getTipoPlanObraSocial());
        assertEquals(model.getFechaAlta(), dto.getFechaAlta());
        assertTrue(dto.isEstado());

        // DTO → Modelo
        Paciente model2 = PacienteResponseMapper.toModel(dto);
        assertNotNull(model2);
        assertEquals(model, model2);
    }

    @Test
    void toDTO_nullThrows() {
        assertThrows(PacienteNullException.class,
                () -> PacienteResponseMapper.toDTO(null));
    }

    @Test
    void toModel_nullThrows() {
        assertThrows(PacienteNullException.class,
                () -> PacienteResponseMapper.toModel(null));
    }
}

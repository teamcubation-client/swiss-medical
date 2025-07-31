package microservice.pacientes.infrastructure.adapter.out.persistence;
import microservice.pacientes.application.domain.model.Paciente;
import microservice.pacientes.shared.PacienteNullException;
import org.junit.jupiter.api.Test;

import java.lang.reflect.Constructor;
import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;
import static org.assertj.core.api.Assertions.assertThat;

public class PacienteMapperTest {


    @Test
    void toEntity_givenValidPaciente_returnsPacienteEntityWithSameFields() {
        LocalDate fecha = LocalDate.of(2025, 7, 30);
        Paciente paciente = Paciente.builder()
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

        PacienteEntity entidad = PacienteMapper.toEntity(paciente);

        assertNotNull(entidad);
        assertEquals(paciente.getId(), entidad.getId());
        assertEquals(paciente.getDni(), entidad.getDni());
        assertEquals(paciente.getNombre(), entidad.getNombre());
        assertEquals(paciente.getApellido(), entidad.getApellido());
        assertEquals(paciente.getObraSocial(), entidad.getObraSocial());
        assertEquals(paciente.getEmail(), entidad.getEmail());
        assertEquals(paciente.getTelefono(), entidad.getTelefono());
        assertEquals(paciente.getTipoPlanObraSocial(), entidad.getTipoPlanObraSocial());
        assertEquals(paciente.getFechaAlta(), entidad.getFechaAlta());
        assertTrue(entidad.isEstado());
    }

    @Test
    void toModel_givenValidPacienteEntity_returnsPacienteModelWithSameFields() {
        LocalDate fecha = LocalDate.of(2025, 7, 30);
        PacienteEntity entidad = PacienteEntity.builder()
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

        Paciente model = PacienteMapper.toModel(entidad);

        assertNotNull(model);
        assertThat(model)
                .usingRecursiveComparison()
                .isEqualTo(Paciente.builder()
                        .id(entidad.getId())
                        .dni(entidad.getDni())
                        .nombre(entidad.getNombre())
                        .apellido(entidad.getApellido())
                        .obraSocial(entidad.getObraSocial())
                        .email(entidad.getEmail())
                        .telefono(entidad.getTelefono())
                        .tipoPlanObraSocial(entidad.getTipoPlanObraSocial())
                        .fechaAlta(entidad.getFechaAlta())
                        .estado(entidad.isEstado())
                        .build());
    }

    @Test
    void toEntity_givenNull_throwsPacienteNullException() {
        assertThrows(PacienteNullException.class,
                () -> PacienteMapper.toEntity(null));
    }

    @Test
    void toModel_givenNull_throwsPacienteNullException(){
        assertThrows(PacienteNullException.class,
                () -> PacienteMapper.toModel(null));
    }

    @Test
    void privateConstructor_whenInvokedReflectively_instantiatesObject() throws Exception {
        Constructor<PacienteMapper> constructor =
                PacienteMapper.class.getDeclaredConstructor();
        constructor.setAccessible(true);
        Object instance = constructor.newInstance();
        assertNotNull(instance);
    }
}

package microservice.pacientes.infrastructure.adapter.out.persistence;
import microservice.pacientes.application.domain.model.Paciente;
import microservice.pacientes.shared.PacienteNullException;
import org.junit.jupiter.api.Test;

import java.lang.reflect.Constructor;
import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;


public class PacienteMapperTest {

    @Test
    void pacienteMapper_Model_Entity() {
        Paciente paciente = Paciente.builder()
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

        // A entidad
        PacienteEntity entidad = PacienteMapper.toEntity(paciente);
        assertNotNull(entidad);
        assertEquals(paciente.getId(), entidad.getId());
        assertEquals(paciente.getDni(), entidad.getDni());
        assertEquals(paciente.getApellido(), entidad.getApellido());
        assertEquals(paciente.getObraSocial(), entidad.getObraSocial());
        assertEquals(paciente.getEmail(), entidad.getEmail());
        assertEquals(paciente.getTelefono(), entidad.getTelefono());
        assertEquals(paciente.getTipoPlanObraSocial(), entidad.getTipoPlanObraSocial());
        assertEquals(paciente.getFechaAlta(), entidad.getFechaAlta());
        assertTrue(entidad.isEstado());

        //a modelo
        Paciente model = PacienteMapper.toModel(entidad);
        assertNotNull(model);
        assertEquals(paciente, model);
    }

    @Test
    void toEntity_null_throws() {
        assertThrows(PacienteNullException.class,
                () -> PacienteMapper.toEntity(null));
    }

    @Test
    void toModel_null_throws() {
        assertThrows(PacienteNullException.class,
                () -> PacienteMapper.toModel(null));
    }

    @Test
    void privateConstructor_invocation() throws Exception {
        Constructor<PacienteMapper> constructor =
                PacienteMapper.class.getDeclaredConstructor();
        constructor.setAccessible(true);
        Object instance = constructor.newInstance();
        assertNotNull(instance);
    }
}

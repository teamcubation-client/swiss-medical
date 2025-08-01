package microservice.pacientes.infrastructure.adapter.out.persistence;
import microservice.pacientes.application.domain.model.Paciente;
import microservice.pacientes.shared.PacienteNotFoundException;
import microservice.pacientes.shared.PacienteNullException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;


import java.time.LocalDate;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

@ExtendWith(org.mockito.junit.jupiter.MockitoExtension.class)
public class PacientePersistenceAdapterTest {

    @Mock
    private PacienteRepository repositorio;

    @InjectMocks
    private PacientePersistenceAdapter adaptador;

    private PacienteEntity entidadActivo, entidadInactivo;
    private Paciente pacienteActivo, pacienteInactivo;
    private static final LocalDate FIXED_DATE = LocalDate.of(2025, 7, 30);

    @BeforeEach
    void setUp() {
        entidadActivo = PacienteEntity.builder()
                .id(1L)
                .dni("12345678")
                .nombre("Ana")
                .apellido("Lopez")
                .obraSocial("OSDE")
                .email("ana@mail.com")
                .telefono("112233456")
                .tipoPlanObraSocial("PlanA")
                .fechaAlta(FIXED_DATE.minusDays(1))
                .estado(true)
                .build();

        entidadInactivo = PacienteEntity.builder()
                .id(2L)
                .dni("23456789")
                .nombre("Carlos")
                .apellido("Perez")
                .obraSocial("OSDE")
                .email("carlos@mail.com")
                .telefono("22334455")
                .tipoPlanObraSocial("PlanB")
                .fechaAlta(FIXED_DATE.minusDays(1))
                .estado(false)
                .build();

        pacienteActivo = PacienteMapper.toModel(entidadActivo);
        pacienteInactivo = PacienteMapper.toModel(entidadInactivo);
    }

    @Test
    void findByNombreContainingIgnoreCase() {
        when(repositorio.findByNombreContainingIgnoreCase("an"))
                .thenReturn(List.of(entidadActivo));

        List<Paciente> out = adaptador.findByNombreContainingIgnoreCase("an");

        assertThat(out)
                .usingRecursiveFieldByFieldElementComparator()
                .containsExactly(pacienteActivo);

        verify(repositorio).findByNombreContainingIgnoreCase("an");
    }

    @Test
    void findByNombreContainingIgnoreCase_vacio() {
        when(repositorio.findByNombreContainingIgnoreCase("zzz"))
                .thenReturn(Collections.emptyList());

        assertThat(adaptador.findByNombreContainingIgnoreCase("zzz")).isEmpty();
        verify(repositorio).findByNombreContainingIgnoreCase("zzz");
    }

    @Test
    void buscarByDni() {
        when(repositorio.buscarByDni("12345678"))
                .thenReturn(Optional.of(entidadActivo));

        Optional<Paciente> paciente = adaptador.buscarByDni("12345678");

        assertThat(paciente).isPresent();
        assertThat(paciente.get())
                .usingRecursiveComparison()
                .isEqualTo(pacienteActivo);
        verify(repositorio).buscarByDni("12345678");
    }

    @Test
    void buscarByDni_vacio() {
        when(repositorio.buscarByDni("11111111"))
                .thenReturn(Optional.empty());

        assertThat(adaptador.buscarByDni("11111111")).isEmpty();
        verify(repositorio).buscarByDni("11111111");
    }

    @Test
    void buscarByNombre() {
        when(repositorio.buscarByNombre("Carlos"))
                .thenReturn(List.of(entidadInactivo));

        List<Paciente> paciente = adaptador.buscarByNombre("Carlos");

        assertThat(paciente)
                .usingRecursiveFieldByFieldElementComparator()
                .containsExactly(pacienteInactivo);

        verify(repositorio).buscarByNombre("Carlos");
    }

    @Test
    void buscarPorObraSocialPaginado() {
        when(repositorio.buscarPorObraSocialPaginado("OSDE", 5, 0))
                .thenReturn(List.of(entidadActivo, entidadInactivo));

        List<Paciente> resultado = adaptador.buscarPorObraSocialPaginado("OSDE", 5, 0);

        assertThat(resultado)
                .hasSize(2)
                .usingRecursiveFieldByFieldElementComparator()
                .containsExactly(pacienteActivo, pacienteInactivo);

        verify(repositorio).buscarPorObraSocialPaginado("OSDE", 5, 0);
    }


    @Test
    void findById_existeId() {
        when(repositorio.findById(2L)).thenReturn(Optional.of(entidadInactivo));

        Optional<Paciente> opt = adaptador.findById(2L);

        assertThat(opt).isPresent();
        assertThat(opt.get())
                .usingRecursiveComparison()
                .isEqualTo(pacienteInactivo);

        verify(repositorio).findById(2L);
    }

    @Test
    void findById_vacio() {
        when(repositorio.findById(99L)).thenReturn(Optional.empty());

        assertThat(adaptador.findById(99L)).isEmpty();
        verify(repositorio).findById(99L);
    }


    @Test
    void save() {
        Paciente pacienteNuevo = Paciente.builder()
                .nombre("Maria")
                .apellido("Dimaria")
                .dni("33333333")
                .obraSocial("PAMI")
                .email("maria@dimaria.com")
                .telefono("33113213")
                .tipoPlanObraSocial("Premium")
                .fechaAlta(FIXED_DATE)
                .estado(true)
                .build();

        PacienteEntity entidadNuevo = PacienteEntity.builder()
                .id(3L)
                .nombre(pacienteNuevo.getNombre())
                .apellido(pacienteNuevo.getApellido())
                .dni(pacienteNuevo.getDni())
                .obraSocial(pacienteNuevo.getObraSocial())
                .email(pacienteNuevo.getEmail())
                .telefono(pacienteNuevo.getTelefono())
                .tipoPlanObraSocial(pacienteNuevo.getTipoPlanObraSocial())
                .fechaAlta(pacienteNuevo.getFechaAlta())
                .estado(pacienteNuevo.isEstado())
                .build();

        when(repositorio.save(any(PacienteEntity.class))).thenReturn(entidadNuevo);

        Paciente resultado = adaptador.save(pacienteNuevo);

        assertThat(resultado.getId()).isEqualTo(3L);
        assertThat(resultado.getNombre()).isEqualTo("Maria");
        assertThat(resultado.getDni()).isEqualTo("33333333");
    }

    @Test
    void save_pacienteNulo() {
        assertThrows(PacienteNullException.class, () -> adaptador.save(null));
    }

    @Test
    void update_existePacienteNombreEstado() {
        when(repositorio.findById(1L)).thenReturn(Optional.of(entidadActivo));

        Paciente cambios = Paciente.builder()
                .id(1L).nombre("Ana Modificado")
                .estado(false).build();

        Paciente paciente = adaptador.update(cambios);

        assertThat(paciente.getNombre()).isEqualTo(cambios.getNombre());
        assertThat(paciente.isEstado()).isFalse();
    }

    @Test
    void update_pacienteCampos() {

        when(repositorio.findById(2L)).thenReturn(Optional.of(entidadInactivo));

        Paciente cambios = Paciente.builder()
                .id(2L)
                .nombre("Neo")
                .apellido("Anderson")
                .dni("77777777")
                .obraSocial("PAMI")
                .email("neo@matrix.com")
                .telefono("9999")
                .tipoPlanObraSocial("Premium")
                .fechaAlta(FIXED_DATE)
                .estado(false)
                .build();

        Paciente paciente = adaptador.update(cambios);

        assertThat(paciente.getNombre()).isEqualTo(cambios.getNombre());
        assertThat(paciente.getApellido()).isEqualTo(cambios.getApellido());
        assertThat(paciente.getDni()).isEqualTo(cambios.getDni());
        assertThat(paciente.getObraSocial()).isEqualTo(cambios.getObraSocial());
        assertThat(paciente.getEmail()).isEqualTo(cambios.getEmail());
        assertThat(paciente.getTelefono()).isEqualTo(cambios.getTelefono());
        assertThat(paciente.getTipoPlanObraSocial()).isEqualTo(cambios.getTipoPlanObraSocial());
        assertThat(paciente.getFechaAlta()).isEqualTo(cambios.getFechaAlta());
        assertThat(paciente.isEstado()).isFalse();
    }


    @Test
    void update_noExistePaciente() {
        when(repositorio.findById(99L)).thenReturn(Optional.empty());

        Paciente paciente = Paciente.builder()
                .id(99L)
                .build();

        assertThatThrownBy(() -> adaptador.update(paciente))
                .isInstanceOf(PacienteNotFoundException.class)
                .hasMessageContaining("99");
    }

    @Test
    void deleteById() {
        assertDoesNotThrow(() -> adaptador.deleteById(5L));
        verify(repositorio).deleteById(5L);
    }

    @Test
    void deleteById_NoExisteId() {
        assertThatNoException().isThrownBy(() -> adaptador.deleteById(99L));
        verify(repositorio).deleteById(99L);
    }

    @Test
    void findAll() {
        when(repositorio.findAll()).thenReturn(List.of(entidadActivo, entidadInactivo));

        List<Paciente> pacientes = adaptador.findAll();

        assertThat(pacientes)
                .usingRecursiveFieldByFieldElementComparator()
                .containsExactly(pacienteActivo, pacienteInactivo);

        verify(repositorio).findAll();
    }

    @Test
    void findAll_listaVacia() {
        when(repositorio.findAll()).thenReturn(Collections.emptyList());

        List<Paciente> pacientes = adaptador.findAll();

        assertThat(pacientes).isEmpty();
        verify(repositorio).findAll();
    }

}

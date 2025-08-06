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

import static org.assertj.core.api.AssertionsForClassTypes.assertThatNoException;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;
import static org.assertj.core.api.AssertionsForInterfaceTypes.assertThat;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;


@ExtendWith(org.mockito.junit.jupiter.MockitoExtension.class)
public class PacientePersistenceAdapterTest {

    @Mock
    private PacienteRepository repositorio;

    @InjectMocks
    private PacientePersistenceAdapter adaptador;

    private PacienteEntity entidadActivo, entidadInactivo;
    private Paciente pacienteActivo, pacienteInactivo;
    private static final LocalDate FIXED_DATE = LocalDate.of(2025, 7, 30);


    private static final Long idActivo = 1L;
    private static final String dniActivo = "12345678";
    private static final String nombreActivo = "Ana";
    private static final String apellidoActivo = "Lopez";
    private static final String obraSocialActivo = "OSDE";
    private static final String emailActivo = "ana@mail.com";
    private static final String telefonoActivo = "112233456";
    private static final String tipoPlanActivo = "PlanA";

    private static final Long idInactivo = 2L;
    private static final String dniInactivo = "23456789";
    private static final String nombreInactivo = "Carlos";
    private static final String apellidoInactivo = "Perez";
    private static final String obraSocialInactivo = "OSDE";
    private static final String emailInactivo = "carlos@mail.com";
    private static final String telefonoInactivo = "22334455";
    private static final String tipoPlanInactivo = "PlanB";

    private static final String nombreParcialCorrecto = "an";
    private static final String nombraParcialIncorrecto = "zzz";
    private static final String dniInexistente = "11111111";
    private static final Long idInexistente = 999L;

    @BeforeEach
    void setUp() {
        entidadActivo = PacienteEntity.builder()
                .id(idActivo)
                .dni(dniActivo)
                .nombre(nombreActivo)
                .apellido(apellidoActivo)
                .obraSocial(obraSocialActivo)
                .email(emailActivo)
                .telefono(telefonoActivo)
                .tipoPlanObraSocial(tipoPlanActivo)
                .fechaAlta(FIXED_DATE.minusDays(1))
                .estado(true)
                .build();

        entidadInactivo = PacienteEntity.builder()
                .id(idInactivo)
                .dni(dniInactivo)
                .nombre(nombreInactivo)
                .apellido(apellidoInactivo)
                .obraSocial(obraSocialInactivo)
                .email(emailInactivo)
                .telefono(telefonoInactivo)
                .tipoPlanObraSocial(tipoPlanInactivo)
                .fechaAlta(FIXED_DATE.minusDays(1))
                .estado(false)
                .build();

        pacienteActivo = PacienteMapper.toModel(entidadActivo);
        pacienteInactivo = PacienteMapper.toModel(entidadInactivo);
    }

    @Test
    void findByNombreContainingIgnoreCase() {
        when(repositorio.findByNombreContainingIgnoreCase(nombreParcialCorrecto))
                .thenReturn(List.of(entidadActivo));

        List<Paciente> out = adaptador.findByNombreContainingIgnoreCase(nombreParcialCorrecto);

        assertThat(out)
                .usingRecursiveFieldByFieldElementComparator()
                .containsExactly(pacienteActivo);

        verify(repositorio).findByNombreContainingIgnoreCase(nombreParcialCorrecto);
    }

    @Test
    void findByNombreContainingIgnoreCase_vacio() {
        when(repositorio.findByNombreContainingIgnoreCase(nombraParcialIncorrecto))
                .thenReturn(Collections.emptyList());

        assertThat(adaptador.findByNombreContainingIgnoreCase(nombraParcialIncorrecto)).isEmpty();
        verify(repositorio).findByNombreContainingIgnoreCase(nombraParcialIncorrecto);
    }

    @Test
    void buscarByDni() {
        when(repositorio.buscarByDni(dniActivo))
                .thenReturn(Optional.of(entidadActivo));

        Optional<Paciente> paciente = adaptador.buscarByDni(dniActivo);

        assertThat(paciente).isPresent();
        assertThat(paciente.get())
                .usingRecursiveComparison()
                .isEqualTo(pacienteActivo);
        verify(repositorio).buscarByDni(dniActivo);
    }

    @Test
    void buscarByDni_vacio() {
        when(repositorio.buscarByDni(dniInexistente))
                .thenReturn(Optional.empty());

        assertThat(adaptador.buscarByDni(dniInexistente)).isEmpty();
        verify(repositorio).buscarByDni(dniInexistente);
    }

    @Test
    void buscarByNombre() {
        when(repositorio.buscarByNombre(nombreInactivo))
                .thenReturn(List.of(entidadInactivo));

        List<Paciente> paciente = adaptador.buscarByNombre(nombreInactivo);

        assertThat(paciente)
                .usingRecursiveFieldByFieldElementComparator()
                .containsExactly(pacienteInactivo);

        verify(repositorio).buscarByNombre(nombreInactivo);
    }

    @Test
    void buscarPorObraSocialPaginado() {
        when(repositorio.buscarPorObraSocialPaginado(obraSocialActivo, 5, 0))
                .thenReturn(List.of(entidadActivo, entidadInactivo));

        List<Paciente> resultado = adaptador.buscarPorObraSocialPaginado(obraSocialActivo, 5, 0);

        assertThat(resultado)
                .hasSize(2)
                .usingRecursiveFieldByFieldElementComparator()
                .containsExactly(pacienteActivo, pacienteInactivo);

        verify(repositorio).buscarPorObraSocialPaginado(obraSocialActivo, 5, 0);
    }


    @Test
    void findById_existeId() {
        when(repositorio.findById(idInactivo)).thenReturn(Optional.of(entidadInactivo));

        Optional<Paciente> opt = adaptador.findById(idInactivo);

        assertThat(opt).isPresent();
        assertThat(opt.get())
                .usingRecursiveComparison()
                .isEqualTo(pacienteInactivo);

        verify(repositorio).findById(idInactivo);
    }

    @Test
    void findById_vacio() {
        when(repositorio.findById(idInexistente)).thenReturn(Optional.empty());

        assertThat(adaptador.findById(idInexistente)).isEmpty();
        verify(repositorio).findById(idInexistente);
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
        when(repositorio.findById(idActivo)).thenReturn(Optional.of(entidadActivo));

        Paciente cambios = Paciente.builder()
                .id(idActivo).nombre("Ana Modificado")
                .estado(false).build();

        Paciente paciente = adaptador.update(cambios);

        assertThat(paciente.getNombre()).isEqualTo(cambios.getNombre());
        assertThat(paciente.isEstado()).isFalse();
    }

    @Test
    void update_pacienteCampos() {

        when(repositorio.findById(idInactivo)).thenReturn(Optional.of(entidadInactivo));

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
        when(repositorio.findById(idInexistente)).thenReturn(Optional.empty());

        Paciente paciente = Paciente.builder()
                .id(idInexistente)
                .build();

        assertThatThrownBy(() -> adaptador.update(paciente))
                .isInstanceOf(PacienteNotFoundException.class)
                .hasMessageContaining(String.valueOf(idInexistente));
    }

    @Test
    void deleteById() {
        assertDoesNotThrow(() -> adaptador.deleteById(5L));
        verify(repositorio).deleteById(5L);
    }

    @Test
    void deleteById_NoExisteId() {
        assertThatNoException().isThrownBy(() -> adaptador.deleteById(idInexistente));
        verify(repositorio).deleteById(idInexistente);
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

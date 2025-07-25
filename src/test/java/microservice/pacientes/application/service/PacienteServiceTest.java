package microservice.pacientes.application.service;

import microservice.pacientes.application.domain.model.Paciente;
import microservice.pacientes.application.domain.port.out.PacientePortOutRead;
import microservice.pacientes.application.domain.port.out.PacientePortOutWrite;
import microservice.pacientes.shared.PacienteActivoException;
import microservice.pacientes.shared.PacienteDuplicadoException;
import microservice.pacientes.shared.PacienteNotFoundException;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class PacienteServiceTest {


    @Mock
    private PacientePortOutRead portOutRead;

    @Mock
    private PacientePortOutWrite portOutWrite;

    @InjectMocks
    private PacienteService service;

    private Paciente paciente;
    private Paciente pacienteInactivo;

    private AutoCloseable mocks;

    @BeforeEach
    void setUp() {
        // Inicializa @Mock y @InjectMocks
        mocks = MockitoAnnotations.openMocks(this);

        // Invoca el init de cadenas (equivalente a tu @PostConstruct)
        service.initValidatorChain();

        paciente = Paciente.builder()
                .id(1L)
                .dni("12345678")
                .nombre("Ana")
                .apellido("Lopez")
                .obraSocial("OSDE")
                .email("ana@mail.com")
                .telefono("112233456")
                .tipoPlanObraSocial("PlanA")
                .fechaAlta(LocalDate.now().minusDays(1))
                .estado(true)
                .build();

        pacienteInactivo = Paciente.builder()
                .id(2L)
                .dni("23456789")
                .nombre("Carlos")
                .apellido("Perez")
                .obraSocial("OSDE")
                .email("carlos@mail.com")
                .telefono("22334455")
                .tipoPlanObraSocial("PlanB")
                .fechaAlta(LocalDate.now().minusDays(1))
                .estado(false)
                .build();

    }

    @AfterEach
    void tearDown() throws Exception {
        mocks.close();
    }

    @Test
    void crearPaciente_GuardarCuandoDniNoExiste() {
        // Simula que no existe duplicado
        when(portOutRead.buscarByDni("12345678")).thenReturn(Optional.empty());
        // Simula que guardo y devuelve el mismo paciente
        when(portOutWrite.save(paciente)).thenReturn(paciente);

        Paciente result = service.crearPaciente(paciente);

        assertEquals("Ana", result.getNombre());

        // Verifica que se invocaron correctamente los puertos
        verify(portOutRead).buscarByDni("12345678");
        verify(portOutWrite).save(paciente);
    }

    @Test
    void crearPaciente_FallarSiDniDuplicado(){
        when(portOutRead.buscarByDni("12345678")).thenReturn(Optional.of(paciente));

        assertThrows(PacienteDuplicadoException.class,
                () -> service.crearPaciente(paciente));

        verify(portOutRead).buscarByDni("12345678");
        verify(portOutWrite, never()).save(any());
    }

    @Test
    void eliminarPaciente_FallarEliminarCuandoExisteYEstaActivo() {
        //findById devuelve nuestro paciente activo
        when(portOutRead.findById(1L))
                .thenReturn(Optional.of(paciente));

        assertThrows(
                PacienteActivoException.class,
                () -> service.eliminarPaciente(1L)
        );

        verify(portOutRead, atLeastOnce()).findById(1L);
        verify(portOutWrite, never()).deleteById(anyLong());
    }

    @Test
    void eliminarPaciente_EliminarSiPacienteInactivo() {

        when(portOutRead.findById(2L))
                .thenReturn(Optional.of(pacienteInactivo));

        assertDoesNotThrow(() -> service.eliminarPaciente(2L));

        verify(portOutRead, atLeastOnce()).findById(2L);
        verify(portOutWrite).deleteById(2L);
    }

    @Test
    void obtenerPacientePorId_DevolverSiExisteId(){
        when(portOutRead.findById(1L))
                .thenReturn(Optional.of(paciente));

        Paciente result = service.obtenerPacientePorId(1L);

        assertSame(paciente, result);
        verify(portOutRead).findById(1L);
    }

    @Test
    void obtenerPacientePorId_FallarSiNoExisteId(){
        when(portOutRead.findById(1L))
                .thenReturn(Optional.empty());

        PacienteNotFoundException ex = assertThrows(
                PacienteNotFoundException.class,
                () -> service.obtenerPacientePorId(1L)
        );

        assertTrue(ex.getMessage().contains("1"));

        verify(portOutRead).findById(1L);
    }

    @Test
    void buscarPorNombreParcial_ListarCuandoHayCoincidencia(){
        when(portOutRead.findByNombreContainingIgnoreCase("an"))
                .thenReturn(List.of(paciente));

        List<Paciente> result = service.buscarPorNombreParcial("an");

        assertEquals(1,result.size());
        assertEquals("Ana", result.get(0).getNombre());

        verify(portOutRead).findByNombreContainingIgnoreCase("an");

    }

    @Test
    void buscarPorNombreParcial_FallarSiNoHayCoincidencia(){
        when(portOutRead.findByNombreContainingIgnoreCase("zz"))
                .thenReturn(List.of());

        List<Paciente> result = service.buscarPorNombreParcial("zz");

        assertTrue(result.isEmpty());
        verify(portOutRead).findByNombreContainingIgnoreCase("zz");

    }

    @Test
    void buscarByDni_DevolverPacienteSiExiste(){
        when(portOutRead.buscarByDni("12345678"))
                .thenReturn(Optional.of(paciente));

        Paciente result = service.buscarByDni("12345678");

        assertSame(paciente , result);
        verify(portOutRead).buscarByDni("12345678");
    }

    @Test
    void buscarByDni_FallarSiNoExiste(){
        when(portOutRead.buscarByDni("1111111"))
                .thenReturn(Optional.empty());

        assertThrows(PacienteNotFoundException.class,
                () -> service.buscarByDni("1111111"));

        verify(portOutRead).buscarByDni("1111111");
    }

    @Test
    void buscarByNombre_ListarCuandoHayCoincidencia(){
        when(portOutRead.buscarByNombre("Ana"))
                .thenReturn(List.of(paciente));

        List<Paciente> result = service.buscarByNombre("Ana");

        assertEquals(1,result.size());
        assertEquals("Ana", result.get(0).getNombre());

        verify(portOutRead).buscarByNombre("Ana");
    }

    @Test
    void buscarByNombre_FallarCuandoNoHayCoincidencia(){
        when(portOutRead.buscarByNombre("Pepe"))
                .thenReturn(List.of());

        assertThrows(PacienteNotFoundException.class,
                () -> service.buscarByNombre("Pepe"));

        verify(portOutRead).buscarByNombre("Pepe");

    }

    @Test
    void buscarPorObraSocialPaginado_ListarCuandoHayCoincidencia() {
        when(portOutRead.buscarPorObraSocialPaginado("OSDE", 10, 0))
                .thenReturn(List.of(paciente, pacienteInactivo));

        List<Paciente> result = service.buscarPorObraSocialPaginado("OSDE", 10, 0);

        assertEquals(2, result.size());
        verify(portOutRead).buscarPorObraSocialPaginado("OSDE", 10, 0);
    }

    @Test
    void buscarPorObraSocialPaginado_FallarSiNoHayCoincidencia() {
        when(portOutRead.buscarPorObraSocialPaginado("OSDE", 10, 0))
                .thenReturn(List.of());

        assertThrows(PacienteNotFoundException.class,
                () -> service.buscarPorObraSocialPaginado("OSDE", 10, 0)
        );

        verify(portOutRead).buscarPorObraSocialPaginado("OSDE", 10, 0);
    }

    @Test
    void activarPaciente_SiExisteId(){
        pacienteInactivo.setEstado(false);
        when(portOutRead.findById(2L))
                .thenReturn(Optional.of(pacienteInactivo));

        pacienteInactivo.setEstado(true);
        when(portOutWrite.save(pacienteInactivo)).thenReturn(pacienteInactivo);

        Paciente result = service.activarPaciente(2L);

        assertTrue(result.isEstado(), "El paciente esta activado");

        verify(portOutRead).findById(2L);
        verify(portOutWrite).save(pacienteInactivo);

    }

    @Test
    void activarPaciente_FallarSiNoExisteId(){


    }
}

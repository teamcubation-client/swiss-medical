package microservice.pacientes.application.service;

import microservice.pacientes.application.domain.model.Paciente;
import microservice.pacientes.application.domain.port.out.PacientePortOutRead;
import microservice.pacientes.application.domain.port.out.PacientePortOutWrite;
import microservice.pacientes.shared.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;


@ExtendWith(MockitoExtension.class)
public class PacienteServiceTest {


    @Mock
    private PacientePortOutRead portOutRead;

    @Mock
    private PacientePortOutWrite portOutWrite;

    @InjectMocks
    private PacienteService service;

    private Paciente paciente;
    private Paciente pacienteInactivo;

    @BeforeEach
    void setUp() {
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
                .fechaAlta(LocalDate.now())
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
                .fechaAlta(LocalDate.now())
                .estado(false)
                .build();
    }

    @Test
    @DisplayName("Crear paciente con datos validos cuando no existe DNI duplicado")
    void crearPaciente_CrearExitoso() {
        when(portOutRead.buscarByDni("12345678")).thenReturn(Optional.empty());
        when(portOutWrite.save(paciente)).thenReturn(paciente);

        Paciente result = service.crearPaciente(paciente);

        assertEquals("Ana", result.getNombre());

        verify(portOutRead).buscarByDni("12345678");
        verify(portOutWrite).save(paciente);
    }

    @Test
    @DisplayName("Debe lanzar PacienteDuplicadoException si el DNI ya existe")
    void crearPaciente_FallarSiDniDuplicado(){
        when(portOutRead.buscarByDni("12345678")).thenReturn(Optional.of(paciente));

        Paciente nuevoPaciente = Paciente.builder()
                .dni("12345678")
                .build();

        assertThrows(PacienteDuplicadoException.class,
                () -> service.crearPaciente(nuevoPaciente));

        verify(portOutRead).buscarByDni("12345678");
        verify(portOutWrite, never()).save(any());
    }

    @Test
    @DisplayName("Debe lanzar InvalidEmailFormatException si el email tiene formato invalido")
    void crearPaciente_FallarSiEmailFormatoIncorrecto(){
        paciente.setEmail("correo-no-valido");

        when(portOutRead.buscarByDni("12345678"))
                .thenReturn(Optional.empty());

        assertThrows(
                InvalidEmailFormatException.class,
                () -> service.crearPaciente(paciente)
        );

        verify(portOutRead).buscarByDni("12345678");
        verify(portOutWrite, never()).save(any());
    }

    @Test
    @DisplayName("Debe lanzar InvalidFechaAltaException si la fecha de alta es futura")
    void crearPaciente_FallarSiFechaAltaFutura(){
        paciente.setFechaAlta(LocalDate.now().plusDays(2));

        when(portOutRead.buscarByDni("12345678"))
                .thenReturn(Optional.empty());

        assertThrows(
                InvalidFechaAltaException.class,
                () -> service.crearPaciente(paciente)
        );

        verify(portOutRead).buscarByDni("12345678");
        verify(portOutWrite, never()).save(any());
    }

    @Test
    @DisplayName("Eliminar paciente correctamente cuando existe e estado inactivo")
    void eliminarPaciente_EliminarExitoso() {
        when(portOutRead.findById(2L))
                .thenReturn(Optional.of(pacienteInactivo));

        assertDoesNotThrow(() -> service.eliminarPaciente(2L));

        verify(portOutRead, atLeastOnce()).findById(2L);
        verify(portOutWrite).deleteById(2L);
    }

    @Test
    @DisplayName("Debe lanzar PacienteNotFoundException al intentar eliminar un ID inexistente")
    void eliminarPaciente_FallarEliminarSiNoExisteId(){
        when(portOutRead.findById(1L))
                .thenReturn(Optional.empty());

        assertThrows(
                PacienteNotFoundException.class,
                () -> service.eliminarPaciente(1L)
        );

        verify(portOutRead, atLeastOnce()).findById(1L);
        verify(portOutWrite, never()).deleteById(anyLong());
    }

    @Test
    @DisplayName("Debe lanzar PacienteActivoException si el paciente esta activo al eliminar")
    void eliminarPaciente_FallarEliminarCuandoExisteYEstaActivo() {
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
    @DisplayName("Obtener paciente por ID exitosamente cuando existe")
    void obtenerPacientePorId_DevolverSiExisteId(){
        when(portOutRead.findById(1L))
                .thenReturn(Optional.of(paciente));

        Paciente result = service.obtenerPacientePorId(1L);

        assertSame(paciente, result);
        verify(portOutRead).findById(1L);
    }

    @Test
    @DisplayName("Debe lanzar PacienteNotFoundException si no existe paciente con ese ID")
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
    @DisplayName("Listar pacientes con coincidencia parcial de nombre")
    void buscarPorNombreParcial_ListarCuandoHayCoincidencia(){
        when(portOutRead.findByNombreContainingIgnoreCase("an"))
                .thenReturn(List.of(paciente));

        List<Paciente> result = service.buscarPorNombreParcial("an");

        assertEquals(1,result.size());
        assertEquals("Ana", result.get(0).getNombre());

        verify(portOutRead).findByNombreContainingIgnoreCase("an");

    }

    @Test
    @DisplayName("Devolver lista vacia cuando no hay coincidencias de nombre parcial")
    void buscarPorNombreParcial_FallarSiNoHayCoincidencia(){
        when(portOutRead.findByNombreContainingIgnoreCase("zz"))
                .thenReturn(List.of());

        List<Paciente> result = service.buscarPorNombreParcial("zz");

        assertTrue(result.isEmpty());
        verify(portOutRead).findByNombreContainingIgnoreCase("zz");

    }

    @Test
    @DisplayName("Obtener paciente por DNI exitosamente cuando existe")
    void buscarByDni_DevolverPacienteSiExiste(){
        when(portOutRead.buscarByDni("12345678"))
                .thenReturn(Optional.of(paciente));

        Paciente result = service.buscarByDni("12345678");

        assertSame(paciente , result);
        verify(portOutRead).buscarByDni("12345678");
    }

    @Test
    @DisplayName("Debe lanzar PacienteNotFoundException si no existe paciente con ese DNI")
    void buscarByDni_FallarSiNoExiste(){
        when(portOutRead.buscarByDni("1111111"))
                .thenReturn(Optional.empty());

        assertThrows(PacienteNotFoundException.class,
                () -> service.buscarByDni("1111111"));

        verify(portOutRead).buscarByDni("1111111");
    }

    @Test
    @DisplayName("Listar pacientes por nombre exacto cuando hay coincidencias")
    void buscarByNombre_ListarCuandoHayCoincidencia(){
        when(portOutRead.buscarByNombre("Ana"))
                .thenReturn(List.of(paciente));

        List<Paciente> result = service.buscarByNombre("Ana");

        assertEquals(1,result.size());
        assertEquals("Ana", result.get(0).getNombre());

        verify(portOutRead).buscarByNombre("Ana");
    }

    @Test
    @DisplayName("Debe lanzar PacienteNotFoundException si el resultado es null")
    void buscarByNombre_FallarSiNull() {
        when(portOutRead.buscarByNombre("Ana"))
                .thenReturn(null);

        assertThrows(
                PacienteNotFoundException.class,
                () -> service.buscarByNombre("Ana")
        );

        verify(portOutRead).buscarByNombre("Ana");
        verifyNoMoreInteractions(portOutRead, portOutWrite);
    }

    @Test
    @DisplayName("Debe lanzar PacienteNotFoundException si no hay coincidencias de nombre exacto")
    void buscarByNombre_FallarCuandoNoHayCoincidencia(){
        when(portOutRead.buscarByNombre("Pepe"))
                .thenReturn(List.of());

        assertThrows(PacienteNotFoundException.class,
                () -> service.buscarByNombre("Pepe"));

        verify(portOutRead).buscarByNombre("Pepe");

    }

    @Test
    @DisplayName("Listar pacientes por obra social paginada cuando hay coincidencias")
    void buscarPorObraSocialPaginado_ListarCuandoHayCoincidencia() {
        when(portOutRead.buscarPorObraSocialPaginado("OSDE", 10, 0))
                .thenReturn(List.of(paciente, pacienteInactivo));

        List<Paciente> result = service.buscarPorObraSocialPaginado("OSDE", 10, 0);

        assertEquals(2, result.size());
        verify(portOutRead).buscarPorObraSocialPaginado("OSDE", 10, 0);
    }

    @Test
    @DisplayName("Debe lanzar PacienteNotFoundException si el resultado paginado es null")
    void buscarPorObraSocialPaginado_FallarSiNull() {

        when(portOutRead.buscarPorObraSocialPaginado("OSDE", 10, 0))
                .thenReturn(null);

        assertThrows(
                PacienteNotFoundException.class,
                () -> service.buscarPorObraSocialPaginado("OSDE", 10, 0)
        );

        verify(portOutRead).buscarPorObraSocialPaginado("OSDE", 10, 0);
        verifyNoMoreInteractions(portOutRead, portOutWrite);
    }

    @Test
    @DisplayName("Debe lanzar PacienteNotFoundException si no hay coincidencias en busqueda paginada")
    void buscarPorObraSocialPaginado_FallarSiNoHayCoincidencia() {
        when(portOutRead.buscarPorObraSocialPaginado("OSDE", 10, 0))
                .thenReturn(List.of());

        assertThrows(PacienteNotFoundException.class,
                () -> service.buscarPorObraSocialPaginado("OSDE", 10, 0)
        );

        verify(portOutRead).buscarPorObraSocialPaginado("OSDE", 10, 0);
    }

    @Test
    @DisplayName("Activar paciente exitosamente cuando existe e inactivo")
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
    @DisplayName("Debe lanzar PacienteNotFoundException si no existe paciente al activar")
    void activarPaciente_FallarSiNoExisteId(){
        pacienteInactivo.setEstado(false);
        when(portOutRead.findById(2L))
                .thenReturn(Optional.empty());

        PacienteNotFoundException ex = assertThrows(
                PacienteNotFoundException.class,
                () -> service.activarPaciente(2L)
        );

        assertTrue(ex.getMessage().contains("2"));

        verify(portOutRead).findById(2L);
        verify(portOutWrite,never()).save(any());
    }

    @Test
    @DisplayName("Desactivar paciente exitosamente cuando existe y activo")
    void desactivarPaciente_SiExisteId(){
        paciente.setEstado(true);
        when(portOutRead.findById(1L))
                .thenReturn(Optional.of(paciente));

        paciente.setEstado(false);
        when(portOutWrite.save(paciente)).thenReturn(paciente);

        Paciente result = service.desactivarPaciente(1L);

        assertFalse(result.isEstado(), "El paciente esta desactivado");

        verify(portOutRead).findById(1L);
        verify(portOutWrite).save(paciente);
    }

    @Test
    @DisplayName("Actualizar paciente exitosamente con datos validos")
    void actualizarPaciente_ActualizarExitoso(){
        when(portOutRead.findById(1L))
                .thenReturn(Optional.of(paciente));
        when(portOutRead.buscarByDni("12345678"))
                .thenReturn(Optional.of(paciente));

        Paciente actualizado = Paciente.builder()
                .nombre("Ana Actualizada")
                .build();

        when(portOutWrite.update(any(Paciente.class))).thenReturn(actualizado);

        Paciente result = service.actualizarPaciente(1L, paciente);

        assertSame(actualizado, result);

        verify(portOutRead).findById(1L);
        verify(portOutWrite).update(paciente);
    }

    @Test
    @DisplayName("Debe lanzar PacienteNotFoundException si no existe paciente al actualizar")
    void actualizarPaciente_FallarSiNoExiste(){
        when(portOutRead.findById(1L))
                .thenReturn(Optional.empty());

        assertThrows(
                PacienteNotFoundException.class,
                () -> service.actualizarPaciente(1L, paciente)
        );

        verify(portOutRead).findById(1L);
        verify(portOutWrite, never()).update(any());
    }

    @Test
    @DisplayName("Debe lanzar PacienteDuplicadoException si el DNI esta duplicado al actualizar")
    void actualizarPaciente_FallarSiDniDuplicado(){
        when(portOutRead.findById(1L))
                .thenReturn(Optional.of(paciente));

        Paciente pacienteConMismoDni = Paciente.builder()
                        .id(3L)
                        .dni("12345678")
                        .build();

        when(portOutRead.buscarByDni("12345678"))
                .thenReturn(Optional.of(pacienteConMismoDni));

        assertThrows(PacienteDuplicadoException.class,
                () -> service.actualizarPaciente(1L, paciente));

        verify(portOutRead).findById(1L);
        verify(portOutRead).buscarByDni("12345678");
        verify(portOutWrite, never()).update(any());
    }

    @Test
    @DisplayName("Debe lanzar InvalidEmailFormatException si el correo es invalido al actualizar")
    void actualizarPaciente_FallarSiEmailFormatoIncorrecto(){
        paciente.setEmail("correo-no-valido");

        when(portOutRead.findById(1L))
                .thenReturn(Optional.of(paciente));

        when(portOutRead.buscarByDni("12345678"))
                .thenReturn(Optional.empty());

        assertThrows(
                InvalidEmailFormatException.class,
                () -> service.actualizarPaciente(1L, paciente)
        );

        verify(portOutRead).findById(1L);
        verify(portOutRead).buscarByDni("12345678");
        verify(portOutWrite, never()).update(any());
    }

    @Test
    @DisplayName("Debe lanzar InvalidFechaAltaException si la fecha de alta es futura al actualizar")
    void actualizarPaciente_FallarSiFechaAltaFutura(){
        paciente.setFechaAlta(LocalDate.now().plusDays(2));

        when(portOutRead.findById(1L))
                .thenReturn(Optional.of(paciente));

        when(portOutRead.buscarByDni("12345678"))
                .thenReturn(Optional.empty());

        assertThrows(
                InvalidFechaAltaException.class,
                () -> service.actualizarPaciente(1L, paciente)
        );

        verify(portOutRead).findById(1L);
        verify(portOutRead).buscarByDni("12345678");
        verify(portOutWrite, never()).update(any());
    }


}

package microservice.pacientes.unit.infrastructure.in.rest;

import microservice.pacientes.application.domain.command.CreatePacienteCommand;
import microservice.pacientes.application.domain.command.UpdatePacienteCommand;
import microservice.pacientes.application.domain.model.Paciente;

import static org.junit.jupiter.params.provider.Arguments.arguments;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import microservice.pacientes.application.domain.port.in.CreatePacienteUseCase;
import microservice.pacientes.application.domain.port.in.DeletePacienteUseCase;
import microservice.pacientes.application.domain.port.in.FindPacienteUseCase;
import microservice.pacientes.application.domain.port.in.UpdatePacienteUseCase;
import microservice.pacientes.infrastructure.adapter.in.rest.PacienteController;
import microservice.pacientes.shared.exception.PacienteDuplicadoException;
import microservice.pacientes.shared.exception.PacienteNoEncontradoException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;
import java.util.stream.Stream;

@WebMvcTest(PacienteController.class)
public class PacienteControllerTest {

    private static final String DNI_VALID = "12345678";
    private static final String DNI_NO_VALID = "12345678";
    private static final String EMAIL_VALID = "ana@mail.com";
    private static final String NOMBRE_VALID = "Ana";
    private static final String NOMBRE_NO_VALID = "12s";
    private static final String APELLIDO_VALID = "Perez";
    private static final String APELLIDO_NO_VALID = "as2";
    private static final String OBRA_SOCIAL = "Obra social";
    private static final String TELEFONO_VALID = "123456789";
    private static final String BASE_URL = "/pacientes";
    private static final String UPDATE_NOMBRE_VALID = "Juana";
    private static final String UPDATE_APELLIDO_VALID = "Rodriguez";
    private static final String UPDATE_OBRA_SOCIAL_VALID = "OSDE";

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private FindPacienteUseCase findPacienteUseCase;

    @MockitoBean
    private CreatePacienteUseCase createPacienteUseCase;

    @MockitoBean
    private UpdatePacienteUseCase updatePacienteUseCase;

    @MockitoBean
    private DeletePacienteUseCase deletePacienteUseCase;

    private Paciente createPacienteValid() {
        return new Paciente(DNI_VALID, NOMBRE_VALID, APELLIDO_VALID, OBRA_SOCIAL, EMAIL_VALID, TELEFONO_VALID);
    }

    private Paciente createUpdatePacienteValid() {
        return new Paciente(DNI_VALID, UPDATE_NOMBRE_VALID, UPDATE_APELLIDO_VALID, UPDATE_OBRA_SOCIAL_VALID, EMAIL_VALID, TELEFONO_VALID);
    }

    @Test
    @DisplayName("Debería obtener correctamente una lista de pacientes")
    void shouldReturnListOfPacientes_whenGetAll() throws Exception {
        Paciente paciente = createPacienteValid();

        when(findPacienteUseCase.getAll()).thenReturn(List.of(paciente));

        mockMvc.perform(get(BASE_URL))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].dni").value(DNI_VALID))
                .andExpect(jsonPath("$[0].nombre").value(NOMBRE_VALID))
                .andExpect(jsonPath("$[0].apellido").value(APELLIDO_VALID))
                .andExpect(jsonPath("$[0].obraSocial").value(OBRA_SOCIAL));
        verify(findPacienteUseCase, times(1)).getAll();
    }

    @Test
    @DisplayName("Debería obtener correctamente una lista de pacientes con nombre")
    void shouldReturnListOfPacientesWithNombre_whenGetAllWithNombre() throws Exception {
        Paciente paciente = createPacienteValid();
        when(findPacienteUseCase.getByNombreContainingIgnoreCase(NOMBRE_VALID)).thenReturn(List.of(paciente));

        mockMvc.perform(get(BASE_URL+"?nombre="+NOMBRE_VALID))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].dni").value(DNI_VALID))
                .andExpect(jsonPath("$[0].nombre").value(NOMBRE_VALID))
                .andExpect(jsonPath("$[0].apellido").value(APELLIDO_VALID))
                .andExpect(jsonPath("$[0].obraSocial").value(OBRA_SOCIAL));
         verify(findPacienteUseCase, times(1)).getByNombreContainingIgnoreCase(NOMBRE_VALID);
    }

    @Test
    @DisplayName("Debería obtener correctamente un paciente")
    void shouldReturnPaciente_whenGetByDniValid() throws Exception {
        Paciente paciente = createPacienteValid();

        when(findPacienteUseCase.getByDni(DNI_VALID)).thenReturn(paciente);

        mockMvc.perform(get(BASE_URL+"/"+DNI_VALID))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.dni").value(DNI_VALID))
                .andExpect(jsonPath("$.nombre").value(NOMBRE_VALID))
                .andExpect(jsonPath("$.apellido").value(APELLIDO_VALID))
                .andExpect(jsonPath("$.obraSocial").value(OBRA_SOCIAL));
        verify(findPacienteUseCase, times(1)).getByDni(DNI_VALID);
    }

    @Test
    @DisplayName("Debería lanzar una excepción al intentar obtener un paciente inexistente")
    void shouldThrowException_whenGetByDniInvalid() throws Exception {

        when(findPacienteUseCase.getByDni(DNI_NO_VALID)).thenThrow(new PacienteNoEncontradoException());

        mockMvc.perform(get(BASE_URL+"/"+DNI_NO_VALID))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.message").value("Paciente no encontrado"))
                .andExpect(jsonPath("$.status").value(404));
        verify(findPacienteUseCase, times(1)).getByDni(DNI_NO_VALID);
    }

    @Test
    @DisplayName("Debería obtener correctamente un paciente")
    void shouldCreateValidPaciente_whenCreateValidPaciente() throws Exception {
        String jsonBody = String.format(
                "{\"dni\": \"%s\", \"nombre\": \"%s\", \"apellido\": \"%s\", \"obraSocial\": \"%s\"}",
                DNI_VALID, NOMBRE_VALID, APELLIDO_VALID, OBRA_SOCIAL
        );
        Paciente paciente = createPacienteValid();

        when(createPacienteUseCase.create(any(CreatePacienteCommand.class))).thenReturn(paciente);


        mockMvc.perform(post("/pacientes")
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .content(jsonBody))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.dni").value(DNI_VALID))
                .andExpect(jsonPath("$.nombre").value(NOMBRE_VALID))
                .andExpect(jsonPath("$.apellido").value(APELLIDO_VALID))
                .andExpect(jsonPath("$.obraSocial").value(OBRA_SOCIAL));
        verify(createPacienteUseCase, times(1)).create(any(CreatePacienteCommand.class));
    }

    @ParameterizedTest
    @MethodSource("invalidPacienteProvider")
    @DisplayName("Debería lanzar una excepción al intentar crear un paciente con datos inválidos")
    void shouldThrowException_whenCreateInvalidPaciente(String pacienteDTO) throws Exception {
        mockMvc.perform(post("/pacientes")
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .content(pacienteDTO))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.status").value(400));
        verify(createPacienteUseCase, times(0)).create(any(CreatePacienteCommand.class));
    }

    static Stream<Arguments> invalidPacienteProvider() {
        return Stream.of(
                arguments("{\"dni\": \"\", \"nombre\": \"Ana\", \"apellido\": \"Perez\", \"obraSocial\": \"Obra social\"}"),
                arguments("{\"dni\": \"123\", \"nombre\": \"Juan\", \"apellido\": \"\", \"obraSocial\": \"Obra social\"}"),
                arguments("{\"dni\": \"123\", \"nombre\": \"\", \"apellido\": \"Perez\", \"obraSocial\": \"Obra social\"}")
        );
    }

    @Test
    @DisplayName("Debería lanzar excepción cuando intenta crear un paciente existente")
    void shouldThrowException_whenCreateExistsPaciente() throws Exception {
        String jsonBody = String.format(
                "{\"dni\": \"%s\", \"nombre\": \"%s\", \"apellido\": \"%s\", \"obraSocial\": \"%s\"}",
                DNI_VALID, NOMBRE_VALID, APELLIDO_VALID, OBRA_SOCIAL
        );

        when(createPacienteUseCase.create(any(CreatePacienteCommand.class))).thenThrow(new PacienteDuplicadoException());

        mockMvc.perform(post("/pacientes")
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .content(jsonBody))
                .andExpect(status().isConflict())
                .andExpect(jsonPath("$.status").value(409));
        verify(createPacienteUseCase, times(1)).create(any(CreatePacienteCommand.class));
    }

    @Test
    @DisplayName("Debería actualizar correctamente un paciente")
    void shouldUpdateValidPaciente_whenUpdateValidPaciente() throws Exception {
        String jsonBody = String.format(
                "{\"nombre\": \"%s\", \"apellido\": \"%s\", \"obraSocial\": \"%s\"}",
                UPDATE_NOMBRE_VALID, UPDATE_APELLIDO_VALID, UPDATE_OBRA_SOCIAL_VALID
        );

        Paciente paciente = createUpdatePacienteValid();

        when(updatePacienteUseCase.update(anyString(), any(UpdatePacienteCommand.class))).thenReturn(paciente);

        mockMvc.perform(put(BASE_URL+"/"+DNI_VALID)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .content(jsonBody))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.dni").value(DNI_VALID))
                .andExpect(jsonPath("$.nombre").value(UPDATE_NOMBRE_VALID))
                .andExpect(jsonPath("$.apellido").value(UPDATE_APELLIDO_VALID))
                .andExpect(jsonPath("$.obraSocial").value(UPDATE_OBRA_SOCIAL_VALID));
        verify(updatePacienteUseCase, times(1)).update(anyString(), any(UpdatePacienteCommand.class));
    }

    @Test
    @DisplayName("Debería lanzar una excepción al intentar actualizar un paciente con datos inválidos")
    void shouldThrowException_whenUpdateInvalidPaciente() throws Exception {
        String jsonBody = String.format(
                "{\"nombre\": \"%s\", \"apellido\": \"%s\"}",
                NOMBRE_NO_VALID, APELLIDO_NO_VALID
        );

        mockMvc.perform(put(BASE_URL+"/"+DNI_VALID)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .content(jsonBody))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.status").value(400));
        verify(updatePacienteUseCase, times(0)).update(anyString(), any(UpdatePacienteCommand.class));
    }

    @Test
    @DisplayName("Debería lanzar una excepción al intentar actualizar un paciente inexistente")
    void shouldThrowException_whenUpdateNoExistsPaciente() throws Exception {
        String jsonBody = String.format(
                "{\"nombre\": \"%s\", \"apellido\": \"%s\", \"obraSocial\": \"%s\"}",
                UPDATE_NOMBRE_VALID, UPDATE_APELLIDO_VALID, UPDATE_OBRA_SOCIAL_VALID
        );

        when(updatePacienteUseCase.update(anyString(), any(UpdatePacienteCommand.class))).thenThrow(new PacienteNoEncontradoException());

        mockMvc.perform(put(BASE_URL+"/"+DNI_VALID)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .content(jsonBody))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.message").value("Paciente no encontrado"))
                .andExpect(jsonPath("$.status").value(404));
        verify(updatePacienteUseCase, times(1)).update(anyString(), any(UpdatePacienteCommand.class));
    }

    @Test
    @DisplayName("Debería eliminar correctamente un paciente")
    void shouldDeletePaciente_whenDeleteExistsPaciente() throws Exception {
        mockMvc.perform(delete(BASE_URL+"/"+DNI_VALID))
                .andExpect(status().isNoContent());
        verify(deletePacienteUseCase, times(1)).delete(DNI_VALID);
    }

    @Test
    @DisplayName("Debería lanzar una excepción al intentar eliminar un paciente inexistente")
    void shouldThrowException_whenDeleteNoExistsPaciente() throws Exception {
        doThrow(new PacienteNoEncontradoException()).when(deletePacienteUseCase).delete(DNI_VALID);

        mockMvc.perform(delete(BASE_URL+"/"+DNI_VALID))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.message").value("Paciente no encontrado"))
                .andExpect(jsonPath("$.status").value(404));
        verify(deletePacienteUseCase, times(1)).delete(DNI_VALID);
    }

    @Test
    @DisplayName("Debería obtener correctamente un paciente por dni con SP")
    void shouldReturnPacienteByDni_whenGetByDniSPValid() throws Exception {
        Paciente paciente = createPacienteValid();

        when(findPacienteUseCase.getByDni(DNI_VALID)).thenReturn(paciente);

        mockMvc.perform(get(BASE_URL+"/dni/"+DNI_VALID))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.dni").value(DNI_VALID))
                .andExpect(jsonPath("$.nombre").value(NOMBRE_VALID))
                .andExpect(jsonPath("$.apellido").value(APELLIDO_VALID))
                .andExpect(jsonPath("$.obraSocial").value(OBRA_SOCIAL));
        verify(findPacienteUseCase, times(1)).getByDni(DNI_VALID);
    }

    @Test
    @DisplayName("Debería lanzar una excepción al intentar obtener un paciente inexistente por dni con SP")
    void shouldThrowException_whenGetByDniSPInvalid() throws Exception {
        when(findPacienteUseCase.getByDni(DNI_VALID)).thenThrow(new PacienteNoEncontradoException());

        mockMvc.perform(get(BASE_URL+"/dni/"+DNI_VALID))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.message").value("Paciente no encontrado"))
                .andExpect(jsonPath("$.status").value(404));
        verify(findPacienteUseCase, times(1)).getByDni(DNI_VALID);
    }

    @Test
    @DisplayName("Debería obtener correctamente un paciente por nombre con SP")
    void shouldReturnPacienteByNombre_whenGetByNombreValid() throws Exception {
        Paciente paciente = createPacienteValid();

        when(findPacienteUseCase.getByNombre(NOMBRE_VALID)).thenReturn(paciente);

        mockMvc.perform(get(BASE_URL+"/nombre/"+NOMBRE_VALID))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.dni").value(DNI_VALID))
                .andExpect(jsonPath("$.nombre").value(NOMBRE_VALID))
                .andExpect(jsonPath("$.apellido").value(APELLIDO_VALID))
                .andExpect(jsonPath("$.obraSocial").value(OBRA_SOCIAL));
        verify(findPacienteUseCase, times(1)).getByNombre(NOMBRE_VALID);
    }

    @Test
    @DisplayName("Debería lanzar una excepción al intentar obtener un paciente inexistente por nombre con SP")
    void shouldThrowException_whenGetByNombreInvalid() throws Exception {
        when(findPacienteUseCase.getByNombre(NOMBRE_VALID)).thenThrow(new PacienteNoEncontradoException());

        mockMvc.perform(get(BASE_URL+"/nombre/"+NOMBRE_VALID))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.message").value("Paciente no encontrado"))
                .andExpect(jsonPath("$.status").value(404));
        verify(findPacienteUseCase, times(1)).getByNombre(NOMBRE_VALID);
    }

    @Test
    @DisplayName("Debería obtener correctamente una lista de pacientes por obra social con SP")
    void shouldReturnListOfPacientesByObraSocial_whenGetByObraSocialValid() throws Exception {
        Paciente paciente = createPacienteValid();

        when(findPacienteUseCase.getByObraSocial(OBRA_SOCIAL, 10, 0)).thenReturn(List.of(paciente));

        mockMvc.perform(get(BASE_URL+"/obra_social?obraSocial="+OBRA_SOCIAL+"&page=1&size=10"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].dni").value(DNI_VALID))
                .andExpect(jsonPath("$[0].nombre").value(NOMBRE_VALID))
                .andExpect(jsonPath("$[0].apellido").value(APELLIDO_VALID))
                .andExpect(jsonPath("$[0].obraSocial").value(OBRA_SOCIAL));
        verify(findPacienteUseCase, times(1)).getByObraSocial(OBRA_SOCIAL, 10, 0);
    }

    @Test
    @DisplayName("Debería devolver una lista vacía al buscar pacientes por obra social con SP y no encontrar ninguno")
    void shouldReturnEmptyList_whenSearchByObraSocialAndFindNone() throws Exception {
        when(findPacienteUseCase.getByObraSocial(OBRA_SOCIAL, 10, 0)).thenReturn(List.of());

        mockMvc.perform(get(BASE_URL+"/obra_social?obraSocial="+OBRA_SOCIAL+"&page=1&size=10"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isEmpty());
        verify(findPacienteUseCase, times(1)).getByObraSocial(OBRA_SOCIAL, 10, 0);
    }
}

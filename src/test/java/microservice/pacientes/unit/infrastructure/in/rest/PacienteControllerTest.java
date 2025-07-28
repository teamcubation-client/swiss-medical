package microservice.pacientes.unit.infrastructure.in.rest;

import microservice.pacientes.application.domain.command.CreatePacienteCommand;
import microservice.pacientes.application.domain.command.UpdatePacienteCommand;
import microservice.pacientes.application.domain.model.Paciente;

import static org.junit.jupiter.params.provider.Arguments.arguments;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
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
import org.junit.jupiter.api.extension.MediaType;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;
import java.util.stream.Stream;

@WebMvcTest(PacienteController.class)
public class PacienteControllerTest {

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

    @Test
    @DisplayName("Debería obtener correctamente una lista de pacientes")
    void getAll() throws Exception {
        Paciente paciente = new Paciente("123", "Ana", "Perez", "Obra social", "email@test.com", "123456789");

        when(findPacienteUseCase.getAll()).thenReturn(List.of(paciente));

        mockMvc.perform(get("/pacientes"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].dni").value("123"))
                .andExpect(jsonPath("$[0].nombre").value("Ana"))
                .andExpect(jsonPath("$[0].apellido").value("Perez"))
                .andExpect(jsonPath("$[0].obraSocial").value("Obra social"));
        verify(findPacienteUseCase, times(1)).getAll();
    }

    @Test
    @DisplayName("Debería obtener correctamente una lista de pacientes con nombre")
    void getAllWithNombre() throws Exception {
        Paciente paciente = new Paciente("123", "Ana", "Perez", "Obra social", "email@test.com", "123456789");

        when(findPacienteUseCase.getByNombreContainingIgnoreCase("Ana")).thenReturn(List.of(paciente));

        mockMvc.perform(get("/pacientes?nombre=Ana"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].dni").value("123"))
                .andExpect(jsonPath("$[0].nombre").value("Ana"))
                .andExpect(jsonPath("$[0].apellido").value("Perez"))
                .andExpect(jsonPath("$[0].obraSocial").value("Obra social"));
         verify(findPacienteUseCase, times(1)).getByNombreContainingIgnoreCase("Ana");
    }

    @Test
    @DisplayName("Debería obtener correctamente un paciente")
    void getByDniValid() throws Exception {
        Paciente paciente = new Paciente("123", "Ana", "Perez", "Obra social", "email@test.com", "123456789");

        when(findPacienteUseCase.getByDni("123")).thenReturn(paciente);

        mockMvc.perform(get("/pacientes/123"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.dni").value("123"))
                .andExpect(jsonPath("$.nombre").value("Ana"))
                .andExpect(jsonPath("$.apellido").value("Perez"))
                .andExpect(jsonPath("$.obraSocial").value("Obra social"));
        verify(findPacienteUseCase, times(1)).getByDni("123");
    }

    @Test
    @DisplayName("Debería lanzar una excepción al intentar obtener un paciente inexistente")
    void getByDniInvalid() throws Exception {

        when(findPacienteUseCase.getByDni("123")).thenThrow(new PacienteNoEncontradoException());

        mockMvc.perform(get("/pacientes/123"))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.message").value("Paciente no encontrado"))
                .andExpect(jsonPath("$.status").value(404));
        verify(findPacienteUseCase, times(1)).getByDni("123");
    }

    @Test
    @DisplayName("Debería obtener correctamente un paciente")
    void createValidPaciente() throws Exception {
        Paciente paciente = new Paciente("123", "Ana", "Perez", "Obra social", "email@test.com", "123456789");

        when(createPacienteUseCase.create(any(CreatePacienteCommand.class))).thenReturn(paciente);

        mockMvc.perform(post("/pacientes")
                .contentType(MediaType.APPLICATION_JSON.toString())
                .content("{\"dni\": \"123\", \"nombre\": \"Ana\", \"apellido\": \"Perez\", \"obraSocial\": \"Obra social\"}"))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.dni").value("123"))
                .andExpect(jsonPath("$.nombre").value("Ana"))
                .andExpect(jsonPath("$.apellido").value("Perez"))
                .andExpect(jsonPath("$.obraSocial").value("Obra social"));
        verify(createPacienteUseCase, times(1)).create(any(CreatePacienteCommand.class));
    }

    @ParameterizedTest
    @MethodSource("invalidPacienteProvider")
    @DisplayName("Debería lanzar una excepción al intentar crear un paciente con datos inválidos")
    void createInvalidPaciente(String pacienteDTO) throws Exception {
        mockMvc.perform(post("/pacientes")
                .contentType(MediaType.APPLICATION_JSON.toString())
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
    void createExistsPaciente() throws Exception {
        when(createPacienteUseCase.create(any(CreatePacienteCommand.class))).thenThrow(new PacienteDuplicadoException());

        mockMvc.perform(post("/pacientes")
                .contentType(MediaType.APPLICATION_JSON.toString())
                .content("{\"dni\": \"123\", \"nombre\": \"Ana\", \"apellido\": \"Perez\", \"obraSocial\": \"Obra social\"}"))
                .andExpect(status().isConflict())
                .andExpect(jsonPath("$.status").value(409));
        verify(createPacienteUseCase, times(1)).create(any(CreatePacienteCommand.class));
    }

    @Test
    @DisplayName("Debería actualizar correctamente un paciente")
    void updateValidPaciente() throws Exception {
        Paciente paciente = new Paciente("123", "Ana", "Perez", "Obra social", "email@test.com", "123456789");

        when(updatePacienteUseCase.update(anyString(), any(UpdatePacienteCommand.class))).thenReturn(paciente);

        mockMvc.perform(put("/pacientes/123")
                .contentType(MediaType.APPLICATION_JSON.toString())
                .content("{\"nombre\": \"Ana\", \"apellido\": \"Perez\", \"obraSocial\": \"Obra social\"}"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.dni").value("123"))
                .andExpect(jsonPath("$.nombre").value("Ana"))
                .andExpect(jsonPath("$.apellido").value("Perez"))
                .andExpect(jsonPath("$.obraSocial").value("Obra social"));
        verify(updatePacienteUseCase, times(1)).update(anyString(), any(UpdatePacienteCommand.class));
    }

    @Test
    @DisplayName("Debería lanzar una excepción al intentar actualizar un paciente con datos inválidos")
    void updateInvalidPaciente() throws Exception {
        mockMvc.perform(put("/pacientes/123")
                .contentType(MediaType.APPLICATION_JSON.toString())
                .content("{\"nombre\": \"\", \"apellido\": \"Perez\", \"obraSocial\": \"Obra social\"}"))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.status").value(400));
        verify(updatePacienteUseCase, times(0)).update(anyString(), any(UpdatePacienteCommand.class));
    }

    @Test
    @DisplayName("Debería lanzar una excepción al intentar actualizar un paciente inexistente")
    void updateNoExistsPaciente() throws Exception {
        when(updatePacienteUseCase.update(anyString(), any(UpdatePacienteCommand.class))).thenThrow(new PacienteNoEncontradoException());

        mockMvc.perform(put("/pacientes/123")
                .contentType(MediaType.APPLICATION_JSON.toString())
                .content("{\"nombre\": \"Ana\", \"apellido\": \"Perez\", \"obraSocial\": \"Obra social\"}"))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.message").value("Paciente no encontrado"))
                .andExpect(jsonPath("$.status").value(404));
        verify(updatePacienteUseCase, times(1)).update(anyString(), any(UpdatePacienteCommand.class));
    }

    @Test
    @DisplayName("Debería eliminar correctamente un paciente")
    void deleteExistsPaciente() throws Exception {
        mockMvc.perform(delete("/pacientes/123"))
                .andExpect(status().isNoContent());
        verify(deletePacienteUseCase, times(1)).delete("123");
    }

    @Test
    @DisplayName("Debería lanzar una excepción al intentar eliminar un paciente inexistente")
    void deleteNoExistsPaciente() throws Exception {
        doThrow(new PacienteNoEncontradoException()).when(deletePacienteUseCase).delete("123");

        mockMvc.perform(delete("/pacientes/123"))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.message").value("Paciente no encontrado"))
                .andExpect(jsonPath("$.status").value(404));
        verify(deletePacienteUseCase, times(1)).delete("123");
    }

    @Test
    @DisplayName("Debería obtener correctamente un paciente por dni con SP")
    void getByDniSPValid() throws Exception {
        Paciente paciente = new Paciente("123", "Ana", "Perez", "Obra social", "email@test.com", "123456789");

        when(findPacienteUseCase.getByDni("123")).thenReturn(paciente);

        mockMvc.perform(get("/pacientes/dni/123"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.dni").value("123"))
                .andExpect(jsonPath("$.nombre").value("Ana"))
                .andExpect(jsonPath("$.apellido").value("Perez"))
                .andExpect(jsonPath("$.obraSocial").value("Obra social"));
        verify(findPacienteUseCase, times(1)).getByDni("123");
    }

    @Test
    @DisplayName("Debería lanzar una excepción al intentar obtener un paciente inexistente por dni con SP")
    void getByDniSPInvalid() throws Exception {
        when(findPacienteUseCase.getByDni("123")).thenThrow(new PacienteNoEncontradoException());

        mockMvc.perform(get("/pacientes/dni/123"))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.message").value("Paciente no encontrado"))
                .andExpect(jsonPath("$.status").value(404));
        verify(findPacienteUseCase, times(1)).getByDni("123");
    }

    @Test
    @DisplayName("Debería obtener correctamente un paciente por nombre con SP")
    void getByNombreValid() throws Exception {
        Paciente paciente = new Paciente("123", "Ana", "Perez", "Obra social", "email@test.com", "123456789");

        when(findPacienteUseCase.getByNombre("Ana")).thenReturn(paciente);

        mockMvc.perform(get("/pacientes/nombre/Ana"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.dni").value("123"))
                .andExpect(jsonPath("$.nombre").value("Ana"))
                .andExpect(jsonPath("$.apellido").value("Perez"))
                .andExpect(jsonPath("$.obraSocial").value("Obra social"));
        verify(findPacienteUseCase, times(1)).getByNombre("Ana");
    }

    @Test
    @DisplayName("Debería lanzar una excepción al intentar obtener un paciente inexistente por nombre con SP")
    void getByNombreInvalid() throws Exception {
        when(findPacienteUseCase.getByNombre("Ana")).thenThrow(new PacienteNoEncontradoException());

        mockMvc.perform(get("/pacientes/nombre/Ana"))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.message").value("Paciente no encontrado"))
                .andExpect(jsonPath("$.status").value(404));
        verify(findPacienteUseCase, times(1)).getByNombre("Ana");
    }

    @Test
    @DisplayName("Debería obtener correctamente una lista de pacientes por obra social con SP")
    void getByObraSocialValid() throws Exception {
        Paciente paciente = new Paciente("123", "Ana", "Perez", "Obra social", "email@test.com", "123456789");

        when(findPacienteUseCase.getByObraSocial("Obra social", 10, 0)).thenReturn(List.of(paciente));

        mockMvc.perform(get("/pacientes/obra_social?obraSocial=Obra social&page=1&size=10"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].dni").value("123"))
                .andExpect(jsonPath("$[0].nombre").value("Ana"))
                .andExpect(jsonPath("$[0].apellido").value("Perez"))
                .andExpect(jsonPath("$[0].obraSocial").value("Obra social"));
        verify(findPacienteUseCase, times(1)).getByObraSocial("Obra social", 10, 0);
    }

    @Test
    @DisplayName("Debería devolver una lista vacía al buscar pacientes por obra social con SP y no encontrar ninguno")
    void getByObraSocialNoExists() throws Exception {
        when(findPacienteUseCase.getByObraSocial("Obra social", 10, 0)).thenReturn(List.of());

        mockMvc.perform(get("/pacientes/obra_social?obraSocial=Obra social&page=1&size=10"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isEmpty());
        verify(findPacienteUseCase, times(1)).getByObraSocial("Obra social", 10, 0);
    }
}

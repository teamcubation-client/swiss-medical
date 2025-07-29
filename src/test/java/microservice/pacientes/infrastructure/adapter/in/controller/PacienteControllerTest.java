package microservice.pacientes.infrastructure.adapter.in.controller;


import com.fasterxml.jackson.databind.ObjectMapper;
import microservice.pacientes.application.domain.model.Paciente;
import microservice.pacientes.application.domain.port.in.PacientePortInRead;
import microservice.pacientes.application.domain.port.in.PacientePortInWrite;
import microservice.pacientes.shared.GlobalExceptionHandler;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import java.time.LocalDate;
import java.util.List;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(PacienteController.class)
@Import(GlobalExceptionHandler.class)
public class PacienteControllerTest {

    @Autowired
    private MockMvc mvc;

    @Autowired
    private ObjectMapper mapper;

    @MockBean
    private PacientePortInRead portInRead;

    @MockBean
    private PacientePortInWrite portInWrite;

    private Paciente pacienteActivo;
    private Paciente pacienteInactivo;

    @BeforeEach
    void setup() {
        pacienteActivo = Paciente.builder()
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
        reset(portInRead, portInWrite);
    }

    @Test
    void listarPacientes() throws Exception {
        when(portInRead.listarPacientes()).thenReturn(List.of(pacienteActivo, pacienteInactivo));

        mvc.perform(get("/api/pacientes"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$[0].id").value(pacienteActivo.getId()))
                .andExpect(jsonPath("$[0].dni").value(pacienteActivo.getDni()))
                .andExpect(jsonPath("$[1].id").value(pacienteInactivo.getId()))
                .andExpect(jsonPath("$[1].dni").value(pacienteInactivo.getDni()));

        verify(portInRead).listarPacientes();
        verifyNoMoreInteractions(portInRead, portInWrite);
    }


    @Test
    void crearPaciente() throws Exception {
        var request = PacienteDTO.builder()
                .id(1L)
                .dni(pacienteActivo.getDni())
                .nombre(pacienteActivo.getNombre())
                .apellido(pacienteActivo.getApellido())
                .obraSocial(pacienteActivo.getObraSocial())
                .email(pacienteActivo.getEmail())
                .telefono(pacienteActivo.getTelefono())
                .tipoPlanObraSocial(pacienteActivo.getTipoPlanObraSocial())
                .fechaAlta(pacienteActivo.getFechaAlta())
                .estado(pacienteActivo.isEstado())
                .build();

        when(portInWrite.crearPaciente(any(Paciente.class))).thenReturn(pacienteActivo);

        mvc.perform(post("/api/pacientes")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(pacienteActivo.getId()))
                .andExpect(jsonPath("$.dni").value(pacienteActivo.getDni()))
                .andExpect(jsonPath("$.nombre").value(pacienteActivo.getNombre()));

        verify(portInWrite).crearPaciente(any(Paciente.class));
        verifyNoMoreInteractions(portInRead, portInWrite);
    }

    @Test
    void crearPaciente_DatosInvalidos_400() throws Exception {
        var request = PacienteDTO.builder()
                .id(1L)
                .apellido(pacienteActivo.getApellido())
                .obraSocial(pacienteActivo.getObraSocial())
                .email("formato-invalido")
                .telefono(pacienteActivo.getTelefono())
                .tipoPlanObraSocial(pacienteActivo.getTipoPlanObraSocial())
                .fechaAlta(pacienteActivo.getFechaAlta())
                .estado(pacienteActivo.isEstado())
                .build();

        mvc.perform(post("/api/pacientes")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(request))
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());

        verifyNoInteractions(portInWrite);
    }

    @Test
    void obtenerPaciente_encontrado() throws Exception {
        when(portInRead.obtenerPacientePorId(1L)).thenReturn(pacienteActivo);

        mvc.perform(get("/api/pacientes/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(pacienteActivo.getId()))
                .andExpect(jsonPath("$.nombre").value(pacienteActivo.getNombre()));

        verify(portInRead).obtenerPacientePorId(1L);
        verifyNoMoreInteractions(portInRead, portInWrite);
    }

    @Test
    void obtenerPaciente_noEncontrado() throws Exception {
        when(portInRead.obtenerPacientePorId(9L)).thenReturn(null);

        mvc.perform(get("/api/pacientes/9"))
                .andExpect(status().isNotFound());

        verify(portInRead).obtenerPacientePorId(9L);
        verifyNoMoreInteractions(portInRead, portInWrite);
    }

    @Test
    void eliminarPaciente() throws Exception {
        mvc.perform(delete("/api/pacientes/1"))
                .andExpect(status().isNoContent());

        verify(portInWrite).eliminarPaciente(1L);
        verifyNoMoreInteractions(portInRead, portInWrite);
    }

    @Test
    void buscarPorNombre() throws Exception {
        when(portInRead.buscarPorNombreParcial("An")).thenReturn(List.of(pacienteActivo));

        mvc.perform(get("/api/pacientes/buscar/nombre")
                        .param("nombre", "An"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].nombre").value(pacienteActivo.getNombre()));

        verify(portInRead).buscarPorNombreParcial("An");
        verifyNoMoreInteractions(portInRead, portInWrite);
    }

    @Test
    void buscarPorNombre_sinParametroNombre_devuelve400() throws Exception {
        mvc.perform(get("/api/pacientes/buscar/nombre"))
                .andExpect(status().isBadRequest());

        verifyNoInteractions(portInRead);
    }

    @Test
    void actualizarPaciente() throws Exception {
        Paciente pacienteActualizar = Paciente.builder()
                .id(1L)
                .dni(pacienteActivo.getDni())
                .nombre("Ana Modificado")
                .apellido("Ana Modificado")
                .obraSocial(pacienteActivo.getObraSocial())
                .email(pacienteActivo.getEmail())
                .telefono(pacienteActivo.getTelefono())
                .tipoPlanObraSocial(pacienteActivo.getTipoPlanObraSocial())
                .fechaAlta(pacienteActivo.getFechaAlta())
                .estado(pacienteActivo.isEstado())
                .build();

        when(portInWrite.actualizarPaciente(eq(1L), any(Paciente.class)))
                .thenReturn(pacienteActualizar);

        PacienteDTO pacienteActualizado = PacienteDTO.builder()
                .id(1L)
                .dni(pacienteActivo.getDni())
                .nombre("Ana Modificado")
                .apellido(pacienteActivo.getApellido())
                .obraSocial(pacienteActivo.getObraSocial())
                .email(pacienteActivo.getEmail())
                .telefono(pacienteActivo.getTelefono())
                .tipoPlanObraSocial(pacienteActivo.getTipoPlanObraSocial())
                .fechaAlta(pacienteActivo.getFechaAlta())
                .estado(pacienteActivo.isEstado())
                .build();

        mvc.perform(put("/api/pacientes/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(pacienteActualizado)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.nombre").value("Ana Modificado"))
                .andExpect(jsonPath("$.apellido").value("Ana Modificado"));

        verify(portInWrite).actualizarPaciente(eq(1L), any(Paciente.class));
        verifyNoMoreInteractions(portInRead, portInWrite);
    }

    @Test
    void actualizarPaciente_inexistente_404() throws Exception {
        PacienteDTO dtoRequest = PacienteDTO.builder()
                .id(999L)
                .dni("11111111")
                .nombre("No")
                .apellido("Existo")
                .obraSocial("Galeno")
                .email("noexisto@inexistente.com")
                .telefono("11111111")
                .tipoPlanObraSocial("PlanX")
                .fechaAlta(LocalDate.now())
                .estado(true)
                .build();

        when(portInWrite.actualizarPaciente(eq(999L), any(Paciente.class)))
                .thenReturn(null);

        mvc.perform(put("/api/pacientes/999")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(dtoRequest)))
                .andExpect(status().isNotFound());

        verify(portInWrite).actualizarPaciente(eq(999L), any(Paciente.class));
        verifyNoMoreInteractions(portInRead, portInWrite);
    }


    @Test
    void activarPaciente() throws Exception {
        Paciente pacienteActivado = Paciente.builder()
                .id(pacienteInactivo.getId())
                .dni(pacienteInactivo.getDni())
                .nombre(pacienteInactivo.getNombre())
                .apellido(pacienteInactivo.getApellido())
                .obraSocial(pacienteInactivo.getObraSocial())
                .email(pacienteInactivo.getEmail())
                .telefono(pacienteInactivo.getTelefono())
                .tipoPlanObraSocial(pacienteInactivo.getTipoPlanObraSocial())
                .fechaAlta(pacienteInactivo.getFechaAlta())
                .estado(true)
                .build();

        when(portInWrite.activarPaciente(2L)).thenReturn(pacienteActivado);

        mvc.perform(patch("/api/pacientes/2/activar"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(2))
                .andExpect(jsonPath("$.estado").value(true));

        verify(portInWrite).activarPaciente(2L);
        verifyNoMoreInteractions(portInRead, portInWrite);
    }


    @Test
    void desactivarPaciente() throws Exception {
        Paciente pacienteDesactivado = Paciente.builder()
                .id(pacienteActivo.getId())
                .dni(pacienteActivo.getDni())
                .nombre(pacienteActivo.getNombre())
                .apellido(pacienteActivo.getApellido())
                .obraSocial(pacienteActivo.getObraSocial())
                .email(pacienteActivo.getEmail())
                .telefono(pacienteActivo.getTelefono())
                .tipoPlanObraSocial(pacienteActivo.getTipoPlanObraSocial())
                .fechaAlta(pacienteActivo.getFechaAlta())
                .estado(false)
                .build();

        when(portInWrite.desactivarPaciente(1L)).thenReturn(pacienteDesactivado);

        mvc.perform(patch("/api/pacientes/1/desactivar"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.estado").value(false));

        verify(portInWrite).desactivarPaciente(1L);
        verifyNoMoreInteractions(portInRead, portInWrite);
    }

    @Test
    void listarPacientesActivos() throws Exception {
        when(portInRead.listarPacientes()).thenReturn(List.of(pacienteActivo, pacienteInactivo));

        mvc.perform(get("/api/pacientes/activos"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(1))
                .andExpect(jsonPath("$[0].id").value(1))
                .andExpect(jsonPath("$[0].estado").value(true));

        verify(portInRead).listarPacientes();
        verifyNoMoreInteractions(portInRead, portInWrite);
    }

    @Test
    void listarPacientesInactivos() throws Exception {
        when(portInRead.listarPacientes()).thenReturn(List.of(pacienteActivo, pacienteInactivo));

        mvc.perform(get("/api/pacientes/inactivos"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(1))
                .andExpect(jsonPath("$[0].id").value(2))
                .andExpect(jsonPath("$[0].estado").value(false));

        verify(portInRead).listarPacientes();
        verifyNoMoreInteractions(portInRead, portInWrite);
    }

    @Test
    void buscarByDni() throws Exception {
        when(portInRead.buscarByDni("12345678")).thenReturn(pacienteActivo);

        mvc.perform(get("/api/pacientes/sp/buscar/dni/12345678"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.dni").value("12345678"))
                .andExpect(jsonPath("$.id").value(1));

        verify(portInRead).buscarByDni("12345678");
        verifyNoMoreInteractions(portInRead, portInWrite);
    }

    @Test
    void buscarByNombre() throws Exception {
        when(portInRead.buscarByNombre("Ana")).thenReturn(List.of(pacienteActivo, pacienteActivo));

        mvc.perform(get("/api/pacientes/sp/buscar/nombre/Ana"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(2))
                .andExpect(jsonPath("$[0].nombre").value("Ana"))
                .andExpect(jsonPath("$[1].nombre").value("Ana"));

        verify(portInRead).buscarByNombre("Ana");
        verifyNoMoreInteractions(portInRead, portInWrite);
    }

    @Test
    void buscarPorObraSocialPaginado_sp() throws Exception {
        when(portInRead.buscarPorObraSocialPaginado("OSDE", 5, 2))
                .thenReturn(List.of(pacienteActivo, pacienteInactivo));

        mvc.perform(get("/api/pacientes/sp/buscar/obra-social")
                        .param("obraSocial", "OSDE")
                        .param("limit", "5")
                        .param("offset", "2"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(2))
                .andExpect(jsonPath("$[0].obraSocial").value("OSDE"))
                .andExpect(jsonPath("$[1].obraSocial").value("OSDE"));

        verify(portInRead).buscarPorObraSocialPaginado("OSDE", 5, 2);
        verifyNoMoreInteractions(portInRead, portInWrite);
    }
}

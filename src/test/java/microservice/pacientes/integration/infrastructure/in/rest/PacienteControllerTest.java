package microservice.pacientes.integration.infrastructure.in.rest;

import com.fasterxml.jackson.databind.ObjectMapper;
import microservice.pacientes.application.domain.model.Paciente;
import microservice.pacientes.infrastructure.adapter.in.rest.dto.PacienteRequestDTO;
import microservice.pacientes.infrastructure.adapter.out.persistence.mysql.repository.PacienteRepository;
import org.junit.jupiter.api.*;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.context.bean.override.mockito.MockitoSpyBean;
import org.springframework.test.web.servlet.MockMvc;
import org.testcontainers.containers.MySQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import java.util.List;

import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

// OBS: Para utilizar Testcontainers hay que agregar la notación @Testcontainers a la clase,
// sacar el perfil de test (para que no detecte H2) y agregar el MySQLContainer + setear las properties.

//@Testcontainers
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
@ActiveProfiles("test")
public class PacienteControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoSpyBean
    private PacienteRepository pacienteRepository;

    @Autowired
    private ObjectMapper objectMapper;

    private Paciente paciente;

/*    @Container
    private static MySQLContainer<?> mysqlContainer = new MySQLContainer<>("mysql:8.0")
            .withDatabaseName("testdb")
            .withUsername("test")
            .withPassword("test")
            .withInitScript("init.sql"); // test/java/resources/init.sql

    @BeforeAll
    static void setUpAll() {
        System.setProperty("spring.datasource.url", mysqlContainer.getJdbcUrl());
        System.setProperty("spring.datasource.username", mysqlContainer.getUsername());
        System.setProperty("spring.datasource.password", mysqlContainer.getPassword());
    }
*/
    @BeforeEach
    void setUp() {
        for (Paciente paciente : pacienteRepository.getAll())
            pacienteRepository.delete(paciente);
        paciente = new Paciente("12345678", "Juan", "Perez", "OSDE", "juan@mail.com", "123456789");
    }

    @AfterEach
    void tearDown() {
        for (Paciente paciente : pacienteRepository.getAll())
            pacienteRepository.delete(paciente);
    }

    @Test
    @DisplayName("Debe crear un paciente y guardarlo en la DB")
    void crearPacienteExitosamente() throws Exception {
        String pacienteJson = objectMapper.writeValueAsString(new PacienteRequestDTO("12345678", "Ana", "Perez", "OSDE", "ana@mail.com", "112233445"));

        mockMvc.perform(post("/pacientes")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(pacienteJson))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.dni", is("12345678")))
                .andExpect(jsonPath("$.nombre", is("Ana")))
                .andExpect(jsonPath("$.apellido", is("Perez")))
                .andExpect(jsonPath("$.obraSocial", is("OSDE")));

        Paciente pacienteGuardado = pacienteRepository.getByDni("12345678").orElse(null);
        assertNotNull(pacienteGuardado);
        assertEquals("Ana", pacienteGuardado.getNombre());
    }

    @Test
    @DisplayName("Debe fallar al crear un paciente con DNI existente")
    void crearPacienteConDniExistente() throws Exception {
        pacienteRepository.save(paciente);
        String pacienteJson = objectMapper.writeValueAsString(new PacienteRequestDTO("12345678", "Otro", "Nombre", "Swiss", "otro@mail.com", "123456789"));

        mockMvc.perform(post("/pacientes")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(pacienteJson))
                .andExpect(status().isConflict());
    }

    @Test
    @DisplayName("Debe fallar al crear un paciente con datos inválidos")
    void crearPacienteConDatosInvalidos() throws Exception {
        String pacienteJson = objectMapper.writeValueAsString(new PacienteRequestDTO("", "", "", "", "mail-invalido", ""));

        mockMvc.perform(post("/pacientes")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(pacienteJson))
                .andExpect(status().isBadRequest());
    }

    @Test
    @DisplayName("Debe actualizar un paciente existente")
    void actualizarPacienteExitosamente() throws Exception {
        Paciente savedPaciente = pacienteRepository.save(paciente);
        String pacienteUpdateJson = """
            {
                "nombre": "Carlos",
                "email": "jc@mail.com"
            }
            """;

        mockMvc.perform(put("/pacientes/" + savedPaciente.getDni())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(pacienteUpdateJson))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.nombre", is("Carlos")));

        Paciente pacienteActualizado = pacienteRepository.getByDni(savedPaciente.getDni()).orElseThrow();
        assertEquals("Carlos", pacienteActualizado.getNombre());
    }

    @Test
    @DisplayName("Debe fallar al actualizar un paciente inexistente")
    void actualizarPacienteInexistente() throws Exception {
        String pacienteUpdateJson = """
            {
                "nombre": "Inexistente"
            }
            """;
        mockMvc.perform(put("/pacientes/23232323")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(pacienteUpdateJson))
                .andExpect(status().isNotFound());
    }

    @Test
    @DisplayName("Debe borrar un paciente existente")
    void borrarPacienteExitosamente() throws Exception {
        Paciente savedPaciente = pacienteRepository.save(paciente);

        mockMvc.perform(delete("/pacientes/" + savedPaciente.getDni()))
                .andExpect(status().isNoContent());

        assertFalse(pacienteRepository.getByDni(savedPaciente.getDni()).isPresent());
    }

    @Test
    @DisplayName("Debe fallar al borrar un paciente inexistente")
    void borrarPacienteInexistente() throws Exception {
        mockMvc.perform(delete("/pacientes/999"))
                .andExpect(status().isNotFound());
    }

    @Test
    @DisplayName("Debe obtener todos los pacientes")
    void obtenerTodosLosPacientes() throws Exception {
        pacienteRepository.save(paciente);
        pacienteRepository.save(new Paciente("87654321", "Maria", "Gomez", "Galeno", "maria@mail.com", "123456789"));

        mockMvc.perform(get("/pacientes"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(2)));
    }

    @Test
    @DisplayName("Debe obtener pacientes por nombre")
    void obtenerPacientesPorNombre() throws Exception {
        /*pacienteRepository.save(paciente);
        pacienteRepository.save(new Paciente("87654321", "Juan", "Gonzalez", "Galeno", "juan.gonzalez@mail.com", "123456789"));
*/
        // Se mockea solamente el retorno de la DB por que usa un SP para obtener por nombre.
        when(pacienteRepository.getByNombreContainingIgnoreCase("Juan")).thenReturn(List.of(paciente, paciente));

        mockMvc.perform(get("/pacientes?nombre=Juan"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(2)))
                .andExpect(jsonPath("$[0].nombre", is("Juan")))
                .andExpect(jsonPath("$[1].nombre", is("Juan")));
    }

    @Test
    @DisplayName("Debe obtener paciente por DNI")
    void obtenerPacientePorDni() throws Exception {
        pacienteRepository.save(paciente);

        mockMvc.perform(get("/pacientes/dni/" + paciente.getDni()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.dni", is(paciente.getDni())));
    }

    @Test
    @DisplayName("Debe fallar al buscar un paciente por DNI inexistente")
    void obtenerPacientePorDniInexistente() throws Exception {
        mockMvc.perform(get("/pacientes/dni/00000000"))
                .andExpect(status().isNotFound());
    }
}
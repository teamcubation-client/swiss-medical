package microservice.pacientes.integration.infrastructure.in.rest;

import com.fasterxml.jackson.databind.ObjectMapper;
import microservice.pacientes.application.domain.model.Paciente;
import microservice.pacientes.infrastructure.adapter.in.rest.dto.PacienteRequestDTO;
import microservice.pacientes.infrastructure.adapter.out.persistence.mysql.repository.PacienteRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
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

    private static final String DNI_VALID = "12345678";
    private static final String DNI_NO_VALID = "12345678";
    private static final String EMAIL_VALID = "ana@mail.com";
    private static final String EMAIL_NO_VALID = "ana";
    private static final String NOMBRE_VALID = "Ana";
    private static final String NOMBRE_NO_VALID = "12s";
    private static final String APELLIDO_VALID = "Perez";
    private static final String APELLIDO_NO_VALID = "as2";
    private static final String OBRA_SOCIAL = "Obra social";
    private static final String TELEFONO_VALID = "123456789";
    private static final String TELEFONO_NO_VALID = "1";
    private static final String BASE_URL = "/pacientes";
    private static final String UPDATE_NOMBRE_VALID = "Juana";
    private static final String UPDATE_APELLIDO_VALID = "Rodriguez";
    private static final String UPDATE_OBRA_SOCIAL_VALID = "OSDE";
    private static final String UPDATE_EMAIL_VALID = "jc@mail.com";
    private static final String DNI_NO_EXISTS = "87654321";

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

    private Paciente createPacienteValid() {
        return new Paciente(DNI_VALID, NOMBRE_VALID, APELLIDO_VALID, OBRA_SOCIAL, EMAIL_VALID, TELEFONO_VALID);
    }

    private Paciente createUpdatePacienteValid() {
        return new Paciente(DNI_VALID, UPDATE_NOMBRE_VALID, UPDATE_APELLIDO_VALID, UPDATE_OBRA_SOCIAL_VALID, EMAIL_VALID, TELEFONO_VALID);
    }

    private void limpiarPacientes() {
        pacienteRepository.getAll().forEach(pacienteRepository::delete);
    }

    @BeforeEach
    void setUp() {
        limpiarPacientes();
        paciente = createPacienteValid();
    }

    @AfterEach
    void tearDown() {
        limpiarPacientes();
    }

    @Test
    @DisplayName("Debe crear un paciente y guardarlo en la DB")
    void crearPacienteExitosamente() throws Exception {
        String pacienteJson = objectMapper.writeValueAsString(new PacienteRequestDTO(DNI_VALID, NOMBRE_VALID, APELLIDO_VALID, OBRA_SOCIAL, EMAIL_VALID, TELEFONO_VALID));

        mockMvc.perform(post(BASE_URL)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(pacienteJson))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.dni", is(DNI_VALID)))
                .andExpect(jsonPath("$.nombre", is(NOMBRE_VALID)))
                .andExpect(jsonPath("$.apellido", is(APELLIDO_VALID)))
                .andExpect(jsonPath("$.obraSocial", is(OBRA_SOCIAL)));

        Paciente pacienteGuardado = pacienteRepository.getByDni(DNI_VALID).orElse(null);
        assertNotNull(pacienteGuardado);
        assertEquals(NOMBRE_VALID, pacienteGuardado.getNombre());
        assertEquals(APELLIDO_VALID, pacienteGuardado.getApellido());
        assertEquals(OBRA_SOCIAL, pacienteGuardado.getObraSocial());
        assertEquals(EMAIL_VALID, pacienteGuardado.getEmail());
        assertEquals(TELEFONO_VALID, pacienteGuardado.getTelefono());
    }

    @Test
    @DisplayName("Debe fallar al crear un paciente con DNI existente")
    void crearPacienteConDniExistente() throws Exception {
        pacienteRepository.save(paciente);
        String pacienteJson = objectMapper.writeValueAsString(new PacienteRequestDTO(DNI_VALID, "Otro", "Nombre", "Swiss", "otro@mail.com", "123456789"));

        mockMvc.perform(post(BASE_URL)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(pacienteJson))
                .andExpect(status().isConflict());
    }

    @Test
    @DisplayName("Debe fallar al crear un paciente con datos inválidos")
    void crearPacienteConDatosInvalidos() throws Exception {
        String pacienteJson = objectMapper.writeValueAsString(new PacienteRequestDTO(DNI_NO_VALID, NOMBRE_NO_VALID, APELLIDO_NO_VALID, OBRA_SOCIAL, EMAIL_NO_VALID, TELEFONO_NO_VALID));

        mockMvc.perform(post(BASE_URL)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(pacienteJson))
                .andExpect(status().isBadRequest());
    }

    @Test
    @DisplayName("Debe actualizar un paciente existente")
    void actualizarPacienteExitosamente() throws Exception {
        Paciente savedPaciente = pacienteRepository.save(paciente);
        String pacienteUpdateJson = String.format(
                "{\"nombre\": \"%s\", \"email\": \"%s\"}",
                UPDATE_NOMBRE_VALID, UPDATE_EMAIL_VALID
        );

        mockMvc.perform(put(BASE_URL + "/" + savedPaciente.getDni())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(pacienteUpdateJson))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.nombre", is(UPDATE_NOMBRE_VALID)));

        Paciente pacienteActualizado = pacienteRepository.getByDni(savedPaciente.getDni()).orElseThrow();
        assertEquals(UPDATE_NOMBRE_VALID, pacienteActualizado.getNombre());
    }

    @Test
    @DisplayName("Debe fallar al actualizar un paciente inexistente")
    void actualizarPacienteInexistente() throws Exception {
        String pacienteUpdateJson = String.format(
                "{\"nombre\": \"%s\", \"email\": \"%s\"}",
                UPDATE_NOMBRE_VALID, UPDATE_EMAIL_VALID
        );
        mockMvc.perform(put(BASE_URL+"/"+DNI_NO_EXISTS)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(pacienteUpdateJson))
                .andExpect(status().isNotFound());
    }

    @Test
    @DisplayName("Debe fallar al actualizar un paciente con datos inválidos")
    void actualizarPacienteConDatosInvalidos() throws Exception {
        Paciente savedPaciente = pacienteRepository.save(paciente);
        String pacienteUpdateJson = String.format(
                "{\"nombre\": \"%s\", \"email\": \"%s\"}",
                NOMBRE_NO_VALID, EMAIL_NO_VALID
        );

        mockMvc.perform(put(BASE_URL+"/" + savedPaciente.getDni())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(pacienteUpdateJson))
                .andExpect(status().isBadRequest());

        Paciente pacienteNoActualizado = pacienteRepository.getByDni(savedPaciente.getDni()).orElseThrow();
        assertEquals(EMAIL_VALID, pacienteNoActualizado.getEmail());
    }

    @Test
    @DisplayName("Debe borrar un paciente existente")
    void borrarPacienteExitosamente() throws Exception {
        Paciente savedPaciente = pacienteRepository.save(paciente);

        mockMvc.perform(delete(BASE_URL+"/" + savedPaciente.getDni()))
                .andExpect(status().isNoContent());

        assertFalse(pacienteRepository.getByDni(savedPaciente.getDni()).isPresent());
    }

    @Test
    @DisplayName("Debe fallar al borrar un paciente inexistente")
    void borrarPacienteInexistente() throws Exception {
        mockMvc.perform(delete(BASE_URL+"/"+DNI_NO_EXISTS))
                .andExpect(status().isNotFound());
    }

    @Test
    @DisplayName("Debe obtener todos los pacientes")
    void obtenerTodosLosPacientes() throws Exception {
        pacienteRepository.save(paciente);
        pacienteRepository.save(new Paciente("87654321", "Maria", "Gomez", "Galeno", "maria@mail.com", "123456789"));

        mockMvc.perform(get(BASE_URL))
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
        when(pacienteRepository.getByNombreContainingIgnoreCase(NOMBRE_VALID)).thenReturn(List.of(paciente, paciente));

        mockMvc.perform(get(BASE_URL+"?nombre="+NOMBRE_VALID))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(2)))
                .andExpect(jsonPath("$[0].nombre", is(NOMBRE_VALID)))
                .andExpect(jsonPath("$[1].nombre", is(NOMBRE_VALID)));
    }

    @Test
    @DisplayName("Debe obtener paciente por DNI")
    void obtenerPacientePorDni() throws Exception {
        pacienteRepository.save(paciente);

        mockMvc.perform(get(BASE_URL+"/dni/" + paciente.getDni()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.dni", is(paciente.getDni())));
    }

    @Test
    @DisplayName("Debe fallar al buscar un paciente por DNI inexistente")
    void obtenerPacientePorDniInexistente() throws Exception {
        mockMvc.perform(get(BASE_URL+"/dni/"+DNI_NO_EXISTS))
                .andExpect(status().isNotFound());
    }
}
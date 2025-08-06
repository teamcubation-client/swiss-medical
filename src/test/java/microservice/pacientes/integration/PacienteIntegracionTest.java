package microservice.pacientes.integration;


import com.fasterxml.jackson.databind.ObjectMapper;
import microservice.pacientes.infrastructure.adapter.in.controller.PacienteDTO;
import microservice.pacientes.infrastructure.adapter.out.persistence.PacienteEntity;
import microservice.pacientes.infrastructure.adapter.out.persistence.PacienteRepository;

import microservice.pacientes.shared.*;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import static microservice.pacientes.shared.GlobalExceptionHandler.VALIDATION_ERROR_MESSAGE;
import static org.hamcrest.Matchers.containsInAnyOrder;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;

import java.time.LocalDate;



import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


import org.springframework.transaction.annotation.Transactional;



@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
class PacienteIntegracionTest {

    @Autowired
    MockMvc mockMvc;

    @Autowired
    ObjectMapper objectMapper;

    @Autowired
    PacienteRepository repository;

    private PacienteDTO pacienteDTO;
    private PacienteEntity pacienteEntity;
    private String nombrePacienteParcialExistente = "an";
    private String nombrePacienteInexistente = "zzzz";
    @BeforeEach
    void init() {
        pacienteDTO = PacienteDTO.builder()
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

        pacienteEntity = new PacienteEntity();
        pacienteEntity.setDni(pacienteDTO.getDni());
        pacienteEntity.setNombre(pacienteDTO.getNombre());
        pacienteEntity.setApellido(pacienteDTO.getApellido());
        pacienteEntity.setObraSocial(pacienteDTO.getObraSocial());
        pacienteEntity.setEmail(pacienteDTO.getEmail());
        pacienteEntity.setTelefono(pacienteDTO.getTelefono());
        pacienteEntity.setTipoPlanObraSocial(pacienteDTO.getTipoPlanObraSocial());
        pacienteEntity.setFechaAlta(pacienteDTO.getFechaAlta());
        pacienteEntity.setEstado(pacienteDTO.getEstado());
    }

    @AfterEach
    void limpiaBD() {
        repository.deleteAll();
    }

    //Crear Pacientes
    @Test
    @Transactional
    void crearPaciente_CuandoRequestValido_Retorna201() throws Exception {
        String dni = pacienteEntity.getDni();
        mockMvc.perform(post("/api/pacientes")
                        .contentType(APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(pacienteDTO)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.dni").value(pacienteDTO.getDni()))
                .andExpect(jsonPath("$.nombre").value(pacienteDTO.getNombre()));

        var pacienteGuardado = repository.buscarByDni(dni);
        assertTrue(pacienteGuardado.isPresent());
    }


    @Test
    void crearPaciente_CuandoRequestInvalido_Retorna400() throws Exception {
        PacienteDTO pacienteInvalido = pacienteDTO.toBuilder()
                .nombre("")
                .email("bad-email-format")
                .fechaAlta(LocalDate.now().plusDays(1))
                .estado(null)
                .build();

        mockMvc.perform(post("/api/pacientes")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(pacienteInvalido)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.status").value(HttpStatus.BAD_REQUEST.value()))
                .andExpect(jsonPath("$.mensaje").value(VALIDATION_ERROR_MESSAGE))
                .andExpect(jsonPath("$.timestamp").exists());;
    }

    @Test
    @Transactional
    void crearPaciente_CuandoDniDuplicado_Retorna400() throws Exception {
        repository.save(pacienteEntity);
        PacienteDTO pacienteDuplicado = pacienteDTO.toBuilder()
                .dni("12345678")
                .build();

        String expectedMsg = new PacienteDuplicadoException(pacienteEntity.getDni()).getMessage();
        mockMvc.perform(post("/api/pacientes")
                        .contentType(APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(pacienteDuplicado)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.status").value(HttpStatus.BAD_REQUEST.value()))
                .andExpect(jsonPath("$.mensaje").value(expectedMsg))
                .andExpect(jsonPath("$.timestamp").exists());
    }

    @Test
    void crearPaciente_CuandoEmailInvalido_Retorna400() throws Exception {
        PacienteDTO pacienteMailInvalido = pacienteDTO.toBuilder()
                .email("correo-invalido")
                .build();

        String expectedMsg = new InvalidEmailFormatException(pacienteMailInvalido.getEmail()).getMessage();
        mockMvc.perform(post("/api/pacientes")
                        .contentType(APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(pacienteMailInvalido)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.status").value(HttpStatus.BAD_REQUEST.value()))
                .andExpect(jsonPath("$.mensaje").value(expectedMsg))
                .andExpect(jsonPath("$.timestamp").exists());
    }

    @Test
    void crearPaciente_CuandoFechaAltaFutura_Retorna400() throws Exception {
        PacienteDTO pacienteFechaFutura = pacienteDTO.toBuilder()
                .fechaAlta(LocalDate.now().plusDays(1))
                .build();

        String expectedMsg = new InvalidFechaAltaException(pacienteFechaFutura.getFechaAlta()).getMessage();
        mockMvc.perform(post("/api/pacientes")
                        .contentType(APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(pacienteFechaFutura)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.status").value(HttpStatus.BAD_REQUEST.value()))
                .andExpect(jsonPath("$.mensaje").value(expectedMsg))
                .andExpect(jsonPath("$.timestamp").exists());
    }

    //Listar y Obtener Pacientes
    @Test
    void listarPacientes_CuandoNoHayPacientes_Retorna200() throws Exception {
        mockMvc.perform(get("/api/pacientes")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().json("[]"));
    }

    @Test
    @Transactional
    void listarPacientes_CuandoExistenPacientes_Retorna200() throws Exception {
        PacienteEntity pacienteEntity2 = new PacienteEntity();
        pacienteEntity2.setDni("22222222");
        pacienteEntity2.setNombre("Maria");
        pacienteEntity2.setApellido("Gomez");
        pacienteEntity2.setObraSocial("Galeno");
        pacienteEntity2.setEmail("mgomez@mail.com");
        pacienteEntity2.setTelefono("222");
        pacienteEntity2.setTipoPlanObraSocial("Plan2");
        pacienteEntity2.setFechaAlta(LocalDate.now().minusDays(2));
        pacienteEntity2.setEstado(false);

        repository.save(pacienteEntity);
        repository.save(pacienteEntity2);

        mockMvc.perform(get("/api/pacientes")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(2))
                .andExpect(jsonPath("$[*].dni", containsInAnyOrder(
                        pacienteEntity.getDni(),
                        pacienteEntity2.getDni()
                )))
                .andExpect(jsonPath("$[*].nombre", containsInAnyOrder(
                        pacienteEntity.getNombre(),
                        pacienteEntity2.getNombre()
                )));
    }

    @Test
    @Transactional
    void listarPacientesActivos_CuandoExistenActivos_Retorna200() throws Exception {
        pacienteEntity.setEstado(true);
        repository.save(pacienteEntity);

        mockMvc.perform(get("/api/pacientes?estado=true")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(1))
                .andExpect(jsonPath("$[0].dni").value(pacienteEntity.getDni()))
                .andExpect(jsonPath("$[0].estado").value(true));
    }

    @Test
    void listarPacientesActivos_CuandoNoHayActivos_Retorna200() throws Exception {
        mockMvc.perform(get("/api/pacientes?estado=true")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().json("[]"));
    }


    @Test
    void listarPacientesInactivos_CuandoNoHayInactivos_Retorna200() throws Exception {
        mockMvc.perform(get("/api/pacientes?estado=false")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().json("[]"));
    }

    @Test
    void obtenerPaciente_CuandoExiste_Retorna200() throws Exception {
        pacienteEntity = repository.save(pacienteEntity);
        mockMvc.perform(get("/api/pacientes/{id}", pacienteEntity.getId())
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(pacienteEntity.getId()))
                .andExpect(jsonPath("$.dni").value(pacienteEntity.getDni()));
    }

    @Test
    void obtenerPaciente_CuandoNoExiste_Retorna404() throws Exception {
        pacienteEntity = repository.save(pacienteEntity);
        Long idTest = pacienteEntity.getId()+1;
        mockMvc.perform(get("/api/pacientes/{id}", idTest)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
        assertFalse(repository.findById(idTest).isPresent());
    }


    //Eliminar Paciente
    @Test
    @Transactional
    void eliminarPaciente_CuandoInactivo_Retorna204() throws Exception {
        pacienteEntity.setEstado(false);
        pacienteEntity= repository.save(pacienteEntity);
        mockMvc.perform(delete("/api/pacientes/{id}", pacienteEntity.getId()))
                .andExpect(status().isNoContent());

        assertFalse(repository.findById(pacienteEntity.getId()).isPresent());
    }

    @Test
    @Transactional
    void eliminarPaciente_CuandoActivoYSeDesactiva_Retorna204() throws Exception {
        pacienteEntity.setEstado(true);
        pacienteEntity= repository.save(pacienteEntity);

        mockMvc.perform(patch("/api/pacientes/{id}/desactivar", pacienteEntity.getId()))
                .andExpect(status().isOk());
        mockMvc.perform(delete("/api/pacientes/{id}", pacienteEntity.getId()))
                .andExpect(status().isNoContent());

        assertFalse(repository.findById(pacienteEntity.getId()).isPresent());
    }

    @Test
    @Transactional
    void eliminarPaciente_CuandoActivoSinDesactivar_Retorna400() throws Exception {
        pacienteEntity.setEstado(true);
        pacienteEntity= repository.save(pacienteEntity);

        String expectedMsg = new PacienteActivoException(pacienteEntity.getId()).getMessage();

        mockMvc.perform(delete("/api/pacientes/{id}", pacienteEntity.getId()))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.status").value(HttpStatus.BAD_REQUEST.value()))
                .andExpect(jsonPath("$.mensaje").value(expectedMsg))
                .andExpect(jsonPath("$.timestamp").exists());
    }

    //Buscar pacientes
    @Test
    @Transactional
    void buscarPacientesPorNombreParcial_CuandoExistenResultados_Retorna200() throws Exception {
        repository.save(pacienteEntity);
        mockMvc.perform(get("/api/pacientes/buscar/nombre").param("nombre", nombrePacienteParcialExistente))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].nombre").value(pacienteEntity.getNombre()));
    }

    @Test
    void buscarPacientesPorNombreParcial_CuandoNoExistenResultados_Retorna200() throws Exception {
        mockMvc.perform(get("/api/pacientes/buscar/nombre").param("nombre", nombrePacienteInexistente))
                .andExpect(status().isOk())
                .andExpect(content().json("[]"));
    }

    @Test
    @Transactional
    void uscarPacientePorDni_CuandoExiste_Retorna200() throws Exception {
        repository.save(pacienteEntity);
        mockMvc.perform(get("/api/pacientes/sp/buscar/dni/{dni}",pacienteEntity.getDni()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.dni").value(pacienteEntity.getDni()));
    }

    @Test
    void buscarPacientePorDni_CuandoNoExiste_Retorna404() throws Exception {
        String expectedMsg = PacienteNotFoundException.porDni(pacienteEntity.getDni()).getMessage();
        mockMvc.perform(get("/api/pacientes/sp/buscar/dni/{dni}", pacienteEntity.getDni()))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.status").value(HttpStatus.NOT_FOUND.value()))
                .andExpect(jsonPath("$.mensaje").value(expectedMsg))
                .andExpect(jsonPath("$.timestamp").exists());
    }

    @Test
    @Transactional
    void buscarPacientesPorNombreExacto_CuandoExisten_Retorna200() throws Exception {
        repository.save(pacienteEntity);
        mockMvc.perform(get("/api/pacientes/sp/buscar/nombre/{nombre}", nombrePacienteParcialExistente))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].nombre").value(pacienteEntity.getNombre()))
                .andExpect(jsonPath("$.length()").value(1));
    }

    @Test
    void buscarPacientesPorNombreExacto_CuandoNoExisten_Retorna404() throws Exception {
        String expectedMsg = PacienteNotFoundException.porNombre(nombrePacienteInexistente).getMessage();
        mockMvc.perform(get("/api/pacientes/sp/buscar/nombre/{nombre}", nombrePacienteInexistente))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.status").value(HttpStatus.NOT_FOUND.value()))
                .andExpect(jsonPath("$.mensaje").value(expectedMsg))
                .andExpect(jsonPath("$.timestamp").exists());
    }

    @Test
    @Transactional
    void buscarPacientesPorObraSocialPaginado_CuandoExisten_Retorna200() throws Exception {
        repository.save(pacienteEntity);
        mockMvc.perform(get("/api/pacientes/sp/buscar/obra-social")
                        .param("obraSocial", pacienteEntity.getObraSocial())
                        .param("limit", "5")
                        .param("offset", "0"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].obraSocial").value(pacienteEntity.getObraSocial()));
    }

    @Test
    void buscarPacientesPorObraSocialPaginado_CuandoNoExisten_Retorna404() throws Exception {
        String obraSocial = "Sin-obra-social";
        String expectedMsg = PacienteNotFoundException.porObraSocial(obraSocial).getMessage();

        mockMvc.perform(get("/api/pacientes/sp/buscar/obra-social")
                        .param("obraSocial", obraSocial)
                        .param("limit", "5")
                        .param("offset", "0"))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.status").value(HttpStatus.NOT_FOUND.value()))
                .andExpect(jsonPath("$.mensaje").value(expectedMsg))
                .andExpect(jsonPath("$.timestamp").exists());
    }

    //Actualizar Paciente
    @Test
    @Transactional
    void actualizarPaciente_CuandoExiste_Retorna200() throws Exception {
        pacienteEntity = repository.save(pacienteEntity);

        PacienteDTO pacienteActualizado = pacienteDTO.toBuilder()
                .nombre("Modificado")
                .apellido("ModificadoApellido")
                .estado(false)
                .build();

        mockMvc.perform(put("/api/pacientes/{id}", pacienteEntity.getId())
                        .contentType(APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(pacienteActualizado)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(pacienteEntity.getId()))
                .andExpect(jsonPath("$.nombre").value(pacienteActualizado.getNombre()))
                .andExpect(jsonPath("$.apellido").value(pacienteActualizado.getApellido()))
                .andExpect(jsonPath("$.estado").value(pacienteActualizado.getEstado()));

        var actualizado = repository.findById(pacienteEntity.getId()).get();
        assertEquals("Modificado", pacienteActualizado.getNombre());
        assertEquals("ModificadoApellido", pacienteActualizado.getApellido());
        assertFalse(actualizado.isEstado());
    }

    @Test
    @Transactional
    void actualizarPaciente_CuandoNoExiste_Retorna404() throws Exception {
        pacienteEntity = repository.save(pacienteEntity);
        Long idTest = pacienteEntity.getId()+1;
        mockMvc.perform(put("/api/pacientes/{id}", idTest)
                        .contentType(APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(pacienteDTO)))
                .andExpect(status().isNotFound());

        assertFalse(repository.existsById(idTest));
    }

    @Test
    @Transactional
    void actualizarPaciente_CuandoDniDuplicado_Retorna400() throws Exception {
        PacienteEntity pacientePrueba = new PacienteEntity();
        pacientePrueba.setDni("88888888");
        pacientePrueba.setNombre("A");
        pacientePrueba.setApellido("A");
        pacientePrueba.setObraSocial("OSDE");
        pacientePrueba.setEmail("a@a.com");
        pacientePrueba.setTelefono("1");
        pacientePrueba.setTipoPlanObraSocial("P");
        pacientePrueba.setFechaAlta(LocalDate.now().minusDays(1));
        pacientePrueba.setEstado(true);

        pacienteEntity = repository.save(pacienteEntity);
        pacientePrueba = repository.save(pacientePrueba);

        PacienteDTO invalidDto = pacienteDTO.toBuilder()
                .dni(pacientePrueba.getDni())
                .nombre(pacientePrueba.getNombre())
                .apellido(pacientePrueba.getApellido())
                .build();

        String expectedMsg = new PacienteDuplicadoException(invalidDto.getDni()).getMessage();
        mockMvc.perform(put("/api/pacientes/{id}", pacienteEntity.getId())
                        .contentType(APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(invalidDto)))
                .andExpect(jsonPath("$.status").value(HttpStatus.BAD_REQUEST.value()))
                .andExpect(jsonPath("$.mensaje").value(expectedMsg))
                .andExpect(jsonPath("$.timestamp").exists());
    }

    @Test
    @Transactional
    void actualizarPaciente_CuandoEmailInvalido_Retorna400() throws Exception {

        pacienteEntity = repository.save(pacienteEntity);

        PacienteDTO invalidDto = pacienteDTO.toBuilder()
                .email("mal@formato")
                .build();

        String expectedMsg = new InvalidEmailFormatException(invalidDto.getEmail()).getMessage();

        mockMvc.perform(put("/api/pacientes/{id}", pacienteEntity.getId())
                        .contentType(APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(invalidDto)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.status").value(HttpStatus.BAD_REQUEST.value()))
                .andExpect(jsonPath("$.mensaje").value(expectedMsg))
                .andExpect(jsonPath("$.timestamp").exists());
    }

    @Test
    @Transactional
    void actualizarPaciente_CuandoFechaAltaFutura_Retorna400() throws Exception {
        pacienteEntity = repository.save(pacienteEntity);

        PacienteDTO invalidDto = pacienteDTO.toBuilder()
                .fechaAlta(LocalDate.now().plusDays(2))
                .build();

        String expectedMsg = new InvalidFechaAltaException(invalidDto.getFechaAlta()).getMessage();

        mockMvc.perform(put("/api/pacientes/{id}", pacienteEntity.getId())
                        .contentType(APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(invalidDto)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.status").value(HttpStatus.BAD_REQUEST.value()))
                .andExpect(jsonPath("$.mensaje").value(expectedMsg))
                .andExpect(jsonPath("$.timestamp").exists());
    }

    @Test
    @Transactional
    void activarPaciente_CuandoInactivo_Retorna200() throws Exception {
        pacienteEntity.setEstado(false);
        repository.save(pacienteEntity);

        mockMvc.perform(patch("/api/pacientes/{id}/activar", pacienteEntity.getId()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(pacienteEntity.getId()))
                .andExpect(jsonPath("$.estado").value(true));

        PacienteEntity reloaded = repository.findById(pacienteEntity.getId()).get();
        assertTrue(reloaded.isEstado());
    }

    @Test
    @Transactional
    void desactivarPaciente_CuandoActivo_Retorna200() throws Exception {
        pacienteEntity.setEstado(true);
        pacienteEntity = repository.save(pacienteEntity);

        mockMvc.perform(patch("/api/pacientes/{id}/desactivar", pacienteEntity.getId()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(pacienteEntity.getId()))
                .andExpect(jsonPath("$.estado").value(false));

        PacienteEntity reloaded = repository.findById(pacienteEntity.getId()).get();
        assertFalse(reloaded.isEstado());
    }

    @Test
    void activarPaciente_CuandoNoExiste_Retorna404() throws Exception {
        pacienteEntity = repository.save(pacienteEntity);
        long idTest = pacienteEntity.getId()+1;
        mockMvc.perform(patch("/api/pacientes/{id}/activar", idTest))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.status").value(HttpStatus.NOT_FOUND.value()))
                .andExpect(jsonPath("$.mensaje").value(PacienteNotFoundException.porId(idTest).getMessage()))
                .andExpect(jsonPath("$.timestamp").exists());
    }

    @Test
    void desactivarPaciente_CuandoNoExiste_Retorna404() throws Exception {
        pacienteEntity = repository.save(pacienteEntity);
        long idTest = pacienteEntity.getId()+1;
        mockMvc.perform(patch("/api/pacientes/{id}/desactivar", idTest))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.status").value(HttpStatus.NOT_FOUND.value()))
                .andExpect(jsonPath("$.mensaje").value(PacienteNotFoundException.porId(idTest).getMessage()))
                .andExpect(jsonPath("$.timestamp").exists());
    }
}

package microservice.pacientes.infrastructure.adapter.in.controller;

import io.swagger.v3.oas.annotations.Parameter;
import lombok.AllArgsConstructor;
import microservice.pacientes.application.domain.port.in.PacientePortInRead;
import microservice.pacientes.application.domain.port.in.PacientePortInWrite;
import microservice.pacientes.application.domain.port.out.LoggerPort;
import microservice.pacientes.shared.LoggerHelper;
import microservice.pacientes.shared.PacienteNotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;


import java.util.List;
import java.util.stream.Collectors;

import microservice.pacientes.application.domain.model.Paciente;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

/**
 * Controlador REST para la gestion de pacientes
 * Expone endpoints para listar, crear, consultar, actualizar y eliminar pacientes
 */

@RestController
@RequestMapping("/api/pacientes")
@AllArgsConstructor
public class PacienteController implements PacienteApi {

    private final PacientePortInWrite pacientePortInWrite;
    private final PacientePortInRead pacientePortInRead;
    private final LoggerPort logger;

    @GetMapping
    public ResponseEntity<List<PacienteDTO>> listarPacientes(
            @Parameter(description = "Filtrar por estado: true (activos), false (inactivos), null (todos)", example = "true")
            @RequestParam(required = false) Boolean estado) {
        LoggerHelper.entrada(logger, this);
        LoggerHelper.info(logger, this, "Endpoint invocado: GET /api/pacientes");
        List<PacienteDTO> pacientes = pacientePortInRead.listarPacientesPorEstado(estado).stream()
                .map(PacienteResponseMapper::toDTO)
                .toList();
        return ResponseEntity.ok(pacientes);
    }

    @PostMapping
    public ResponseEntity<PacienteDTO> crearPaciente(@Valid @RequestBody PacienteDTO pacienteDTO) {
        LoggerHelper.entrada(logger, this);
        LoggerHelper.info(logger, this, "Endpoint invocado: POST /api/pacientes");
        LoggerHelper.info(logger, this, "Datos recibidos: {}", pacienteDTO.toString());

        Paciente paciente = PacienteResponseMapper.toModel(pacienteDTO);
        Paciente creado = pacientePortInWrite.crearPaciente(paciente);
        PacienteDTO response = PacienteResponseMapper.toDTO(creado);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @GetMapping("/{id}")
    public ResponseEntity<PacienteDTO> obtenerPaciente(@PathVariable Long id) {
        LoggerHelper.entrada(logger, this);
        LoggerHelper.info(logger, this, "Endpoint invocado:GET /api/pacientes/{}", id);
        Paciente paciente = pacientePortInRead.obtenerPacientePorId(id);

        if (paciente == null) {
            LoggerHelper.warn(logger, this, "Paciente con ID {} no encontrado", id);
            throw PacienteNotFoundException.porId(id);
        }
        PacienteDTO response = PacienteResponseMapper.toDTO(paciente);
        LoggerHelper.info(logger, this, "Paciente con ID {} encontrado", response.getId());
        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminarPaciente(@PathVariable Long id) {
        LoggerHelper.entrada(logger, this);
        LoggerHelper.info(logger, this, "Endpoint invocado: DELETE /api/pacientes/{}", id);
        pacientePortInWrite.eliminarPaciente(id);
        LoggerHelper.info(logger, this, "Paciente con ID {} eliminado correctamente", id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/buscar/nombre")
    public ResponseEntity<List<PacienteDTO>> buscarPorNombre(@RequestParam String nombre) {
        LoggerHelper.entrada(logger, this);
        LoggerHelper.info(logger, this, "Endpoint invocado: GET /api/pacientes/buscar/nombre?nombre={}", nombre);
        List<PacienteDTO> pacienteDTOs = pacientePortInRead.buscarPorNombreParcial(nombre)
                .stream()
                .map(PacienteResponseMapper::toDTO)
                .collect(Collectors.toList());
        LoggerHelper.info(logger, this, "Se encontraron {} pacientes con nombre '{}'", pacienteDTOs.size(), nombre);
        return ResponseEntity.ok(pacienteDTOs);
    }

    @PutMapping("/{id}")
    public ResponseEntity<PacienteDTO> actualizarPaciente(@PathVariable Long id, @RequestBody PacienteDTO pacienteDTO) {
        LoggerHelper.entrada(logger, this);
        LoggerHelper.info(logger, this, "Endpoint invocado: PUT /api/pacientes/{}", id);
        Paciente paciente = PacienteResponseMapper.toModel(pacienteDTO);
        paciente.setId(id);
        Paciente actualizado = pacientePortInWrite.actualizarPaciente(id, paciente);
        PacienteDTO response = PacienteResponseMapper.toDTO(actualizado);

        LoggerHelper.info(logger, this, "Paciente con ID {} actualizado", response.getId());
        return ResponseEntity.ok(response);
    }

    @PatchMapping("/{id}/activar")
    @Override
    public ResponseEntity<PacienteDTO> activarPaciente(@PathVariable Long id) {
        LoggerHelper.entrada(logger, this);
        LoggerHelper.info(logger, this, "Endpoint invocado: PATCH /api/pacientes/{}/activar", id);
        Paciente activado = pacientePortInWrite.activarPaciente(id);
        PacienteDTO response = PacienteResponseMapper.toDTO(activado);
        LoggerHelper.info(logger, this, "Paciente con ID {} activado", id);
        return ResponseEntity.ok(response);
    }

    @PatchMapping("/{id}/desactivar")
    @Override
    public ResponseEntity<PacienteDTO> desactivarPaciente(@PathVariable Long id) {
        LoggerHelper.entrada(logger, this);
        LoggerHelper.info(logger, this, "Endpoint invocado: PATCH /api/pacientes/{}/desactivar", id);
        Paciente desactivado = pacientePortInWrite.desactivarPaciente(id);
        PacienteDTO response = PacienteResponseMapper.toDTO(desactivado);
        LoggerHelper.info(logger, this, "Paciente con ID {} desactivado", id);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/sp/buscar/dni/{dni}")
    public ResponseEntity<PacienteDTO> buscarByDni(@PathVariable String dni) {
        LoggerHelper.entrada(logger, this);
        LoggerHelper.info(logger, this, "Endpoint invocado: GET /api/pacientes/sp/buscar/dni/{}", dni);
        Paciente paciente = pacientePortInRead.buscarByDni(dni);
        PacienteDTO dto = PacienteResponseMapper.toDTO(paciente);
        LoggerHelper.info(logger, this, "Paciente encontrado por DNI {}", dni);
        return ResponseEntity.ok(dto);
    }

    @GetMapping("/sp/buscar/nombre/{nombre}")
    public ResponseEntity<List<PacienteDTO>> buscarByNombre(@PathVariable String nombre) {
        LoggerHelper.entrada(logger, this);
        LoggerHelper.info(logger, this, "Endpoint invocado: GET /api/pacientes/sp/buscar/nombre/{}", nombre);
        List<Paciente> pacientes = pacientePortInRead.buscarByNombre(nombre);
        List<PacienteDTO> dto = pacientes.stream()
                .map(PacienteResponseMapper::toDTO)
                .collect(Collectors.toList());
        LoggerHelper.info(logger, this, "Se encontraron {} pacientes por nombre '{}'", dto.size(), nombre);
        return ResponseEntity.ok(dto);
    }

    @GetMapping("/sp/buscar/obra-social")
    public ResponseEntity<List<PacienteDTO>> buscarPorObraSocialPaginado(
            @RequestParam String obraSocial,
            @RequestParam(defaultValue = "10") int limit,
            @RequestParam(defaultValue = "0") int offset) {
        LoggerHelper.entrada(logger, this);
        LoggerHelper.info(logger, this, "Endpoint invocado: GET /api/pacientes/sp/buscar/obra-social?obraSocial={}&limit={}&offset={}",
                obraSocial, limit, offset);
        List<Paciente> pacientes = pacientePortInRead.buscarPorObraSocialPaginado(obraSocial, limit, offset);
        List<PacienteDTO> dto = pacientes.stream()
                .map(PacienteResponseMapper::toDTO)
                .collect(Collectors.toList());
        LoggerHelper.info(logger, this, "Se encontraron {} pacientes por obra social '{}'", dto.size(), obraSocial);
        return ResponseEntity.ok(dto);
    }
}

package microservice.pacientes.infrastructure.adapter.in.controller;

import lombok.AllArgsConstructor;
import microservice.pacientes.application.domain.port.in.PacientePortInRead;
import microservice.pacientes.application.domain.port.in.PacientePortInWrite;
import microservice.pacientes.shared.PacienteNotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import microservice.pacientes.application.domain.model.Paciente;

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

    @GetMapping
    public ResponseEntity<List<PacienteDTO>> listarPacientes() {
        List<PacienteDTO> dtos = pacientePortInRead.listarPacientes().stream()
                .map(PacienteResponseMapper::toDTO)
                .collect(Collectors.toList());
        return ResponseEntity.ok(dtos);
    }

    @PostMapping
    public ResponseEntity<PacienteDTO> crearPaciente(@Valid @RequestBody PacienteDTO pacienteDTO) {
        Paciente paciente = PacienteResponseMapper.toModel(pacienteDTO);
        Paciente creado = pacientePortInWrite.crearPaciente(paciente);
        PacienteDTO response = PacienteResponseMapper.toDTO(creado);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @GetMapping("/{id}")
    public ResponseEntity<PacienteDTO> obtenerPaciente(@PathVariable Long id) {
        Paciente paciente = pacientePortInRead.obtenerPacientePorId(id);
        if (paciente == null) {
            throw PacienteNotFoundException.porId(id);
        }
        PacienteDTO response = PacienteResponseMapper.toDTO(paciente);
        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminarPaciente(@PathVariable Long id) {
        pacientePortInWrite.eliminarPaciente(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/buscar/nombre")
    public ResponseEntity<List<PacienteDTO>> buscarPorNombre(@RequestParam String nombre) {
        List<PacienteDTO> pacienteDTOs = pacientePortInRead.buscarPorNombreParcial(nombre)
                .stream()
                .map(PacienteResponseMapper::toDTO)
                .collect(Collectors.toList());
        return ResponseEntity.ok(pacienteDTOs);
    }

    @PutMapping("/{id}")
    public ResponseEntity<PacienteDTO> actualizarPaciente(@PathVariable Long id, @RequestBody PacienteDTO pacienteDTO) {
        Paciente paciente = PacienteResponseMapper.toModel(pacienteDTO);
        Paciente actualizado = pacientePortInWrite.actualizarPaciente(id, paciente);
        PacienteDTO response = PacienteResponseMapper.toDTO(actualizado);

        return ResponseEntity.ok(response);
    }

    @PatchMapping("/{id}/activar")
    @Override
    public ResponseEntity<PacienteDTO> activarPaciente(@PathVariable Long id) {
        Paciente activado = pacientePortInWrite.activarPaciente(id);
        PacienteDTO response = PacienteResponseMapper.toDTO(activado);

        return ResponseEntity.ok(response);
    }

    @PatchMapping("/{id}/desactivar")
    @Override
    public ResponseEntity<PacienteDTO> desactivarPaciente(@PathVariable Long id) {
        Paciente desactivado = pacientePortInWrite.desactivarPaciente(id);
        PacienteDTO response = PacienteResponseMapper.toDTO(desactivado);

        return ResponseEntity.ok(response);
    }

    @GetMapping("/activos")
    @Override
    public ResponseEntity<List<PacienteDTO>> listarPacientesActivos() {
        List<PacienteDTO> activos = pacientePortInRead.listarPacientes().stream()
                .filter(Paciente::isEstado)
                .map(PacienteResponseMapper::toDTO)
                .collect(Collectors.toList());
        return ResponseEntity.ok(activos);
    }

    @GetMapping("/inactivos")
    @Override
    public ResponseEntity<List<PacienteDTO>> listarPacientesInactivos() {
        List<PacienteDTO> inactivos = pacientePortInRead.listarPacientes().stream()
                .filter(p -> !p.isEstado())
                .map(PacienteResponseMapper::toDTO)
                .collect(Collectors.toList());

        return ResponseEntity.ok(inactivos);
    }

    @GetMapping("/sp/buscar/dni/{dni}")
    public ResponseEntity<PacienteDTO> buscarByDni(@PathVariable String dni) {
        Paciente paciente = pacientePortInRead.buscarByDni(dni);
        PacienteDTO dto = PacienteResponseMapper.toDTO(paciente);

        return ResponseEntity.ok(dto);
    }

    @GetMapping("/sp/buscar/nombre/{nombre}")
    public ResponseEntity<List<PacienteDTO>> buscarByNombre(@PathVariable String nombre) {
        List<Paciente> pacientes = pacientePortInRead.buscarByNombre(nombre);
        List<PacienteDTO> dto = pacientes.stream()
                .map(PacienteResponseMapper::toDTO)
                .collect(Collectors.toList());

        return ResponseEntity.ok(dto);
    }

    @GetMapping("/sp/buscar/obra-social")
    public ResponseEntity<List<PacienteDTO>> buscarPorObraSocialPaginado(
            @RequestParam String obraSocial,
            @RequestParam(defaultValue = "10") int limit,
            @RequestParam(defaultValue = "0") int offset) {
        List<Paciente> pacientes = pacientePortInRead.buscarPorObraSocialPaginado(obraSocial, limit, offset);
        List<PacienteDTO> dto = pacientes.stream()
                .map(PacienteResponseMapper::toDTO)
                .collect(Collectors.toList());

        return ResponseEntity.ok(dto);
    }
}

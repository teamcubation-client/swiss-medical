package microservice.pacientes.infrastructure.adapter.in.rest;

import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import microservice.pacientes.application.domain.command.CreatePacienteCommand;
import microservice.pacientes.application.domain.command.UpdatePacienteCommand;
import microservice.pacientes.application.domain.model.Paciente;
import microservice.pacientes.application.domain.port.in.CreatePacienteUseCase;
import microservice.pacientes.application.domain.port.in.DeletePacienteUseCase;
import microservice.pacientes.application.domain.port.in.FindPacienteUseCase;
import microservice.pacientes.application.domain.port.in.UpdatePacienteUseCase;
import microservice.pacientes.infrastructure.adapter.in.rest.docs.PacienteControllerDocs;
import microservice.pacientes.infrastructure.adapter.in.rest.dto.PacienteRequestDTO;
import microservice.pacientes.infrastructure.adapter.in.rest.dto.PacienteResponseDTO;
import microservice.pacientes.infrastructure.adapter.in.rest.dto.PacienteUpdateDTO;
import microservice.pacientes.infrastructure.adapter.in.rest.dto.mapper.PacienteRequestMapper;
import microservice.pacientes.infrastructure.adapter.in.rest.dto.mapper.PacienteResponseMapper;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/pacientes")
@AllArgsConstructor
public class PacienteController implements PacienteControllerDocs {

    private final CreatePacienteUseCase createPacienteUseCase;
    private final DeletePacienteUseCase deletePacienteUseCase;
    private final FindPacienteUseCase findPacienteUseCase;
    private final UpdatePacienteUseCase updatePacienteUseCase;

    @GetMapping
    public ResponseEntity<List<PacienteResponseDTO>> getAll(@RequestParam(required = false) String nombre) {
        List<Paciente> pacientes;
        if(nombre != null){
            pacientes = findPacienteUseCase.getByNombreContainingIgnoreCase(nombre);
        } else {
            pacientes = findPacienteUseCase.getAll();
        }
        List<PacienteResponseDTO> pacientesResponseDTO = PacienteResponseMapper.toDTO(pacientes);
        return ResponseEntity.ok(pacientesResponseDTO);
    }

    @GetMapping("/{dni}")
    public ResponseEntity<PacienteResponseDTO> getByDni(@PathVariable String dni) {
        Paciente paciente = findPacienteUseCase.getByDni(dni);
        PacienteResponseDTO pacienteResponseDTO = PacienteResponseMapper.toDTO(paciente);
        return ResponseEntity.ok(pacienteResponseDTO);
    }

    @PostMapping
    public ResponseEntity<PacienteResponseDTO> create(@Valid @RequestBody PacienteRequestDTO pacienteRequestDTO) {
        CreatePacienteCommand createPacienteCommand = PacienteRequestMapper.toCreateCommand(pacienteRequestDTO);
        Paciente paciente = createPacienteUseCase.create(createPacienteCommand);
        PacienteResponseDTO pacienteResponseDTO = PacienteResponseMapper.toDTO(paciente);
        return ResponseEntity.status(HttpStatus.CREATED).body(pacienteResponseDTO);
    }

    @PutMapping("/{dni}")
    public ResponseEntity<PacienteResponseDTO> update(@PathVariable String dni, @Valid @RequestBody PacienteUpdateDTO pacienteUpdateDTO) {
        UpdatePacienteCommand updatePacienteCommand = PacienteRequestMapper.toUpdateCommand(pacienteUpdateDTO);
        Paciente paciente = updatePacienteUseCase.update(dni, updatePacienteCommand);
        PacienteResponseDTO pacienteResponseDTO = PacienteResponseMapper.toDTO(paciente);
        return ResponseEntity.ok(pacienteResponseDTO);
    }

    @DeleteMapping("/{dni}")
    public ResponseEntity<Void> delete(@PathVariable String dni) {
        deletePacienteUseCase.delete(dni);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/dni/{dni}")
    public ResponseEntity<PacienteResponseDTO> getByDniSP(@PathVariable String dni) {
        Paciente paciente = findPacienteUseCase.getByDni(dni);
        PacienteResponseDTO pacienteResponseDTO = PacienteResponseMapper.toDTO(paciente);
        return ResponseEntity.ok(pacienteResponseDTO);
    }


    @GetMapping("/nombre/{nombre}")
    public ResponseEntity<PacienteResponseDTO> getByNombreSP(@PathVariable String nombre) {
        Paciente paciente = findPacienteUseCase.getByNombre(nombre);
        PacienteResponseDTO pacienteResponseDTO = PacienteResponseMapper.toDTO(paciente);
        return ResponseEntity.ok(pacienteResponseDTO);
    }


    @GetMapping("/obra_social")
    public ResponseEntity<List<PacienteResponseDTO>> getByObraSocialSP(
            @RequestParam(required = true) String obraSocial,
            @RequestParam(required = true) int page,
            @RequestParam(required = true) int size
    ) {
        int limit = size;
        int offset = (page - 1) * size; // asumo que page empieza con 1
        List<Paciente> pacientes = findPacienteUseCase.getByObraSocial(obraSocial, limit, offset);
        List<PacienteResponseDTO> pacientesResponseDTO = PacienteResponseMapper.toDTO(pacientes);
        return ResponseEntity.ok(pacientesResponseDTO);
    }

}

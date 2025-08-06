package microservice.pacientes.infrastructure.adapter.in.rest;

import jakarta.validation.Valid;
import microservice.pacientes.application.domain.command.CreatePacienteCommand;
import microservice.pacientes.application.domain.command.UpdatePacienteCommand;
import microservice.pacientes.application.domain.model.Paciente;
import microservice.pacientes.application.domain.port.in.CreatePacienteUseCase;
import microservice.pacientes.application.domain.port.in.DeletePacienteUseCase;
import microservice.pacientes.application.domain.port.in.FindPacienteUseCase;
import microservice.pacientes.application.domain.port.in.UpdatePacienteUseCase;
import microservice.pacientes.application.domain.port.out.LoggerPort;
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
public class PacienteController implements PacienteControllerDocs {

    private final CreatePacienteUseCase createPacienteUseCase;
    private final DeletePacienteUseCase deletePacienteUseCase;
    private final FindPacienteUseCase findPacienteUseCase;
    private final UpdatePacienteUseCase updatePacienteUseCase;
    private final LoggerPort logger;

    public PacienteController(CreatePacienteUseCase createPacienteUseCase, DeletePacienteUseCase deletePacienteUseCase, FindPacienteUseCase findPacienteUseCase, UpdatePacienteUseCase updatePacienteUseCase, LoggerPort logger) {
        this.createPacienteUseCase = createPacienteUseCase;
        this.deletePacienteUseCase = deletePacienteUseCase;
        this.findPacienteUseCase = findPacienteUseCase;
        this.updatePacienteUseCase = updatePacienteUseCase;
        this.logger = logger;
        this.logger.setOrigin(getClass().getSimpleName());
    }

    @GetMapping
    public ResponseEntity<List<PacienteResponseDTO>> getAll(@RequestParam(required = false) String nombre) {
        logger.info("Request received to get all patients. Filter by name: " + (nombre != null ? nombre : "none"));
        List<Paciente> pacientes;
        if(nombre != null){
            pacientes = findPacienteUseCase.getByNombreContainingIgnoreCase(nombre);
        } else {
            pacientes = findPacienteUseCase.getAll();
        }
        List<PacienteResponseDTO> pacientesResponseDTO = PacienteResponseMapper.toDTO(pacientes);
        logger.info("Response sent with " + pacientesResponseDTO.size() + " patients. Status: " + HttpStatus.OK);
        return ResponseEntity.ok(pacientesResponseDTO);
    }

    @GetMapping("/{dni}")
    public ResponseEntity<PacienteResponseDTO> getByDni(@PathVariable String dni) {
        logger.info("Request received to get patient by DNI: " + dni);
        Paciente paciente = findPacienteUseCase.getByDni(dni);
        PacienteResponseDTO pacienteResponseDTO = PacienteResponseMapper.toDTO(paciente);
        logger.info("Response sent for DNI " + dni + ". Status: " + HttpStatus.OK);
        return ResponseEntity.ok(pacienteResponseDTO);
    }

    @PostMapping
    public ResponseEntity<PacienteResponseDTO> create(@Valid @RequestBody PacienteRequestDTO pacienteRequestDTO) {
        logger.info("Request received to create a new patient.");
        CreatePacienteCommand createPacienteCommand = PacienteRequestMapper.toCreateCommand(pacienteRequestDTO);
        Paciente paciente = createPacienteUseCase.create(createPacienteCommand);
        PacienteResponseDTO pacienteResponseDTO = PacienteResponseMapper.toDTO(paciente);
        logger.info("Patient created with DNI " + paciente.getDni() + ". Response sent with status: " + HttpStatus.CREATED);
        return ResponseEntity.status(HttpStatus.CREATED).body(pacienteResponseDTO);
    }

    @PutMapping("/{dni}")
    public ResponseEntity<PacienteResponseDTO> update(@PathVariable String dni, @Valid @RequestBody PacienteUpdateDTO pacienteUpdateDTO) {
        logger.info("Request received to update patient with DNI: " + dni);
        UpdatePacienteCommand updatePacienteCommand = PacienteRequestMapper.toUpdateCommand(pacienteUpdateDTO);
        Paciente paciente = updatePacienteUseCase.update(dni, updatePacienteCommand);
        PacienteResponseDTO pacienteResponseDTO = PacienteResponseMapper.toDTO(paciente);
        logger.info("Patient with DNI " + dni + " updated. Response sent with status: " + HttpStatus.OK);
        return ResponseEntity.ok(pacienteResponseDTO);
    }

    @DeleteMapping("/{dni}")
    public ResponseEntity<Void> delete(@PathVariable String dni) {
        logger.info("Request received to delete patient with DNI: " + dni);
        deletePacienteUseCase.delete(dni);
        logger.info("Patient with DNI " + dni + " deleted. Response sent with status: " + HttpStatus.NO_CONTENT);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/dni/{dni}")
    public ResponseEntity<PacienteResponseDTO> getByDniSP(@PathVariable String dni) {
        logger.info("Request (SP) received to get patient by DNI: " + dni);
        Paciente paciente = findPacienteUseCase.getByDni(dni);
        PacienteResponseDTO pacienteResponseDTO = PacienteResponseMapper.toDTO(paciente);
        logger.info("Response (SP) sent for DNI " + dni + ". Status: " + HttpStatus.OK);
        return ResponseEntity.ok(pacienteResponseDTO);
    }


    @GetMapping("/nombre/{nombre}")
    public ResponseEntity<PacienteResponseDTO> getByNombreSP(@PathVariable String nombre) {
        logger.info("Request (SP) received to get patient by name: " + nombre);
        Paciente paciente = findPacienteUseCase.getByNombre(nombre);
        PacienteResponseDTO pacienteResponseDTO = PacienteResponseMapper.toDTO(paciente);
        logger.info("Response (SP) sent for name " + nombre + ". Status: " + HttpStatus.OK);
        return ResponseEntity.ok(pacienteResponseDTO);
    }


    @GetMapping("/obra_social")
    public ResponseEntity<List<PacienteResponseDTO>> getByObraSocialSP(
            @RequestParam(required = true) String obraSocial,
            @RequestParam(required = true) int page,
            @RequestParam(required = true) int size
    ) {
        logger.info("Request (SP) to search patients by health insurance: " + obraSocial + " with page: " + page + " and size: " + size);
        int limit = size;
        int offset = (page - 1) * size; // assuming page starts at 1
        List<Paciente> pacientes = findPacienteUseCase.getByObraSocial(obraSocial, limit, offset);
        List<PacienteResponseDTO> pacientesResponseDTO = PacienteResponseMapper.toDTO(pacientes);
        logger.info("Response (SP) sent with " + pacientesResponseDTO.size() + " patients for health insurance '" + obraSocial + "'. Status: " + HttpStatus.OK);
        return ResponseEntity.ok(pacientesResponseDTO);
    }

}

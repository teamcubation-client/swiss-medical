package microservice.pacientes.infrastructure.adapter.in.controller;

import lombok.AllArgsConstructor;
import microservice.pacientes.application.domain.port.in.PacientePortInRead;
import microservice.pacientes.application.domain.port.in.PacientePortInWrite;
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

    /**
     * Obtiene la lista de todos los pacientes registrados
     * @return lista de PacienteDTO
     */
    @GetMapping
    public ResponseEntity<List<PacienteDTO>> listarPacientes() {
        List<PacienteDTO> dtos = pacientePortInRead.listarPacientes().stream()
                .map(PacienteResponseMapper::toDTO)
                .collect(Collectors.toList());
        return ResponseEntity.ok(dtos);
    }
    
    /**
     * Crea un nuevo paciente a partir de un DTO
     * @param pacienteDTO datos del paciente a crear
     * @return PacienteDTO creado
     */
    @PostMapping
    public ResponseEntity<PacienteDTO> crearPaciente(@Valid @RequestBody PacienteDTO pacienteDTO) {
        Paciente paciente = PacienteResponseMapper.toModel(pacienteDTO);
        Paciente creado = pacientePortInWrite.crearPaciente(paciente);
        return ResponseEntity.status(HttpStatus.CREATED).body(PacienteResponseMapper.toDTO(creado));
    }

    /**
     * Obtiene un paciente por su identificador unico
     * @param id identificador del paciente
     * @return PacienteDTO encontrado o null si no existe
     */

    @GetMapping("/{id}")
    public ResponseEntity<PacienteDTO> obtenerPaciente(@PathVariable Long id) {
        Paciente paciente = pacientePortInRead.obtenerPacientePorId(id);
        if (paciente == null)
            return ResponseEntity.notFound().build();
        return ResponseEntity.ok(PacienteResponseMapper.toDTO(paciente));
    }
    
    /**
     * Elimina un paciente por su identificador unico
     * @param id identificador del paciente a eliminar
     */

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminarPaciente(@PathVariable Long id) {
        pacientePortInWrite.eliminarPaciente(id);
        return ResponseEntity.noContent().build();
    }

    /**
     * Busca un paciente por su DNI
     * @param dni Documento Nacional de Identidad
     * @return PacienteDTO encontrado o null si no existe
     */

    @GetMapping("/buscar/dni")
    public ResponseEntity<PacienteDTO> buscarPorDni(@RequestParam String dni) {
        Paciente paciente = pacientePortInRead.buscarPorDni(dni);
        if (paciente == null)
            return ResponseEntity.notFound().build();
        return ResponseEntity.ok(PacienteResponseMapper.toDTO(paciente));
    }

    /**
     * Busca pacientes cuyo nombre contenga el string especificado
     * @param nombre parte o nombre completo a buscar
     * @return lista de PacienteDTO que coinciden con el parametro
     */

    @GetMapping("/buscar/nombre")
    public ResponseEntity<List<PacienteDTO>> buscarPorNombre(@RequestParam String nombre) {
        List<PacienteDTO> pacienteDTOs = pacientePortInRead.buscarPorNombreParcial(nombre)
                .stream()
                .map(PacienteResponseMapper::toDTO)
                .collect(Collectors.toList());
        return ResponseEntity.ok(pacienteDTOs);
    }



    /**
     * Actualiza los datos de un paciente existente
     * @param id identificador del paciente a actualizar
     * @param pacienteDTO datos nuevos del paciente
     * @return PacienteDTO actualizado o null si no existe
     */

    @PutMapping("/{id}")
    public ResponseEntity<PacienteDTO> actualizarPaciente(@PathVariable Long id, @RequestBody PacienteDTO pacienteDTO) {
        Paciente paciente = PacienteResponseMapper.toModel(pacienteDTO);
        Paciente actualizado = pacientePortInWrite.actualizarPaciente(id, paciente);
        if (actualizado == null)
            return ResponseEntity.notFound().build();
        return ResponseEntity.ok(PacienteResponseMapper.toDTO(actualizado));
    }

    @PatchMapping("/{id}/activar")
    @Override
    public ResponseEntity<PacienteDTO> activarPaciente(@PathVariable Long id) {
        Paciente activado = pacientePortInWrite.activarPaciente(id);
        return ResponseEntity.ok(PacienteResponseMapper.toDTO(activado));
    }

    @PatchMapping("/{id}/desactivar")
    @Override
    public ResponseEntity<PacienteDTO> desactivarPaciente(@PathVariable Long id) {
        Paciente desactivado = pacientePortInWrite.desactivarPaciente(id);
        return ResponseEntity.ok(PacienteResponseMapper.toDTO(desactivado));
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

    /**
     * Busca un paciente por su DNI usando stored procedure
     * @param dni Documento Nacional de Identidad
     * @return PacienteDTO encontrado
     */
    @GetMapping("/sp/buscar/dni/{dni}")
    public ResponseEntity<PacienteDTO> buscarByDni(@PathVariable String dni) {
        Paciente paciente = pacientePortInRead.buscarByDni(dni);
        PacienteDTO dto = PacienteResponseMapper.toDTO(paciente);
        return ResponseEntity.ok(dto);
    }

    /**
     * Busca pacientes cuyo nombre contenga la cadena especificada usando stored procedure
     * @param nombre parte o nombre completo a buscar
     * @return lista de PacienteDTO que coinciden con el parametro
     */
    @GetMapping("/sp/buscar/nombre/{nombre}")
    public ResponseEntity<List<PacienteDTO>> buscarByNombre(@PathVariable String nombre) {
        List<Paciente> paciente = pacientePortInRead.buscarByNombre(nombre);
        List<PacienteDTO> dto = paciente.stream()
                .map(PacienteResponseMapper::toDTO)
                .collect(Collectors.toList());
        return ResponseEntity.ok(dto);
    }

    /**
     * Busca pacientes por obra social con paginacion usando stored procedure
     * @param obraSocial nombre de la obra social
     * @param limit cantidad maxima de resultados
     * @param offset desplazamiento de resultados
     * @return lista de PacienteDTO
     */
    @GetMapping("/sp/buscar/obra-social")
    public ResponseEntity<List<PacienteDTO>> buscarPorObraSocialPaginado(
            @RequestParam String obraSocial,
            @RequestParam(defaultValue = "10") int limit,
            @RequestParam(defaultValue = "0") int offset) {
        List<Paciente> paciente = pacientePortInRead.buscarPorObraSocialPaginado(obraSocial, limit, offset);
        List<PacienteDTO> dto = paciente.stream()
                .map(PacienteResponseMapper::toDTO)
                .collect(Collectors.toList());
        return ResponseEntity.ok(dto);
    }
}

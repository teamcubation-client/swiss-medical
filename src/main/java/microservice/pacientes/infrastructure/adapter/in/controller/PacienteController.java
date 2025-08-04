package microservice.pacientes.infrastructure.adapter.in.controller;

import lombok.AllArgsConstructor;
import microservice.pacientes.application.domain.port.in.PacientePortInRead;
import microservice.pacientes.application.domain.port.in.PacientePortInWrite;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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
    private static final Logger logger = LoggerFactory.getLogger(PacienteController.class);

    @GetMapping
    public ResponseEntity<List<PacienteDTO>> listarPacientes() {
        logger.info("GET /api/pacientes - Iniciando listado de todos los pacientes");
        try {
            List<PacienteDTO> dtos = pacientePortInRead.listarPacientes().stream()
                    .map(PacienteResponseMapper::toDTO)
                    .collect(Collectors.toList());
            logger.info("GET /api/pacientes - Listado completado. Se encontraron {} pacientes", dtos.size());
            return ResponseEntity.ok(dtos);
        } catch (Exception e) {
            logger.error("GET /api/pacientes - Error al listar pacientes: {}", e.getMessage());
            throw e;
        }
    }

    @PostMapping
    public ResponseEntity<PacienteDTO> crearPaciente(@Valid @RequestBody PacienteDTO pacienteDTO) {
        logger.info("POST /api/pacientes - Creando paciente con DNI: {}", pacienteDTO.getDni());
        logger.debug("POST /api/pacientes - Datos del paciente: {}", pacienteDTO);
        
        try {
            Paciente paciente = PacienteResponseMapper.toModel(pacienteDTO);
            Paciente creado = pacientePortInWrite.crearPaciente(paciente);
            PacienteDTO response = PacienteResponseMapper.toDTO(creado);
            
            logger.info("POST /api/pacientes - Paciente creado exitosamente con ID: {}", creado.getId());
            return ResponseEntity.status(HttpStatus.CREATED).body(response);
        } catch (Exception e) {
            logger.error("POST /api/pacientes - Error al crear paciente con DNI {}: {}", 
                       pacienteDTO.getDni(), e.getMessage());
            throw e;
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<PacienteDTO> obtenerPaciente(@PathVariable Long id) {
        logger.info("GET /api/pacientes/{} - Buscando paciente por ID", id);
        
        try {
            Paciente paciente = pacientePortInRead.obtenerPacientePorId(id);
            if (paciente == null) {
                logger.warn("GET /api/pacientes/{} - Paciente no encontrado", id);
                return ResponseEntity.notFound().build();
            }
            
            PacienteDTO response = PacienteResponseMapper.toDTO(paciente);
            logger.info("GET /api/pacientes/{} - Paciente encontrado exitosamente", id);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            logger.error("GET /api/pacientes/{} - Error al buscar paciente: {}", id, e.getMessage());
            throw e;
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminarPaciente(@PathVariable Long id) {
        logger.info("DELETE /api/pacientes/{} - Eliminando paciente con ID: {}", id);
        
        try {
            pacientePortInWrite.eliminarPaciente(id);
            logger.info("DELETE /api/pacientes/{} - Paciente eliminado exitosamente", id);
            return ResponseEntity.noContent().build();
        } catch (Exception e) {
            logger.error("DELETE /api/pacientes/{} - Error al eliminar paciente: {}", id, e.getMessage());
            throw e;
        }
    }

    @GetMapping("/buscar/nombre")
    public ResponseEntity<List<PacienteDTO>> buscarPorNombre(@RequestParam String nombre) {
        logger.info("GET /api/pacientes/buscar/nombre?nombre={} - Buscando pacientes por nombre", nombre);
        
        try {
            List<PacienteDTO> pacienteDTOs = pacientePortInRead.buscarPorNombreParcial(nombre)
                    .stream()
                    .map(PacienteResponseMapper::toDTO)
                    .collect(Collectors.toList());
            
            logger.info("GET /api/pacientes/buscar/nombre - Busqueda completada. Se encontraron {} pacientes", 
                       pacienteDTOs.size());
            return ResponseEntity.ok(pacienteDTOs);
        } catch (Exception e) {
            logger.error("GET /api/pacientes/buscar/nombre - Error al buscar pacientes por nombre '{}': {}", 
                       nombre, e.getMessage());
            throw e;
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<PacienteDTO> actualizarPaciente(@PathVariable Long id, @RequestBody PacienteDTO pacienteDTO) {
        logger.info("PUT /api/pacientes/{} - Actualizando paciente con ID: {}", id);
        logger.debug("PUT /api/pacientes/{} - Datos del paciente: {}", id, pacienteDTO);
        
        try {
            Paciente paciente = PacienteResponseMapper.toModel(pacienteDTO);
            Paciente actualizado = pacientePortInWrite.actualizarPaciente(id, paciente);
            if (actualizado == null) {
                logger.warn("PUT /api/pacientes/{} - Paciente no encontrado para actualizar", id);
                return ResponseEntity.notFound().build();
            }
            
            PacienteDTO response = PacienteResponseMapper.toDTO(actualizado);
            logger.info("PUT /api/pacientes/{} - Paciente actualizado exitosamente", id);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            logger.error("PUT /api/pacientes/{} - Error al actualizar paciente: {}", id, e.getMessage());
            throw e;
        }
    }

    @PatchMapping("/{id}/activar")
    @Override
    public ResponseEntity<PacienteDTO> activarPaciente(@PathVariable Long id) {
        logger.info("PATCH /api/pacientes/{}/activar - Activando paciente", id);
        
        try {
            Paciente activado = pacientePortInWrite.activarPaciente(id);
            PacienteDTO response = PacienteResponseMapper.toDTO(activado);
            
            logger.info("PATCH /api/pacientes/{}/activar - Paciente activado exitosamente", id);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            logger.error("PATCH /api/pacientes/{}/activar - Error al activar paciente: {}", id, e.getMessage());
            throw e;
        }
    }

    @PatchMapping("/{id}/desactivar")
    @Override
    public ResponseEntity<PacienteDTO> desactivarPaciente(@PathVariable Long id) {
        logger.info("PATCH /api/pacientes/{}/desactivar - Desactivando paciente", id);
        
        try {
            Paciente desactivado = pacientePortInWrite.desactivarPaciente(id);
            PacienteDTO response = PacienteResponseMapper.toDTO(desactivado);
            
            logger.info("PATCH /api/pacientes/{}/desactivar - Paciente desactivado exitosamente", id);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            logger.error("PATCH /api/pacientes/{}/desactivar - Error al desactivar paciente: {}", id, e.getMessage());
            throw e;
        }
    }

    @GetMapping("/activos")
    @Override
    public ResponseEntity<List<PacienteDTO>> listarPacientesActivos() {
        logger.info("GET /api/pacientes/activos - Listando pacientes activos");
        
        try {
            List<PacienteDTO> activos = pacientePortInRead.listarPacientes().stream()
                    .filter(Paciente::isEstado)
                    .map(PacienteResponseMapper::toDTO)
                    .collect(Collectors.toList());
            
            logger.info("GET /api/pacientes/activos - Listado completado. Se encontraron {} pacientes activos", 
                       activos.size());
            return ResponseEntity.ok(activos);
        } catch (Exception e) {
            logger.error("GET /api/pacientes/activos - Error al listar pacientes activos: {}", e.getMessage());
            throw e;
        }
    }

    @GetMapping("/inactivos")
    @Override
    public ResponseEntity<List<PacienteDTO>> listarPacientesInactivos() {
        logger.info("GET /api/pacientes/inactivos - Listando pacientes inactivos");
        
        try {
            List<PacienteDTO> inactivos = pacientePortInRead.listarPacientes().stream()
                    .filter(p -> !p.isEstado())
                    .map(PacienteResponseMapper::toDTO)
                    .collect(Collectors.toList());
            
            logger.info("GET /api/pacientes/inactivos - Listado completado. Se encontraron {} pacientes inactivos", 
                       inactivos.size());
            return ResponseEntity.ok(inactivos);
        } catch (Exception e) {
            logger.error("GET /api/pacientes/inactivos - Error al listar pacientes inactivos: {}", e.getMessage());
            throw e;
        }
    }

    @GetMapping("/sp/buscar/dni/{dni}")
    public ResponseEntity<PacienteDTO> buscarByDni(@PathVariable String dni) {
        logger.info("GET /api/pacientes/sp/buscar/dni/{} - Buscando paciente por DNI", dni);
        
        try {
            Paciente paciente = pacientePortInRead.buscarByDni(dni);
            PacienteDTO dto = PacienteResponseMapper.toDTO(paciente);
            
            logger.info("GET /api/pacientes/sp/buscar/dni/{} - Paciente encontrado exitosamente", dni);
            return ResponseEntity.ok(dto);
        } catch (Exception e) {
            logger.error("GET /api/pacientes/sp/buscar/dni/{} - Error al buscar paciente por DNI: {}", 
                       dni, e.getMessage());
            throw e;
        }
    }

    @GetMapping("/sp/buscar/nombre/{nombre}")
    public ResponseEntity<List<PacienteDTO>> buscarByNombre(@PathVariable String nombre) {
        logger.info("GET /api/pacientes/sp/buscar/nombre/{} - Buscando pacientes por nombre exacto", nombre);
        
        try {
            List<Paciente> paciente = pacientePortInRead.buscarByNombre(nombre);
            List<PacienteDTO> dto = paciente.stream()
                    .map(PacienteResponseMapper::toDTO)
                    .collect(Collectors.toList());
            
            logger.info("GET /api/pacientes/sp/buscar/nombre/{} - Busqueda completada. Se encontraron {} pacientes", 
                       nombre, dto.size());
            return ResponseEntity.ok(dto);
        } catch (Exception e) {
            logger.error("GET /api/pacientes/sp/buscar/nombre/{} - Error al buscar pacientes por nombre: {}", 
                       nombre, e.getMessage());
            throw e;
        }
    }

    @GetMapping("/sp/buscar/obra-social")
    public ResponseEntity<List<PacienteDTO>> buscarPorObraSocialPaginado(
            @RequestParam String obraSocial,
            @RequestParam(defaultValue = "10") int limit,
            @RequestParam(defaultValue = "0") int offset) {
        logger.info("GET /api/pacientes/sp/buscar/obra-social?obraSocial={}&limit={}&offset={} - Buscando pacientes por obra social", 
                   obraSocial, limit, offset);
        
        try {
            List<Paciente> paciente = pacientePortInRead.buscarPorObraSocialPaginado(obraSocial, limit, offset);
            List<PacienteDTO> dto = paciente.stream()
                    .map(PacienteResponseMapper::toDTO)
                    .collect(Collectors.toList());
            
            logger.info("GET /api/pacientes/sp/buscar/obra-social - Busqueda completada. Se encontraron {} pacientes", 
                       dto.size());
            return ResponseEntity.ok(dto);
        } catch (Exception e) {
            logger.error("GET /api/pacientes/sp/buscar/obra-social - Error al buscar pacientes por obra social '{}': {}", 
                       obraSocial, e.getMessage());
            throw e;
        }
    }
}

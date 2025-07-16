package microservice.pacientes.infrastructure.configuration;

import microservice.pacientes.application.domain.port.in.CreatePacienteUseCase;
import microservice.pacientes.application.domain.port.in.DeletePacienteUseCase;
import microservice.pacientes.application.domain.port.in.FindPacienteUseCase;
import microservice.pacientes.application.domain.port.in.UpdatePacienteUseCase;
import microservice.pacientes.application.domain.port.out.PacienteRepositoryPort;
import microservice.pacientes.application.service.CreatePacienteService;
import microservice.pacientes.application.service.DeletePacienteService;
import microservice.pacientes.application.service.FindPacienteService;
import microservice.pacientes.application.service.UpdatePacienteService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

// i.e inyector class
@Configuration
public class ApplicationConfig {

    @Bean
    public CreatePacienteUseCase createPacienteUseCase(PacienteRepositoryPort pacienteRepositoryPort) {
        return new CreatePacienteService(pacienteRepositoryPort);
    }

    @Bean
    public DeletePacienteUseCase deletePacienteUseCase(PacienteRepositoryPort pacienteRepositoryPort) {
        return new DeletePacienteService(pacienteRepositoryPort);
    }

    @Bean
    public FindPacienteUseCase findPacienteUseCase(PacienteRepositoryPort pacienteRepositoryPort) {
        return new FindPacienteService(pacienteRepositoryPort);
    }

    @Bean
    public UpdatePacienteUseCase updatePacienteUseCase(PacienteRepositoryPort pacienteRepositoryPort) {
        return new UpdatePacienteService(pacienteRepositoryPort);
    }

}

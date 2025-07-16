package microservice.pacientes.infrastructure.configuration;

import microservice.pacientes.application.domain.port.in.CreatePacienteUseCase;
import microservice.pacientes.application.domain.port.in.DeletePacienteUseCase;
import microservice.pacientes.application.domain.port.in.FindPacienteUseCase;
import microservice.pacientes.application.domain.port.in.UpdatePacienteUseCase;
import microservice.pacientes.application.domain.port.out.PacienteRepositoryPort;
import microservice.pacientes.application.service.CreatePacienteUseCaseImpl;
import microservice.pacientes.application.service.DeletePacienteUseCaseImpl;
import microservice.pacientes.application.service.FindPacienteUseCaseImpl;
import microservice.pacientes.application.service.UpdatePacienteUseCaseImpl;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

// i.e inyector class
@Configuration
public class ApplicationConfig {

    @Bean
    public CreatePacienteUseCase createPacienteUseCase(PacienteRepositoryPort pacienteRepositoryPort) {
        return new CreatePacienteUseCaseImpl(pacienteRepositoryPort);
    }

    @Bean
    public DeletePacienteUseCase deletePacienteUseCase(PacienteRepositoryPort pacienteRepositoryPort) {
        return new DeletePacienteUseCaseImpl(pacienteRepositoryPort);
    }

    @Bean
    public FindPacienteUseCase findPacienteUseCase(PacienteRepositoryPort pacienteRepositoryPort) {
        return new FindPacienteUseCaseImpl(pacienteRepositoryPort);
    }

    @Bean
    public UpdatePacienteUseCase updatePacienteUseCase(PacienteRepositoryPort pacienteRepositoryPort) {
        return new UpdatePacienteUseCaseImpl(pacienteRepositoryPort);
    }

}

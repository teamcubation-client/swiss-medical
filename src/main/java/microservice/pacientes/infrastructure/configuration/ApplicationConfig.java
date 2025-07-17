package microservice.pacientes.infrastructure.configuration;

import microservice.pacientes.application.domain.port.in.CreatePacienteUseCase;
import microservice.pacientes.application.domain.port.in.DeletePacienteUseCase;
import microservice.pacientes.application.domain.port.in.FindPacienteUseCase;
import microservice.pacientes.application.domain.port.in.UpdatePacienteUseCase;
import microservice.pacientes.application.domain.port.out.PacientePortOut;
import microservice.pacientes.application.service.PacienteService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

// i.e inyector class
@Configuration
public class ApplicationConfig {

    private PacienteService pacienteService;
    public PacienteService getPacienteService(PacientePortOut pacientePortOut) {
        if(pacienteService == null)
            return pacienteService = new PacienteService(pacientePortOut);
        return pacienteService;
    }

    @Bean
    public CreatePacienteUseCase createPacienteUseCase(PacientePortOut pacientePortOut) {
        return getPacienteService(pacientePortOut);
    }

    @Bean
    public DeletePacienteUseCase deletePacienteUseCase(PacientePortOut pacientePortOut) {
        return getPacienteService(pacientePortOut);
    }

    @Bean
    public FindPacienteUseCase findPacienteUseCase(PacientePortOut pacientePortOut) {
        return getPacienteService(pacientePortOut);
    }

    @Bean
    public UpdatePacienteUseCase updatePacienteUseCase(PacientePortOut pacientePortOut) {
        return getPacienteService(pacientePortOut);
    }

}

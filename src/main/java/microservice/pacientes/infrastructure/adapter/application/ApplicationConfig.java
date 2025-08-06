package microservice.pacientes.infrastructure.adapter.application;

import microservice.pacientes.application.domain.port.out.LoggerPort;
import microservice.pacientes.application.domain.validator.PacienteValidator;
import microservice.pacientes.application.domain.validator.PacienteValidatorImpl;
import microservice.pacientes.application.domain.validator.rules.*;
import microservice.pacientes.infrastructure.adapter.out.logger.LoggerImpl;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;

@Configuration
public class ApplicationConfig {

    @Bean
    public PacienteValidator createPacienteValidator() {
        return new PacienteValidatorImpl(
                List.of(
                        new ApellidoValidatorRule(),
                        new DniValidatorRule(),
                        new EmailValidatorRule(),
                        new NombreValidatorRule(),
                        new TelefonoValidatorRule()
                )
        );
    }

    @Bean
    public LoggerPort createLoggerPort() {
        return new LoggerImpl();
    }
}

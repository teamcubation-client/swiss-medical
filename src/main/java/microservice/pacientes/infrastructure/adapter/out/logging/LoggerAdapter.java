package microservice.pacientes.infrastructure.adapter.out.logging;

import microservice.pacientes.application.domain.port.out.LoggerPort;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

@Component
public class LoggerAdapter implements LoggerPort {
    private final Logger logger = LoggerFactory.getLogger("PacienteLogger");

    @Override
    public void info(String message, Object... args) {
        logger.info(message, args);
    }

    @Override
    public void debug(String message, Object... args) {
        logger.debug(message, args);
    }

    @Override
    public void warn(String message, Object... args) {
        logger.warn(message, args);
    }

    @Override
    public void error(String message, Object... args) {
        logger.error(message, args);
    }
}

package com.practica.crud_pacientes.infrastructure.adapter.out.logging;

import com.practica.crud_pacientes.application.domain.port.out.PacienteLoggerPort;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

@Component
public class PacienteLoggerAdapter implements PacienteLoggerPort {
    private static final Logger log = LoggerFactory.getLogger("PACIENTE-LOGS");

    @Override
    public void info(String message) {
        log.info(message);
    }

    @Override
    public void info(String message, Object... args) {
        log.info(message, args);
    }

    @Override
    public void warn(String message, Object... args) {
        log.warn(message, args);
    }

    @Override
    public void error(String message, Object... args) {
        log.error(message, args);
    }

    @Override
    public void debug(String message) {
        log.debug(message);
    }
}

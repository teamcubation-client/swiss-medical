package com.practica.crud_pacientes.application.domain.port.out;

public interface PacienteLoggerPort {
    void info(String message);
    void info(String message, Object... args);

    void warn(String message, Object... args);

    void error(String message, Object... args);

    void debug(String message);
}

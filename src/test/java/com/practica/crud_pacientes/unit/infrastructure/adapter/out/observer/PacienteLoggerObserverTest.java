package com.practica.crud_pacientes.unit.infrastructure.adapter.out.observer;

import ch.qos.logback.classic.Logger;
import ch.qos.logback.classic.spi.ILoggingEvent;
import ch.qos.logback.core.read.ListAppender;
import com.practica.crud_pacientes.application.domain.model.Paciente;
import com.practica.crud_pacientes.infrastructure.adapter.out.observer.PacienteLoggerObserver;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.slf4j.LoggerFactory;

import static com.practica.crud_pacientes.utils.PacienteTestFactory.buildDomain;
import static com.practica.crud_pacientes.utils.TestConstants.*;
import static org.assertj.core.api.Assertions.assertThat;

class PacienteLoggerObserverTest {

    private PacienteLoggerObserver observer;
    private Paciente paciente;
    private final Logger logger = (Logger) LoggerFactory.getLogger("PACIENTE-LOGS");
    private ListAppender<ILoggingEvent> listApprender;

    @BeforeEach
    void setUp() {
        observer = new PacienteLoggerObserver();
        paciente = buildDomain();

        listApprender = new ListAppender<>();
        listApprender.start();
        logger.addAppender(listApprender);
    }

    @Test
    @DisplayName("Should log when PacienteCreado")
    void shouldLog_whenPacienteCreado() {
        observer.onPacienteCreado(paciente);

        assertThat(listApprender.list)
                .hasSize(1)
                .anySatisfy(event -> {
                    assertThat(event.getLevel().toString()).hasToString(INFO_LEVEL);
                    assertThat(event.getFormattedMessage()).contains(PACIENTE_CREADO_MESSAGE, NOMBRE, APELLIDO);
                });
    }

    @Test
    @DisplayName("Should log when PacienteEliminado")
    void shouldLog_whenPacienteEliminado() {
        observer.onPacienteEliminado(paciente);

        assertThat(listApprender.list)
                .hasSize(1)
                .anySatisfy(event -> {
                    assertThat(event.getLevel().toString()).hasToString(INFO_LEVEL);
                    assertThat(event.getFormattedMessage()).contains(PACIENTE_ELIMINADO_MESSAGE, NOMBRE, APELLIDO);
                });
    }
}

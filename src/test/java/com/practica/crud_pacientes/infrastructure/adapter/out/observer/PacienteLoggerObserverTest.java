package com.practica.crud_pacientes.infrastructure.adapter.out.observer;

import ch.qos.logback.classic.Logger;
import ch.qos.logback.classic.spi.ILoggingEvent;
import ch.qos.logback.core.read.ListAppender;
import com.practica.crud_pacientes.application.domain.model.Paciente;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.slf4j.LoggerFactory;

import static org.assertj.core.api.Assertions.assertThat;

class PacienteLoggerObserverTest {

    private PacienteLoggerObserver observer;
    private Paciente paciente;
    private Logger logger = (Logger) LoggerFactory.getLogger("PACIENTE-LOGS");
    private ListAppender<ILoggingEvent> listApprender;

    @BeforeEach
    void setUp() {
        observer = new PacienteLoggerObserver();
        paciente = new Paciente();
        paciente.setNombre("Jane");
        paciente.setApellido("Doe");
        paciente.setDni("12121212");

        listApprender = new ListAppender<>();
        listApprender.start();
        logger.addAppender(listApprender);
    }

    @Test
    void shouldLogPacienteCreado() {
        observer.onPacienteCreado(paciente);

        assertThat(listApprender.list)
                .hasSize(1)
                .anySatisfy(event -> {
                    assertThat(event.getLevel().toString()).hasToString("INFO");
                    assertThat(event.getFormattedMessage()).contains("Paciente creado", "Jane", "Doe");
                });
    }

    @Test
    void shouldLogPacienteEliminado() {
        observer.onPacienteEliminado(paciente);

        assertThat(listApprender.list)
                .hasSize(1)
                .anySatisfy(event -> {
                    assertThat(event.getLevel().toString()).hasToString("INFO");
                    assertThat(event.getFormattedMessage()).contains("Paciente eliminado", "Jane", "Doe");
                });
    }
}

package com.practica.crud_pacientes.unit.infrastructure.adapter.out.observer;

import ch.qos.logback.classic.Logger;
import ch.qos.logback.classic.spi.ILoggingEvent;
import ch.qos.logback.core.read.ListAppender;
import com.practica.crud_pacientes.infrastructure.adapter.out.observer.SistemaLoggerObserver;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.slf4j.LoggerFactory;

import static com.practica.crud_pacientes.utils.TestConstants.ENDPOINT;
import static com.practica.crud_pacientes.utils.TestConstants.REQUEST_COUNT;
import static org.assertj.core.api.Assertions.assertThat;

class SistemaLoggerObserverTest {

    private SistemaLoggerObserver observer;
    private final Logger logger = (Logger) LoggerFactory.getLogger("SISTEMA-LOGS");
    ListAppender<ILoggingEvent> listApprender;

    @BeforeEach
    void setUp() {
        observer = new SistemaLoggerObserver();
        listApprender = new ListAppender<>();
        listApprender.start();
        logger.addAppender(listApprender);
    }

    @Test
    void shouldLogAlert() {
        observer.onAlertaGenerada(ENDPOINT, REQUEST_COUNT);

        assertThat(listApprender.list)
                .hasSize(1)
                .anySatisfy(logEvent -> {
                    assertThat(logEvent.getLevel().toString()).hasToString("WARN");
                    assertThat(logEvent.getFormattedMessage()).contains("High traffic detected", "/pacientes", String.valueOf(REQUEST_COUNT));
                });
    }
}

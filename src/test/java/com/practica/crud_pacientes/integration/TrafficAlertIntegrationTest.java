package com.practica.crud_pacientes.integration;

import ch.qos.logback.classic.Logger;
import ch.qos.logback.classic.spi.ILoggingEvent;
import ch.qos.logback.core.read.ListAppender;
import com.practica.crud_pacientes.infrastructure.adapter.out.mediator.TrafficMonitor;
import com.practica.crud_pacientes.infrastructure.adapter.out.observer.SistemaLoggerObserver;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import static com.practica.crud_pacientes.utils.TestConstants.ENDPOINT;
import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
class TrafficAlertIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private TrafficMonitor trafficMonitor;

    @Autowired
    private SistemaLoggerObserver observer;
    private final Logger logger = (Logger) LoggerFactory.getLogger("SISTEMA-LOGS");
    ListAppender<ILoggingEvent> listApprender;

    @BeforeEach
    void setUp() {
        listApprender = new ListAppender<>();
        listApprender.start();
        logger.addAppender(listApprender);
    }

    @AfterEach
    void tearDown() {
        logger.detachAppender(listApprender);
        listApprender.stop();
    }

    @Test
    void shouldTriggerAlertWhenTrafficExceedsThreshold() throws Exception {
        int threshold = trafficMonitor.getTHRESHOLD();

        for (int i = 0; i < threshold; i++) {
            mockMvc.perform(get(ENDPOINT))
                    .andExpect(status().isOk());
        }
        mockMvc.perform(get(ENDPOINT))
                .andExpect(status().isOk());

        assertThat(listApprender.list)
                .hasSize(1)
                .anySatisfy(logEvent -> {
                    assertThat(logEvent.getLevel().toString()).hasToString("WARN");
                    assertThat(logEvent.getFormattedMessage()).contains("High traffic detected", ENDPOINT, String.valueOf(threshold + 1));
                });
    }
}

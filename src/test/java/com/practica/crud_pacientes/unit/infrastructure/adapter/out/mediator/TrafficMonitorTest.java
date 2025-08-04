package com.practica.crud_pacientes.unit.infrastructure.adapter.out.mediator;

import com.practica.crud_pacientes.infrastructure.adapter.out.mediator.TrafficMonitor;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.assertFalse;

@ExtendWith(MockitoExtension.class)
class TrafficMonitorTest {

    private TrafficMonitor trafficMonitor;

    @BeforeEach
    void setUp() {
        trafficMonitor = new TrafficMonitor(2);
    }

    @Test
    @DisplayName("Should trigger when theres high request")
    void shouldTrigger_whenHighRequest() {
        trafficMonitor.shouldTriggerAlert();
        trafficMonitor.shouldTriggerAlert();
        boolean result = trafficMonitor.shouldTriggerAlert();

        int finalRequestCount = trafficMonitor.getRequestCount();

        assertTrue(result);
        assertEquals(3, finalRequestCount);
    }

    @Test
    @DisplayName("Should not trigger when is below threshold")
    void shouldNotTrigger_whenBelowThreshold() {
        boolean first = trafficMonitor.shouldTriggerAlert();
        boolean second = trafficMonitor.shouldTriggerAlert();

        assertFalse(first);
        assertFalse(second);
    }

    @Test
    @DisplayName("Should requestCount be zero when reset")
    void shouldRequestCountBeZero_whenReset() {
        trafficMonitor.shouldTriggerAlert();
        trafficMonitor.shouldTriggerAlert();
        trafficMonitor.shouldTriggerAlert();

        int beforeReset = trafficMonitor.getRequestCount();

        trafficMonitor.reset();

        int afterReset = trafficMonitor.getRequestCount();

        assertEquals(3, beforeReset);
        assertEquals(0, afterReset);
    }
}

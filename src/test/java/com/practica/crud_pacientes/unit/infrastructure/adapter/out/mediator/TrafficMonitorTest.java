package com.practica.crud_pacientes.unit.infrastructure.adapter.out.mediator;

import com.practica.crud_pacientes.infrastructure.adapter.out.mediator.TrafficMonitor;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class TrafficMonitorTest {

    private TrafficMonitor trafficMonitor;

    @BeforeEach
    void setUp() {
        trafficMonitor = new TrafficMonitor(2);
    }

    @Test
    void shouldTriggerWhenHighRequest() {
        trafficMonitor.shouldTriggerAlert();
        trafficMonitor.shouldTriggerAlert();
        boolean result = trafficMonitor.shouldTriggerAlert();

        int finalRequestCount = trafficMonitor.getRequestCount();

        assertTrue(result);
        assertEquals(3, finalRequestCount);
    }

    @Test
    void shouldNotTriggerWhenBelowThreshold() {
        boolean first = trafficMonitor.shouldTriggerAlert();
        boolean second = trafficMonitor.shouldTriggerAlert();

        assertFalse(first);
        assertFalse(second);
    }

    @Test
    void shouldRequestCountBeCeroWhenReset() {
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

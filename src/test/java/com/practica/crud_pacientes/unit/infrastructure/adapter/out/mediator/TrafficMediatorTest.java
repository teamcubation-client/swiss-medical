package com.practica.crud_pacientes.unit.infrastructure.adapter.out.mediator;

import com.practica.crud_pacientes.infrastructure.adapter.out.mediator.AlertService;
import com.practica.crud_pacientes.infrastructure.adapter.out.mediator.TrafficMediator;
import com.practica.crud_pacientes.infrastructure.adapter.out.mediator.TrafficMonitor;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class TrafficMediatorTest {

    @Mock
    private TrafficMonitor trafficMonitor;

    @Mock
    private AlertService alertService;

    @InjectMocks
    private TrafficMediator trafficMediator;

    @Test
    void shouldTriggerAndSendAnAlert() {
        String event = "/pacientes";
        int requestCount = 300;
        when(trafficMonitor.shouldTriggerAlert()).thenReturn(true);
        when(trafficMonitor.getRequestCount()).thenReturn(requestCount);

        trafficMediator.notifyTraffic(event);

        verify(trafficMonitor, times(1)).shouldTriggerAlert();
        verify(trafficMonitor, times(1)).reset();
        verify(alertService, times(1)).sendAlert(event, requestCount);
    }

    @Test
    void shouldNotTrigger() {
        String event = "/pacientes";
        int requestCount = 1;

        when(trafficMonitor.shouldTriggerAlert()).thenReturn(false);

        trafficMediator.notifyTraffic(event);

        verify(trafficMonitor, times(1)).shouldTriggerAlert();
        verify(trafficMonitor, never()).reset();
        verify(alertService, never()).sendAlert(event, requestCount);
    }
}

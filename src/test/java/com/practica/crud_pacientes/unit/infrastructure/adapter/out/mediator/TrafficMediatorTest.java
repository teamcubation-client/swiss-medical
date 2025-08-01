package com.practica.crud_pacientes.unit.infrastructure.adapter.out.mediator;

import com.practica.crud_pacientes.infrastructure.adapter.out.mediator.AlertService;
import com.practica.crud_pacientes.infrastructure.adapter.out.mediator.TrafficMediator;
import com.practica.crud_pacientes.infrastructure.adapter.out.mediator.TrafficMonitor;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static com.practica.crud_pacientes.utils.TestConstants.ENDPOINT;
import static com.practica.crud_pacientes.utils.TestConstants.REQUEST_COUNT;
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
    @DisplayName("Should trigger and send an alert when traffic exceeds threshold")
    void shouldTriggerAndSendAlert_whenTrafficExceedsThreshold() {
        when(trafficMonitor.shouldTriggerAlert()).thenReturn(true);
        when(trafficMonitor.getRequestCount()).thenReturn(REQUEST_COUNT);

        trafficMediator.notify(ENDPOINT);

        verify(trafficMonitor, times(1)).shouldTriggerAlert();
        verify(trafficMonitor, times(1)).reset();
        verify(alertService, times(1)).sendAlert(ENDPOINT, REQUEST_COUNT);
    }

    @Test
    @DisplayName("Should not trigger an alert when traffic is below threshold")
    void shouldNotTriggerAlert_whenTrafficBelowThreshold() {
        when(trafficMonitor.shouldTriggerAlert()).thenReturn(false);

        trafficMediator.notify(ENDPOINT);

        verify(trafficMonitor, times(1)).shouldTriggerAlert();
        verify(trafficMonitor, never()).reset();
        verify(alertService, never()).sendAlert(ENDPOINT, REQUEST_COUNT);
    }
}

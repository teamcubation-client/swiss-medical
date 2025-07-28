package com.practica.crud_pacientes.unit.infrastructure.adapter.out.mediator;

import com.practica.crud_pacientes.application.domain.port.out.SistemaEventPublisher;
import com.practica.crud_pacientes.infrastructure.adapter.out.mediator.AlertService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class AlertServiceTest {

    @Mock
    private SistemaEventPublisher publisher;

    @InjectMocks
    private AlertService alertService;

    @Test
    void shouldSendAlert() {
        String event = "/pacientes";
        int requestCount = 10;

        alertService.sendAlert(event, requestCount);

        verify(publisher, times(1)).publishAlertCreated(event, requestCount);
    }
}

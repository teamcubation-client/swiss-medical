package com.practica.crud_pacientes.unit.infrastructure.adapter.out.mediator;

import com.practica.crud_pacientes.application.domain.port.out.SistemaEventPublisher;
import com.practica.crud_pacientes.infrastructure.adapter.out.mediator.AlertService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static com.practica.crud_pacientes.utils.TestConstants.ENDPOINT;
import static com.practica.crud_pacientes.utils.TestConstants.REQUEST_COUNT;
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
        alertService.sendAlert(ENDPOINT, REQUEST_COUNT);

        verify(publisher, times(1)).publishAlertCreated(ENDPOINT, REQUEST_COUNT);
    }
}

package com.practica.crud_pacientes.infrastructure.adapter.out.mediator;

import com.practica.crud_pacientes.application.domain.port.out.SistemaEventPublisher;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;

@AllArgsConstructor
@Component
public class AlertService {

    private final SistemaEventPublisher eventPublisher;

    public void sendAlert(String event, int requestCount){
        eventPublisher.publishAlertCreated(event, requestCount);
    }
}

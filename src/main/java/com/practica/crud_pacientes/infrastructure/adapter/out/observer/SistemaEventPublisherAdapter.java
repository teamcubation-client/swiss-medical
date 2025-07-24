package com.practica.crud_pacientes.infrastructure.adapter.out.observer;

import com.practica.crud_pacientes.application.domain.port.out.SistemaEventPublisher;
import com.practica.crud_pacientes.application.domain.port.out.SistemaObserver;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;

@AllArgsConstructor
@Component
public class SistemaEventPublisherAdapter implements SistemaEventPublisher {

    private final List<SistemaObserver> observers;

    @Override
    public void publishAlertCreated(String event, int requestCount) {
        observers.forEach(observer -> observer.onAlertaGenerada(event, requestCount));
    }
}

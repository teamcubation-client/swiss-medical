package com.practica.crud_pacientes.application.domain.port.out;

public interface SistemaEventPublisher {
    void publishAlertCreated(String event, int requestCount);
}

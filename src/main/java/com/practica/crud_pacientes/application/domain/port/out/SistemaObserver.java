package com.practica.crud_pacientes.application.domain.port.out;

public interface SistemaObserver {
    void onAlertaGenerada(String event, int requestCount);
}

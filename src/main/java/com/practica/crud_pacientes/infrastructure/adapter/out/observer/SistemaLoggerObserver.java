package com.practica.crud_pacientes.infrastructure.adapter.out.observer;

import com.practica.crud_pacientes.application.domain.port.out.SistemaObserver;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

@Component
public class SistemaLoggerObserver implements SistemaObserver {

    private final Logger logger = LoggerFactory.getLogger("SISTEMA-LOGS");

    @Override
    public void onAlertaGenerada(String event, int requestCount) {
        logger.warn("High traffic detected at endpont: {} with {} requests", event, requestCount);
    }
}

package com.practica.crud_pacientes.infrastructure.adapter.out.mediator;

import lombok.Getter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Getter
@Component
public class TrafficMonitor {
    private int requestCount = 0;
    private final int THRESHOLD;

    public TrafficMonitor(@Value("${trafico.alerta.threshold}") int threshold) {
        this.THRESHOLD = threshold;
    }

    public boolean shouldTriggerAlert() {
        requestCount++;
        return requestCount > THRESHOLD;
    }

    public void reset() {
        requestCount = 0;
    }
}

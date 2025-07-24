package com.practica.crud_pacientes.infrastructure.adapter.out.mediator;

import com.practica.crud_pacientes.application.domain.port.in.Mediator;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;

@AllArgsConstructor
@Component
public class TrafficMediator implements Mediator {
    private final AlertService alertService;
    private final TrafficMonitor trafficMonitor;

    @Override
    public void notifyTraffic(String event) {
        if(trafficMonitor.shouldTriggerAlert()) {
            alertService.sendAlert(event, trafficMonitor.getRequestCount());
            trafficMonitor.reset();
        }
    }
}

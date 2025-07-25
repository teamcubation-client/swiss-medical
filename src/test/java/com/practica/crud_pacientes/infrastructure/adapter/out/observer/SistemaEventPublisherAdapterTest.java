package com.practica.crud_pacientes.infrastructure.adapter.out.observer;

import com.practica.crud_pacientes.application.domain.port.out.SistemaObserver;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class SistemaEventPublisherAdapterTest {

    @Mock
    private SistemaObserver observer1;
    @Mock
    private SistemaObserver observer2;

    private SistemaEventPublisherAdapter publisher;

    @BeforeEach
    void setUp() {
        publisher = new SistemaEventPublisherAdapter(List.of(observer1, observer2));
    }

    @Test
    void shouldPublishOnAlertaGenerada() {
        String event = "/pacientes";
        int requestCount = 200;
        publisher.publishAlertCreated(event, requestCount);

        verify(observer1, times(1)).onAlertaGenerada(event, requestCount);
        verify(observer2, times(1)).onAlertaGenerada(event, requestCount);
    }
}

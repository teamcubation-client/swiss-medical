package com.practica.crud_pacientes.unit.infrastructure.adapter.out.observer;

import com.practica.crud_pacientes.application.domain.port.out.SistemaObserver;
import com.practica.crud_pacientes.infrastructure.adapter.out.observer.SistemaEventPublisherAdapter;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static com.practica.crud_pacientes.utils.TestConstants.ENDPOINT;
import static com.practica.crud_pacientes.utils.TestConstants.REQUEST_COUNT;
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
        publisher.publishAlertCreated(ENDPOINT, REQUEST_COUNT);

        verify(observer1, times(1)).onAlertaGenerada(ENDPOINT, REQUEST_COUNT);
        verify(observer2, times(1)).onAlertaGenerada(ENDPOINT, REQUEST_COUNT);
    }
}

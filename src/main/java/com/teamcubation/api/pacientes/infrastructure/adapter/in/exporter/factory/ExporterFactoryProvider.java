package com.teamcubation.api.pacientes.infrastructure.adapter.in.exporter.factory;

import com.teamcubation.api.pacientes.shared.exception.ExporterTypeNotSupportedException;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

@Component
public class ExporterFactoryProvider implements com.teamcubation.api.pacientes.application.domain.port.out.ExporterFactoryProviderPortOut {
    private final Map<String, ExporterFactory> factories = new HashMap<>();

    public ExporterFactoryProvider() {
        factories.put("csv", new CsvExporterFactory());
        factories.put("json", new JsonExporterFactory());
    }

    @Override
    public ExporterFactory getFactory(String type) {
        ExporterFactory factory = factories.get(type.toLowerCase());
        if (factory == null) {
            throw new ExporterTypeNotSupportedException(type);
        }
        return factory;
    }
}

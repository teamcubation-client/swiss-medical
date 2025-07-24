package com.teamcubation.api.pacientes.infrastructure.adapter.in.exporter.factory;

import com.teamcubation.api.pacientes.shared.exception.ExporterTypeNotSupportedException;

import java.util.HashMap;
import java.util.Map;

public class ExporterFactoryProvider {
    private static final Map<String, ExporterFactory> FACTORIES = new HashMap<>();

    static {
        FACTORIES.put("csv", new CsvExporterFactory());
        FACTORIES.put("json", new JsonExporterFactory());
    }

    public static ExporterFactory getFactory(String type) {
        ExporterFactory factory = FACTORIES.get(type.toLowerCase());
        if (factory == null) {
            throw new ExporterTypeNotSupportedException(type);
        }
        return factory;
    }
}

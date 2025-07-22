package com.teamcubation.api.pacientes.application.domain.exporter.factory;

import com.teamcubation.api.pacientes.shared.exception.ExporterTypeNotSupportedException;

public class ExporterFactoryProvider {

    public static ExporterFactory getFactory(String type) {
        if ("csv".equalsIgnoreCase(type)) {
            return new CsvExporterFactory();
        } else if ("json".equalsIgnoreCase(type)) {
            return new JsonExporterFactory();
        } else {
            throw new ExporterTypeNotSupportedException(type);
        }
    }
}

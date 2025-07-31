package com.teamcubation.api.pacientes.application.domain.port.out;

import com.teamcubation.api.pacientes.infrastructure.adapter.in.exporter.factory.ExporterFactory;

public interface ExporterFactoryProviderPortOut {
    ExporterFactory getFactory(String format);
}

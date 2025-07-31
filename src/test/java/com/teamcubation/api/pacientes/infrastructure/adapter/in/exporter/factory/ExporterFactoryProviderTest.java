package com.teamcubation.api.pacientes.infrastructure.adapter.in.exporter.factory;

import com.teamcubation.api.pacientes.shared.exception.ExporterTypeNotSupportedException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class ExporterFactoryProviderTest {
    private ExporterFactoryProvider factoryProvider;

    @BeforeEach
    void setUp() {
        factoryProvider = new ExporterFactoryProvider();
    }

    @Test
    void getFactoryCsvType_ShouldReturnsCsvExporterFactory() {
        ExporterFactory factory = factoryProvider.getFactory("csv");

        assertNotNull(factory);
        assertInstanceOf(CsvExporterFactory.class, factory);
    }

    @Test
    void getFactoryJsonType_ShouldReturnsJsonExporterFactory() {
        ExporterFactory factory = factoryProvider.getFactory("json");

        assertNotNull(factory);
        assertInstanceOf(JsonExporterFactory.class, factory);
    }

    @Test
    void getFactoryUnsupportedType_ShouldThrowExporterTypeNotSupportedException() {
        String unsupportedType = "xml";

        ExporterTypeNotSupportedException exception = assertThrows(
                ExporterTypeNotSupportedException.class,
                () -> factoryProvider.getFactory(unsupportedType)
        );

        assertEquals("Tipo de exportación ingresado 'xml' no soportado", exception.getMessage());
    }
}
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
    void getFactory_csvType_returnsCsvExporterFactory() {
        ExporterFactory factory = factoryProvider.getFactory("csv");

        assertNotNull(factory);
        assertTrue(factory instanceof CsvExporterFactory);
    }

    @Test
    void getFactory_jsonType_returnsJsonExporterFactory() {
        ExporterFactory factory = factoryProvider.getFactory("json");

        assertNotNull(factory);
        assertTrue(factory instanceof JsonExporterFactory);
    }

    @Test
    void getFactory_unsupportedType_throwsExporterTypeNotSupportedException() {
        String unsupportedType = "xml";

        ExporterTypeNotSupportedException exception = assertThrows(
                ExporterTypeNotSupportedException.class,
                () -> factoryProvider.getFactory(unsupportedType)
        );

        assertEquals("Tipo de exportación ingresado 'xml' no soportado", exception.getMessage());
    }
}
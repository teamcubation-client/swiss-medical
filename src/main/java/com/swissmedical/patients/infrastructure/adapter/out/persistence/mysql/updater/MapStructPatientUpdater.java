package com.swissmedical.patients.infrastructure.adapter.out.persistence.mysql.updater;

import com.swissmedical.patients.application.domain.command.UpdatePatientCommand;
import com.swissmedical.patients.application.domain.model.Patient;
import org.mapstruct.BeanMapping;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;

@Mapper(
        componentModel = "spring",
        nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE
)
public interface MapStructPatientUpdater {

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    void update(UpdatePatientCommand command, @MappingTarget Patient patient);
}

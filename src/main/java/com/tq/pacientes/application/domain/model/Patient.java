package com.tq.pacientes.application.domain.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Patient {
    private Long id;
    private String firstName;
    private String lastName;
    private String dni;
    private String healthInsurance;
    private String healthPlan;
    private String address;
    private String phoneNumber;
    private String email;
    private Boolean active;
    private LocalDateTime creationDate;
    private LocalDateTime lastModifiedDate;

}

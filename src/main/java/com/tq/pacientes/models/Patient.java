package com.tq.pacientes.models;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.Where;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "pacientes")
@Where(clause = "active = true")
public class Patient {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Column(nullable = false)
    private String firstName;
    
    @Column(nullable = false)
    private String lastName;

    @Column(unique = true, nullable = false)
    private String dni;
    
    private String healthInsurance;
    private String healthPlan;
    private String address;
    private String phoneNumber;

    private String email;

    @Column(nullable = false)
    private Boolean active = true;

    @Column(nullable = false, updatable = false)
    private LocalDateTime creationDate;
    
    private LocalDateTime lastModifiedDate;
}

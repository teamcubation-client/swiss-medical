package com.swissmedical.patients.infrastructure.adapter.out.persistence.mysql.entity;

import java.time.LocalDate;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "patients")
public class PatientEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "first_name", nullable = false)
    private String firstName;

    @Column(name = "last_name", nullable = false)
    private String lastName;

    @Column(name = "email", nullable = false, unique = true)
    private String email;

    @Column(name = "phone_number", nullable = false)
    private String phoneNumber;

    @Column(name = "dni", nullable = false, unique = true)
    private String dni;

    @Column(name = "member_number", nullable = false, unique = true)
    private String memberNumber;

    @Column(name = "birth_date", nullable = false)
    private LocalDate birthDate;

    @Column(name = "is_active", nullable = false)
    private boolean isActive;

    @Column(name = "social_security", nullable = false, unique = true)
    private String socialSecurity;
}

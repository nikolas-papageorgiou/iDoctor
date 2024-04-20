package com.iDoctor.iDoctor.model;


import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;

@Entity
@Table(name = "patients")
@Getter
@Setter
@NoArgsConstructor

public class Patient {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "full_name")
    @NotNull(message = "Το πεδίο δεν μπορεί αν είναι κενό")
    @NotBlank(message = "Το ονοματεπώνυμο δεν μπορεί να είναι κενό.")
    private String fullName;

    @Column(name = "social_security_number")
    @NotNull(message = "Το πεδίο δεν μπορεί αν είναι κενό")
    @NotBlank(message = "O Αριθμός Μητρώου Κοινωνικής Ασφάλισης(ΑΜΚΑ) δεν μπορεί να είναι κενό.")
    private String socialSecurityNumber;

    @Column(name = "medical_history")
    private String medicalHistory;

    @Column(name = "medical_condition")
    private String medicalCondition;

    @Column(name = "medical_treatment")
    private String medicalTreatment;
    // Lombok annotations negate the need for manually written constructors,
    // getters, setters, and toString() method.

    @ManyToOne
    @JoinColumn(name="doctor_id",nullable = false)
    private Doctor doctor;

}
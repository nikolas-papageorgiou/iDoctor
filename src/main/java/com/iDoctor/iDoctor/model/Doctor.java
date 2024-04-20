package com.iDoctor.iDoctor.model;

import jakarta.persistence.*;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import lombok.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

@Entity
@Table(name = "doctors")
@Getter
@Setter
@NoArgsConstructor
public class Doctor {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "doctor_id", nullable = false)
    private Long doctorId;


    @Column(name = "full_name", nullable = false)
    @NotNull(message = "Το πεδίο δεν μπορεί αν είναι κενό")
    @NotBlank(message = "Το ονοματεπώνυμο δεν μπορεί να είναι κενό.")
    private String fullName;


    @Column(name = "email",unique = true)
    @NotNull(message = "Το πεδίο δεν μπορεί αν είναι κενό")
    @NotBlank(message = "Το email δεν μπορεί να είναι κενό")
    private String email;

    @Column(name ="enabled")
    private boolean isEnabled;

    @Column(name = "password")
    @NotNull(message = "Το πεδίο δεν μπορεί αν είναι κενό")
    @NotBlank(message = "Ο κωδικός πρόσβασης δεν μπορεί να είναι κενός")
    private String password;
    private String passwordConfirm; // Add this field for form validation purpose

    @Column(name = "license_number", unique = true)
    @NotNull(message = "Το πεδίο δεν μπορεί αν είναι κενό")
    @NotBlank(message = "Η ιατρική άδεια ασκήσεως δεν μπορεί να είναι κενή")
    private String licenseNumber;

    @Column(name = "api_key",unique = true)
    @NotNull(message = "Το πεδίο δεν μπορεί αν είναι κενό")
    @NotBlank(message = "Υποχρεωτικά πρέπει να εισάγετε το Api Key που σας παρέχεται από την OpenAI.")
    private String apiKey;

    // Lombok annotations negate the need for manually written constructors,
    // getters, setters, and toString() method.

    public Doctor(String fullName, String email, String password, String licenseNumber, String apiKey) {
        this.fullName = fullName;
        this.email = email;
        this.password = password;
        this.licenseNumber = licenseNumber;
        this.apiKey = apiKey;
    }

    @OneToMany(mappedBy = "doctor",cascade = CascadeType.REMOVE)
    private List<Patient> reviews = new ArrayList<>();

}
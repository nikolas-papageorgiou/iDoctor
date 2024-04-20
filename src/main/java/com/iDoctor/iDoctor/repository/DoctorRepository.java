package com.iDoctor.iDoctor.repository;

import com.iDoctor.iDoctor.model.Doctor;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface DoctorRepository extends JpaRepository<Doctor, Long> {
    boolean existsByEmail(String email);
    Doctor findByEmail(String email);
}


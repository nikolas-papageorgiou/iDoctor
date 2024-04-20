package com.iDoctor.iDoctor.repository;

import com.iDoctor.iDoctor.model.Patient;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface PatientRepository extends JpaRepository<Patient, Long> {
    boolean existsBySocialSecurityNumber(String socialSecurityNumber);

    Patient findBySocialSecurityNumber(String socialSecurityNumber);

    List<Patient> findByDoctorDoctorId(Long doctorId);
}

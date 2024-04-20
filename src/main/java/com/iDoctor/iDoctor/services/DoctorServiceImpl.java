package com.iDoctor.iDoctor.services;

import com.iDoctor.iDoctor.model.Doctor;
import com.iDoctor.iDoctor.repository.DoctorRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class DoctorServiceImpl implements DoctorService {
    @Autowired
    DoctorRepository doctorRepository;


    @Transactional()
    public boolean checkIfDoctorExists(String email) {
        return doctorRepository.existsByEmail(email);
    }

    public Doctor registerDoctor(Doctor doctor) {
        if (checkIfDoctorExists(doctor.getEmail())) {
            throw new IllegalStateException("Email already in use.");
        }
        return doctorRepository.save(doctor);
    }
}

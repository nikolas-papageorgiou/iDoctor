package com.iDoctor.iDoctor.services;

import com.iDoctor.iDoctor.model.Doctor;
import com.iDoctor.iDoctor.model.Patient;
import com.iDoctor.iDoctor.repository.DoctorRepository;
import com.iDoctor.iDoctor.repository.PatientRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class PatientServiceImpl implements PatientService {
    PatientRepository patientRepository;
    DoctorRepository doctorRepository;
    @Autowired
    public PatientServiceImpl(PatientRepository patientRepository,DoctorRepository doctorRepository) {
        this.patientRepository = patientRepository;
        this.doctorRepository = doctorRepository;
    }

    @Transactional()
    public boolean checkIfPatientExists(String socialSecurityNumber) {
        return patientRepository.existsBySocialSecurityNumber(socialSecurityNumber);
    }
    public Patient registerPatient(Patient patient,Long doctorId){
        if (checkIfPatientExists(patient.getSocialSecurityNumber())) {
            throw new IllegalStateException("Social security number already registered.");
        }
        Doctor doctor = doctorRepository.findById(doctorId).get();
        patient.setDoctor(doctor);
        return patientRepository.save(patient);
    }

    @Override
    public Patient getPatientInfo(String socialSecurityNumber) {
        if (!patientRepository.existsBySocialSecurityNumber(socialSecurityNumber)) {
            throw new IllegalStateException("Social security number does not exist.");
        }

        return patientRepository.findBySocialSecurityNumber(socialSecurityNumber);
    }

    @Override
    public void updatePatient(Patient patient,Long doctorId) {
        Doctor doctor = doctorRepository.findById(doctorId).get();
        patient.setDoctor(doctor);
        patientRepository.save(patient);
    }

    @Override
    public void deletePatient(Long patientId) {
        patientRepository.deleteById(patientId);
    }

    @Override
    public List<Patient> findPatientsByDoctorId(Long doctorId) {
        return patientRepository.findByDoctorDoctorId(doctorId);
    }


    @Override
    public List<Patient> findAll() {
        List<Patient> patientList = patientRepository.findAll();

        return patientList;
    }


}

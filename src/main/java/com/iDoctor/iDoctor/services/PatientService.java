package com.iDoctor.iDoctor.services;

import com.iDoctor.iDoctor.model.Patient;

import java.util.List;

public interface PatientService {
    List<Patient> findAll();
    Patient registerPatient(Patient patient,Long doctorId);

    Patient getPatientInfo(String socialSecurityNumber);

    void updatePatient(Patient patient,Long doctorId);

    void deletePatient(Long patientId);

    List<Patient> findPatientsByDoctorId(Long doctorId);
}

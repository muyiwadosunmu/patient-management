package com.pm.patientservice.service;

import java.util.List;

import com.pm.patientservice.exception.EmailAlreadyExistsException;
import org.springframework.stereotype.Service;

import com.pm.patientservice.dto.PatientRequestDTO;
import com.pm.patientservice.dto.PatientResponseDTO;
import com.pm.patientservice.mapper.PatientMapper;
import com.pm.patientservice.model.Patient;
import com.pm.patientservice.repository.PatientRepository;

@Service
public class PatientService {
    private final PatientRepository patientRepository;

    public PatientService(PatientRepository patientRepository) {
        this.patientRepository = patientRepository;
    }

    public List<PatientResponseDTO> getPatients() {
        List<Patient> patients = patientRepository.findAll();

        // List<PatientResponseDTO> patientResponseDTOs =
        // patients.stream().map((patient) -> PatientMapper.toDTO(patient))
        // .toList();
        return patients.stream().map(PatientMapper::toDTO).toList();
    }


    public PatientResponseDTO createPatient(PatientRequestDTO body) {
        if(patientRepository.existsByEmail(body.getEmail())) {
            throw new EmailAlreadyExistsException("A patient with email " + body.getEmail() + " already exists.");
        }
        Patient parsedBody = PatientMapper.toModel(body);
        Patient newPatient = patientRepository.save(parsedBody);
        return PatientMapper.toDTO(newPatient);
        // return PatientMapper.toDTO(newPatient);
    }

}

package com.pm.patientservice.service;

import java.util.List;
import java.util.UUID;
import java.time.LocalDate;

import com.pm.patientservice.exception.EmailAlreadyExistsException;
import com.pm.patientservice.exception.PatientNotFoundException;
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


    public PatientResponseDTO updatePatient(UUID id,  PatientRequestDTO body) {
        Patient patient =  patientRepository.findById(id).orElseThrow(() -> new PatientNotFoundException("Patient not found with ID: " + id));

        if(patientRepository.existsByEmailAndIdNot(body.getEmail(), id)) {
            throw new EmailAlreadyExistsException("A patient with email " + body.getEmail() + " already exists.");
        }

        patient.setName(body.getName());
        patient.setEmail(body.getEmail());
        patient.setAddress(body.getAddress());
        patient.setDateOfBirth(LocalDate.parse(body.getDateOfBirth()));

        Patient data = patientRepository.save(patient);
        return PatientMapper.toDTO(data);
    }

    public void deletePatient(UUID id) {
        patientRepository.deleteById(id);
    }
}

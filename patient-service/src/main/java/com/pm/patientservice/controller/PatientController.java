package com.pm.patientservice.controller;

import java.util.List;
import java.util.UUID;

import com.pm.patientservice.dto.validators.CreatePatientValidationGroup;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.groups.Default;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import com.pm.patientservice.dto.PatientRequestDTO;
import com.pm.patientservice.dto.PatientResponseDTO;
import com.pm.patientservice.service.PatientService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/patients")
@Tag(name="Patient", description = "APIs for managing patients")
public class PatientController {

    private final PatientService patientService;

    public PatientController(PatientService patientService) {
        this.patientService = patientService;
    }

    @GetMapping
    @Operation(summary = "Get all patients", description = "Retrieve a list of all patients")
    public ResponseEntity<List<PatientResponseDTO>> getPatients() {
        List<PatientResponseDTO> data = patientService.getPatients();
        return ResponseEntity.ok().body(data);
    }

    @PostMapping()
    @Operation(summary = "Create a new patient", description = "Create a new patient with the provided details")
    public ResponseEntity<PatientResponseDTO> createPatient(@Validated({
            Default.class, CreatePatientValidationGroup.class}) @RequestBody PatientRequestDTO body) {
        PatientResponseDTO data = patientService.createPatient(body);
        return ResponseEntity.ok().body(data);
    }

    @PutMapping("/{id}")
    @Operation(summary = "Update an existing patient", description = "Update the details of an existing patient by ID")
    public ResponseEntity<PatientResponseDTO> updatePatient(@Valid @PathVariable UUID id, @Validated({Default.class}) @RequestBody PatientRequestDTO body) {
        PatientResponseDTO data = patientService.updatePatient(id, body);
        return ResponseEntity.ok().body(data);
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Delete a patient", description = "Delete an existing patient by ID")
    public ResponseEntity<Void> deletePatient(@PathVariable UUID id) {
        patientService.deletePatient(id);
        return ResponseEntity.noContent().build();
    }
}
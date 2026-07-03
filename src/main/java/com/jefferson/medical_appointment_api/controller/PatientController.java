package com.jefferson.medical_appointment_api.controller;

import com.jefferson.medical_appointment_api.dto.request.PatientRequest;
import com.jefferson.medical_appointment_api.dto.response.PatientResponse;
import com.jefferson.medical_appointment_api.service.PatientService;
import jakarta.validation.Valid;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/patients")
@RequiredArgsConstructor
public class PatientController {

  private final PatientService service;

  @PostMapping
  @ResponseStatus(HttpStatus.CREATED)
  public PatientResponse createPatient(@Valid @RequestBody PatientRequest request) {
    return service.createPatient(request);
  }

  @GetMapping
  public List<PatientResponse> getAllPatients() {
    return service.getAllPatients();
  }

  @GetMapping("/{id}")
  public PatientResponse getPatientById(@PathVariable Long id) {
    return service.getPatientById(id);
  }
}

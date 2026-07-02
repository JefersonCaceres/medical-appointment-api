package com.jefferson.medical_appointment_api.controller;

import com.jefferson.medical_appointment_api.dto.request.MedicalDoctorRequest;
import com.jefferson.medical_appointment_api.dto.response.MedicalDoctorResponse;
import com.jefferson.medical_appointment_api.service.MedicalDoctorService;
import jakarta.validation.Valid;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/doctors")
@RequiredArgsConstructor
public class MedicalDoctorController {

  private final MedicalDoctorService service;

  @PostMapping
  @ResponseStatus(HttpStatus.CREATED)
  public MedicalDoctorResponse create(@Valid @RequestBody MedicalDoctorRequest request) {
    return service.createMedicalDoctor(request);
  }

  @GetMapping
  public List<MedicalDoctorResponse> findAll() {
    return service.getAllMedicalDoctors();
  }

  @GetMapping("/{id}")
  public MedicalDoctorResponse findById(@PathVariable Long id) {
    return service.getMedicalDoctorById(id);
  }
}

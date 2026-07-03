package com.jefferson.medical_appointment_api.service.impl;

import com.jefferson.medical_appointment_api.dto.request.PatientRequest;
import com.jefferson.medical_appointment_api.dto.response.PatientResponse;
import com.jefferson.medical_appointment_api.entity.PatientEntity;
import com.jefferson.medical_appointment_api.exception.BusinessException;
import com.jefferson.medical_appointment_api.exception.ResourceNotFoundException;
import com.jefferson.medical_appointment_api.mapper.PatientMapper;
import com.jefferson.medical_appointment_api.repository.PatientRepository;
import com.jefferson.medical_appointment_api.service.PatientService;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class PatientServiceImpl implements PatientService {

  private final PatientRepository repository;
  private final PatientMapper mapper;

  @Override
  public PatientResponse createPatient(PatientRequest request) {
    if (repository.existsByDocument(request.document())) {
      throw new BusinessException("A patient with this document already exists.");
    }

    PatientEntity patient = mapper.toEntity(request);
    PatientEntity savedPatient = repository.save(patient);

    return mapper.toResponse(savedPatient);
  }

  @Override
  public List<PatientResponse> getAllPatients() {
    return repository.findAll()
        .stream()
        .map(mapper::toResponse)
        .toList();
  }

  @Override
  public PatientResponse getPatientById(Long id) {
    PatientEntity patient = repository.findById(id)
        .orElseThrow(() -> new ResourceNotFoundException("Patient", "id", id));

    return mapper.toResponse(patient);
  }
}

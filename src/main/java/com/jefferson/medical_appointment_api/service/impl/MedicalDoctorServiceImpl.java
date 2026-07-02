package com.jefferson.medical_appointment_api.service.impl;

import com.jefferson.medical_appointment_api.dto.request.MedicalDoctorRequest;
import com.jefferson.medical_appointment_api.dto.response.MedicalDoctorResponse;
import com.jefferson.medical_appointment_api.entity.MedicalDoctor;
import com.jefferson.medical_appointment_api.exception.ResourceNotFoundException;
import com.jefferson.medical_appointment_api.mapper.MedicalDoctorMapper;
import com.jefferson.medical_appointment_api.repository.MedicalDoctorRepository;
import com.jefferson.medical_appointment_api.service.MedicalDoctorService;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class MedicalDoctorServiceImpl implements MedicalDoctorService {

  private final MedicalDoctorRepository repository;
  private final MedicalDoctorMapper mapper;

  @Override
  public MedicalDoctorResponse createMedicalDoctor(MedicalDoctorRequest request) {
    MedicalDoctor doctor = mapper.toEntity(request);
    MedicalDoctor savedDoctor = repository.save(doctor);
    return mapper.toResponse(savedDoctor);
  }

  @Override
  public List<MedicalDoctorResponse> getAllMedicalDoctors() {
    return repository.findAll()
        .stream()
        .map(mapper::toResponse)
        .toList();
  }

  @Override
  public MedicalDoctorResponse getMedicalDoctorById(Long id) {
    MedicalDoctor doctor = repository.findById(id)
        .orElseThrow(() -> new ResourceNotFoundException("MedicalDoctor", "id", id));

    return mapper.toResponse(doctor);
  }
}
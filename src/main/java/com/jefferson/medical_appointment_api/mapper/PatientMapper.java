package com.jefferson.medical_appointment_api.mapper;

import com.jefferson.medical_appointment_api.dto.request.PatientRequest;
import com.jefferson.medical_appointment_api.dto.response.PatientResponse;
import com.jefferson.medical_appointment_api.entity.PatientEntity;
import org.springframework.stereotype.Component;

@Component
public class PatientMapper {

  public PatientEntity toEntity(PatientRequest request) {
    return PatientEntity.builder()
        .fullName(request.fullName())
        .document(request.document())
        .phone(request.phone())
        .email(request.email())
        .birthDate(request.birthDate())
        .build();
  }

  public PatientResponse toResponse(PatientEntity patient) {
    return new PatientResponse(
        patient.getId(),
        patient.getFullName(),
        patient.getDocument(),
        patient.getPhone(),
        patient.getEmail(),
        patient.getBirthDate()
    );
  }
}
package com.jefferson.medical_appointment_api.mapper;


import com.jefferson.medical_appointment_api.dto.request.MedicalDoctorRequest;
import com.jefferson.medical_appointment_api.dto.response.MedicalDoctorResponse;
import com.jefferson.medical_appointment_api.entity.MedicalDoctorEntity;
import org.springframework.stereotype.Component;

@Component
public class MedicalDoctorMapper {

  public MedicalDoctorEntity toEntity(MedicalDoctorRequest request) {
    return MedicalDoctorEntity.builder()
        .fullName(request.fullName())
        .specialty(request.specialty())
        .phone(request.phone())
        .email(request.email())
        .build();
  }

  public MedicalDoctorResponse toResponse(MedicalDoctorEntity doctor) {
    return new MedicalDoctorResponse(
        doctor.getId(),
        doctor.getFullName(),
        doctor.getSpecialty(),
        doctor.getPhone(),
        doctor.getEmail()
    );
  }
}

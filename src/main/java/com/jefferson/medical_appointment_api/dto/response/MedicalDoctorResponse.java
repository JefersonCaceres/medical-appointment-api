package com.jefferson.medical_appointment_api.dto.response;

public record MedicalDoctorResponse(
    Long id,
    String fullName,
    String specialty,
    String phone,
    String email
) {
}

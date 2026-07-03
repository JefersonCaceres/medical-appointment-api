package com.jefferson.medical_appointment_api.dto.response;

import java.time.LocalDate;

public record PatientResponse(
    Long id,
    String fullName,
    String document,
    String phone,
    String email,
    LocalDate birthDate
) {
}
